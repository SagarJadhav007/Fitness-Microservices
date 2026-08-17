pipeline {
    agent any

    environment {
        SERVICE_NAME = "activityservice"
        IMAGE_NAME = "020641930163.dkr.ecr.ap-south-1.amazonaws.com/fitx/activityservice"
        SONAR_PROJECT_KEY = "fitx-activityservice"
        K8S_MANIFEST = "k8s/activityservice/deployment.yaml"
        AWS_REGION = "ap-south-1"
        ECR_REGISTRY = "020641930163.dkr.ecr.ap-south-1.amazonaws.com"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Detect Changed Services') {
            steps {
                script {
                    def changedFiles = sh(
                        script: '''
                            git diff --name-only HEAD~1 HEAD
                        ''',
                        returnStdout: true
                    ).trim()

                    echo "========================================"
                    echo "Changed Files"
                    echo "========================================"

                    if (changedFiles) {
                        echo changedFiles
                    } else {
                        echo "No changed files detected."
                    }

                    def services = [
                        'activityservice',
                        'aiservice',
                        'analyticsservice',
                        'configserver',
                        'gateway',
                        'userservice'
                    ]

                    def changedServices = []

                    if (changedFiles) {
                        def files = changedFiles.split('\n')

                        changedServices = services.findAll { service ->
                            files.any { file ->
                                file.startsWith("${service}/")
                            }
                        }
                    }

                    echo "========================================"
                    echo "Changed Services"
                    echo "========================================"

                    if (changedServices.isEmpty()) {
                        echo "No microservice source changes detected."
                    } else {
                        changedServices.each { service ->
                            echo " - ${service}"
                        }
                    }

                    env.CHANGED_SERVICES = changedServices.join(',')

                    echo "CHANGED_SERVICES=${env.CHANGED_SERVICES}"
                }
            }
        }

        stage('Test') {
            steps {
                dir('activityservice') {
                    sh './mvnw clean test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('activityservice') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                            ./mvnw sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY}
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build \
                      -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                      ./activityservice
                '''
            }
        }

        stage('Trivy Scan') {
            steps {
                sh '''
                    trivy image \
                      --severity HIGH,CRITICAL \
                      ${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                sh '''
                    set -e

                    echo "Logging into ECR..."

                    aws ecr get-login-password \
                      --region ${AWS_REGION} | \
                    docker login \
                      --username AWS \
                      --password-stdin \
                      ${ECR_REGISTRY}

                    echo "Pushing ${IMAGE_NAME}:${BUILD_NUMBER}..."

                    docker push \
                      ${IMAGE_NAME}:${BUILD_NUMBER}

                    echo "Tagging image as latest..."

                    docker tag \
                      ${IMAGE_NAME}:${BUILD_NUMBER} \
                      ${IMAGE_NAME}:latest

                    echo "Pushing latest..."

                    docker push \
                      ${IMAGE_NAME}:latest

                    docker logout ${ECR_REGISTRY}
                '''
            }
        }

        stage('Update GitOps Manifest') {
            steps {
                sh '''
                    set -e

                    echo "Updating GitOps manifest..."

                    echo "Before:"
                    grep "image:" ${K8S_MANIFEST}

                    sed -i \
                      "s|image: ${IMAGE_NAME}:.*|image: ${IMAGE_NAME}:${BUILD_NUMBER}|" \
                      ${K8S_MANIFEST}

                    echo "After:"
                    grep "image:" ${K8S_MANIFEST}
                '''
            }
        }

        stage('Commit and Push GitOps Change') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'github-fitx',
                        usernameVariable: 'GIT_USERNAME',
                        passwordVariable: 'GIT_PASSWORD'
                    )
                ]) {
                    sh '''
                        set -e

                        git config user.name "Jenkins"
                        git config user.email "jenkins@fitx.local"

                        git add ${K8S_MANIFEST}

                        if git diff --cached --quiet; then
                            echo "No GitOps changes detected."
                            exit 0
                        fi

                        echo "Git changes:"
                        git diff --cached -- ${K8S_MANIFEST}

                        git commit \
                          -m "chore: deploy ${SERVICE_NAME}:${BUILD_NUMBER}"

                        git push \
                          https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/SagarJadhav007/Fitness-Microservices.git \
                          HEAD:master
                    '''
                }
            }
        }
    }

    post {

        always {
            junit(
                testResults: 'activityservice/target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            sh '''
                docker image rm \
                  ${IMAGE_NAME}:${BUILD_NUMBER} \
                  ${IMAGE_NAME}:latest \
                  2>/dev/null || true
            '''
        }

        success {
            echo 'FitX Activity Service CI/CD pipeline completed successfully!'
        }

        failure {
            echo 'FitX Activity Service CI/CD pipeline failed.'
        }
    }
}