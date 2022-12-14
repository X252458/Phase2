@Ignore
Feature: RCMS Renewal API - TC11_Perform_renewal_from_TAB_HWS_to_TAB_HWS_with_charge_and_full_waive

  Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token = karate.properties['karate.auth_token']
    * def accID = karate.properties['karate.accID']
    * def subID = karate.properties['karate.subID']
    * def subNum = karate.properties['karate.subNum']
    * def startDate = karate.properties['karate.startDate']
    * def payload = read(PATH_API_PAYLOAD + 'Renewal/TC11_Telus_TAB_HWS_to_TAB_HWS_Pay_charge_full_waive.json')
    * header Authorization = 'Bearer ' + auth_token
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
    * param actionName = 'RENEWAL'

  Scenario: Firing API
    #Set endpoint url
    Given url 'https://apigw-st.tsl.telus.com/customer/loyaltyAgreement/v1/loyaltyAgreement/70919909'
    #Request XML passed for the operation and printing the same for verification
    And request payload
    #		When REST operation post
    When method patch
