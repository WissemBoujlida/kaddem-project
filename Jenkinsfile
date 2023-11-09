pipeline {
	agent any
	tools {
		jdk 'OpenJDK-11'
        }
	stages {

		stage("GIT") {

			steps {
				echo "getting project from GIT..."
				script {
					checkout([$class: 'GitSCM', 
					branches: [[name: '*/master']], 
					extensions: [], 
					userRemoteConfigs: [[credentialsId: '5d1f4f9a-979b-4a29-8e74-d15252589520', url: 'https://github.com/WissemBoujlida/kaddem-project.git']]])
				}
			}
		}

		stage("COMPILING") {

			steps {
				echo "clearing the target directory..."			
				sh "mvn clean package -DskipTests"
				echo "compiling..."
				sh "mvn compile"
			}
		}

		stage("SONARQUBE") {

			steps {
				echo "static testing using SonarQube...";
				withSonarQubeEnv('Sonar-Server'){
				    sh "mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=sonar -Dmaven.test.skip=true";
				}
			}
		}

		stage("JUNIT/MOCKITO") {

			steps {
				echo "unit testing Universite services using JUnit/Mockito..."
				sh "mvn test -Dtest=IUniversiteServiceImplTest -DfailIfNoTests=false"
			}
		}

		stage("DEPLOY ARTIFACT TO NEXUS") {

			steps {
				echo "publishing .jar artifact to Nexus..."
			    sh "mvn deploy -DskipTests"
			}
		}

		stage('BUILD DOCKER IMAGE') {

            steps {
            	echo "building docker image..."
            	sh "docker build -t kaddemproject-app ."
            }
    	}

    	stage('PUSH DOCKER IMAGE TO NEXUS') {

            steps {
            	echo "pushing docker image to Nexus..."
            	sh "docker login -u admin -p nexus http://192.168.1.13:5000"
		sh "docker tag kaddemproject-app:latest 192.168.1.13:5000/repository/docker-hosted/kaddemproject-app:latest"
		sh "docker push 192.168.1.13:5000/repository/docker-hosted/kaddemproject-app:latest"
            }
    	}

    	stage('START DOCKER COMPOSE') {

    		steps {
    			echo "starting docker compose..."
    			sh 'docker compose up -d'
            }
        }

        stage('CHECK DOCKER COMPOSE') {

        	steps {
        		echo "checking docker compose..."
        		sh 'docker compose ps'
            }
        }
	}
}
