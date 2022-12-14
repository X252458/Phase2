@Ignore
Feature: RCMS Activation API - TC01 Call getRewardCommitment operation for Telus Subscriber having ACCESSORYFINANCE Renewed to ACCESSORYFINANCE

  Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token = karate.properties['karate.auth_token']
    * def subID = karate.properties['karate.subID']
    * header Authorization = 'Bearer ' + auth_token
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
    * param subscriptionId = subID;
		* param brandId = '1' 
		

  Scenario: Retrieve SPID from the LSMS
    #Set endpoint url
    Given url ENDPOINT_REWARD_COMMITMENT
    #		When REST operation GET
    When method get
