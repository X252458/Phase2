Feature: Updating TIAssetCredit and TIPromoCredit installment date

  Scenario: Verify return on installment date
    #Operation 1
    When def tc01returnTelusSubWithDFAFTICredit = call read(PATH_API_OPS+'Return/callReturnTC1.feature')
    #Request
    * json tc01returnTelusSubWithDFAFTICreditRequest = tc01returnTelusSubWithDFAFTICredit.payload
    #Status
    * def tc01returnTelusSubWithDFAFTICreditStatus = tc01returnTelusSubWithDFAFTICredit.responseStatus
    #Validation
    Then match tc01returnTelusSubWithDFAFTICreditStatus == 200
