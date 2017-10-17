pipeline {
  agent none
  stages {
    stage('Initialize') {
      agent {
        docker {
          image 'hello-world'
        }
        
      }
      steps {
        sh 'echo "Initialize"'
      }
    }
    stage('Build') {
      agent {
        docker {
          image 'hello-world'
        }
        
      }
      steps {
        echo 'Build'
      }
    }
  }
}