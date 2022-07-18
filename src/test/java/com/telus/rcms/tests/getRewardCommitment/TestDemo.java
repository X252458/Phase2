package com.telus.rcms.tests.getRewardCommitment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.telus.api.test.utils.APIJava;
import com.telus.rcms.jsonPathLibrary.ActivationPayloadJsonPath;
import com.telus.rcms.jsonPathLibrary.AgreementItem;
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

public class TestDemo extends BaseTest {

	String testCaseName = null;
	String scriptName = null;
	String testCaseDescription = null;
	String requestPayloadFilePath = null;
	String jsonPathLibrary = null;

	String environment = null;
	static String connectionString = null;

	String accountID = null;
	String subscriptionID = "8493928";
	String subscriberNum = null;

	String startDate = null;
	String jsonString = null;

	/**
	 * @param iTestContext
	 */
	@BeforeTest(alwaysRun = true)
	public void BeforeMethod(ITestContext iTestContext) {

		testCaseName = this.getClass().getName();
		scriptName = GenericUtils.getTestCaseName(testCaseName);
		testCaseDescription = "The purpose of this test case is to verify \"" + scriptName + "\" workflow";
		environment = SystemProperties.EXECUTION_ENVIRONMENT;
	}

	@Test(groups = { "TestRCMSActivationAccessoryFinance" })

	public void testMethod_TestRCMCActivationFlow(ITestContext iTestContext) throws Exception {

		requestPayloadFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testSpecs\\RCMS\\temp.json";
		Reporting.logReporter(Status.INFO, "requestPayloadFilePath:  "+requestPayloadFilePath);
		
		jsonString = GenericUtils.readFileAsString(requestPayloadFilePath);

		/*** DB VALIDATION ***/
		Reporting.setNewGroupName("DB VERIFICATION - TC01");
		responseAndDbCheck();
		Reporting.printAndClearLogGroupStatements();

	}

	public void responseAndDbCheck() throws SQLException, IOException {

//		DBUtils.callDBConnect();

		/**
		 * DB Verification Steps
		 */

		Reporting.logReporter(Status.INFO, "Pretty Payload: " + jsonString);

		
		HashMap<String, Integer> noOfAgrmt = new HashMap<String, Integer>();
		HashMap<String, Integer> noOfAgrmtItem = new HashMap<String, Integer>();
		
		noOfAgrmt=GenericUtils.NoOfAgrmt(jsonString);
		Reporting.logReporter(Status.INFO, "Values of noOfAgrmt  : " +noOfAgrmt.entrySet());
		
		for(int i=0;i<noOfAgrmt.size();i++)
		{
		noOfAgrmtItem = GenericUtils.NoOfAgrmtItem(jsonString,i);
		}
		
		Reporting.logReporter(Status.INFO, "Values of noOfAgrmtItem : " +noOfAgrmtItem.entrySet());
		
		// GenericUtils.responseDBCheckAgrmtItemNew(jsonString,subscriptionID,1,1);

		Reporting.logReporter(Status.INFO, "--------------------DB Validation Completed--------------------");
	}

	/**
	 * Close Connections
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
		Reporting.setNewGroupName("Close All Browser");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();
	}

}