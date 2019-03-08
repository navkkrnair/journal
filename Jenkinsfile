pipeline {
    agent any
    environment {
        def mvnHome = tool name: 'maven3.6.0', type: 'maven'
        def mvnCmd = "${mvnHome}/bin/mvn"
    }
    stages 
    {
	    stage('SCM Checkout') 
	    {
		    steps 
		    {
	    	     git credentialsId: 'githubcredentials', url: 'https://github.com/navkkrnair/journal.git'
	    	}     
	    }
	    stage('Mvn package') 
	    {
		    steps 
		    {
	    	     sh label: 'Creating jar file', script: "${mvnCmd} -DskipTests -Dmaven.test.failure.ignore clean package"
	    	}     
	    }		 
		stage('Build docker image') 
	    {
		    steps 
		    {
	    	     sh label: 'Creating Journal Image', script: 'docker build -t navkkrnair/journal:1.0 .'
	    	}     
	    }
	    stage('Push image to docker hub') 
	    {
		    steps 
		    {
	    	    withCredentials([string(credentialsId: 'dockersecret', variable: 'dockersecret')]) 
	    	    {
    				sh label: 'Login in to Docker hub', script: "docker login -u navkkrnair -p ${dockersecret}"
       			}
       			sh label: 'Pushing Journal Image to hub', script: 'docker push navkkrnair/journal:1.0'
  	    	}     
	    }
	}
}
