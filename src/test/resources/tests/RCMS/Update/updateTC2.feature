Feature: Updating All Device return

  Scenario: Verify update on installment date
    #Operation 1
    When def tc02UpdateTelusSubWithAllUpdate = call read(PATH_API_OPS+'Update/callUpdateTC2.feature')
    #Request
    * json tc02UpdateTelusSubWithAllUpdateRequest = tc02UpdateTelusSubWithAllUpdate.payload
    #Status
    * def tc02UpdateTelusSubWithAllUpdateStatus = tc02UpdateTelusSubWithAllUpdate.responseStatus
    #Validation
    Then match tc02UpdateTelusSubWithAllUpdateStatus == 200
