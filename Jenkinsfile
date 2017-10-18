pipeline {
  agent any
  parameters {
    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
    choice(choices: 'prod/dev', description: '', name: 'BRANCH')
  }
  stages {
    stage('Initialize') {
      steps {
        echo 'Initialize ${params.PERSON}'
        echo 'Initialize ${params.BRANCH}'
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