pipeline {
    agent any
    stages {
        stage('Initialize') {
            steps {
                echo "Initialize ${params.PERSON}"
                input message: '', parameters: [choice(choices: ['dev', 'prod'], description: '', name: 'choice')]
            }
        }
        stage('Build') {
            steps {
                echo 'Build'
            }
        }
        stage('Upload') {
            steps {
                echo 'Upload'
            }
        }
        stage('Report') {
            steps {
                echo 'Report'
            }
        }
    }
}