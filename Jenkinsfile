pipeline {
  agent {
    docker {
      image 'hello-world'
    }
    
  }
  stages {
    stage('Initialize') {
      steps {
        echo 'initialize'
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