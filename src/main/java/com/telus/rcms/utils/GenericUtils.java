package com.telus.rcms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.aventstack.extentreports.Status;
import com.telus.rcms.jsonPathLibrary.ActivationPayloadJsonPath;
import com.telus.rcms.jsonPathLibrary.AgreementItem;
import com.telus.rcms.jsonPathLibrary.GetEarlyRenewalPenalty;
import com.telus.rcms.jsonPathLibrary.GetRewardCommitment;
import com.telus.rcms.jsonPathLibrary.GetRewardCommitmentAgreementItem;
import com.telus.rcms.jsonPathLibrary.GetRewardCommitmentNew;
import com.test.files.interaction.ReadJSON;
import com.test.files.interaction.ReadXML;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseSteps;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSession;
import com.test.utils.SystemProperties;

public class GenericUtils {

	static File newFile = null;
	static String apiTestEnvironemnt = null;
	static String apiTestSystem = null;

	static String env = SystemProperties.EXECUTION_ENVIRONMENT;

	static String agrmtId = null;
	static String currentIND = null;
	static String AgreementDurationAmount = null;
	static String Itemtype = null;
	static String custAgmtItemId = null;

	/**
	 * Method Desc
	 * 
	 * @param env
	 * @return
	 * @throws SQLException
	 */
	public static String getUniqueAccountID(String env) throws SQLException {

		String accountIDPrefix02 = "7192";
		String accountIDPrefix03 = "7192";
		String accountIDPrefix04 = "7192";

		String accountID = null;
		Random rad = new Random();

		switch (env) {
		case "it01":
		case "it02":
			do {
				accountID = accountIDPrefix02 + String.valueOf(1000 + rad.nextInt(8999));
			} while (DBUtils.DBAccountIDAvailability(accountID));
			break;

		case "it03":

		case "it04":
		}
		Reporting.logReporter(Status.INFO, "Auto-Generated BAN : [" + accountID + "]");
		return accountID;
	}

	public static String getUniqueSubscriptionID(String env) throws SQLException {

		String subscriptionIDPrefix02 = "861";
		String subscriptionIDPrefix03 = "861";
		String subscriptionIDPrefix04 = "861";

		String subscriptionID = null;
		Random rad = new Random();

		switch (env) {
		case "it02":
		case "it01":
			do {
				subscriptionID = subscriptionIDPrefix02 + String.valueOf(1000 + rad.nextInt(8999));
			} while (DBUtils.DBSubscriptionIDAvailability(subscriptionID));
			break;

		case "it04":
			do {
				subscriptionID = subscriptionIDPrefix02 + String.valueOf(1000 + rad.nextInt(8999));
			} while (DBUtils.DBSubscriptionIDAvailability(subscriptionID));
			break;
		}
		Reporting.logReporter(Status.INFO, "Auto-Generated SUBSCRIPTION_ID : [" + subscriptionID + "]");
		return subscriptionID;
	}

	public static String getUniqueSubscriberNumber(String env) throws SQLException {

		String subscriptionNumPrefix02 = "4273";
		String subscriptionNumPrefix03 = "4273";
		String subscriptionNumPrefix04 = "4273";

		String subscriptionNum = null;
		Random rad = new Random();

		switch (env) {

		case "it02":
		case "it01":
			do {
				subscriptionNum = subscriptionNumPrefix02 + String.valueOf(100000 + rad.nextInt(899999));
			} while (DBUtils.DBSubscriptionNumberAvailability(subscriptionNum));
			break;

		case "it03":

		case "it04":

		}
		Reporting.logReporter(Status.INFO, "Auto-Generated SUBSCRIPTION_NUMBER : [" + subscriptionNum + "]");
		return subscriptionNum;
	}

	/***
	 * This method is used to validate Assert.assertEquals based on parameter passed
	 * 
	 * @param actualValue
	 * @param expectedValue
	 * @param columnName
	 */

	public static void validateAssertEqualsFromDB(Object actualValue, Object expectedValue, String columnName) {
		try {
			Assert.assertEquals(actualValue, expectedValue,
					"Values are Different. Actual Value from DB Column - " + columnName + " is : " + actualValue);
			Reporting.logReporter(Status.INFO,
					"Expected Value is: [" + expectedValue + "], Actual Value From DB Column - " + columnName + " is: ["
							+ actualValue + "], VALIDATION : [SUCCESSFUL]");
		} catch (NullPointerException e) {
			Assert.assertNull(actualValue);
			Assert.assertNull(expectedValue);
			Reporting.logReporter(Status.INFO,
					"Expected Value is: [" + expectedValue + "], Actual Value From DB Column - " + columnName + " is: ["
							+ actualValue + "], VALIDATION : [SUCCESSFUL]");

		}

	}

