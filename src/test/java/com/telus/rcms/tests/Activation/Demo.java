package com.telus.rcms.tests.Activation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.telus.api.test.utils.APIJava;
import com.telus.rcms.jsonPathLibrary.All;
import com.telus.rcms.utils.APIUtils;
import com.telus.rcms.utils.DBUtils;
import com.telus.rcms.utils.GenericUtils;
import com.telus.rcms.utils.JSONUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.SystemProperties;

import net.rcarz.jiraclient.User;



public class Demo extends BaseTest {


	
	String testCaseName = null;
	String scriptName = null;
	String testCaseDescription = null;
	String requestPayloadFilePath = null;
	String jsonPathLibrary = null;

	String environment = null;

	static String connectionString = null;

	static Statement statement = null;
	static ResultSet resultSet = null;

	String accountID = null;
	String subscriptionID = null;
	String subscriberNum = null;

	
	
	/**
	 * @param iTestContext
	 */
	@BeforeTest(alwaysRun = true)
	public void BeforeMethod(ITestContext iTestContext) {

		testCaseName = this.getClass().getName();
		scriptName = GenericUtils.getTestCaseName(testCaseName);
		testCaseDescription = "The purpose of this test case is to verify \"" + scriptName + "\" workflow";

		requestPayloadFilePath = "\\testSpecs\\RCMS\\Activation\\" + scriptName + ".json";
		environment = SystemProperties.EXECUTION_ENVIRONMENT;

	}

	@Test(groups = { "TestRCMCActivationFlowDemo" })
	
	public void testMethod_TestRCMCActivationFlow(ITestContext iTestContext) throws Exception {

		
		Reporting.setNewGroupName("Automation Configurations / Environment Details & Data Setup");
		Reporting.logReporter(Status.INFO,
				"Automation Configuration - Environment Configured for Automation Execution [" + environment + "]");
		Reporting.printAndClearLogGroupStatements();

		/*** Test Case Details ***/
		Reporting.setNewGroupName("Test Case Details");
		Reporting.logReporter(Status.INFO, "Test Case Name : [" + scriptName + "]");
		Reporting.logReporter(Status.INFO, "Test Case Description : [" + testCaseDescription + "]");
		Reporting.logReporter(Status.INFO, "Request Payload Path : [" + requestPayloadFilePath + "]");
		Reporting.printAndClearLogGroupStatements();

		/**
		 * API Call Steps
		 */

		/*** Test Case - Activation 1 ***/
		Reporting.setNewGroupName("ACCESS TOKEN GENERATION");
		String accessToken = APIUtils.getAccessToken(environment,"rewardService");
		Reporting.logReporter(Status.INFO, "ACCESS_TOKEN: " + accessToken);
		Reporting.printAndClearLogGroupStatements();

		/*** Activation API Call ***/
		Reporting.setNewGroupName("ACTIVATION SERVICE API CALL - TC01");
		String apiEnv = GenericUtils.getAPIEnvironment(environment);
		Reporting.logReporter(Status.INFO, "API Test Env is : [" + apiEnv + "]");
		accountID = GenericUtils.getUniqueAccountID(apiEnv);
		subscriptionID = GenericUtils.getUniqueSubscriptionID(apiEnv);
		subscriberNum = GenericUtils.getUniqueSubscriberNumber(apiEnv);
		System.setProperty("karate.auth_token", accessToken);
		System.setProperty("karate.accID", accountID);
		System.setProperty("karate.subID", subscriptionID);
		System.setProperty("karate.subNum", subscriberNum);
		System.setProperty("karate.apiEnv", apiEnv);

		Map<String, Object> apiOperation = APIJava.runKarateFeature(environment,
				"classpath:tests/RCMS/Activation/activationTC1.feature");
		Reporting.logReporter(Status.INFO, "API Operation status: " + apiOperation.get("tc01ActivateTelusSubWithAllStatus"));
		Reporting.logReporter(Status.INFO, "API Operation Request: " + apiOperation.get("tc01ActivateTelusSubWithAllRequest"));

		Reporting.printAndClearLogGroupStatements();
		
		/*** DB VALIDATION ***/
		Reporting.setNewGroupName("DB VERIFICATION - TC01");
		payloadAndDbCheck();
		Reporting.printAndClearLogGroupStatements();
		
	}
	
