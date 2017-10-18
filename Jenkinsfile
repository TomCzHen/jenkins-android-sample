pipeline {
    agent any
    parameters {
        string(name: 'PERSON',
                defaultValue: 'Mr Jenkins',
                description: 'Who should I say hello to?')
        choice(name: 'BRANCH',
                choices: 'prod\ndev',
                description: '')
        booleanParam(name: 'CAN_DANCE',
                defaultValue: true,
                description: 'Checkbox parameter')
    }
    stages {
        stage('Initialize') {
            steps {
                echo 'Initialize ${params.PERSON}'
                echo 'Initialize ${params.BRANCH}'
                echo 'Initialize ${params.CAN_DANCE}'
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