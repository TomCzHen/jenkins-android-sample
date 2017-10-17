pipeline {
  agent {
    docker {
      image 'hello-world'
    }
    
  }
  stages {
    stage('Initialize') {
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