	public void payloadAndDbCheck() throws SQLException, IOException {

		DBUtils.callDBConnect();
		statement = DBUtils.Conn.createStatement();

		resultSet = statement.executeQuery(
				  "SELECT * FROM  CUSTOMER_SERVICE_AGREEMENT agrmt "
				+ "inner join CUST_SRVC_AGRMT_ITEM item "
				+ "on agrmt.CUSTOMER_SVC_AGREEMENT_ID = item.customer_svc_agreement_id "
				+ "and agrmt.subscriber_no ='"+subscriberNum+"' and current_ind='Y' "
				+ "inner join CUST_SRVC_AGRMT_ITM_PROMO promo "
				+ "on item.CUST_SRVC_AGRMT_ITEM_ID = promo.CUST_SRVC_AGRMT_ITEM_ID "
				+ "inner join REWARD_TXN reward "
				+ "on item.CUSTOMER_SVC_AGREEMENT_ID = reward.CUSTOMER_SVC_AGREEMENT_ID");

		// "inner join CUST_SRVC_AGRMT_ITM_TAX item_tax \n"+
		// "on item.CUST_SRVC_AGRMT_ITEM_ID = item_tax.CUST_SRVC_AGRMT_ITEM_ID \n"+
		// "inner join CUST_SRVC_AGRMT_ITM_TAX_DTL tax_dtl \n"+
		// "on item_tax.CUST_SRVC_AGRMT_ITM_TAX_ID = tax_dtl.CUST_SRVC_AGRMT_ITM_TAX_ID
		// \n"+

		/**
		 * DB Verification Steps
		 */

		All jsonPath = new All();
		String jsonString = GenericUtils.readFileAsString(requestPayloadFilePath);
		 jsonString = jsonString.replace("#(subID)",
		 subscriptionID).replace("#(subNum)", subscriberNum)
		 .replace("#(accID)", accountID);
		 Reporting.logReporter(Status.INFO, "Pretty Payload: " + jsonString);

		// Declaring variable from payload

		String AgreementDurationEndDateTime = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.AgreementDurationEndDateTime));
		AgreementDurationEndDateTime = AgreementDurationEndDateTime.split("T")[0];

		String AgreementDurationStartDateTime = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.AgreementDurationStartDateTime));
		AgreementDurationStartDateTime = AgreementDurationStartDateTime.split("T")[0];

		String AgreementDurationAmount = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.AgreementDurationAmount));

		String agreementItemid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_id));

		String agreementItemItemDurationEndDateTime = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_itemDurationEndDateTime));
		agreementItemItemDurationEndDateTime = agreementItemItemDurationEndDateTime.split("T")[0];

		String agreementItemItemDurationStartDateTime = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_itemDurationStartDateTime));
		agreementItemItemDurationStartDateTime = agreementItemItemDurationStartDateTime.split("T")[0];

		String agreementItemItemDurationAmount = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_itemDurationAmount));

		String Itemtype = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_itemType));
		String agreementItemItemType = DBUtils.getItemType(Itemtype);

		String agreementItemIncentiveAmount = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_incentiveAmount));

		String agreementItem_installmentEndDateTime = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentEndDateTime));
		agreementItem_installmentEndDateTime = agreementItem_installmentEndDateTime.split("T")[0];

		String agreementItem_installmentStartDateTime = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentStartDateTime));
		agreementItem_installmentStartDateTime = agreementItem_installmentStartDateTime.split("T")[0];

		String agreementItem_installmentAmount = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentAmount));

		String agreementItem_installmentLeftNumValue = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentLeftNumValue));

		String agreementItem_installmentAppliedNumValue = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentAppliedNumValue));

		String agreementItem_installmentAppliedAmtValue = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentAppliedAmtValue));

		String agreementItem_termOrConditionMinRatePlanValue = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_termOrConditionMinRatePlanValue));

		String agreementItem_termOrConditionMinFeatureValue = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_termOrConditionMinFeatureValue));

		String agreementItem_termOrConditionMinCombinedValue = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_termOrConditionMinCombinedValue));

		/*
		 * String agreementItem_taxPaymentMethodCode = String.valueOf(
		 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxPaymentMethodCode));
		 * 
		 * String agreementItem_taxPaymentMechanismCode = String.valueOf(
		 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxPaymentMechanismCode));
		 * 
		 * String agreementItem_taxPaymentChannelCode = String.valueOf(
		 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxPaymentChannelCode));
		 * 
		 * String agreementItem_taxProvinceCode = String
		 * .valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxProvinceCode));
		 * 
		 * String agreementItem_taxCategory = String
		 * .valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxCategory));
		 * 
		 * String agreementItem_taxRate = String
		 * .valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxRate));
		 * 
		 * String agreementItem_taxAmountValue = String
		 * .valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
		 * jsonPath.agreementItem_taxAmountValue));
		 */
		String agreementItem_productSpecificationid = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productSpecificationid));

		String agreementItem_productCharacteristicValue = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productCharacteristicValue));

		String agreementItem_promotionid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_promotionid));

		String agreementItem_promotionPerspectiveDate = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_promotionPerspectiveDate));
		agreementItem_promotionPerspectiveDate = agreementItem_promotionPerspectiveDate.split("T")[0];

		String agreementItem_productOfferingid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productOfferingid));

		String offerID = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
				jsonPath.agreementItem_productOfferingRedeemedOfferContextCodeValue));
		String agreementItem_productOfferingRedeemedOfferContextCodeValue = DBUtils.getProductOfferID(offerID);

		String agreementItem_productOfferingOfferTierCdValue = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productOfferingOfferTierCdValue));

		String agreementItem_productOfferingOfferTierCapAmtValue = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productOfferingOfferTierCapAmtValue));
		agreementItem_productOfferingOfferTierCapAmtValue = agreementItem_productOfferingOfferTierCapAmtValue
				.split("\\.")[0];

		String agreementItem_productOfferingDataCommitmentIndValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
						jsonPath.agreementItem_productOfferingDataCommitmentIndValue));
		agreementItem_productOfferingDataCommitmentIndValue = DBUtils
				.convertYN(agreementItem_productOfferingDataCommitmentIndValue);

		String agreementItem_productOfferingContractEnforcedPlanIndValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
						jsonPath.agreementItem_productOfferingContractEnforcedPlanIndValue));
		agreementItem_productOfferingContractEnforcedPlanIndValue = DBUtils
				.convertYN(agreementItem_productOfferingContractEnforcedPlanIndValue);

		String agreementItem_productOfferingPerspectiveDate = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productOfferingPerspectiveDate));
		agreementItem_productOfferingPerspectiveDate = agreementItem_productOfferingPerspectiveDate.split("T")[0];

		String agreementItem_productOfferingSourceSystemId = String.valueOf(JSONUtils
				.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productOfferingSourceSystemId));

		String relatedParty_Accid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_Accid));

		String relatedParty_brandidValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_brandidValue));

		String relatedParty_AccTypeCodeValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_AccTypeCodeValue));

		String relatedParty_AccSubTypeCodeValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_AccSubTypeCodeValue));

		String relatedParty_Oriid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_Oriid));

		String relatedParty_TransactionidValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_TransactionidValue));

		String relatedParty_ChnlOrgValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_ChnlOrgValue));

		String relatedParty_SalesRepidValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_SalesRepidValue));

		String relatedParty_TeamMemberidValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_TeamMemberidValue));

		String relatedParty_Subid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_Subid));

		String relatedParty_MarketProvinceValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_MarketProvinceValue));

		String relatedParty_HomeProvinceValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_HomeProvinceValue));

		String relatedParty_SubscriberNumValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_SubscriberNumValue));

		String relatedParty_ComboRatePlanIndValue = String.valueOf(
				JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_ComboRatePlanIndValue));
		relatedParty_ComboRatePlanIndValue = DBUtils.convertYN(relatedParty_ComboRatePlanIndValue);

		Reporting.logReporter(Status.INFO, "--------------------DB Validation Starts-----------------------");

		while (resultSet.next()) {

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_END_DT")),
					AgreementDurationEndDateTime, "AGREEMENT_END_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_START_DT")),
					AgreementDurationStartDateTime, "AGREEMENT_START_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("COMMITMENT_LENGTH_NUM")),
					AgreementDurationAmount, "COMMITMENT_LENGTH_NUM");

			/*
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("CUST_SRVC_AGRMT_ITEM_ID")), agreementItemid,
			 * "CUST_SRVC_AGRMT_ITEM_ID");
			 */

			Reporting.logReporter(Status.INFO, "CUST_SRVC_AGRMT_ITEM_ID Value from DB is : "
					+ String.valueOf(resultSet.getInt("CUST_SRVC_AGRMT_ITEM_ID")));

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("COMMITMENT_EFF_START_DT")),
					agreementItemItemDurationStartDateTime, "COMMITMENT_EFF_START_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("COMMITMENT_EFF_END_DT")),
					agreementItemItemDurationEndDateTime, "COMMITMENT_EFF_END_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("ORIG_COMMITMENT_LENGTH_NUM")),
					agreementItemItemDurationAmount, "ORIG_COMMITMENT_LENGTH_NUM");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REWARD_PROGRAM_TYP_ID")),
					agreementItemItemType, "REWARD_PROGRAM_TYP_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("INCENTIVE_AMT")),
					agreementItemIncentiveAmount, "INCENTIVE_AMT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("REWARD_INSTLMNT_END_DT")),
					agreementItem_installmentEndDateTime, "REWARD_INSTLMNT_END_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("REWARD_INSTLMNT_START_DT")),
					agreementItem_installmentStartDateTime, "REWARD_INSTLMNT_START_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REWARD_INSTLMNT_QTY")),
					agreementItem_installmentAmount, "REWARD_INSTLMNT_QTY");

			/*
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("")),
			 * agreementItem_installmentLeftNumValue, "");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("")),
			 * agreementItem_installmentAppliedNumValue, "");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("")),
			 * agreementItem_installmentAppliedAmtValue, "");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getFloat("RTPLN_MIN_COMMITMENT_AMT")),
			 * agreementItem_termOrConditionMinRatePlanValue, "RTPLN_MIN_COMMITMENT_AMT");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("FEAT_MIN_COMMITMENT_AMT")),
			 * agreementItem_termOrConditionMinFeatureValue, "FEAT_MIN_COMMITMENT_AMT");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("MIN_SRVC_COMMITMENT_LEN_MTH")),
			 * agreementItem_termOrConditionMinCombinedValue,
			 * "MIN_SRVC_COMMITMENT_LEN_MTH");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("TAX_PAYMENT_METHOD_CD ")),
			 * agreementItem_taxPaymentMethodCode, "TAX_PAYMENT_METHOD_CD ");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("TAX_PYMT_MECHANISM_CD")),
			 * agreementItem_taxPaymentMechanismCode, "TAX_PYMT_MECHANISM_CD");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("TAX_PAYMENT_CHANNEL_CD")),
			 * agreementItem_taxPaymentChannelCode, "TAX_PAYMENT_CHANNEL_CD");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(
			 * String.valueOf(resultSet.getInt("TAXATION_PROVINCE_CD ")),
			 * agreementItem_taxProvinceCode, "TAXATION_PROVINCE_CD ");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "TAX_TYPE_CD")), agreementItem_taxCategory, "TAX_TYPE_CD");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "TAX_RATE_PCT")), agreementItem_taxRate, "TAX_RATE_PCT");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "TAX_AMT")), agreementItem_taxAmountValue, "TAX_AMT");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "HANDSET_SERIAL_NUM")), agreementItem_productSpecificationid,
			 * "HANDSET_SERIAL_NUM");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "USIM_ID")), agreementItem_productCharacteristicValue, "USIM_ID");
			 */
			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REDEEMED_PROMOTION_ID")),
					agreementItem_promotionid, "REDEEMED_PROMOTION_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("REDEEMED_PROMOTION_TS")),
					agreementItem_promotionPerspectiveDate, "REDEEMED_PROMOTION_TS");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REDEEMED_OFFER_ID")),
					agreementItem_productOfferingid, "REDEEMED_OFFER_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REDEEMED_OFFER_TYPE_ID")),
					agreementItem_productOfferingRedeemedOfferContextCodeValue, "REDEEMED_OFFER_TYPE_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REDEEMED_OFFER_TIER_CD")),
					agreementItem_productOfferingOfferTierCdValue, "REDEEMED_OFFER_TIER_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REDEEMED_OFFER_TIER_CAP_AMT")),
					agreementItem_productOfferingOfferTierCapAmtValue, "REDEEMED_OFFER_TIER_CAP_AMT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("DATA_SRVC_REQ_IND")),
					agreementItem_productOfferingDataCommitmentIndValue, "DATA_SRVC_REQ_IND");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("COMB_MIN_CMITMT_DISCHRG_IND")),
					agreementItem_productOfferingContractEnforcedPlanIndValue, "COMB_MIN_CMITMT_DISCHRG_IND");

			 GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("REDEEMED_OFFER_TS")), 
					 agreementItem_productOfferingPerspectiveDate, "REDEEMED_OFFER_TS"); 
			 
			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("REDEEMED_OFFER_SYS_ID")),
					agreementItem_productOfferingSourceSystemId, "REDEEMED_OFFER_SYS_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BAN")), accountID, "BAN");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BRAND_ID")),
					relatedParty_brandidValue, "BRAND_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("ACCOUNT_TYPE_CD")),
					relatedParty_AccTypeCodeValue, "ACCOUNT_TYPE_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("ACCOUNT_SUBTYPE_CD")),
					relatedParty_AccSubTypeCodeValue, "ACCOUNT_SUBTYPE_CD");

			/*
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("")),
			 * relatedParty_Oriid, "");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "REWARD_TXN_ID")), relatedParty_TransactionidValue, "REWARD_TXN_ID");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "CHNL_ORG_ID")), relatedParty_ChnlOrgValue, "CHNL_ORG_ID");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "SALES_REP_ID")), relatedParty_SalesRepidValue, "SALES_REP_ID");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "TEAM_MEMBER_ID")), relatedParty_ChnlOrgValue, "TEAM_MEMBER_ID");
			 */
			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("SUBSCRIPTION_ID")),
					subscriptionID, "SUBSCRIPTION_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("CUST_PHONE_PROV_CD")),
					relatedParty_MarketProvinceValue, "CUST_PHONE_PROV_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("CUST_HOME_PROV_CD")),
					relatedParty_HomeProvinceValue, "CUST_HOME_PROV_CD");
			
			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getLong("SUBSCRIBER_NO")),
					subscriberNum, "SUBSCRIBER_NO");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("COMBO_RTPLN_IND")),
					relatedParty_ComboRatePlanIndValue, "COMBO_RTPLN_IND");

		}

		Reporting.logReporter(Status.INFO, "--------------------DB Validation Completed--------------------");
		

	}

	

	/**
	 * Close DB Connection
	 */
	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		Reporting.setNewGroupName("Close DB Connection");
		try {
			DBUtils.dbDisConnect();
		} catch (SQLException e) {
			Reporting.logReporter(Status.INFO, "DB Connection Closed Successfully!");
		}
		Reporting.printAndClearLogGroupStatements();
	}

	/**
	 * Close any opened browser instances
	 */
	@AfterMethod(alwaysRun = true)
	public void afterTest1() {
		Reporting.setNewGroupName("Close All Browser");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();
	}
}