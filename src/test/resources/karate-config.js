function() {    
    
    karate.log('Env Params :', karate.env);
     
     var envName = karate.env;
  
  // var appName = karate.properties['telus.appName'];
     // karate.log('telus.appName system property was:', appName);
  
  var config = karate.read('classpath:config/AppConfig.json');  
  config.myEndPoints = karate.read('classpath:config/EndPoints.json');
  
   if (envName == 'PT140') {
	    config.ENDPOINT_ACTIVATION= config.myEndPoints.PT140.ENDPOINT_ACTIVATION
  	    config.ENDPOINT_EARLY_RENEWAL_PENALTY= config.myEndPoints.PT140.ENDPOINT_EARLY_RENEWAL_PENALTY
  	    config.ENDPOINT_MIGRATION_PENALTY= config.myEndPoints.PT140.ENDPOINT_MIGRATION_PENALTY
  	    config.ENDPOINT_TERMINATION_PENALTY= config.myEndPoints.PT140.ENDPOINT_TERMINATION_PENALTY
  	    config.ENDPOINT_REWARD_COMMITMENT= config.myEndPoints.PT140.ENDPOINT_REWARD_COMMITMENT
  	} else if (envName == 'PT148') {
  		config.ENDPOINT_ACTIVATION= config.myEndPoints.PT148.ENDPOINT_ACTIVATION
  	    config.ENDPOINT_EARLY_RENEWAL_PENALTY= config.myEndPoints.PT148.ENDPOINT_EARLY_RENEWAL_PENALTY
  	    config.ENDPOINT_MIGRATION_PENALTY= config.myEndPoints.PT148.ENDPOINT_MIGRATION_PENALTY
  	    config.ENDPOINT_TERMINATION_PENALTY= config.myEndPoints.PT148.ENDPOINT_TERMINATION_PENALTY
  	    config.ENDPOINT_REWARD_COMMITMENT= config.myEndPoints.PT148.ENDPOINT_REWARD_COMMITMENT
    } else if (envName == 'PT168') {
    	config.ENDPOINT_ACTIVATION= config.myEndPoints.PT168.ENDPOINT_ACTIVATION
  	    config.ENDPOINT_EARLY_RENEWAL_PENALTY= config.myEndPoints.PT168.ENDPOINT_EARLY_RENEWAL_PENALTY
  	    config.ENDPOINT_MIGRATION_PENALTY= config.myEndPoints.PT168.ENDPOINT_MIGRATION_PENALTY
  	    config.ENDPOINT_TERMINATION_PENALTY= config.myEndPoints.PT168.ENDPOINT_TERMINATION_PENALTY
  	    config.ENDPOINT_REWARD_COMMITMENT= config.myEndPoints.PT168.ENDPOINT_REWARD_COMMITMENT
    } 
   

  karate.configure('connectTimeout', 116000);
  karate.configure('readTimeout', 116000);
  return config;
}
