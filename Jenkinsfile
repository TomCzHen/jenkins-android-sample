pipeline {
  agent any
  stages {
    stage('Initialize') {
      agent {
        docker {
          image 'busybox'
        }
        
      }
      steps {
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