function() {    
    
    karate.log('Env Params :', karate.env);
     
     var envName = karate.env;
  
  // var appName = karate.properties['telus.appName'];
     // karate.log('telus.appName system property was:', appName);
  
  var config = karate.read('classpath:config/AppConfig.json');  
  config.myEndPoints = karate.read('classpath:config/EndPoints.json');
  
   if (envName == 'PT140') {
	    config.ENDPOINT_LSMS= config.myEndPoints.PT140.ENDPOINT_LSMS
  	    config.ENDPOINT_2FA= config.myEndPoints.PT140.ENDPOINT_2FA
  	    config.ENDPOINT_PORTIN_PENDING= config.myEndPoints.PT140.ENDPOINT_PORTIN_PENDING
  	    config.ENDPOINT_OOPS= config.myEndPoints.PT140.ENDPOINT_OOPS
  	} else if (envName == 'PT148') {
  		config.ENDPOINT_LSMS= config.myEndPoints.PT148.ENDPOINT_LSMS
    	config.ENDPOINT_2FA= config.myEndPoints.PT148.ENDPOINT_2FA
    	config.ENDPOINT_PORTIN_PENDING= config.myEndPoints.PT148.ENDPOINT_PORTIN_PENDING
    	config.ENDPOINT_OOPS= config.myEndPoints.PT148.ENDPOINT_OOPS
    } else if (envName == 'PT168') {
    	config.ENDPOINT_LSMS= config.myEndPoints.PT168.ENDPOINT_LSMS
    	config.ENDPOINT_2FA= config.myEndPoints.PT168.ENDPOINT_2FA
    	config.ENDPOINT_PORTIN_PENDING= config.myEndPoints.PT168.ENDPOINT_PORTIN_PENDING
    	config.ENDPOINT_OOPS= config.myEndPoints.PT168.ENDPOINT_OOPS
    } 
   

  karate.configure('connectTimeout', 116000);
  karate.configure('readTimeout', 116000);
  return config;
}