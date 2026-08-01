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

        stage('Smoke Tests') {
            steps {
                sh '''
                    CID=$(docker create ${IMAGE_NAME} mvn test -Dgroups=smoke)
                    echo $CID > smoke_container_id.txt
                    docker start -a $CID
                '''
            }
            post {
                always {
                    sh '''
                        CID=$(cat smoke_container_id.txt)
                        mkdir -p target
                        docker cp $CID:/app/target/extent-report.html target/smoke-extent-report.html || true
                        docker rm $CID || true
                    '''
                }
            }
        }

        stage('UI Tests') {
            matrix {
                axes {
                    axis {
                        name 'BROWSER'
                        values 'chrome', 'firefox'
                    }
                }
                stages {
                    stage('Run') {
                        steps {
                            sh '''
                                CID=$(docker create ${IMAGE_NAME} mvn test -DsuiteXmlFile=testng.xml -Dbrowser=${BROWSER} -Dtest=LoginTest,InvalidLoginTest,LoginDataDrivenTest)
                                echo $CID > ui_container_id_${BROWSER}.txt
                                docker start -a $CID
                            '''
                        }
                        post {
                            always {
                                sh '''
                                    CID=$(cat ui_container_id_${BROWSER}.txt)
                                    mkdir -p target
                                    docker cp $CID:/app/target/extent-report.html target/ui-extent-report-${BROWSER}.html || true
                                    docker cp $CID:/app/target/screenshots target/screenshots-${BROWSER} || true
                                    docker rm $CID || true
                                '''
                            }
                        }
                    }
                }
            }
        }

        stage('API Tests') {
            steps {
                withCredentials([string(credentialsId: 'reqres-api-key', variable: 'REQRES_API_KEY')]) {
                    sh '''
                        CID=$(docker create -e REQRES_API_KEY=${REQRES_API_KEY} ${IMAGE_NAME} mvn test -DsuiteXmlFile=testng.xml -Dtest=AuthApiTest,UserApiTest)
                        echo $CID > api_container_id.txt
                        docker start -a $CID
                    '''
                }
            }
            post {
                always {
                    sh '''
                        CID=$(cat api_container_id.txt)
                        mkdir -p target
                        docker cp $CID:/app/target/extent-report.html target/api-extent-report.html || true
                        docker rm $CID || true
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/smoke-extent-report.html,target/ui-extent-report-*.html,target/api-extent-report.html', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/screenshots-*/**', allowEmptyArchive: true
            sh 'docker rmi ${IMAGE_NAME} || true'
        }
    }
}
