# Selenium-Example

A reference/template project demonstrating modern coding, architecture, and testing
best practices for Java + Selenium test automation.

UI tests run against [SauceDemo](https://www.saucedemo.com). API tests run against
[reqres.in](https://reqres.in)'s Legacy + Auth endpoints.

## Architecture

```
src/
  main/java/com/automation/       # reusable framework code (see note below)
    api/
      client/                     # ApiClient (thread-safe lazy singleton) + ExtentLoggingFilter
      assertions/                 # ApiAssertions - status code / timing / field-present helpers
      fixtures/                   # UserFixtures - thread-safe fresh-id generator for mutations
      models/                     # LegacyUser, list/single/mutation, Login/Register POJOs
      base/                       # BaseApiTest - builds the shared RequestSpecification
    ui/
      base/                       # BaseTest - browser lifecycle, ThreadLocal<WebDriver>
      factory/                    # DriverFactory - Chrome/Firefox, headless-aware
      pages/                      # LoginPage, InventoryPage - locators + actions, no asserts
      dataproviders/              # LoginDataProvider + typed LoginTestData row
    listeners/                    # ExtentTestNameListener, ExtentFlushListener, RetryAnalyzer(+Transformer)
    testgroups/                   # smoke / regression / ui / api constants
    utils/                        # ConfigReader, WaitUtils, ExtentManager, ExtentTestManager
    resources/META-INF/services/  # ServiceLoader registration for the TestNG listeners
  test/java/com/automation/       # @Test classes and @DataProvider classes only
    api/tests/                    # AuthApiTest, UserApiTest
    ui/tests/                     # LoginTest, InvalidLoginTest, LoginDataDrivenTest
  test/resources/                 # config.properties, log4j2.xml
Dockerfile                        # Maven + Java 21 + Chrome + Firefox, test execution image
jenkins-infra/Dockerfile          # builds the Jenkins server itself (DooD), not used by the pipeline
Jenkinsfile                       # Multibranch Pipeline: static analysis -> smoke -> UI matrix -> API
checkstyle.xml / spotbugs-exclude.xml
testng.xml
```

**`src/main/java`** holds everything tests consume but isn't itself a `@Test`: page
objects, the API client, config, listeners, utils, DTOs. **`src/test/java`** holds only
`@Test` and `@DataProvider` classes. This is a deliberate departure from "test
automation project = everything in `src/test`," driven by two things: it's the more
correct Maven convention for code that would be reusable if handed to another project
tomorrow, and it's a hard requirement for JaCoco, whose Maven plugin has no supported
way to measure coverage of `src/test/java`-only code.

### Design decisions (and why)

- **`ui/` and `api/` are fully parallel package trees**, each with its own `base/`
  and test-support packages. `listeners/`, `utils/`, and `testgroups/` stay shared at
  the top level since both test types genuinely use them.

- **POM returns data, never asserts.** `LoginPage`/`InventoryPage` methods either
  perform an action (`login`, `clickLogin`) or return a value (`getErrorMessage()`,
  `getCurrentUrl()`). All `Assert` calls live in the `@Test` classes, so failure
  messages stay attributable to the actual test assertion and page objects stay
  reusable.

- **Explicit waits only.** `WaitUtils` wraps `WebDriverWait` with named
  `visibilityOfElementLocated`/`elementToBeClickable` helpers behind a single
  timeout constant. No `Thread.sleep` anywhere in the codebase.

- **Config is a resolvable chain.** `ConfigReader` reads
  `config.properties` for non-sensitive values (base URLs, demo credentials that are
  intentionally public, like reqres.in's documented `eve.holt@reqres.in`/`pistol`).
  `ConfigReader.getSecret()` reads _only_ from environment variables and fails loudly
  if unset - nothing sensitive is ever committed to a tracked file.

- **Reporting is decoupled from `BaseTest`.** `ExtentTestNameListener` and
  `ExtentFlushListener` are registered via TestNG's `ServiceLoader` mechanism
  (`META-INF/services/org.testng.ITestNGListener`), not a `@Listeners` annotation on
  every class. Test classes have zero reporting-related code in them.

- **Parallel-safety is explicit.** `BaseTest` uses
  `ThreadLocal<WebDriver>` for cross-thread isolation. `ApiClient` and `ExtentManager`
  are lazy singletons guarded by a private lock object (not `synchronized` on the
  method/class, which would lock on a globally-shared object) - both were real
  double-checked-locking bugs found and fixed via SpotBugs.

- **Retry is applied globally.** `RetryAnalyzerTransformer` implements
  `IAnnotationTransformer` to attach `RetryAnalyzer` to every `@Test` automatically,
  so flaky-test recovery doesn't depend on remembering a `retryAnalyzer` attribute on
  each annotation.

- **Static analysis and coverage are real gates.** Checkstyle,
  SpotBugs, and JaCoco are wired into `pom.xml`, bound to the `verify` phase, with
  every plugin/dependency version pinned in `<properties>`. `checkstyle.xml` is tuned
  to the codebase's actual conventions (case-sensitive import order, lowerCamelCase
  for stateful `static final` fields like `Logger`/`AtomicInteger`) rather than
  left at generic defaults. `spotbugs-exclude.xml` documents _why_ each suppression
  is safe instead of silently ignoring findings.

## Running

```bash
mvn test                       # full suite, default browser from config.properties
mvn test -Dgroups=smoke        # smoke subset only
mvn test -Dbrowser=firefox     # override browser via system property
mvn clean verify               # + Checkstyle, SpotBugs, JaCoco coverage report
```

Suite: 3 UI specs (valid login, locked-out user, 4-case data-driven login) x
Chrome/Firefox, plus 8 API specs against reqres.in's Legacy + Auth endpoints, all
runnable in parallel (`testng.xml`, `parallel="methods"`).

## Tooling

- **ExtentReports**, auto-registered via `ServiceLoader`, with a custom
  `ExtentLoggingFilter` logging every API request/response to both Extent and log4j2.
- **Checkstyle + SpotBugs + JaCoco**, bound to `verify`, all versions verified
  against Maven Central directly.
- **Jenkins (self-hosted, Docker-outside-of-Docker)**, Multibranch Pipeline, SCM
  polling trigger. Pipeline stages: build image -> static analysis -> smoke tests
  -> UI matrix (Chrome/Firefox as parallel matrix branches) -> API tests, each stage
  archiving its own Extent report and (for UI) screenshots-on-failure.

## Roadmap / not yet built

These are known, intentional gaps:

- **Unified JaCoco coverage.** Each Jenkins stage runs in its own container and
  produces an honest but partial coverage report (e.g. the UI stage shows ~0% on
  API client code). Merging them into
  one real number needs `jacoco:merge` across stages and isn't done yet.
- **AssertJ soft assertions** for multi-field checks (`UserApiTest`,
  `LoginDataDrivenTest`) - currently a first failed `Assert` stops the test instead
  of collecting every failure.
- **JSON schema validation** on API responses, in addition to POJO deserialization,
  to catch contract drift that mapping alone won't.
- **Docker + Selenium Grid**, to demonstrate scaling execution across nodes rather
  than a single container per stage.
- **Unit tests for the framework's own code** (`ConfigReader`'s override chain,
  `RetryAnalyzer`'s retry-count logic) - straightforward now that this code lives in
  `src/main/java`, and the direct enabler for the unified coverage number above.
- **Cucumber BDD layer** on top of the existing structure, as an optional
  alternative test-authoring style.
