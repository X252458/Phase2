Feature: tc03 Activate DB+DF+RCB+AF

  @v1-0
  Scenario: Verify Activation Koodo Subscriber with SIM only
    #Operation 1
    When def tc03ActivateDB_DF_RCB_AF = call read(PATH_API_OPS+'Activation/callActivationTC3.feature')
    #Request
    * json tc03ActivateDB_DF_RCB_AFRequest = tc03ActivateDB_DF_RCB_AF.payload
    #Status
    * def tc03ActivateDB_DF_RCB_AFStatus = tc03ActivateDB_DF_RCB_AF.responseStatus
    #Validation
    Then match tc03ActivateDB_DF_RCB_AFStatus == 200
