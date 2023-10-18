
def isReleaseBranch(branchName){
    return branchName == 'release-deployment' || branchName == 'hotfix-deployment'
}


pipeline {
    agent any

    parameters {
        choice(name: 'FLAVOR_DIMENSION', choices: ['release', 'debug'], description: 'Select the flavor dimension')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Unit Tests') {
            steps {
                script {
                    try {
                        sh "./gradlew test${flavorDimension.capitalize()}"
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        error("Unit tests failed: ${e.message}")
                    }
                }
            }
        }

        stage('Build APK') {
            when {
                expression {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS') && !(isReleaseBranch(env.BRANCH_NAME))
                }
            }
            steps {
                script {
                    def flavorDimension = params.FLAVOR_DIMENSION
                    try {
                        sh "./gradlew assemble${flavorDimension.capitalize()}"
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        error("APK build failed: ${e.message}")
                    }
                }
                archiveArtifacts artifacts: '**/build/outputs/**/*.apk', allowEmptyArchive: true
            }
        }
         stage('Build AAB') {
            when {
                expression {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS') && (isReleaseBranch(env.BRANCH_NAME))
                }
            }
            steps {
                script {
                    def flavorDimension = params.FLAVOR_DIMENSION
                    try {
                        sh "./gradlew bundle${flavorDimension.capitalize()}"
                    } catch (Exception e) {
                        currentBuild.result = 'FAILURE'
                        error("AAB build failed: ${e.message}")
                    }
                }
                archiveArtifacts artifacts: '**/build/outputs/**/*.apk', allowEmptyArchive: true
            }
        }

        stage('Distribute to Appstores') {
            when {
                expression {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS') &&
                    params.FLAVOR_DIMENSION == 'release' &&
                    isReleaseBranch(env.BRANCH_NAME)
                }
            }
            steps {
                // Upload the release APK to Google Play using the Google Play Android Publisher plugin
                // You'll need to configure your Google Play credentials and track information in Jenkins
               // sh 'gplay-publisher <your_upload_command_for_release_apk>'
            }
        }

        stage('Distribute Debug APK to App Center') {
            when {
                expression {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS') &&
                   ( params.FLAVOR_DIMENSION == 'debug' ||
                    (params.FLAVOR_DIMENSION == 'release' && !isReleaseBranch(env.BRANCH_NAME)) )
                }
            }
            steps {
                // Upload the debug APK to App Center using the App Center Plugin
                // You'll need to configure your App Center credentials and app information in Jenkins
               // appcenter distribute release-notes: '', groups: '', notify-testers: 'true', symbols: '**/*.pdb', filePath: '**/build/outputs/**/*.apk'
            }
        }
    }

    post {
        failure {
            echo 'Jenkins pipeline failed. See error details below:'
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                echo "Error in pipeline: ${currentBuild.rawBuild.logFile.text}"
            }
        }
    }
}
