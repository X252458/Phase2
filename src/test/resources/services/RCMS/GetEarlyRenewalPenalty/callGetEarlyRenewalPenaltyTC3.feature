@Ignore
Feature: TC03 Call getEarlyRenewalPenalty for Telus Subscriber with DB+DF+BIB+ACB+TIASSETCREDIT+TIPROMOCREDIT+ACCESSORYFINANCE

  Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token_violation = karate.properties['karate.auth_token_violation']
    * def subID = karate.properties['karate.subID']
    * header Authorization = 'Bearer ' + auth_token_violation
    * def payload = read(PATH_API_PAYLOAD +'EarlyRenewalPenalty/CommonEarlyRenewalPenalty.json')
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
 
	
  Scenario: Firing API

    Given url 'https://apigw-st.tsl.telus.com/customer/loyaltyAgreementViolation/v1/slaViolation'
    And request payload
    When method post
