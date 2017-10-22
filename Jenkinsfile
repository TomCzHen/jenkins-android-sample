pipeline {
    agent any
    stages {
        stage('Initialize') {
            steps {
                echo 'Initialize...'
                echo "PERSON=${params.PERSON} BRANCH=${params.BRANCH} CAN_DANCE=${params.CAN_DANCE}"
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                sh './gradlew clean assembleRelease'
            }
        }
        stage('Sign APK') {
            steps {
                echo 'Sign APK'
                signAndroidApks(
                        keyStoreId: "94ddde4c-7b9e-459f-bd15-11eda63541de",
                        keyAlias: "tomczhen",
                        apksToSign: "**/*-unsigned.apk",
                        archiveSignedApks: false,
                        archiveUnsignedApks: false
                )
            }
        }
        stage('Upload') {
            steps {
                echo 'Upload'
                archiveArtifacts(onlyIfSuccessful: true, artifacts: 'app/build/outputs/apk/*.apk')
            }
        }
        stage('Report') {
            steps {
                echo 'Report'
            }
        }
    }
    parameters {
        string(
                name: 'PERSON',
                defaultValue: 'Mr Jenkins',
                description: 'Who should I say hello to?'
        )
        choice(
                name: 'BRANCH',
                choices: 'prod\ndev',
                description: 'Choice Branch'
        )
        booleanParam(
                name: 'CAN_DANCE',
                defaultValue: true,
                description: 'Checkbox parameter'
        )
    }
    post {
        always {
            echo 'Always Echo!'
        }
        success {
            echo 'Build Success!'
        }
    }
}