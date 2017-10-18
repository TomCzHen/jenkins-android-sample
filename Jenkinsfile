pipeline {
  agent any
  parameters {
    choice(choices: 'dev,prod', description: '', name: 'branch')
  }
  stages {
    stage('Initialize') {
      steps {
        echo "Initialize ${params.PERSON}"
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