Feature: RCMS Activation API - TC03 Call getTerminationPenalty for Telus Subscriber Activated with  DB+DF+BIB+ACB+TIASSETCREDIT+TIPROMOCREDIT+AF

  Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token = karate.properties['karate.auth_token_violation']
    * def subID = karate.properties['karate.subID']
    * header Authorization = 'Bearer ' + auth_token
    * def payload = read(PATH_API_PAYLOAD +'TerminationPenalty/CommonTerminationPenalty.json')
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
 
	
  Scenario: Firing GetEarlyRenewalPenalty API

    Given url 'https://apigw-st.tsl.telus.com/customer/loyaltyAgreementViolation/v1/slaViolation'
    And request payload
    When method post