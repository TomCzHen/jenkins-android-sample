pipeline {
  agent any
  stages {
    stage('Initialize') {
      agent {
        docker {
          image 'hello-world'
        }
        
      }
      steps {
        sh 'echo "Initialize"'
        echo 'Initialize'
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