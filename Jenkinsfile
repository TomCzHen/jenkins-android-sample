pipeline {
    agent any

    parameters {
        string(
                name: 'PARAM_STRING',
                defaultValue: 'String',
                description: 'String Parameter'
        )

        choice(
                name: 'PARAM_CHOICE',
                choices: '1st\n2nd\n3rd',
                description: 'Choice Parameter'
        )

        booleanParam(
                name: 'PARAM_CHECKBOX',
                defaultValue: true,
                description: 'Checkbox Parameter'
        )
    }

    stages {

        stage('Parameters Example') {
            steps {
                echo "Output Parameters"
                echo "PARAM_STRING=${params.PARAM_STRING}"
                echo "PARAM_CHOICE=${params.PARAM_CHOICE}"
                echo "PARAM_CHECKBOX=${params.PARAM_CHECKBOX}"
            }
        }

        stage('withEnv Example') {
            steps {
                echo 'Run Step With Env'
                withEnv(['ENV_FIRST=true', 'ENV_SECOND=sqlite']) {
                    echo "ENV_FIRST=${env.ENV_FIRST}"
                    echo "ENV_SECOND=${env.ENV_SECOND}"
                }
            }
        }

        stage('Try Catch Example') {
            steps {
                try {
                    sh "echo message"
                } catch (error) {
                    throw error
                }
            }
        }

        stage('Build Develop APK') {

            when {
                branch 'master'
            }
            steps {
                echo 'Building Develop APK...'
                sh './gradlew clean assembleDevDebug'
            }
        }

        stage('Build Beta APK') {
            when {
                branch 'beta'
            }
            steps {
                echo 'Building Beta APK...'
                withCredentials([string(credentialsId: 'BETA_SECRET_KEY', variable: 'SECRET_KEY')]) {
                    sh './gradlew clean assembleBetaDebug'
                }
            }
        }

        stage('Build Prod APK') {
            when {
                branch 'prod'
            }
            steps {
                echo 'Building Production APK...'
                withCredentials([string(credentialsId: 'PROD_SECRET_KEY', variable: 'SECRET_KEY')]) {
                    sh './gradlew clean assembleProd'
                }
            }
        }

        stage('Sign Prod APK') {
            when {
                branch 'prod'
            }
            steps {
                echo 'Sign APK'
                signAndroidApks(
                        keyStoreId: "ANDROID_SIGN_KEY_STORE",
                        keyAlias: "tomczhen",
                        apksToSign: "**/*-prod-release-unsigned.apk",
                        archiveSignedApks: false,
                        archiveUnsignedApks: false
                )
            }
        }

        stage('Upload') {
            steps {
                echo 'Upload'
                archiveArtifacts(onlyIfSuccessful: true, artifacts: 'app/build/outputs/apk/**/*.apk')
            }
        }

        stage('Report') {
            steps {
                echo 'Report'
            }
        }
    }

    post {
        always {
            echo 'Always Echo!'
        }
        success {
            echo 'Build Success!'
        }
        failure {
            echo 'Build Failure!'
        }
        changed {
            echo 'Build Status Changed!'
        }
        unstable {
            echo 'Test Failure!'
        }
        aborted {
            echo 'Build Aborted!'
        }
    }
}

@NonCPS
def getChangeString() {
    MAX_MSG_LEN = 100
    def changeString = ""

    echo "Gathering SCM changes"
    def changeLogSets = currentBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += "[${entry.author}] ${truncated_msg}\n"
        }
    }

    if (!changeString) {
        changeString = " - No new changes"
    }
    return changeString
}