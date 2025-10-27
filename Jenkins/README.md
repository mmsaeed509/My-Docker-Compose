# Jenkins on Docker with Webhooks

This setup provides a complete Jenkins installation with Docker support and webhook capabilities.

## Custom Docker Image

The setup uses a custom Jenkins image that includes:
- **Docker CLI** installed inside the container
- **Jenkins user added to docker group** for proper permissions
- **Docker socket mounted** for container management from Jenkins

This allows Jenkins to build, run, and manage Docker containers as part of your CI/CD pipelines.

## Quick Start

1. **Build and start Jenkins**:
   ```bash
   docker compose up -d --build
   ```

2. **Get initial admin password**:
   ```bash
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```

3. **Access Jenkins**: Open http://localhost:8081

## Initial Setup

### First Time Setup
1. Use the initial admin password from step 2 above
2. Install suggested plugins (includes Git and GitHub plugins)
3. Create your first admin user
4. Configure Jenkins URL (important for webhooks)

### Essential Plugins for Webhooks
Install these plugins via Manage Jenkins > Manage Plugins:
- **GitHub Integration Plugin** - For GitHub webhooks
- **GitLab Plugin** - For GitLab webhooks  
- **Generic Webhook Trigger Plugin** - For custom webhooks
- **Pipeline Plugin** - For pipeline jobs
- **Docker Pipeline Plugin** - For Docker in pipelines

## Webhook Configuration

### GitHub Webhooks

1. **In Jenkins**:
   - Create a new Pipeline job
   - Under "Build Triggers", check "GitHub hook trigger for GITScm polling"
   - Configure your GitHub repository in the Pipeline section

2. **In GitHub Repository**:
   - Go to Settings > Webhooks > Add webhook
   - Payload URL: `http://your-jenkins-url:8081/github-webhook/`
   - Content type: `application/json`
   - Select "Just the push event" or customize events
   - Add webhook

### GitLab Webhooks

1. **In Jenkins**:
   - Install GitLab plugin
   - Create Pipeline job
   - Under "Build Triggers", check "Build when a change is pushed to GitLab"
   - Note the GitLab webhook URL provided

2. **In GitLab Project**:
   - Go to Settings > Webhooks
   - URL: Use the webhook URL from Jenkins
   - Trigger: Push events, Merge request events, etc.
   - Add webhook

### Generic Webhooks

For custom webhook triggers:

1. **Install Generic Webhook Trigger Plugin**
2. **In Pipeline job**:
   - Check "Generic Webhook Trigger"
   - Set a unique token
   - Configure post content parameters if needed

3. **Webhook URL format**:
   ```
   http://your-jenkins-url:8081/generic-webhook-trigger/invoke?token=YOUR_TOKEN
   ```

## Sample Pipeline Scripts

### Basic Pipeline with Docker Support

Create a `Jenkinsfile` in your repository (or use the provided `sample-pipeline.groovy`):

```groovy
pipeline {
    agent any
    
    triggers {
        // GitHub webhook trigger
        githubPush()
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // Example: Build Docker image
                    sh 'docker build -t my-app .'
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    // Run tests
                    sh 'docker run --rm my-app npm test'
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Deploy logic
                    sh 'docker run -d --name my-app-prod my-app'
                }
            }
        }
    }
    
    post {
        always {
            // Cleanup
            sh 'docker system prune -f'
        }
    }
}
```

## Security Considerations

### Webhook Security
1. **Use HTTPS** in production
2. **Configure webhook secrets** when possible
3. **Restrict Jenkins access** with proper authentication
4. **Use Jenkins credentials** for sensitive data

### Network Security
```yaml
# Add to docker-compose.yaml for production
networks:
  jenkins-network:
    driver: bridge

services:
  jenkins:
    networks:
      - jenkins-network
```

## Troubleshooting

### Common Issues

1. **Webhooks not triggering**:
   - Check Jenkins URL is accessible from webhook source
   - Verify webhook URL format
   - Check Jenkins logs: `docker logs jenkins`

2. **Permission issues**:
   - Ensure Jenkins user has Docker permissions
   - Check volume mounts are correct

3. **Plugin issues**:
   - Restart Jenkins after plugin installation
   - Check plugin compatibility with Jenkins version

### Useful Commands

```bash
# View Jenkins logs
docker logs -f jenkins

# Restart Jenkins
docker restart jenkins

# Access Jenkins container
docker exec -it jenkins bash

# Backup Jenkins data
docker cp jenkins:/var/jenkins_home ./jenkins-backup
```

## Advanced Configuration

### Reverse Proxy Setup (Nginx)
For production with SSL:

```nginx
server {
    listen 443 ssl;
    server_name jenkins.yourdomain.com;
    
    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Environment Variables
Add to docker-compose.yaml:
```yaml
environment:
  - JENKINS_OPTS=--httpPort=8080 --prefix=/jenkins
  - JAVA_OPTS=-Xmx2048m -Djenkins.install.runSetupWizard=false
```