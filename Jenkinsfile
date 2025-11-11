pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo '📥 Pulling latest code from GitHub...'
                git branch: 'main', url: 'https://github.com/Amrithkishore14/cloud-lb.git'
            }
        }

        stage('Build') {
            steps {
                echo '⚙️ Building Cloud Load Balancer project...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy HostManager & Containers') {
            steps {
                echo '🚀 Deploying HostManager and FileStore containers...'
                sh 'chmod +x scripts/deploy_hostmanager.sh'
                sh './scripts/deploy_hostmanager.sh'
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful! CloudLB is running.'
        }
        failure {
            echo '❌ Build failed. Please check the console logs.'
        }
    }
}

