@Ignore
Feature: RCMS Activation API - TC01 Call getRewardAccountInformation operation for Telus Subscriber having DF_AF_ACB_TIA_TIP Renewed to DF_Pay_BILL

  Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token = karate.properties['karate.auth_token']
    * def accID = karate.properties['karate.accID']
    * def subID = karate.properties['karate.subID']
    * def subNum = karate.properties['karate.subNum']
    * def startDate = karate.properties['karate.startDate']
    * def payload = read(PATH_API_PAYLOAD + 'Renewal/getResumePenalty/TC09_Telus_DB_Renewed_DB_RCB.json')
    * header Authorization = 'Bearer ' + auth_token
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
    * param actionName = 'RENEWAL'

  Scenario: Retrieve SPID from the LSMS
    #Set endpoint url
    Given url ENDPOINT_RENEWAL
    #Request XML passed for the operation and printing the same for verification
    And request payload
    #		When REST operation post
    When method patch
