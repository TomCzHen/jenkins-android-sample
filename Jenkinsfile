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
        sh './gradlew clean assembleDebug'
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
    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
    choice(name: 'BRANCH', choices: '''prod
dev''', description: 'Choice Branch')
    booleanParam(name: 'CAN_DANCE', defaultValue: true, description: 'Checkbox parameter')
  }
}