@Ignore
Feature: RCMS Activation API - TC01 Call getRewardCommitment operation for Telus Subscriber having ACCESSORYFINANCE Renewed to ACCESSORYFINANCE

  Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token_violation = karate.properties['karate.auth_token_violation']
    * def subID = karate.properties['karate.subID']
    * header Authorization = 'Bearer ' + auth_token_violation
    * def payload = read(PATH_API_PAYLOAD +'EarlyRenewalPenalty/CommonEarlyRenewalPenalty_with_LoyaltyType.json')
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
 
	
  Scenario: Firing GetEarlyRenewalPenalty API

    Given url 'https://apigw-st.tsl.telus.com/customer/loyaltyAgreementViolation/v1/slaViolation'
    And request payload
    When method post
    Then print response