	public static void validateAssertEquals(Object actualValue, Object expectedValue, String columnName) {
		try {
			Assert.assertEquals(actualValue, expectedValue,
					"Values are Different. Actual Value for - " + columnName + " is : " + actualValue);
			Reporting.logReporter(Status.INFO, "Expected Value is: [" + expectedValue + "], Actual Value for - "
					+ columnName + " is: [" + actualValue + "], VALIDATION : [SUCCESSFUL]");
		} catch (NullPointerException e) {
			Assert.assertNull(actualValue);
			Assert.assertNull(expectedValue);
			Reporting.logReporter(Status.INFO, "Expected Value is: [" + expectedValue + "], Actual Value for - "
					+ columnName + " is: [" + actualValue + "], VALIDATION : [SUCCESSFUL]");

		}

	}

	public static void validateAssertNotNull(Object actualValue, String columnName) {
		try {
			Assert.assertNotNull(actualValue, "Values is Null");
			Reporting.logReporter(Status.INFO, "Expected Value is : not null , Actual Value From " + columnName
					+ " is: [" + actualValue + "], VALIDATION : [SUCCESSFUL]");
		} catch (NullPointerException e) {
			Assert.assertNull(actualValue);
			Reporting.logReporter(Status.INFO, "Expected Value is : not null , Actual Value From " + columnName
					+ " is: [" + actualValue + "], VALIDATION : [SUCCESSFUL]");

		}

	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @return current date in MM DD YYYY format.
	 */
	public static String getCurrentDateInMMDDYYYY() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

		return dateFormat.format(date);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding 30 minutes of delay.
	 */
	public static String getDDDTWithDelay_new(int delayInMinutes) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		date = DateUtils.addMinutes(date, delayInMinutes);

		Reporting.logReporter(Status.INFO, "Changed DDDT Value: " + date);
		return dateFormat.format(date);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding specific minutes of delay.
	 * 
	 */
	public static String getDDDTWithDelay(int delayInMinutes) {

		Instant instant = Instant.now();
		Instant delayedTime = instant.plus(delayInMinutes, ChronoUnit.MINUTES);

		ZonedDateTime zdtNewYork = delayedTime.atZone(ZoneId.of("America/New_York"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
		return zdtNewYork.format(formatter);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding 30 minutes of delay.
	 */
	public static String getDDDTWithDelayFutureDate(int days) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		date = DateUtils.addDays(date, days);
		return dateFormat.format(date);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding specific minutes of delay.
	 * 
	 */
	public static String getDDDTWithDelayInPACFormat(int delayInMinutes) {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		date = DateUtils.addMinutes(date, delayInMinutes);
		return dateFormat.format(date);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding specific minutes of delay.
	 * 
	 */
	public static String getDDDTWithDelayInPACFormat_Old(int delayInMinutes) {

		Instant instant = Instant.now();
		Instant delayedTime = instant.plus(delayInMinutes, ChronoUnit.MINUTES);

		ZonedDateTime zdtNewYork = delayedTime.atZone(ZoneId.of("America/New_York"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		return zdtNewYork.format(formatter);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding 30 minutes of delay.
	 */
	public static String getFutureDateFromCurrentDate(int addDays) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		date = DateUtils.addDays(date, addDays);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @return current system date in mm/dd/yyyy format
	 */
	public static String getSystemDateInMMDDYYYY() {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(currentDate);
	}

	/**
	 * 
	 * @return current system date in mm/dd/yyyy format
	 */
	public static String getSystemDateInMMDDYYYYInPST() {
		Instant instant = Instant.now();
		ZonedDateTime zdtNewYork = instant.atZone(ZoneId.of("America/Los_Angeles"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

		return zdtNewYork.format(formatter);
	}

	/**
	 */

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding specific minutes of delay.
	 * 
	 */
	public static String getSystemDateInMMDDYYYY_Nodes() {

		Instant instant = Instant.now();

		ZonedDateTime zdtNewYork = instant.atZone(ZoneId.of("America/New_York"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return zdtNewYork.format(formatter);

	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding specific minutes of delay.
	 * @throws ParseException
	 * 
	 */
	public static String getDateInESTTimeZoneInMMDDYYYY_Nodes(String expectedDDT) {

		Date date = new Date();

		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		try {
			formatter.parse(expectedDDT);
		} catch (ParseException e) {
			Reporting.logReporter(Status.INFO, "Unable to parse dateTime" + e.getMessage());
		}

		// Set the formatter to use a different timezone
		formatter.setTimeZone(TimeZone.getTimeZone("EST"));

		return formatter.format(date);

	}

	/**
	 * 
	 * @return current system date in MMM DD, YYYY format
	 */
	public static String getSystemDateInMMMDDYYYY() {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat;
		dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		return dateFormat.format(currentDate);
	}

	/**
	 * 
	 * @return Current system time in h:mm:ss:ttt format
	 */
	public static String getSystemTimeInHHMMSSTTT() {
		Date currentDate = new Date();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String timeOnly = dateTimeFormat.format(currentDate).split(" ")[1];
		return timeOnly;
	}

	public static void updateLSMSSPIDPayload(String xmlFilePath, String sub) {
		ReadXML.updateFirtsMatchingElementText(xmlFilePath, "wtn", sub);
	}

	/**
	 * 
	 * @param element
	 * @param tagName
	 * @return string value of xml node
	 */
	protected static String getStringValueOfNode(Element element, String tagName) {

		String actualVal = null;
		try {
			NodeList list = element.getElementsByTagName(tagName);
			if (list != null && list.getLength() > 0) {
				NodeList subList = list.item(0).getChildNodes();

				if (subList != null && subList.getLength() > 0) {
					actualVal = subList.item(0).getNodeValue();
				}
			}
		} catch (DOMException e) {
			Reporting.logReporter(Status.DEBUG, "Unable to retrieve node value from XML: " + e);
		}
		return actualVal;

	}

	/**
	 * 
	 * @param xmlResponse
	 * @param searchNode
	 * @return
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String returnKeyValueFromXMLNode(String xmlResponse, String searchNode)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {

		Element rootElement = null;

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));
			rootElement = document.getDocumentElement();
		} catch (Exception e) {
			Reporting.logReporter(Status.DEBUG, "Unable to read value from XML Object : " + e);
			return "";
		}

		return getStringValueOfNode(rootElement, searchNode);

	}

	public static String getTestCaseName(String qualifiedTcName) {
		int lastIndex = qualifiedTcName.lastIndexOf('.');
		return qualifiedTcName.substring(lastIndex + 1);

	}

	/**
	 * Retrieve the value withing JSON Object/Node/key specified- AUthor : Mukesh
	 * Pandit
	 */
	public static String getKeyValueFromJsonNode(String fileName, String node, String key) {
		JSONObject obj = null;
		String output = null;

		try {
			JSONObject jsonFile = new JSONObject(ReadJSON.parse(fileName));
			obj = jsonFile.getJSONObject(node);
			output = obj.getString(key);
		} catch (JSONException e) {
			Reporting.logReporter(Status.DEBUG, "JSON_EXCEPTION " + e);
		}

		return output;
	}

	/**
	 * Retrieve the value withing JSON Object/Node/key specified- AUthor : Mukesh
	 * Pandit
	 */
	public static JSONObject getJSONObjectFromJSONFile(String fileName) {
		JSONObject jsonObj = null;

		try {
			jsonObj = new JSONObject(ReadJSON.parse(fileName));

		} catch (JSONException e) {
			Reporting.logReporter(Status.DEBUG, "JSON_EXCEPTION " + e);
		}

		return jsonObj;
	}

	/**
	 * 
	 * @param maxRange
	 * @return random integer
	 */
	public static int generateRandomInteger(int maxRange) {
		int len = String.valueOf(maxRange).length();

		Random random = new Random();
		int rand = 0;
		while (true) {
			rand = random.nextInt(maxRange);

			if (rand != 0 && String.valueOf(rand).length() == len) {
				break;
			}
		}
		return rand;
	}

	/**
	 * 
	 * @param maxRange
	 * @return random integer
	 */
	public static String generateRandomIntegerInStringFormat(int maxRange) {
		int len = String.valueOf(maxRange).length();
		String str = "";
		Random random = new Random();
		int rand = 0;
		while (true) {
			rand = random.nextInt(maxRange);

			if (rand != 0 && String.valueOf(rand).length() == len) {
				str = str + String.valueOf(rand);
				break;
			}
		}
		return str;
	}

	/**
	 * 
	 * @param mainStr
	 * @param separator
	 * @return arraylist of String
	 */
	public static ArrayList<String> getListFromString(String mainStr, String separator) {

		ArrayList<String> expectedList = new ArrayList<String>();

		for (String item : mainStr.split(separator)) {
			expectedList.add(item);
		}
		return expectedList;

	}

	public static String getHyphenSeparatedTN(String TN) {
		TN = TN.substring(0, 3) + "-" + TN.substring(3);
		TN = TN.substring(0, 7) + "-" + TN.substring(7);
		return TN;
	}

	public static void openLoginPopUpBasedApplication(String appName, String un, String pwd) {

		Reporting.logReporter(Status.INFO, "openApplication" + " " + appName, null, true);
		// WebDriverSteps nav = new WebDriverSteps();
		navigateToApplicationUsingPopUpLogin(appName, un, pwd);
	}

	public static void navigateToApplicationUsingPopUpLogin(String appName, String un, String pwd) {
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

		String appUrl = getApplicationURL(appName);
		appUrl = "https://" + un + ":" + pwd + "@" + appUrl;
		// this.setUrl(appUrl);

		WebDriverSession.getWebDriverSession().get(appUrl);

		Reporting.logReporter(Status.DEBUG, "URL opened: " + appUrl + "." + nameofCurrMethod);

		// HANDLE ERRORS WITH JS IF THE PROFILE TO DO IT IS NOT VALID OR BROWSER
		// FEATURES DO NOT ALLOW IT (IE11/EDGE)
		BaseSteps.JavaScripts.handleCertificateError();
		BaseSteps.JavaScripts.handleUnsecureConnectionError();
		BaseSteps.JavaScripts.handleInvalidCertificateError();
	}

	public static String getApplicationURL(String appName) {
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

		JSONObject jsonFile = new JSONObject(ReadJSON.parse("Environments.json"));
		JSONObject env = jsonFile.getJSONObject(SystemProperties.EXECUTION_ENVIRONMENT);
		Reporting.logReporter(Status.DEBUG, "URL Value Returned for environment " + env + "." + nameofCurrMethod);

		return ReadJSON.getString(env, appName);
	}

	public static void removeEmptyLinesFromXMLFile(String filePath) throws IOException {

		List<String> lines = new ArrayList<String>();

		String readlines = new String();
		BufferedReader r = null;
		try {
			// read the file line by line
			r = new BufferedReader(new FileReader(filePath));
			for (String line; (line = r.readLine()) != null; readlines += line)
				;
		} finally {
			r.close();
		}

		readlines = readlines.trim();
		readlines = readlines.replaceAll("(?m)^[ \t]*\r?\n", "");

		File updatedFile = new File(filePath);
		FileWriter writer = new FileWriter(updatedFile);
		writer.write(readlines);
		writer.flush();

		if (updatedFile.length() == 0)
			Reporting.logReporter(Status.INFO, "Empty payload ...");
		else
			System.out.println("Payload is not empty ...");

	}

	/*
	 * public static void prettyFormatXML() { Transformer transformer =
	 * TransformerFactory.newInstance().newTransformer();
	 * transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	 * transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
	 * "2"); // initialize StreamResult with File object to save to file
	 * StreamResult result = new StreamResult(new StringWriter()); DOMSource source
	 * = new DOMSource(doc); transformer.transform(source, result); }
	 */

	/*
	 * public static String prettyFormat(String input, int indent) { try { Source
	 * xmlInput = new StreamSource(new StringReader(input)); StringWriter
	 * stringWriter = new StringWriter(); StreamResult xmlOutput = new
	 * StreamResult(stringWriter); TransformerFactory transformerFactory =
	 * TransformerFactory.newInstance();
	 * transformerFactory.setAttribute("indent-number", indent);
	 * transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	 * transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
	 * Transformer transformer = transformerFactory.newTransformer();
	 * transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	 * transformer.transform(xmlInput, xmlOutput); return
	 * xmlOutput.getWriter().toString(); } catch (Exception e) { throw new
	 * RuntimeException(e); // simple exception handling, please review it } }
	 */

	public static void removeEmptyLinesFromXMLFileTemp(String sourceFilePath, String filePath) throws IOException {

		List<String> lines = new ArrayList<String>();

		String response = new String();
		BufferedReader r = null;
		try {
			// read the file into lines
			r = new BufferedReader(new FileReader(sourceFilePath));
			/* response = new String(); */
			for (String line; (line = r.readLine()) != null; response += line)
				;
		} finally {
			r.close();
		}
		response = response.trim();
		response = response.replaceAll("(?m)^[ \t]*\r?\n", "");
		System.out.println("rrr" + response);

		FileUtils.writeStringToFile(new File(filePath), response);
	}

	public static void deleteFile(String filePath) {
		File myObj = new File(filePath);
		if (myObj.exists()) {
			if (myObj.delete())
				System.out.println("Deleted the old file: " + myObj.getName());
			else
				System.out.println("Failed to delete the file.");
		} else
			System.out.println("File does not exists !");
	}

	/**
	 * 
	 * @param srcPath
	 * @param destPath
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void moveFile(String srcPath, String destPath) throws IOException, InterruptedException {

		String batFilePath = ".\\src\\test\\resources\\copyFile.bat";
		deleteFile(destPath + "2FA_SMS_Payload.xml");
		String command = "cmd /C start /min " + batFilePath + " " + srcPath + " " + destPath + "";

		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		pr.waitFor();
		System.out.println("File moved successfully..");
		pr.destroy();
		BaseSteps.Waits.waitGeneric(10000);
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean checkIfFileExists(String filePath) {

		File file = new File(filePath);

		if (file.exists()) {
			return true;
		} else
			return false;
	}

	public static void logRequestXML(String filePath) throws IOException {

		File xmlFile = new File(filePath);
		Reader fileReader = new FileReader(xmlFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		String xml2String = sb.toString();
		Reporting.logReporter(Status.INFO, "Request Payload used for Sending 2FA Response" + xml2String);
		bufReader.close();

	}

	public static String convertDateFormatToYYYYMMDD(String inputDate) {

		try {

			SimpleDateFormat f1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			Date date = f1.parse(inputDate);
			SimpleDateFormat f2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			return f2.format(date);
		} catch (ParseException e) {
			Reporting.logReporter(Status.DEBUG, "Unable to parse given date format" + e);
			return "";
		}
	}

	public static String getRandomEmailId() {

		return "Test" + GenericUtils.generateRandomInteger(50000) + "@telus.com";
	}

	public static String getCurrentSystemDateTimeWithDelay(int waitInSeconds) {

		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		calendar.add(Calendar.SECOND, waitInSeconds);

		return calendar.getTime().toString();

	}

	public static String getCurrentSystemDateTime() {

		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		return calendar.getTime().toString();

	}

	public static void verifyModifiedDDTFromPAC(String PACDate, String SMGDate) {

		SMGDate = GenericUtils.convertDateFormatToYYYYMMDD(SMGDate);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:m", Locale.ENGLISH);
		Date firstDate = null;
		Date secondDate = null;
		long actualDiff = 0;
		try {
			firstDate = sdf.parse(PACDate);
			secondDate = sdf.parse(SMGDate);
			long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
			actualDiff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int expectedDiff = 0;
		/*
		 * if(GenericUtils.getSystemTimeZone().toUpperCase().
		 * equalsIgnoreCase("India Standard Time")) { expectedDiff = 10; }
		 */
		if (GenericUtils.getSystemTimeZone().toUpperCase().equalsIgnoreCase("Pacific Standard Time")) {
			expectedDiff = 3;
			Validate.assertEquals(actualDiff, expectedDiff, "PAC - Modified DDDT Validation", false);

		} else
			Reporting.logReporter(Status.INFO, "Time zone difference");

		Reporting.logReporter(Status.INFO, "PAC - Modified DDDT Validation is successful" + PACDate);

	}

	public static String getSystemTimeZone() {
		// get Calendar instance
		Calendar now = Calendar.getInstance();

		// get current TimeZone using getTimeZone method of Calendar class
		TimeZone timeZone = now.getTimeZone();

		return timeZone.getDisplayName();
	}

	/**
	 * 
	 * @param delayInMinutes
	 * @return PAC Format Time
	 */
	public static String updateDDDTFromPAC_PST(int delayInMinutes) {
		String s = null;
		String year = null;
		String currentTime = null;
		String updatedDDT = null;

		try {
			Date date1 = new Date();

			String tZone = GenericUtils.getSystemTimeZone();

			if (tZone.equalsIgnoreCase("Pacific Standard Time")) {
				DateFormat estFormat = new SimpleDateFormat();
				TimeZone estTime = TimeZone.getTimeZone("PST");

				currentTime = GenericUtils.getCurrentSystemDateTime();

				year = currentTime.substring(currentTime.length() - 4, currentTime.length());

				estFormat.setTimeZone(estTime);

				s = estFormat.format(date1);
				s = s.replace("AM", "").trim();
				s = s.replace("PM", "").trim();

				SimpleDateFormat f1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				Date date = f1.parse(s);

				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.MINUTE, delayInMinutes);

				updatedDDT = f1.format(cal.getTime());
				updatedDDT = updatedDDT.replace(updatedDDT.substring(6, 10), year);
				updatedDDT = GenericUtils.convertDateFormatToYYYYMMDD(updatedDDT);

			} else {
				updatedDDT = GenericUtils.getDDDTWithDelayInPACFormat(delayInMinutes);

			}

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return updatedDDT;

	}

	/**
	 * 
	 * @param days
	 * @param hour
	 * @param minutes
	 * @return
	 */

	public static String getFutureDateTime(int days, int hour, int minutes) {
		Instant instant = Instant.now();
		Instant delayedTime = instant.plus(days, ChronoUnit.DAYS);
		ZonedDateTime zdtNewYork = delayedTime.atZone(ZoneId.of("America/New_York"));

		zdtNewYork = zdtNewYork.withHour(hour).withMinute(minutes);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

		return zdtNewYork.format(formatter);
	}

	/**
	 * 
	 * @param days
	 * @param hour
	 * @param minutes
	 * @return
	 */

	public static String getFutureDateTimeInPST(int days, int hour, int minutes) {
		Instant instant = Instant.now();
		Instant delayedTime = instant.plus(days, ChronoUnit.DAYS);
		ZonedDateTime zdtNewYork = delayedTime.atZone(ZoneId.of("America/Los_Angeles"));

		zdtNewYork = zdtNewYork.withHour(hour).withMinute(minutes);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

		return zdtNewYork.format(formatter);
	}

	/**
	 * This method is used to return current date in MM DD YYYY format.
	 * 
	 * @param delayInMinutes
	 *            This is the delay in minutes
	 * @return current date in MM DD YYYY format adding specific minutes of delay.
	 * 
	 */
	public static String getDDDTWithDelayInPACFormatForJenkinsNodes(int delayInMinutes) {
		/*
		 * Date date = new Date(); DateFormat dateFormat = new
		 * SimpleDateFormat("yyyy/MM/dd HH:mm"); date = DateUtils.addMinutes(date,
		 * delayInMinutes); return dateFormat.format(date);
		 */

		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// Set the formatter to use a different timezone
		formatter.setTimeZone(TimeZone.getTimeZone("EST"));
		date = DateUtils.addMinutes(date, delayInMinutes);

		// Prints the date in the EST timezone
		return formatter.format(date);

		/*
		 * Instant instant = Instant.now(); Instant delayedTime =
		 * instant.plus(delayInMinutes, ChronoUnit.MINUTES);
		 * 
		 * ZonedDateTime zdtNewYork = delayedTime.atZone(ZoneId.of("America/New_York"));
		 * 
		 * DateTimeFormatter formatter =
		 * DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"); return
		 * zdtNewYork.format(formatter);
		 */

	}

	/**
	 * 
	 * @param days
	 * @param hour
	 * @param minutes
	 * @return
	 */

	public static String getSystemDateTimeJenkinsNode() {
		Instant instant = Instant.now();

		ZonedDateTime zdtNewYork = instant.atZone(ZoneId.of("America/New_York"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

		return zdtNewYork.format(formatter);
	}

	public static String getCurrentSystemDateTimeSMG() {
		Instant instant = Instant.now();

		ZonedDateTime zdtNewYork = instant.atZone(ZoneId.of("America/New_York"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

		return zdtNewYork.format(formatter);
	}

	public static int diffBetweenTwoDates_PAC(String d1, String d2) {
		Date date1 = null;
		Date date2 = null;
		long diff;
		long minutes;

		int minDiff = 0;
		try {
			date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(d1);
			date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(d2);

			diff = date2.getTime() - date1.getTime();
			minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
			minDiff = (int) minutes;

		} catch (ParseException e) {
			Reporting.logReporter(Status.INFO, "Unable to parse and find the difference between two dates");
			e.printStackTrace();
		}

		return minDiff;

	}

	public static int diffBetweenTwoDates_SMG(String d1, String d2) {
		Date date1 = null;
		Date date2 = null;
		long diff;
		long minutes;

		int minDiff = 0;
		try {
			date1 = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(d1);
			date2 = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(d2);

			diff = date2.getTime() - date1.getTime();
			minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
			minDiff = (int) minutes;

		} catch (ParseException e) {
			Reporting.logReporter(Status.INFO, "Unable to parse and find the difference between two dates");
			e.printStackTrace();
		}

		return minDiff;

	}

	public static String convertDateTimeInEST_SMG(String dateTime) {

		Date date = new Date(dateTime);
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

		// Set the formatter to use a different timezone
		formatter.setTimeZone(TimeZone.getTimeZone("EST"));

		// Prints the date in the EST timezone
		return formatter.format(date);
	}

	public static String currentDateTimeInEST() {

		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// Set the formatter to use a different timezone
		formatter.setTimeZone(TimeZone.getTimeZone("EST"));

		// Prints the date in the EST timezone
		return formatter.format(date);
	}

	public static String dateTimeInESTWithDelayInMinutes(int minutes) {

		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		date = DateUtils.addMinutes(date, minutes);
		// Set the formatter to use a different timezone
		formatter.setTimeZone(TimeZone.getTimeZone("EST"));

		// Prints the date in the EST timezone
		return formatter.format(date);
	}

	/**
	 * 
	 * @param
	 * @param num
	 */
	public static void typeNumFromKeyboard(WebElement e, int num) {

		switch (num) {

		case 0:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD0);
			break;

		case 1:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD1);
			break;

		case 2:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD2);
			break;

		case 3:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD3);
			break;

		case 4:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD4);
			break;

		case 5:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD5);
			break;

		case 6:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD6);
			break;

		case 7:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD7);
			break;

		case 8:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD8);
			break;

		case 9:
			BaseSteps.SendKeys.sendKey(e, Keys.NUMPAD9);
			break;

		default:
			Reporting.logReporter(Status.INFO, "Enter a Valid Number !");

		}
	}

	public static void convertStringIntoIntegerAndEnterNumbers(WebElement e, String num) {

		if (num.length() > 1) {
			for (int i = 0; i < num.length(); i++) {
				GenericUtils.typeNumFromKeyboard(e, num.charAt(i));
			}
		}
	}

	public static long getCurrentSystemDateTimeInMillis() {

		return System.currentTimeMillis();

	}

	public static String getSpecificValueFromCurrentDate(String param) {
		LocalDate currentDate = LocalDate.now();

		String str = null;

		switch (param.toUpperCase()) {
		case "DAY":
			DayOfWeek dow = currentDate.getDayOfWeek(); // say FRIDAY
			str = dow.toString();
			break;
		case "MONTH":
			Month m = currentDate.getMonth(); // say JUNE
			str = m.toString();
			break;
		case "DATE":
			int dom = currentDate.getDayOfMonth(); // say 17
			str = String.valueOf(dom);
			break;
		default:
			str = "";
		}
		return str;

	}

	public static String getAPIEnvironment(String environment) {

		environment = environment.toUpperCase();
		switch (environment) {
		case "PT148":
			apiTestEnvironemnt = "it02";
			break;
		case "PT168":
			apiTestEnvironemnt = "it01";
			break;
		case "PT140":
			apiTestEnvironemnt = "it03";
			break;
		case "Staging":
			apiTestEnvironemnt = "st101-a";
			break;

		}
		return apiTestEnvironemnt;
	}

	public static String readFileAsString(String filePath) throws IOException {
		BufferedReader bufReader = null;
		String fileContent = null;
		try {
			File xmlFile = new File(filePath);
			Reader fileReader = new FileReader(xmlFile);
			bufReader = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String line = bufReader.readLine();
			while (line != null) {
				sb.append(line).append("\n");
				line = bufReader.readLine();
			}
			fileContent = sb.toString();
		} finally {
			bufReader.close();
		}

		return fileContent;

	}

	



}
