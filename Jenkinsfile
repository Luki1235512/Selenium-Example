pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    environment {
        IMAGE_NAME = "selenium-example-test:${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('UI Tests') {
            steps {
                sh '''
                    docker run --rm \
                      -v ${WORKSPACE}/target:/app/target \
                      ${IMAGE_NAME} \
                      mvn test -Dtest=LoginTest,InvalidLoginTest,LoginDataDrivenTest
                '''
            }
            post {
                always {
                    sh 'cp target/extent-report.html target/ui-extent-report.html || true'
                }
            }
        }

        stage('API Tests') {
            steps {
                withCredentials([string(credentialsId: 'reqres-api-key', variable: 'REQRES_API_KEY')]) {
                    sh '''
                        docker run --rm \
                          -e REQRES_API_KEY=${REQRES_API_KEY} \
                          -v ${WORKSPACE}/target:/app/target \
                          ${IMAGE_NAME} \
                          mvn test -Dtest=AuthApiTest,UserApiTest
                    '''
                }
            }
            post {
                always {
                    sh 'cp target/extent-report.html target/api-extent-report.html || true'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/ui-extent-report.html,target/api-extent-report.html', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/screenshots/**', allowEmptyArchive: true
            sh 'docker rmi ${IMAGE_NAME} || true'
        }
    }
}
