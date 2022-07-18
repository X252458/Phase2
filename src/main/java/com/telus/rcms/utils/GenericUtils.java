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

		String accountIDPrefix02 = "7090";
		String accountIDPrefix03 = "7090";
		String accountIDPrefix04 = "7090";

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

		String subscriptionIDPrefix02 = "849";
		String subscriptionIDPrefix03 = "849";
		String subscriptionIDPrefix04 = "849";

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

		String subscriptionNumPrefix02 = "4161";
		String subscriptionNumPrefix03 = "4161";
		String subscriptionNumPrefix04 = "4161";

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

	public static void payloadValueDeclaration(String jsonString) throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet resultSet = null;

		ActivationPayloadJsonPath jsonPath = new ActivationPayloadJsonPath();

		String AgreementDurationStartDateTime = JSONUtils.checkValue(jsonString,
				jsonPath.AgreementDurationStartDateTime);

		AgreementDurationAmount = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.AgreementDurationAmount));

		String AgreementDurationEndDateTime = JSONUtils.checkValue(jsonString, jsonPath.AgreementDurationEndDateTime);
		AgreementDurationEndDateTime = JSONUtils.getAgrmtEndDate(AgreementDurationEndDateTime,
				AgreementDurationStartDateTime, AgreementDurationAmount);

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

		String relatedParty_Subid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_Subid));

		String relatedParty_MarketProvinceValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_MarketProvinceValue));

		String relatedParty_HomeProvinceValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_HomeProvinceValue));

		String relatedParty_SubscriberNumValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_SubscriberNumValue));

		String relatedParty_ComboRatePlanIndValue = JSONUtils.checkValue(jsonString,
				jsonPath.relatedParty_ComboRatePlanIndValue);
		relatedParty_ComboRatePlanIndValue = DBUtils.convertYN(relatedParty_ComboRatePlanIndValue);

		Reporting.logReporter(Status.INFO, "--------------------DB Validation Starts-----------------------");

		resultSet = statement.executeQuery("SELECT * FROM  CUSTOMER_SERVICE_AGREEMENT agrmt "
				+ "where agrmt.subscriber_no ='" + relatedParty_SubscriberNumValue + "' and current_ind='Y' ");

		while (resultSet.next()) {

			agrmtId = String.valueOf(resultSet.getInt("CUSTOMER_SVC_AGREEMENT_ID"));

			Reporting.logReporter(Status.INFO, "Customer Agreement ID : " + agrmtId);

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_END_DT")),
					AgreementDurationEndDateTime, "AGREEMENT_END_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_START_DT")),
					AgreementDurationStartDateTime, "AGREEMENT_START_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("COMMITMENT_LENGTH_NUM")),
					AgreementDurationAmount, "COMMITMENT_LENGTH_NUM");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BAN")), relatedParty_Accid, "BAN");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BRAND_ID")),
					relatedParty_brandidValue, "BRAND_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("ACCOUNT_TYPE_CD")),
					relatedParty_AccTypeCodeValue, "ACCOUNT_TYPE_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("ACCOUNT_SUBTYPE_CD")),
					relatedParty_AccSubTypeCodeValue, "ACCOUNT_SUBTYPE_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("CHNL_ORG_ID")),
					relatedParty_ChnlOrgValue, "CHNL_ORG_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("SALES_REP_ID")),
					relatedParty_SalesRepidValue, "SALES_REP_ID");

			/*
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("")),
			 * relatedParty_Oriid, "");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "REWARD_TXN_ID")), relatedParty_TransactionidValue, "REWARD_TXN_ID");
			 * 
			 */
			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("SUBSCRIPTION_ID")),
					relatedParty_Subid, "SUBSCRIPTION_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("CUST_PHONE_PROV_CD")),
					relatedParty_MarketProvinceValue, "CUST_PHONE_PROV_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("CUST_HOME_PROV_CD")),
					relatedParty_HomeProvinceValue, "CUST_HOME_PROV_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getLong("SUBSCRIBER_NO")),
					relatedParty_SubscriberNumValue, "SUBSCRIBER_NO");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("COMBO_RTPLN_IND")),
					relatedParty_ComboRatePlanIndValue, "COMBO_RTPLN_IND");

		}
	}

	public static void payloadnDBCheckAgrmtItem(String jsonString, int itemNo) throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet rsAgreementItem = null;

		Statement statement2 = null;
		statement2 = DBUtils.Conn.createStatement();
		ResultSet rsAgreementTax = null;

		Statement statement3 = null;
		statement3 = DBUtils.Conn.createStatement();
		ResultSet rsAgreementPromo = null;

		int agreementItemNo = 0;

		for (int i = 0; i < itemNo; i++) {

			AgreementItem agrmtItem = new AgreementItem(i);
			agreementItemNo = i + 1;

			String agreementItem_id = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_id));

			Itemtype = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemType));
			String agreementItem_itemType = DBUtils.getItemType(Itemtype);

			Reporting.logReporter(Status.INFO, "--- Agreement Item : " + agreementItemNo + " : " + Itemtype + "---");

			String agreementItem_itemDurationAmount = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemDurationAmount));

			String agreementItem_itemDurationStartDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_itemDurationStartDateTime);

			String agreementItem_itemDurationEndDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_itemDurationEndDateTime);
			/*
			 * agreementItem_itemDurationEndDateTime =
			 * JSONUtils.getAgrmtEndDate(agreementItem_itemDurationEndDateTime,
			 * agreementItem_itemDurationStartDateTime, agreementItem_itemDurationAmount);
			 */
			agreementItem_itemDurationEndDateTime = JSONUtils.getEndDate(agreementItem_itemDurationEndDateTime,
					agreementItem_itemDurationStartDateTime, agreementItem_itemType, agreementItem_itemDurationAmount);

			/*
			 * if (Itemtype.equals("BIB") || Itemtype.equals("TIASSETCREDIT") ||
			 * Itemtype.equals("TIPROMOCRDIT")) { LocalDate endDateFormatted;
			 * endDateFormatted = LocalDate.parse(agreementItem_itemDurationStartDateTime);
			 * endDateFormatted = endDateFormatted.plusDays(180);
			 * agreementItem_itemDurationEndDateTime = endDateFormatted.toString(); }
			 */

			String agreementItem_incentiveAmount = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_incentiveAmount));
			if (AgreementDurationAmount.equals("0") && agreementItem_itemType.equals("1")) {
				Itemtype = "NA";
				agreementItem_itemDurationEndDateTime = "null";
				agreementItem_itemType = "5";
				agreementItem_itemDurationAmount = "0";
				agreementItem_incentiveAmount = "0";
			}

			String agreementItem_incentiveServiceCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_incentiveServiceCode);

			String agreementItem_installmentAmount = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentAmount));

			String agreementItem_installmentStartDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_installmentStartDateTime);
			if (agreementItem_itemType.equals("9"))
				agreementItem_installmentStartDateTime = JSONUtils.getInstallmentStartDate(
						agreementItem_installmentStartDateTime, agreementItem_itemType,
						agreementItem_installmentAmount);

			String agreementItem_installmentEndDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_installmentEndDateTime);
			agreementItem_installmentEndDateTime = JSONUtils.getEndDate(agreementItem_installmentEndDateTime,
					agreementItem_installmentStartDateTime, agreementItem_itemType, agreementItem_installmentAmount);
			if (agreementItem_itemType.equals("10") || agreementItem_itemType.equals("11"))
				agreementItem_installmentEndDateTime = "null";

			String agreementItem_installmentLeftNumValue = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentLeftNumValue));

			String agreementItem_installmentAppliedNumValue = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentAppliedNumValue));

			String agreementItem_installmentAppliedAmtValue = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentAppliedAmtValue));

			String agreementItem_termOrConditionMinRatePlanValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionMinRatePlanValue);

			String agreementItem_termOrConditionMinFeatureValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionMinFeatureValue);

			String agreementItem_termOrConditionMinCombinedValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionMinCombinedValue);

			String agreementItem_termOrConditionCommitmentServiceCdValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionCommitmentServiceCdValue);

			String agreementItem_termOrConditionAutoTopupCommitmentIndValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionAutoTopupCommitmentIndValue);
			agreementItem_termOrConditionAutoTopupCommitmentIndValue = DBUtils
					.convertYN(agreementItem_termOrConditionAutoTopupCommitmentIndValue);

			String agreementItem_taxPaymentMethodCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxPaymentMethodCode);

			String agreementItem_taxPaymentMechanismCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxPaymentMechanismCode);

			String agreementItem_taxPaymentChannelCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxPaymentChannelCode);

			String agreementItem_taxProvinceCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxProvinceCode);

			String agreementItem_taxCategory = JSONUtils.checkValue(jsonString, agrmtItem.agreementItem_taxCategory);

			String agreementItem_taxRate = JSONUtils.checkValue(jsonString, agrmtItem.agreementItem_taxRate);

			String agreementItem_taxAmountValue = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_taxAmountValue));

			String agreementItem_productSerialNumber = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productSerialNumber);

			String agreementItem_productPriceValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productPriceValue);

			String agreementItem_productCharacteristicValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productCharacteristicValue);

			/*
			 * String agreementItem_productSpecificationid = String.valueOf(
			 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
			 * agrmtItem.agreementItem_productSpecificationid));
			 * 
			 * String agreementItem_productCharacteristicValue = String.valueOf(
			 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
			 * agrmtItem.agreementItem_productCharacteristicValue));
			 * 
			 * String agreementItem_productSpecificationLocale =String.valueOf(
			 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
			 * agrmtItem.agreementItem_productSpecificationLocale));
			 *
			 * 
			 * String agreementItem_productSpecificationDesc =String.valueOf(
			 * JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
			 * agrmtItem.agreementItem_productSpecificationDesc));
			 * 
			 * 
			 */

			String agreementItem_promotionid = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_promotionid));

			String agreementItem_promotionPerspectiveDate = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_promotionPerspectiveDate));
			agreementItem_promotionPerspectiveDate = agreementItem_promotionPerspectiveDate.split("T")[0];

			String agreementItem_productOfferingid = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_productOfferingid));

			String offerID = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
					agrmtItem.agreementItem_productOfferingRedeemedOfferContextCodeValue));
			String agreementItem_productOfferingRedeemedOfferContextCodeValue = DBUtils.getProductOfferID(offerID);

			String agreementItem_productOfferingOfferTierCdValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productOfferingOfferTierCdValue);
			agreementItem_productOfferingOfferTierCdValue = DBUtils
					.getNullCode(agreementItem_productOfferingOfferTierCdValue);

			String agreementItem_productOfferingOfferTierCapAmtValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productOfferingOfferTierCapAmtValue);
			agreementItem_productOfferingOfferTierCapAmtValue = DBUtils
					.getNullCode(agreementItem_productOfferingOfferTierCapAmtValue);

			String agreementItem_productOfferingDataCommitmentIndValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
							agrmtItem.agreementItem_productOfferingDataCommitmentIndValue));
			agreementItem_productOfferingDataCommitmentIndValue = DBUtils
					.convertYN(agreementItem_productOfferingDataCommitmentIndValue);

			String agreementItem_productOfferingContractEnforcedPlanIndValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
							agrmtItem.agreementItem_productOfferingContractEnforcedPlanIndValue));
			agreementItem_productOfferingContractEnforcedPlanIndValue = DBUtils
					.convertYN(agreementItem_productOfferingContractEnforcedPlanIndValue);

			String agreementItem_productOfferingPerspectiveDate = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_productOfferingPerspectiveDate));
			agreementItem_productOfferingPerspectiveDate = agreementItem_productOfferingPerspectiveDate.split("T")[0];

			String agreementItem_productOfferingSourceSystemId = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_productOfferingSourceSystemId));

			if (agreementItem_itemType.equals("17")) {
				agreementItem_termOrConditionMinRatePlanValue = "null";
				agreementItem_termOrConditionMinFeatureValue = "null";
				agreementItem_termOrConditionMinCombinedValue = "null";
				agreementItem_productSerialNumber = "null";
				agreementItem_productPriceValue = "null";
				agreementItem_productCharacteristicValue = "null";
			}

			if (AgreementDurationAmount.equals("0") && agreementItem_itemType.equals("5")) {
				agreementItem_termOrConditionMinRatePlanValue = "null";
				agreementItem_termOrConditionMinFeatureValue = "null";
				agreementItem_termOrConditionMinCombinedValue = "null";
			}

			String tax = null;
			switch (Itemtype) {
			case "HWS":
			case "TIASSETCREDIT":
			case "TIPROMOCREDIT":
			case "PRESOC":
			case "PRECREDIT":
			case "TAB":
			case "ACCESSORYFINANCE":
			case "ACTIVATIONBILLCREDIT":
			case "HARDWARE":
			case "BIB":
			case "FINANCE":
				rsAgreementItem = statement.executeQuery("Select * from cust_srvc_agrmt_item item  "
						+ "inner join CUST_SRVC_AGRMT_ITM_PROMO promo  "
						+ "on item.CUST_SRVC_AGRMT_ITEM_ID = promo.CUST_SRVC_AGRMT_ITEM_ID and item.customer_svc_agreement_id ='"
						+ agrmtId + "' and item.REWARD_PROGRAM_TYP_ID='" + agreementItem_itemType + "'");

				break;
			case "NA":
				rsAgreementItem = statement.executeQuery(
						"Select * from(select a.*,row_number() over(partition by customer_svc_agreement_id order by CUST_SRVC_AGRMT_ITEM_ID)  "
								+ "as rn from cust_srvc_agrmt_item a where customer_svc_agreement_id ='" + agrmtId
								+ "') where rn=" + agreementItemNo);
				break;

			}

			while (rsAgreementItem.next()) {

				custAgmtItemId = String.valueOf(rsAgreementItem.getInt("CUST_SRVC_AGRMT_ITEM_ID"));

				Reporting.logReporter(Status.INFO, "CUST_SRVC_AGRMT_ITEM_ID Value from DB is : " + custAgmtItemId);

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getDate("COMMITMENT_EFF_START_DT")),
						agreementItem_itemDurationStartDateTime, "COMMITMENT_EFF_START_DT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getDate("COMMITMENT_EFF_END_DT")),
						agreementItem_itemDurationEndDateTime, "COMMITMENT_EFF_END_DT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("ORIG_COMMITMENT_LENGTH_NUM")),
						agreementItem_itemDurationAmount, "ORIG_COMMITMENT_LENGTH_NUM");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REWARD_PROGRAM_TYP_ID")),
						agreementItem_itemType, "REWARD_PROGRAM_TYP_ID");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("INCENTIVE_AMT")),
						agreementItem_incentiveAmount, "INCENTIVE_AMT");

				if (!agreementItem_incentiveServiceCode.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("INCENTIVE_CD")),
							agreementItem_incentiveServiceCode, "INCENTIVE_CD");
				}

				if (!agreementItem_installmentStartDateTime.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getDate("REWARD_INSTLMNT_START_DT")),
							agreementItem_installmentStartDateTime, "REWARD_INSTLMNT_START_DT");
				}
				if (!agreementItem_installmentEndDateTime.equals("NA")) {

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getDate("REWARD_INSTLMNT_END_DT")),
							agreementItem_installmentEndDateTime, "REWARD_INSTLMNT_END_DT");
				}

				if (!agreementItem_installmentAmount.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getInt("REWARD_INSTLMNT_QTY")),
							agreementItem_installmentAmount, "REWARD_INSTLMNT_QTY");
				}
				// INSTALLMENTS_LEFT_NUM, INSTALLMENTS_APPLIED_NUM, INSTALLMENTS_APPLIED_AMT ->
				// No need to DB validation

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("RTPLN_MIN_COMMITMENT_AMT")),
						agreementItem_termOrConditionMinRatePlanValue, "RTPLN_MIN_COMMITMENT_AMT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("FEAT_MIN_COMMITMENT_AMT")),
						agreementItem_termOrConditionMinFeatureValue, "FEAT_MIN_COMMITMENT_AMT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("COMB_MIN_COMMITMENT_AMT")),
						agreementItem_termOrConditionMinCombinedValue, "COMB_MIN_COMMITMENT_AMT");

				if (!agreementItem_termOrConditionCommitmentServiceCdValue.equals("NA"))
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("COMMITMENT_SERVICE_CD")),
							agreementItem_termOrConditionCommitmentServiceCdValue, "COMMITMENT_SERVICE_CD");

				if (!agreementItem_termOrConditionAutoTopupCommitmentIndValue.equals("NA"))
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("COMMITMENT_AUTOTOPUP_IND")),
							agreementItem_termOrConditionAutoTopupCommitmentIndValue, "COMMITMENT_AUTOTOPUP_IND");

				if (agreementItem_itemType.equals("4") || agreementItem_itemType.equals("9")) {

					rsAgreementTax = statement3.executeQuery(
							"select * from CUST_SRVC_AGRMT_ITM_TAX tax inner join CUST_SRVC_AGRMT_ITM_TAX_DTL dtl "
									+ "on tax.CUST_SRVC_AGRMT_ITM_TAX_ID = dtl.CUST_SRVC_AGRMT_ITM_TAX_ID "
									+ "and tax.CUST_SRVC_AGRMT_ITEM_ID=" + custAgmtItemId);

					while (rsAgreementTax.next()) {
						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementTax.getString("TAX_PAYMENT_METHOD_CD")),
								agreementItem_taxPaymentMethodCode, "TAX_PAYMENT_METHOD_CD");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementTax.getString("TAX_PYMT_MECHANISM_CD")),
								agreementItem_taxPaymentMechanismCode, "TAX_PYMT_MECHANISM_CD");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementTax.getString("TAX_PAYMENT_CHANNEL_CD")),
								agreementItem_taxPaymentChannelCode, "TAX_PAYMENT_CHANNEL_CD");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementTax.getString("TAXATION_PROVINCE_CD")),
								agreementItem_taxProvinceCode, "TAXATION_PROVINCE_CD");

						GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementTax.getString("TAX_TYPE_CD")),
								agreementItem_taxCategory, "TAX_TYPE_CD");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementTax.getString("TAX_RATE_PCT")), agreementItem_taxRate,
								"TAX_RATE_PCT");

						GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementTax.getString("TAX_AMT")),
								agreementItem_taxAmountValue, "TAX_AMT");
					}
				}

				/*
				 * 
				 * 
				 * Value for these json
				 * 
				 * "description": [ { "locale": "EN_CA", "description":
				 * "APPLE IPHONE 4S 32GB BLACK" }, { "locale": "FR_CA", "description":
				 * "APPLE IPHONE 4S 32GB BLACK" } ] agreementItem_productSpecificationLocale1
				 * agreementItem_productSpecificationDesc1
				 * 
				 */
				if (!agreementItem_itemType.equals("5")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getInt("REDEEMED_PROMOTION_ID")), agreementItem_promotionid,
							"REDEEMED_PROMOTION_ID");

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getDate("REDEEMED_PROMOTION_TS")),
							agreementItem_promotionPerspectiveDate, "REDEEMED_PROMOTION_TS");
				}
				if (!agreementItem_productSerialNumber.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("HANDSET_SERIAL_NUM")),
							agreementItem_productSerialNumber, "HANDSET_SERIAL_NUM");
				}
				if (!agreementItem_productPriceValue.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("CATALOGUE_ITEM_PRICE_AMT")),
							agreementItem_productPriceValue, "CATALOGUE_ITEM_PRICE_AMT");
				}
				if (!agreementItem_productCharacteristicValue.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getString("USIM_ID")),
							agreementItem_productCharacteristicValue, "USIM_ID");
				}

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_ID")),
						agreementItem_productOfferingid, "REDEEMED_OFFER_ID");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TYPE_ID")),
						agreementItem_productOfferingRedeemedOfferContextCodeValue, "REDEEMED_OFFER_TYPE_ID");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TIER_CD")),
						agreementItem_productOfferingOfferTierCdValue, "REDEEMED_OFFER_TIER_CD");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TIER_CAP_AMT")),
						agreementItem_productOfferingOfferTierCapAmtValue, "REDEEMED_OFFER_TIER_CAP_AMT");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getString("DATA_SRVC_REQ_IND")),
						agreementItem_productOfferingDataCommitmentIndValue, "DATA_SRVC_REQ_IND");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("COMB_MIN_CMITMT_DISCHRG_IND")),
						agreementItem_productOfferingContractEnforcedPlanIndValue, "COMB_MIN_CMITMT_DISCHRG_IND");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getDate("REDEEMED_OFFER_TS")),
						agreementItem_productOfferingPerspectiveDate, "REDEEMED_OFFER_TS");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_SYS_ID")),
						agreementItem_productOfferingSourceSystemId, "REDEEMED_OFFER_SYS_ID");

			}
			rsAgreementItem.close();
		}

	}

	public static void responseDBCheckAgrmt(String jsonString, String subscriptionID, int itemNo) throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet resultSet = null;

		int agreementNo = 0;

		for (int i = 0; i < itemNo; i++) {

			GetRewardCommitment jsonPath = new GetRewardCommitment(i);
			agreementNo = i + 1;
			Reporting.logReporter(Status.INFO, "--- Agreement : " + agreementNo + "---");

			String AgreementDurationStartDateTime = JSONUtils.checkValue(jsonString,
					jsonPath.AgreementDurationStartDateTime);

			AgreementDurationAmount = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.AgreementDurationAmount));

			String AgreementDurationEndDateTime = JSONUtils.checkValue(jsonString,
					jsonPath.AgreementDurationEndDateTime);

			String relatedParty_brandidValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_brandidValue));

			Reporting.logReporter(Status.INFO, "--------------------DB Validation Starts-----------------------");

			resultSet = statement.executeQuery("SELECT * FROM  CUSTOMER_SERVICE_AGREEMENT agrmt "
					+ "where agrmt.SUBSCRIPTION_ID ='" + subscriptionID + "' order by CUSTOMER_SVC_AGREEMENT_ID desc");
			// and REDEEMED_OFFER_TYPE_ID='2' ");

			while (resultSet.next()) {

				agrmtId = String.valueOf(resultSet.getInt("CUSTOMER_SVC_AGREEMENT_ID"));
				currentIND = String.valueOf(resultSet.getString("CURRENT_IND"));

				Reporting.logReporter(Status.INFO, "Agreement ID : " + agrmtId);

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_END_DT")),
						AgreementDurationEndDateTime, "AGREEMENT_END_DT");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_START_DT")),
						AgreementDurationStartDateTime, "AGREEMENT_START_DT");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("COMMITMENT_LENGTH_NUM")),
						AgreementDurationAmount, "COMMITMENT_LENGTH_NUM");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BRAND_ID")),
						relatedParty_brandidValue, "BRAND_ID");

				/*
				 * switch (i) {
				 * 
				 * case 0: GenericUtils.responseDBCheckAgrmtItem(jsonString, subscriptionID,
				 * currentIND, 0, 4); break;
				 * 
				 * case 1: GenericUtils.responseDBCheckAgrmtItem(jsonString, subscriptionID,
				 * currentIND, 1, 1); break;
				 * 
				 * }
				 */
			}
		}

	}

	public static void responseDBCheckAgrmtItem(String jsonString, String subscriptionID, int agrmtNo, int itemNo)
			throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet rsAgreementItem = null;

		Statement statement1 = null;
		statement1 = DBUtils.Conn.createStatement();
		ResultSet rsRewardAcc = null;

		Statement statement2 = null;
		statement2 = DBUtils.Conn.createStatement();
		ResultSet rsAgreementTax = null;

		int agreementItemNo = 0;

		for (int i = 0; i < itemNo; i++) {

			GetRewardCommitmentAgreementItem agrmtItem = new GetRewardCommitmentAgreementItem(agrmtNo, i);
			agreementItemNo = i + 1;

			Itemtype = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemType));
			String agreementItem_itemType = DBUtils.getItemType(Itemtype);

			Reporting.logReporter(Status.INFO,
					"--- Agreement Item : " + agreementItemNo + Itemtype + " : " + agreementItem_itemType);

			String agreementItem_itemDurationAmount = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemDurationAmount));

			String agreementItem_itemDurationStartDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_itemDurationStartDateTime);

			String agreementItem_itemDurationEndDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_itemDurationEndDateTime);

			String agreementItem_incentiveAmount = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_incentiveAmount);

			String agreementItem_incentiveServiceCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_incentiveServiceCode);

			String agreementItem_installmentAmount = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentAmount));

			String agreementItem_installmentStartDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_installmentStartDateTime);
			if (agreementItem_itemType.equals("9"))
				agreementItem_installmentStartDateTime = JSONUtils.getInstallmentStartDate(
						agreementItem_installmentStartDateTime, agreementItem_itemType,
						agreementItem_installmentAmount);

			String agreementItem_installmentEndDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_installmentEndDateTime);
			agreementItem_installmentEndDateTime = JSONUtils.getEndDate(agreementItem_installmentEndDateTime,
					agreementItem_installmentStartDateTime, agreementItem_itemType, agreementItem_installmentAmount);
			if (agreementItem_itemType.equals("10") || agreementItem_itemType.equals("11"))
				agreementItem_installmentEndDateTime = "null";

			String agreementItem_installmentLeftNumValue = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentLeftNumValue));

			String agreementItem_installmentAppliedNumValue = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentAppliedNumValue));

			String agreementItem_installmentAppliedAmtValue = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_installmentAppliedAmtValue));

			String agreementItem_termOrConditionMinRatePlanValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionMinRatePlanValue);

			String agreementItem_termOrConditionMinFeatureValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionMinFeatureValue);

			String agreementItem_termOrConditionMinCombinedValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionMinCombinedValue);

			String agreementItem_termOrConditionCommitmentServiceCdValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionCommitmentServiceCdValue);

			if (agreementItem_itemType.equals("15") || agreementItem_itemType.equals("16")) {
				switch (agreementItem_termOrConditionCommitmentServiceCdValue) {
				case "false":
					agreementItem_termOrConditionCommitmentServiceCdValue = "null";
					break;
				}
			}

			String agreementItem_termOrConditionAutoTopupCommitmentIndValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_termOrConditionAutoTopupCommitmentIndValue);
			agreementItem_termOrConditionAutoTopupCommitmentIndValue = DBUtils
					.convertYN(agreementItem_termOrConditionAutoTopupCommitmentIndValue);

			String agreementItem_taxPaymentMethodCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxPaymentMethodCode);

			String agreementItem_taxPaymentMechanismCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxPaymentMechanismCode);

			String agreementItem_taxPaymentChannelCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxPaymentChannelCode);

			String agreementItem_taxProvinceCode = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxProvinceCode);

			String agreementItem_taxCategory = JSONUtils.checkValue(jsonString, agrmtItem.agreementItem_taxCategory);

			String agreementItem_taxRate = JSONUtils.checkValue(jsonString, agrmtItem.agreementItem_taxRate);

			String agreementItem_taxAmountValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_taxAmountValue);

			String agreementItem_productSerialNumber = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productSerialNumber);

			String agreementItem_productPriceValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productPriceValue);

			String agreementItem_productCharacteristicValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productCharacteristicValue);

			String agreementItem_promotionid = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_promotionid));

			String agreementItem_promotionPerspectiveDate = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_promotionPerspectiveDate));
			agreementItem_promotionPerspectiveDate = agreementItem_promotionPerspectiveDate.split("T")[0];

			String agreementItem_productOfferingid = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_productOfferingid));

			String offerID = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
					agrmtItem.agreementItem_productOfferingRedeemedOfferContextCodeValue));
			String agreementItem_productOfferingRedeemedOfferContextCodeValue = DBUtils.getProductOfferID(offerID);

			String agreementItem_productOfferingOfferTierCdValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productOfferingOfferTierCdValue);
			agreementItem_productOfferingOfferTierCdValue = DBUtils
					.getNullCode(agreementItem_productOfferingOfferTierCdValue);

			String agreementItem_productOfferingOfferTierCapAmtValue = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_productOfferingOfferTierCapAmtValue);
			agreementItem_productOfferingOfferTierCapAmtValue = DBUtils
					.getNullCode(agreementItem_productOfferingOfferTierCapAmtValue);

			String agreementItem_productOfferingDataCommitmentIndValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
							agrmtItem.agreementItem_productOfferingDataCommitmentIndValue));
			agreementItem_productOfferingDataCommitmentIndValue = DBUtils
					.convertYN(agreementItem_productOfferingDataCommitmentIndValue);

			String agreementItem_productOfferingContractEnforcedPlanIndValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
							agrmtItem.agreementItem_productOfferingContractEnforcedPlanIndValue));
			agreementItem_productOfferingContractEnforcedPlanIndValue = DBUtils
					.convertYN(agreementItem_productOfferingContractEnforcedPlanIndValue);
			if (agreementItem_itemType.equals("7")) {
				agreementItem_productOfferingContractEnforcedPlanIndValue = "N";
			}

			String agreementItem_productOfferingPerspectiveDate = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_productOfferingPerspectiveDate));
			agreementItem_productOfferingPerspectiveDate = agreementItem_productOfferingPerspectiveDate.split("T")[0];

			String agreementItem_productOfferingSourceSystemId = String.valueOf(JSONUtils
					.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_productOfferingSourceSystemId));

			/*
			 * if(currentIND.equals("N") && !agreementItem_itemType.equals("7")) {
			 * agreementItem_itemDurationEndDateTime=
			 * agreementItem_itemDurationStartDateTime;
			 * agreementItem_itemDurationAmount="0";
			 * agreementItem_productOfferingOfferTierCdValue="9999";
			 * agreementItem_productOfferingOfferTierCapAmtValue="99"; }
			 * if(agreementItem_itemType.equals("7")) {
			 * agreementItem_productOfferingid="1013546";
			 * agreementItem_productOfferingRedeemedOfferContextCodeValue="1";
			 * agreementItem_productOfferingOfferTierCdValue="9999";
			 * agreementItem_productOfferingOfferTierCapAmtValue="99"; }
			 */

			if (agreementItem_itemType.equals("17")) {
				agreementItem_termOrConditionMinRatePlanValue = "null";
				agreementItem_termOrConditionMinFeatureValue = "null";
				agreementItem_termOrConditionMinCombinedValue = "null";
				agreementItem_productSerialNumber = "null";
				agreementItem_productPriceValue = "null";
				agreementItem_productCharacteristicValue = "null";
			}

			if (AgreementDurationAmount.equals("0") && agreementItem_itemType.equals("5")) {
				agreementItem_termOrConditionMinRatePlanValue = "0";
				agreementItem_termOrConditionMinFeatureValue = "0";
				agreementItem_termOrConditionMinCombinedValue = "0";
			}

			String tax = null;
			switch (Itemtype) {
			case "HWS":
			case "TIASSETCREDIT":
			case "TIPROMOCREDIT":
			case "PRESOC":
			case "PRECREDIT":
			case "TAB":
			case "ACCESSORYFINANCE":
			case "ACTIVATIONBILLCREDIT":
			case "BIB":
			case "HARDWARE":
			case "FINANCE":
				rsAgreementItem = statement.executeQuery("Select * from cust_srvc_agrmt_item item  "
						+ "inner join CUST_SRVC_AGRMT_ITM_PROMO promo  "
						+ "on item.CUST_SRVC_AGRMT_ITEM_ID = promo.CUST_SRVC_AGRMT_ITEM_ID and item.customer_svc_agreement_id ='"
						+ agrmtId + "' and item.REWARD_PROGRAM_TYP_ID='" + agreementItem_itemType
						+ "' and item.ORIG_COMMITMENT_LENGTH_NUM !=0");
				tax = "NA";
				break;

			case "NA":
				rsAgreementItem = statement.executeQuery(
						"Select * from(select a.*,row_number() over(partition by customer_svc_agreement_id order by LAST_UPDT_TS)  "
								+ "as rn from cust_srvc_agrmt_item a where customer_svc_agreement_id ='" + agrmtId
								+ "') where rn=" + agreementItemNo);
				tax = "NA";
				break;
			}

			while (rsAgreementItem.next()) {

				rsRewardAcc = statement1.executeQuery("select * from reward_account where REWARD_PROGRAM_TYP_ID='"
						+ agreementItem_itemType + "' and BUSINESS_OBJECT_ID =" + subscriptionID);

				String agrmtItemId = String.valueOf(rsAgreementItem.getInt("CUST_SRVC_AGRMT_ITEM_ID"));

				Reporting.logReporter(Status.INFO, "CUST_SRVC_AGRMT_ITEM_ID Value from DB is : " + agrmtItemId);

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getDate("COMMITMENT_EFF_START_DT")),
						agreementItem_itemDurationStartDateTime, "COMMITMENT_EFF_START_DT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getDate("COMMITMENT_EFF_END_DT")),
						agreementItem_itemDurationEndDateTime, "COMMITMENT_EFF_END_DT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("ORIG_COMMITMENT_LENGTH_NUM")),
						agreementItem_itemDurationAmount, "ORIG_COMMITMENT_LENGTH_NUM");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REWARD_PROGRAM_TYP_ID")),
						agreementItem_itemType, "REWARD_PROGRAM_TYP_ID");

				if (!agreementItem_incentiveServiceCode.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("INCENTIVE_CD")),
							agreementItem_incentiveServiceCode, "INCENTIVE_CD");
				}

				if (!agreementItem_installmentStartDateTime.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getDate("REWARD_INSTLMNT_START_DT")),
							agreementItem_installmentStartDateTime, "REWARD_INSTLMNT_START_DT");
				}
				if (!agreementItem_installmentEndDateTime.equals("NA")) {

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getDate("REWARD_INSTLMNT_END_DT")),
							agreementItem_installmentEndDateTime, "REWARD_INSTLMNT_END_DT");
				}

				if (!agreementItem_installmentAmount.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getInt("REWARD_INSTLMNT_QTY")),
							agreementItem_installmentAmount, "REWARD_INSTLMNT_QTY");
				}
				// INSTALLMENTS_LEFT_NUM, INSTALLMENTS_APPLIED_NUM, INSTALLMENTS_APPLIED_AMT ->
				// No need to DB validation

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("RTPLN_MIN_COMMITMENT_AMT")),
						agreementItem_termOrConditionMinRatePlanValue, "RTPLN_MIN_COMMITMENT_AMT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("FEAT_MIN_COMMITMENT_AMT")),
						agreementItem_termOrConditionMinFeatureValue, "FEAT_MIN_COMMITMENT_AMT");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("COMB_MIN_COMMITMENT_AMT")),
						agreementItem_termOrConditionMinCombinedValue, "COMB_MIN_COMMITMENT_AMT");

				if (!agreementItem_termOrConditionCommitmentServiceCdValue.equals("NA"))
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("COMMITMENT_SERVICE_CD")),
							agreementItem_termOrConditionCommitmentServiceCdValue, "COMMITMENT_SERVICE_CD");

				if (!agreementItem_termOrConditionAutoTopupCommitmentIndValue.equals("NA"))
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("COMMITMENT_AUTOTOPUP_IND")),
							agreementItem_termOrConditionAutoTopupCommitmentIndValue, "COMMITMENT_AUTOTOPUP_IND");

				if (agreementItem_itemType.equals("4") || agreementItem_itemType.equals("9")) {
					rsAgreementTax = statement2.executeQuery(
							"select * from CUST_SRVC_AGRMT_ITM_TAX tax " + "inner join CUST_SRVC_AGRMT_ITM_TAX_DTL dtl "
									+ "on tax.CUST_SRVC_AGRMT_ITM_TAX_ID = dtl.CUST_SRVC_AGRMT_ITM_TAX_ID "
									+ "and tax.CUST_SRVC_AGRMT_ITEM_ID ='" + agrmtItemId + "'");

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementTax.getInt("TAX_PAYMENT_METHOD_CD")),
							agreementItem_taxPaymentMethodCode, "TAX_PAYMENT_METHOD_CD");

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementTax.getInt("TAX_PYMT_MECHANISM_CD")),
							agreementItem_taxPaymentMechanismCode, "TAX_PYMT_MECHANISM_CD");

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementTax.getInt("TAX_PAYMENT_CHANNEL_CD")),
							agreementItem_taxPaymentChannelCode, "TAX_PAYMENT_CHANNEL_CD");

					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementTax.getInt("TAXATION_PROVINCE_CD")),
							agreementItem_taxProvinceCode, "TAXATION_PROVINCE_CD");

					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementTax.getInt("TAX_TYPE_CD")),
							agreementItem_taxCategory, "TAX_TYPE_CD");

					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementTax.getInt("TAX_RATE_PCT")),
							agreementItem_taxRate, "TAX_RATE_PCT");

					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementTax.getInt("TAX_AMT")),
							agreementItem_taxAmountValue, "TAX_AMT");

				}

				/*
				 * 
				 * Value for these json
				 * 
				 * "description": [ { "locale": "EN_CA", "description":
				 * "APPLE IPHONE 4S 32GB BLACK" }, { "locale": "FR_CA", "description":
				 * "APPLE IPHONE 4S 32GB BLACK" } ] agreementItem_productSpecificationLocale1
				 * agreementItem_productSpecificationDesc1
				 * 
				 */

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REDEEMED_PROMOTION_ID")),
						agreementItem_promotionid, "REDEEMED_PROMOTION_ID");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getDate("REDEEMED_PROMOTION_TS")),
						agreementItem_promotionPerspectiveDate, "REDEEMED_PROMOTION_TS");

				if (!agreementItem_productSerialNumber.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("HANDSET_SERIAL_NUM")),
							agreementItem_productSerialNumber, "HANDSET_SERIAL_NUM");
				}
				if (!agreementItem_productPriceValue.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(
							String.valueOf(rsAgreementItem.getString("CATALOGUE_ITEM_PRICE_AMT")),
							agreementItem_productPriceValue, "CATALOGUE_ITEM_PRICE_AMT");
				}
				if (!agreementItem_productCharacteristicValue.equals("NA")) {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getString("USIM_ID")),
							agreementItem_productCharacteristicValue, "USIM_ID");
				}

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_ID")),
						agreementItem_productOfferingid, "REDEEMED_OFFER_ID");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TYPE_ID")),
						agreementItem_productOfferingRedeemedOfferContextCodeValue, "REDEEMED_OFFER_TYPE_ID");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TIER_CD")),
						agreementItem_productOfferingOfferTierCdValue, "REDEEMED_OFFER_TIER_CD");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TIER_CAP_AMT")),
						agreementItem_productOfferingOfferTierCapAmtValue, "REDEEMED_OFFER_TIER_CAP_AMT");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getString("DATA_SRVC_REQ_IND")),
						agreementItem_productOfferingDataCommitmentIndValue, "DATA_SRVC_REQ_IND");

				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(rsAgreementItem.getString("COMB_MIN_CMITMT_DISCHRG_IND")),
						agreementItem_productOfferingContractEnforcedPlanIndValue, "COMB_MIN_CMITMT_DISCHRG_IND");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getDate("REDEEMED_OFFER_TS")),
						agreementItem_productOfferingPerspectiveDate, "REDEEMED_OFFER_TS");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_SYS_ID")),
						agreementItem_productOfferingSourceSystemId, "REDEEMED_OFFER_SYS_ID");

				while (rsRewardAcc.next()) {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsRewardAcc.getInt("CURRENCY_BAL_AMT")),
							"-" + agreementItem_incentiveAmount, "CURRENCY_BAL_AMT");

				}

			}
			rsAgreementItem.close();

		}
	}

	public static void payloadValueDeclarationWithItemtype(String jsonString, String itemType) throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet resultSet = null;

		ActivationPayloadJsonPath jsonPath = new ActivationPayloadJsonPath();

		AgreementDurationAmount = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.AgreementDurationAmount));

		String AgreementDurationStartDateTime = JSONUtils.checkValue(jsonString,
				jsonPath.AgreementDurationStartDateTime);
		/*
		 * if(itemType.equals("BIB") || itemType.equals("TIASSETCREDIT")) {
		 * AgreementDurationStartDateTime=JSONUtils.getInstallmentStartDate(
		 * AgreementDurationStartDateTime, itemType, AgreementDurationAmount); }
		 */

		String AgreementDurationEndDateTime = JSONUtils.checkValue(jsonString, jsonPath.AgreementDurationEndDateTime);
		AgreementDurationEndDateTime = JSONUtils.getAgrmtEndDate(AgreementDurationEndDateTime,
				AgreementDurationStartDateTime, AgreementDurationAmount);
		if (itemType.equals("BIB") || itemType.equals("TIASSETCREDIT") || itemType.equals("TIPROMOCRDIT")) {
			LocalDate endDateFormatted;
			endDateFormatted = LocalDate.parse(AgreementDurationStartDateTime);
			endDateFormatted = endDateFormatted.plusDays(180);
			AgreementDurationEndDateTime = endDateFormatted.toString();
		}

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

		String relatedParty_Subid = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_Subid));

		String relatedParty_MarketProvinceValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_MarketProvinceValue));

		String relatedParty_HomeProvinceValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_HomeProvinceValue));

		String relatedParty_SubscriberNumValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.relatedParty_SubscriberNumValue));

		String relatedParty_ComboRatePlanIndValue = JSONUtils.checkValue(jsonString,
				jsonPath.relatedParty_ComboRatePlanIndValue);
		relatedParty_ComboRatePlanIndValue = DBUtils.convertYN(relatedParty_ComboRatePlanIndValue);

		Reporting.logReporter(Status.INFO, "--------------------DB Validation Starts-----------------------");

		resultSet = statement.executeQuery("SELECT * FROM  CUSTOMER_SERVICE_AGREEMENT agrmt "
				+ "where agrmt.subscriber_no ='" + relatedParty_SubscriberNumValue + "' and current_ind='Y' ");

		while (resultSet.next()) {

			agrmtId = String.valueOf(resultSet.getInt("CUSTOMER_SVC_AGREEMENT_ID"));

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_START_DT")),
					AgreementDurationStartDateTime, "AGREEMENT_START_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getDate("AGREEMENT_END_DT")),
					AgreementDurationEndDateTime, "AGREEMENT_END_DT");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("COMMITMENT_LENGTH_NUM")),
					AgreementDurationAmount, "COMMITMENT_LENGTH_NUM");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BAN")), relatedParty_Accid, "BAN");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("BRAND_ID")),
					relatedParty_brandidValue, "BRAND_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("ACCOUNT_TYPE_CD")),
					relatedParty_AccTypeCodeValue, "ACCOUNT_TYPE_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("ACCOUNT_SUBTYPE_CD")),
					relatedParty_AccSubTypeCodeValue, "ACCOUNT_SUBTYPE_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("CHNL_ORG_ID")),
					relatedParty_ChnlOrgValue, "CHNL_ORG_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("SALES_REP_ID")),
					relatedParty_SalesRepidValue, "SALES_REP_ID");

			/*
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("")),
			 * relatedParty_Oriid, "");
			 * 
			 * GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt(
			 * "REWARD_TXN_ID")), relatedParty_TransactionidValue, "REWARD_TXN_ID");
			 * 
			 */
			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getInt("SUBSCRIPTION_ID")),
					relatedParty_Subid, "SUBSCRIPTION_ID");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("CUST_PHONE_PROV_CD")),
					relatedParty_MarketProvinceValue, "CUST_PHONE_PROV_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("CUST_HOME_PROV_CD")),
					relatedParty_HomeProvinceValue, "CUST_HOME_PROV_CD");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getLong("SUBSCRIBER_NO")),
					relatedParty_SubscriberNumValue, "SUBSCRIBER_NO");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("COMBO_RTPLN_IND")),
					relatedParty_ComboRatePlanIndValue, "COMBO_RTPLN_IND");
		}

	}

	public static void extraDBvalidation(String jsonString, String action, int itemNo) throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet resultSet = null;

		Statement statement1 = null;
		statement1 = DBUtils.Conn.createStatement();
		ResultSet resultSet1 = null;

		Statement statement2 = null;
		statement2 = DBUtils.Conn.createStatement();
		ResultSet resultSet2 = null;

		Statement statement3 = null;
		statement3 = DBUtils.Conn.createStatement();
		ResultSet resultSetReward = null;
		
		ActivationPayloadJsonPath jsonPath=null;
		
		switch(action)
		{
		case "activation":
			jsonPath = new ActivationPayloadJsonPath();
			break;
		
		/*case "renewal":
			jsonPath = new Renewal();
			break;*/
			
		}

		
		String AgreementDurationStartDateTime = JSONUtils.checkValue(jsonString,
				jsonPath.AgreementDurationStartDateTime);

		String relatedParty_SubscriberNumValue = JSONUtils.checkValue(jsonString,
				jsonPath.relatedParty_SubscriberNumValue);

		String relatedParty_Subid = JSONUtils.checkValue(jsonString, jsonPath.relatedParty_Subid);

		String relatedParty_Oriid = JSONUtils.checkValue(jsonString, jsonPath.relatedParty_Oriid);

		String relatedParty_TransactionidValue = JSONUtils.checkValue(jsonString,
				jsonPath.relatedParty_TransactionidValue);

		// DB Validation
		resultSet = statement.executeQuery("SELECT * FROM  CUSTOMER_SERVICE_AGREEMENT agrmt "
				+ "inner join cust_agrmt_lifecycl lyfcycl "
				+ "on agrmt.CUSTOMER_SVC_AGREEMENT_ID = lyfcycl.CUSTOMER_SVC_AGREEMENT_ID "
				+ "and agrmt.subscriber_no ='" + relatedParty_SubscriberNumValue + "' and agrmt.current_ind='Y'");
		while (resultSet.next()) {

			Reporting.logReporter(Status.INFO, "Validating - CUST_AGRMT_LIFECYCL");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("EFF_START_TS").split(" ")[0]),
					AgreementDurationStartDateTime, "EFF_START_TS");

			GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet.getString("EFF_STOP_TS").split(" ")[0]),
					"9999-12-31", "EFF_STOP_TS");

			switch (action) {
			case "activation":
				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(resultSet.getString("CUST_AGRMT_STAT_LIFECYCL_ID")), "1",
						"CUST_AGRMT_STAT_LIFECYCL_ID");
				break;
			case "cancellation":
				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(resultSet.getString("CUST_AGRMT_STAT_LIFECYCL_ID")), "3",
						"CUST_AGRMT_STAT_LIFECYCL_ID");
				break;
			case "suspended":
				GenericUtils.validateAssertEqualsFromDB(
						String.valueOf(resultSet.getString("CUST_AGRMT_STAT_LIFECYCL_ID")), "4",
						"CUST_AGRMT_STAT_LIFECYCL_ID");
				break;
			}
		}
		int agreementItemNo = 0;

		Reporting.logReporter(Status.INFO, "Validating - REWARD_ACCOUNT");
		for (int i = 0; i < itemNo; i++) {

			AgreementItem agrmtItem = new AgreementItem(i);
			agreementItemNo = i + 1;
			Reporting.logReporter(Status.INFO, "--- Agreement Item : " + agreementItemNo + "---");

			Itemtype = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemType));
			String agreementItem_itemType = DBUtils.getItemType(Itemtype);

			Reporting.logReporter(Status.INFO, "Validating - Reward Type  - " + Itemtype);

			if (!agreementItem_itemType.equals("15") || !agreementItem_itemType.equals("16")
					|| !agreementItem_itemType.equals("10") || !agreementItem_itemType.equals("11")
					|| !agreementItem_itemType.equals("5") || !agreementItem_itemType.equals("null")) {

				String agreementItem_incentiveAmount = null;

				if (agreementItem_itemType.equals("1") || agreementItem_itemType.equals("14")) {
					int FirstInst = Integer
							.valueOf(JSONUtils.checkValue(jsonString, agrmtItem.agreementItem_incentiveAmount));
					int instDuration = Integer.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
							agrmtItem.agreementItem_itemDurationAmount));
					try {
						int drawDownAmt = FirstInst / instDuration;
						agreementItem_incentiveAmount = String.valueOf(FirstInst - drawDownAmt);
					} catch (Exception e) {
						float drawDownAmt = FirstInst / instDuration;
						agreementItem_incentiveAmount = String.valueOf(FirstInst - drawDownAmt);
					}

				} else
					agreementItem_incentiveAmount = JSONUtils.checkValue(jsonString,
							agrmtItem.agreementItem_incentiveAmount);

				resultSet1 = statement1
						.executeQuery("Select * from cust_srvc_agrmt_item item where item.customer_svc_agreement_id ='"
								+ agrmtId + "' and item.REWARD_PROGRAM_TYP_ID=" + agreementItem_itemType);

				resultSetReward = statement3.executeQuery("select * from reward_account where REWARD_PROGRAM_TYP_ID='"
						+ agreementItem_itemType + "' and BUSINESS_OBJECT_ID =" + relatedParty_Subid);

				while (resultSet1.next()) {
					while (resultSetReward.next()) {

						String rewardAccID = String.valueOf(resultSetReward.getString("REWARD_ACCOUNT_ID"));

						resultSet2 = statement2.executeQuery(
								"select * from reward_txn txn " + "inner join ASSOCIATED_REWARD_TXN asso_txn "
										+ "on txn.REWARD_TXN_ID = asso_txn.REWARD_TXN_ID "
										+ "inner join REWARD_ACCT_CSA_ITEM_ASSN reward_item "
										+ "on txn.REWARD_ACCOUNT_ID = reward_item.REWARD_ACCOUNT_ID "
										+ "and txn.REWARD_ACCOUNT_ID =" + rewardAccID + " order by txn.TXN_TS");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(resultSetReward.getString("CREATE_TS").split(" ")[0]),
								AgreementDurationStartDateTime, "CREATE_TS");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(resultSetReward.getString("STATUS_LIFECYCLE_ID")), "2",
								"STATUS_LIFECYCLE_ID");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(resultSetReward.getInt("CURR_FIN_COMMITMENT_ID")),
								(String.valueOf(resultSet1.getInt("FIN_COMMITMENT_ID"))), "CURR_FIN_COMMITMENT_ID");

						if (agreementItem_itemType.equals("1") || agreementItem_itemType.equals("14")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(resultSetReward.getInt("REMAINING_INSTLMNT_QTY")), "0",
									"REMAINING_INSTLMNT_QTY");
						} else {

							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(resultSetReward.getString("REMAINING_INSTLMNT_QTY")),
									(String.valueOf(resultSet1.getString("ORIG_COMMITMENT_LENGTH_NUM"))),
									"REMAINING_INSTLMNT_QTY");
						}

						if (!agreementItem_itemType.equals("17"))
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(resultSetReward.getInt("REWARD_PGM_CATGY_ID")), "1",
									"REWARD_PGM_CATGY_ID");
						else
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(resultSetReward.getInt("REWARD_PGM_CATGY_ID")), "0",
									"REWARD_PGM_CATGY_ID");

						if (agreementItem_itemType.equals("4")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(resultSetReward.getString("CURRENCY_BAL_AMT")), "0",
									"CURRENCY_BAL_AMT");

						} else
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(resultSetReward.getString("CURRENCY_BAL_AMT")),
									"-" + agreementItem_incentiveAmount, "CURRENCY_BAL_AMT");

						while (resultSet2.next()) {
							Reporting.logReporter(Status.INFO, "Validating - REWARD_TXN Table");

							Reporting.logReporter(Status.INFO,
									"REWARD_TXN_ID : " + resultSet2.getString("REWARD_TXN_ID"));

							Reporting.logReporter(Status.INFO,
									"CUST_SRVC_AGRMT_ITEM_ID : " + resultSet2.getString("CUST_SRVC_AGRMT_ITEM_ID"));

							if (agreementItem_itemType.equals("1") || agreementItem_itemType.equals("14")) {

								GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet2.getDate("CREATE_TS")),
										AgreementDurationStartDateTime, "CREATE_TS");

								agreementItem_incentiveAmount = JSONUtils.checkValue(jsonString,
										agrmtItem.agreementItem_incentiveAmount);
								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("CURRENCY_BAL_AMT")),
										"-" + agreementItem_incentiveAmount, "CURRENCY_BAL_AMT");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("CURRENCY_AMT")),
										"-" + agreementItem_incentiveAmount, "CURRENCY_AMT");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("REWARD_TXN_RSN_ID")), "1",
										"REWARD_TXN_RSN_ID");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("SOURCE_SYSTEM_ID")), relatedParty_Oriid,
										"SOURCE_SYSTEM_ID");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("SOURCE_TXN_ID")),
										relatedParty_TransactionidValue, "SOURCE_TXN_ID");

								while (resultSet2.next()) {
									Reporting.logReporter(Status.INFO, "Draw down happened");
									int FirstInst = Integer.valueOf(
											JSONUtils.checkValue(jsonString, agrmtItem.agreementItem_incentiveAmount));
									int instDuration = Integer.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(
											jsonString, agrmtItem.agreementItem_itemDurationAmount));
									int drawDownAmt = FirstInst / instDuration;
									agreementItem_incentiveAmount = String.valueOf(FirstInst - drawDownAmt);

									GenericUtils.validateAssertEqualsFromDB(
											String.valueOf(resultSet2.getDate("CREATE_TS")),
											AgreementDurationStartDateTime, "CREATE_TS");

									GenericUtils.validateAssertEqualsFromDB(
											String.valueOf(resultSet2.getString("CURRENCY_BAL_AMT")),
											"-" + agreementItem_incentiveAmount, "CURRENCY_BAL_AMT");

									GenericUtils.validateAssertEqualsFromDB(
											String.valueOf(resultSet2.getString("CURRENCY_AMT")),
											String.valueOf(drawDownAmt), "CURRENCY_AMT");

									GenericUtils.validateAssertEqualsFromDB(
											String.valueOf(resultSet2.getString("REWARD_TXN_RSN_ID")), "4",
											"REWARD_TXN_RSN_ID");

								}

							} else {

								GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSet2.getDate("CREATE_TS")),
										AgreementDurationStartDateTime, "CREATE_TS");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("CURRENCY_BAL_AMT")),
										"-" + agreementItem_incentiveAmount, "CURRENCY_BAL_AMT");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("CURRENCY_AMT")),
										"-" + agreementItem_incentiveAmount, "CURRENCY_AMT");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("SOURCE_SYSTEM_ID")), relatedParty_Oriid,
										"SOURCE_SYSTEM_ID");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(resultSet2.getString("SOURCE_TXN_ID")),
										relatedParty_TransactionidValue, "SOURCE_TXN_ID");

							}
						}

					}
					resultSet.close();

				}
			} else {
				Reporting.logReporter(Status.INFO, Itemtype + "Wont flow to Reward Account or Transaction table");

			}
		}
	}

	public static HashMap NoOfAgrmt(String jsonString) {

		int agrmtNo = 0;
		String agrmt = "null";
		HashMap<String, Integer> noOfAgrmt = new HashMap<String, Integer>();

		do {

			agrmt = JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, "$[" + agrmtNo + "]");
			Reporting.logReporter(Status.INFO, "Agreement String : " + agrmt);
			if (!agrmt.equals("NA"))
				noOfAgrmt.put("Agreement" + agrmtNo, agrmtNo);
			agrmtNo++;
		} while (!agrmt.equals("NA"));
		return noOfAgrmt;
	}

	public static HashMap NoOfAgrmtItem(String jsonString, int agrmtNo) {

		int agrmtItemNo = 0;
		String agrmtItem = "null";
		HashMap<String, Integer> noOfAgrmtItem = new HashMap<String, Integer>();

		do {

			agrmtItem = JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
					"$[" + agrmtNo + "].agreementItem[" + agrmtItemNo + "].itemType");
			if (!agrmtItem.equals("NA"))
				noOfAgrmtItem.put(agrmtItem, agrmtItemNo);
			agrmtItemNo++;
		} while (!agrmtItem.equals("NA"));

		return noOfAgrmtItem;

	}

	public static void responseDBCheckAgrmtItemNew(String jsonString, String subscriptionID, int agrmtNo)
			throws SQLException, InterruptedException {

		Statement statementAgrmt = null;
		statementAgrmt = DBUtils.Conn.createStatement();
		ResultSet resultSetAgrmt = null;

		HashMap<String, Integer> noOfAgrmtItem = new HashMap<String, Integer>();

		int agreementNo = 1;
		int agreementItem = 0;

		Reporting.logReporter(Status.INFO, "--------------------DB Validation Starts-----------------------");

		for (int i = 0; i < agrmtNo; i++) {

			noOfAgrmtItem = GenericUtils.NoOfAgrmtItem(jsonString, i);

			GetRewardCommitmentNew jsonPath1 = new GetRewardCommitmentNew(i);

			String AgreementDurationStartDateTime = JSONUtils.checkValue(jsonString,
					jsonPath1.AgreementDurationStartDateTime);

			AgreementDurationAmount = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath1.AgreementDurationAmount));

			String AgreementDurationEndDateTime = JSONUtils.checkValue(jsonString,
					jsonPath1.AgreementDurationEndDateTime);

			String relatedParty_brandidValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath1.relatedParty_brandidValue));

			resultSetAgrmt = statementAgrmt
					.executeQuery("Select * from(select agmt.*,row_number() over(order by CURRENT_IND desc) \r\n"
							+ "as rn from CUSTOMER_SERVICE_AGREEMENT agmt where agmt.SUBSCRIPTION_ID ='"
							+ subscriptionID + "') where rn=" + agreementNo);

			while (resultSetAgrmt.next()) {

				Reporting.logReporter(Status.INFO, "--- Agreement : " + agreementNo + "---");
				agreementNo++;

				agrmtId = String.valueOf(resultSetAgrmt.getInt("CUSTOMER_SVC_AGREEMENT_ID"));
				currentIND = String.valueOf(resultSetAgrmt.getString("CURRENT_IND"));

				Reporting.logReporter(Status.INFO, "Agreement ID : " + agrmtId);

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetAgrmt.getDate("AGREEMENT_END_DT")),
						AgreementDurationEndDateTime, "AGREEMENT_END_DT");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetAgrmt.getDate("AGREEMENT_START_DT")),
						AgreementDurationStartDateTime, "AGREEMENT_START_DT");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetAgrmt.getInt("COMMITMENT_LENGTH_NUM")),
						AgreementDurationAmount, "COMMITMENT_LENGTH_NUM");

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetAgrmt.getInt("BRAND_ID")),
						relatedParty_brandidValue, "BRAND_ID");

				for (int j = 0; j < noOfAgrmtItem.size(); j++) {

					GetRewardCommitmentNew jsonPath = new GetRewardCommitmentNew(i, j);

					agreementItem = j + 1;

					Statement statement = null;
					statement = DBUtils.Conn.createStatement();
					ResultSet rsAgreementItem = null;

					Statement statement1 = null;
					statement1 = DBUtils.Conn.createStatement();
					ResultSet rsRewardAcc = null;

					Statement statement2 = null;
					statement2 = DBUtils.Conn.createStatement();
					ResultSet rsAgreementTax = null;

					Itemtype = String.valueOf(
							JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_itemType));
					String agreementItem_itemType = DBUtils.getItemType(Itemtype);

					Reporting.logReporter(Status.INFO,
							"--- Agreement Item : " + agreementItem + " " + Itemtype + " : " + agreementItem_itemType);

					String agreementItem_itemDurationAmount = String.valueOf(JSONUtils
							.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_itemDurationAmount));

					String agreementItem_itemDurationStartDateTime = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_itemDurationStartDateTime);

					String agreementItem_itemDurationEndDateTime = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_itemDurationEndDateTime);

					String agreementItem_incentiveAmount = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_incentiveAmount);

					String agreementItem_incentiveServiceCode = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_incentiveServiceCode);

					String agreementItem_installmentAmount = String.valueOf(JSONUtils
							.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentAmount));

					String agreementItem_installmentStartDateTime = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_installmentStartDateTime);
					
					String agreementItem_installmentEndDateTime = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_installmentEndDateTime);
					agreementItem_installmentEndDateTime = JSONUtils.getEndDate(agreementItem_installmentEndDateTime,
							agreementItem_installmentStartDateTime, agreementItem_itemType,
							agreementItem_installmentAmount);
					if (agreementItem_itemType.equals("10") || agreementItem_itemType.equals("11"))
						agreementItem_installmentEndDateTime = "null";

					String agreementItem_installmentLeftNumValue = String.valueOf(JSONUtils
							.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_installmentLeftNumValue));

					String agreementItem_installmentAppliedNumValue = String
							.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
									jsonPath.agreementItem_installmentAppliedNumValue));

					String agreementItem_installmentAppliedAmtValue = String
							.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
									jsonPath.agreementItem_installmentAppliedAmtValue));

					String agreementItem_termOrConditionMinRatePlanValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_termOrConditionMinRatePlanValue);

					String agreementItem_termOrConditionMinFeatureValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_termOrConditionMinFeatureValue);

					String agreementItem_termOrConditionMinCombinedValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_termOrConditionMinCombinedValue);

					String agreementItem_termOrConditionCommitmentServiceCdValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_termOrConditionCommitmentServiceCdValue);

					if (agreementItem_itemType.equals("15") || agreementItem_itemType.equals("16")) {
						switch (agreementItem_termOrConditionCommitmentServiceCdValue) {
						case "false":
							agreementItem_termOrConditionCommitmentServiceCdValue = "null";
							break;
						}
					}

					String agreementItem_termOrConditionAutoTopupCommitmentIndValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_termOrConditionAutoTopupCommitmentIndValue);
					agreementItem_termOrConditionAutoTopupCommitmentIndValue = DBUtils
							.convertYN(agreementItem_termOrConditionAutoTopupCommitmentIndValue);

					String agreementItem_taxPaymentMethodCode = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_taxPaymentMethodCode);

					String agreementItem_taxPaymentMechanismCode = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_taxPaymentMechanismCode);

					String agreementItem_taxPaymentChannelCode = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_taxPaymentChannelCode);

					String agreementItem_taxProvinceCode = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_taxProvinceCode);

					String agreementItem_taxCategory = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_taxCategory);

					String agreementItem_taxRate = JSONUtils.checkValue(jsonString, jsonPath.agreementItem_taxRate);

					String agreementItem_taxAmountValue = String.valueOf(
							JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_taxAmountValue));

					String agreementItem_productSerialNumber = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_productSerialNumber);

					String agreementItem_productPriceValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_productPriceValue);

					String agreementItem_productCharacteristicValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_productCharacteristicValue);

					String agreementItem_promotionid = String.valueOf(
							JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_promotionid));

					String agreementItem_promotionPerspectiveDate = String.valueOf(JSONUtils
							.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_promotionPerspectiveDate));
					agreementItem_promotionPerspectiveDate = agreementItem_promotionPerspectiveDate.split("T")[0];

					String agreementItem_productOfferingid = String.valueOf(JSONUtils
							.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.agreementItem_productOfferingid));

					String offerID = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
							jsonPath.agreementItem_productOfferingRedeemedOfferContextCodeValue));
					String agreementItem_productOfferingRedeemedOfferContextCodeValue = DBUtils
							.getProductOfferID(offerID);

					String agreementItem_productOfferingOfferTierCdValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_productOfferingOfferTierCdValue);
					agreementItem_productOfferingOfferTierCdValue = DBUtils
							.getNullCode(agreementItem_productOfferingOfferTierCdValue);

					String agreementItem_productOfferingOfferTierCapAmtValue = JSONUtils.checkValue(jsonString,
							jsonPath.agreementItem_productOfferingOfferTierCapAmtValue);
					agreementItem_productOfferingOfferTierCapAmtValue = DBUtils
							.getNullCode(agreementItem_productOfferingOfferTierCapAmtValue);

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
					if (agreementItem_itemType.equals("7") || agreementItem_itemType.equals("9")
							|| agreementItem_itemType.equals("1")) {
						agreementItem_productOfferingContractEnforcedPlanIndValue = "N";
					}

					String agreementItem_productOfferingPerspectiveDate = String
							.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
									jsonPath.agreementItem_productOfferingPerspectiveDate));
					agreementItem_productOfferingPerspectiveDate = agreementItem_productOfferingPerspectiveDate
							.split("T")[0];

					String agreementItem_productOfferingSourceSystemId = String
							.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString,
									jsonPath.agreementItem_productOfferingSourceSystemId));

					if (agreementItem_installmentAmount.equals("0")) {
						agreementItem_installmentEndDateTime = agreementItem_installmentStartDateTime;
					}

					if (agreementItem_itemType.equals("17")) {
						agreementItem_termOrConditionMinRatePlanValue = "0";
						agreementItem_termOrConditionMinFeatureValue = "0";
						agreementItem_termOrConditionMinCombinedValue = "0";
						agreementItem_productSerialNumber = "null";
						agreementItem_productPriceValue = "0";
						agreementItem_productCharacteristicValue = "null";
					}

					if (AgreementDurationAmount.equals("0") && agreementItem_itemType.equals("5")) {
						agreementItem_termOrConditionMinRatePlanValue = "0";
						agreementItem_termOrConditionMinFeatureValue = "0";
						agreementItem_termOrConditionMinCombinedValue = "0";
					}

					String tax = null;
					switch (Itemtype) {
					case "HWS":
					case "TIASSETCREDIT":
					case "TIPROMOCREDIT":
					case "PRESOC":
					case "PRECREDIT":
					case "TAB":
					case "ACTIVATIONBILLCREDIT":
					case "RENEWALBILLCREDIT":
					case "BIB":
					case "HARDWARE":
					case "FINANCE":
						rsAgreementItem = statement.executeQuery("Select * from cust_srvc_agrmt_item item  "
								+ "inner join CUST_SRVC_AGRMT_ITM_PROMO promo  "
								+ "on item.CUST_SRVC_AGRMT_ITEM_ID = promo.CUST_SRVC_AGRMT_ITEM_ID and item.customer_svc_agreement_id ='"
								+ agrmtId + "' and item.REWARD_PROGRAM_TYP_ID='" + agreementItem_itemType + "'"
						        + " and item.ORIG_COMMITMENT_LENGTH_NUM !=0");
						tax = "NA";
						Thread.sleep(1000);
						break;

					case "ACCESSORYFINANCE":
						rsAgreementItem = statement.executeQuery("Select * from cust_srvc_agrmt_item item  "
								+ "where item.customer_svc_agreement_id ='" + agrmtId
								+ "' and item.REWARD_PROGRAM_TYP_ID='" + agreementItem_itemType + "'");
						// + "' and item.ORIG_COMMITMENT_LENGTH_NUM !=0");
						tax = "NA";
						Thread.sleep(1000);
						break;
					case "NA":
						rsAgreementItem = statement.executeQuery(
								"Select * from(select a.*,row_number() over(partition by customer_svc_agreement_id order by LAST_UPDT_TS)  "
										+ "as rn from cust_srvc_agrmt_item a where customer_svc_agreement_id ='"
										+ agrmtId + "') where rn=" + agreementItem);
						tax = "NA";
						break;
					}

					while (rsAgreementItem.next()) {

						rsRewardAcc = statement1
								.executeQuery("select * from reward_account where REWARD_PROGRAM_TYP_ID='"
										+ agreementItem_itemType + "' and BUSINESS_OBJECT_ID =" + subscriptionID);

						String agrmtItemID = String.valueOf(rsAgreementItem.getInt("CUST_SRVC_AGRMT_ITEM_ID"));

						Reporting.logReporter(Status.INFO, "CUST_SRVC_AGRMT_ITEM_ID Value from DB is : " + agrmtItemID);

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getDate("COMMITMENT_EFF_START_DT")),
								agreementItem_itemDurationStartDateTime, "COMMITMENT_EFF_START_DT");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getDate("COMMITMENT_EFF_END_DT")),
								agreementItem_itemDurationEndDateTime, "COMMITMENT_EFF_END_DT");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("ORIG_COMMITMENT_LENGTH_NUM")),
								agreementItem_itemDurationAmount, "ORIG_COMMITMENT_LENGTH_NUM");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("REWARD_PROGRAM_TYP_ID")), agreementItem_itemType,
								"REWARD_PROGRAM_TYP_ID");

						if (!agreementItem_incentiveServiceCode.equals("NA")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getInt("INCENTIVE_CD")),
									agreementItem_incentiveServiceCode, "INCENTIVE_CD");
						}

						if (!agreementItem_installmentStartDateTime.equals("NA")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getDate("REWARD_INSTLMNT_START_DT")),
									agreementItem_installmentStartDateTime, "REWARD_INSTLMNT_START_DT");
						}
						if (!agreementItem_installmentEndDateTime.equals("NA")) {

							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getDate("REWARD_INSTLMNT_END_DT")),
									agreementItem_installmentEndDateTime, "REWARD_INSTLMNT_END_DT");
						}

						if (!agreementItem_installmentAmount.equals("NA")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getInt("REWARD_INSTLMNT_QTY")),
									agreementItem_installmentAmount, "REWARD_INSTLMNT_QTY");
						}
						// INSTALLMENTS_LEFT_NUM, INSTALLMENTS_APPLIED_NUM, INSTALLMENTS_APPLIED_AMT ->
						// No need to DB validation

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("RTPLN_MIN_COMMITMENT_AMT")),
								agreementItem_termOrConditionMinRatePlanValue, "RTPLN_MIN_COMMITMENT_AMT");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("FEAT_MIN_COMMITMENT_AMT")),
								agreementItem_termOrConditionMinFeatureValue, "FEAT_MIN_COMMITMENT_AMT");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("COMB_MIN_COMMITMENT_AMT")),
								agreementItem_termOrConditionMinCombinedValue, "COMB_MIN_COMMITMENT_AMT");

						if (!agreementItem_termOrConditionCommitmentServiceCdValue.equals("NA"))
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getString("COMMITMENT_SERVICE_CD")),
									agreementItem_termOrConditionCommitmentServiceCdValue, "COMMITMENT_SERVICE_CD");

						if (!agreementItem_termOrConditionAutoTopupCommitmentIndValue.equals("NA"))
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getString("COMMITMENT_AUTOTOPUP_IND")),
									agreementItem_termOrConditionAutoTopupCommitmentIndValue,
									"COMMITMENT_AUTOTOPUP_IND");

						if (agreementItem_itemType.equals("4") || agreementItem_itemType.equals("9")) {
							rsAgreementTax = statement2.executeQuery("select * from CUST_SRVC_AGRMT_ITM_TAX tax "
									+ "inner join CUST_SRVC_AGRMT_ITM_TAX_DTL dtl "
									+ "on tax.CUST_SRVC_AGRMT_ITM_TAX_ID = dtl.CUST_SRVC_AGRMT_ITM_TAX_ID "
									+ "and tax.CUST_SRVC_AGRMT_ITEM_ID ='" + agrmtItemID + "'");

							while (rsAgreementTax.next()) {
								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAX_PAYMENT_METHOD_CD")),
										agreementItem_taxPaymentMethodCode, "TAX_PAYMENT_METHOD_CD");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAX_PYMT_MECHANISM_CD")),
										agreementItem_taxPaymentMechanismCode, "TAX_PYMT_MECHANISM_CD");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAX_PAYMENT_CHANNEL_CD")),
										agreementItem_taxPaymentChannelCode, "TAX_PAYMENT_CHANNEL_CD");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAXATION_PROVINCE_CD")),
										agreementItem_taxProvinceCode, "TAXATION_PROVINCE_CD");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAX_TYPE_CD")),
										agreementItem_taxCategory, "TAX_TYPE_CD");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAX_RATE_PCT")), agreementItem_taxRate,
										"TAX_RATE_PCT");

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsAgreementTax.getString("TAX_AMT")),
										agreementItem_taxAmountValue, "TAX_AMT");
							}

						}

						if (!agreementItem_itemType.equals("17")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getInt("REDEEMED_PROMOTION_ID")),
									agreementItem_promotionid, "REDEEMED_PROMOTION_ID");

							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getDate("REDEEMED_PROMOTION_TS")),
									agreementItem_promotionPerspectiveDate, "REDEEMED_PROMOTION_TS");
						}

						if (!agreementItem_productSerialNumber.equals("NA")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getString("HANDSET_SERIAL_NUM")),
									agreementItem_productSerialNumber, "HANDSET_SERIAL_NUM");
						}
						if (!agreementItem_productPriceValue.equals("NA")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getInt("CATALOGUE_ITEM_PRICE_AMT")),
									agreementItem_productPriceValue, "CATALOGUE_ITEM_PRICE_AMT");
						}
						if (!agreementItem_productCharacteristicValue.equals("NA")) {
							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getString("USIM_ID")),
									agreementItem_productCharacteristicValue, "USIM_ID");
						}

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_ID")),
								agreementItem_productOfferingid, "REDEEMED_OFFER_ID");

						/*
						 * GenericUtils.validateAssertEqualsFromDB(
						 * String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TYPE_ID")),
						 * agreementItem_productOfferingRedeemedOfferContextCodeValue,
						 * "REDEEMED_OFFER_TYPE_ID");
						 */
						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TIER_CD")),
								agreementItem_productOfferingOfferTierCdValue, "REDEEMED_OFFER_TIER_CD");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_TIER_CAP_AMT")),
								agreementItem_productOfferingOfferTierCapAmtValue, "REDEEMED_OFFER_TIER_CAP_AMT");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getString("DATA_SRVC_REQ_IND")),
								agreementItem_productOfferingDataCommitmentIndValue, "DATA_SRVC_REQ_IND");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getString("COMB_MIN_CMITMT_DISCHRG_IND")),
								agreementItem_productOfferingContractEnforcedPlanIndValue,
								"COMB_MIN_CMITMT_DISCHRG_IND");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getDate("REDEEMED_OFFER_TS")),
								agreementItem_productOfferingPerspectiveDate, "REDEEMED_OFFER_TS");

						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("REDEEMED_OFFER_SYS_ID")),
								agreementItem_productOfferingSourceSystemId, "REDEEMED_OFFER_SYS_ID");
						
						GenericUtils.validateAssertEqualsFromDB(
								String.valueOf(rsAgreementItem.getInt("INCENTIVE_AMT")),
								agreementItem_incentiveAmount, "INCENTIVE_AMT");

						/*if (!currentIND.equals("N")) {
							while (rsRewardAcc.next()) {

								GenericUtils.validateAssertEqualsFromDB(
										String.valueOf(rsRewardAcc.getInt("CURRENCY_BAL_AMT")),
										"-" + agreementItem_incentiveAmount, "CURRENCY_BAL_AMT");

							}
						} else {

							GenericUtils.validateAssertEqualsFromDB(
									String.valueOf(rsAgreementItem.getInt("INCENTIVE_AMT")),
									agreementItem_incentiveAmount, "INCENTIVE_AMT");

						}*/

					}
					rsAgreementItem.close();

				}
			}

		}
	}

	public static void responseDBCheckEarlyRenewalPenalty(String jsonString, String subscriptionID, int itemNo)
			throws SQLException {

		Statement statementRewardAcc = null;
		statementRewardAcc = DBUtils.Conn.createStatement();
		ResultSet resultSetRewardAcc = null;

		String chargeTypeCd = null;
		String chargeCode = null;
		String adjustmentCode = null;
		int consequenceItemNo = 0;

		GetEarlyRenewalPenalty jsonPath = new GetEarlyRenewalPenalty();

		String href = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.href));

		String date = JSONUtils.checkValue(jsonString, jsonPath.date);

		String slaId = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.slaId));

		String violationRefValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationRefValue));

		String violationCommentLocale0 = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale0));

		String violationCommentLocale1 = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale1));

		GenericUtils.validateAssertNotNull(href, "HREF");

		GenericUtils.validateAssertEquals("EarlyRenewal", slaId, "SLAID");

		GenericUtils.validateAssertEqualsFromDB(JSONUtils.getGMTStartDate().split("T")[0], date, "DATE");

		for (int i = 0; i < itemNo; i++) {

			GetEarlyRenewalPenalty itemJsonPath = new GetEarlyRenewalPenalty(i);
			consequenceItemNo = i + 1;

			String consequenceType = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceType));

			String consequenceAdjAmtName = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceAdjAmtName));

			String consequenceAdjAmtValue = JSONUtils.checkValue(jsonString, jsonPath.consequenceAdjAmtValue);

			String consequenceBillingCode = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingCode));

			String consequenceBillingRevCode = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingRevCode));

			String consequenceLoyaltyType = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceLoyaltyType));
			String itemID = DBUtils.getItemType(consequenceLoyaltyType);

			String consequenceAmt = JSONUtils.checkValue(jsonString, jsonPath.consequenceAmt);

			String consequenceTypeCode = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceTypeCode));

			Reporting.logReporter(Status.INFO,
					"---Consequence Item No =" + consequenceItemNo + " : " + consequenceLoyaltyType);

			resultSetRewardAcc = statementRewardAcc
					.executeQuery("select * from reward_account where REWARD_PROGRAM_TYP_ID='" + itemID
							+ "' and BUSINESS_OBJECT_ID =" + subscriptionID);

			switch (consequenceLoyaltyType) {

			case "ACCESSORYFINANCE":
				chargeTypeCd = "AFHB";
				chargeCode = "AFER";
				adjustmentCode = "AFCR";
				break;

			case "BIB":
				chargeTypeCd = "BIBEPB";
				chargeCode = "BIBER";
				adjustmentCode = "BIBCR";
				break;

			case "HARDWARE":
				chargeTypeCd = "HWHB";
				chargeCode = "UPDBC";
				adjustmentCode = "UPDBCR";
				break;

			case "FINANCE":
				chargeTypeCd = "FINEPB";
				chargeCode = "UPDF";
				adjustmentCode = "UPDFCR";
				break;

			case "TAB":
				chargeTypeCd = "TABEPB";
				chargeCode = "TABREN";
				adjustmentCode = "ATABRE";
				break;

			case "HWS":
				chargeTypeCd = "HWSHB";
				chargeCode = "PHCREN";
				adjustmentCode = "APHCRE";
				break;
			}

			GenericUtils.validateAssertNotNull(violationRefValue, "VIOLATION_REF_VALUE");

			GenericUtils.validateAssertEquals("Charge", consequenceType, "TYPE");

			GenericUtils.validateAssertEquals("0", consequenceAdjAmtValue, "ADJUSTMENT_AMT");

			GenericUtils.validateAssertEquals(String.valueOf(chargeCode), consequenceBillingCode, "BILLING CODE");

			GenericUtils.validateAssertEquals(String.valueOf(adjustmentCode), consequenceBillingRevCode,
					"BILLING REVERSAL CODE");

			GenericUtils.validateAssertEquals(String.valueOf(chargeTypeCd), consequenceTypeCode, "TYPE CODE");

			while (resultSetRewardAcc.next()) {

				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
						"-" + consequenceAmt, "AMOUNT");

			}

			GenericUtils.validateAssertEquals("EN_CA", violationCommentLocale0, "COMMENT_0");

			GenericUtils.validateAssertEquals("FR_CA", violationCommentLocale1, "COMMENT_1");

		}

	}

	
	public static void validateActivationAfterRenewal(String jsonString, String subID, int itemNo) throws SQLException {

		Statement statement = null;
		statement = DBUtils.Conn.createStatement();
		ResultSet rsAgmt = null;

		Statement statement1 = null;
		statement1 = DBUtils.Conn.createStatement();
		ResultSet rsAgmtItem = null;

		rsAgmt = statement.executeQuery(
				"SELECT * FROM  CUSTOMER_SERVICE_AGREEMENT where REDEEMED_OFFER_TYPE_ID='1' and SUBSCRIPTION_ID ="
						+ subID);
		Reporting.logReporter(Status.INFO, "---Activation Record validation---");
		while (rsAgmt.next()) {
		agrmtId = String.valueOf(rsAgmt.getInt("CUSTOMER_SVC_AGREEMENT_ID"));

		GenericUtils.validateAssertEqualsFromDB(String.valueOf(rsAgmt.getString("CURRENT_IND")), "N", "CURRENT_IND");
		}
		
		for (int i = 0; i < itemNo; i++) {

			int agreementItemNo = i + 1;
			AgreementItem agrmtItem = new AgreementItem(i);

			Itemtype = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemType));
			String agreementItem_itemType = DBUtils.getItemType(Itemtype);

			String agreementItem_itemDurationStartDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_itemDurationStartDateTime);

			String agreementItem_itemDurationAmount = String.valueOf(
					JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, agrmtItem.agreementItem_itemDurationAmount));

			String agreementItem_itemDurationEndDateTime = JSONUtils.checkValue(jsonString,
					agrmtItem.agreementItem_itemDurationEndDateTime);
			agreementItem_itemDurationEndDateTime = JSONUtils.getEndDate(agreementItem_itemDurationEndDateTime,
					agreementItem_itemDurationStartDateTime, agreementItem_itemType, agreementItem_itemDurationAmount);


			if (agreementItem_itemType.equals("15") || agreementItem_itemType.equals("16")
					|| agreementItem_itemType.equals("17"))

			{
				agreementItem_itemDurationAmount = "0";
				agreementItem_itemDurationEndDateTime = agreementItem_itemDurationStartDateTime;
			}
			
			Reporting.logReporter(Status.INFO, "--- Agreement Item : " + agreementItemNo + " : " + Itemtype + "---");

		rsAgmtItem = statement
				.executeQuery("SELECT * FROM  CUST_SRVC_AGRMT_ITEM where CUSTOMER_SVC_AGREEMENT_ID=" + agrmtId + 
						" and REWARD_PROGRAM_TYP_ID='" + agreementItem_itemType + "'");

		while (rsAgmtItem.next()) {

			GenericUtils.validateAssertEqualsFromDB(
					String.valueOf(rsAgmtItem.getDate("COMMITMENT_EFF_START_DT")),
					agreementItem_itemDurationStartDateTime, "COMMITMENT_EFF_START_DT");

			GenericUtils.validateAssertEqualsFromDB(
					String.valueOf(rsAgmtItem.getDate("COMMITMENT_EFF_END_DT")),
					agreementItem_itemDurationEndDateTime, "COMMITMENT_EFF_END_DT");

			GenericUtils.validateAssertEqualsFromDB(
					String.valueOf(rsAgmtItem.getInt("ORIG_COMMITMENT_LENGTH_NUM")),
					agreementItem_itemDurationAmount, "ORIG_COMMITMENT_LENGTH_NUM");
			
			
			
		}
			
		}
	}

		public static void responseDBCheckTerminationPenalty(String jsonString, String subscriptionID, int itemNo, String paymentMech)
			throws SQLException {

		Statement statementRewardAcc = null;
		statementRewardAcc = DBUtils.Conn.createStatement();
		ResultSet resultSetRewardAcc = null;
		
		Statement statementRewardTxn = null;
		statementRewardTxn = DBUtils.Conn.createStatement();
		ResultSet resultSetRewardTxn = null;

		String typeCode = null;
		String billingCode = null;
		String billingRev = null;
		int consequenceItemNo = 0;

		GetEarlyRenewalPenalty jsonPath = new GetEarlyRenewalPenalty();

		String href = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.href));

		String date = JSONUtils.checkValue(jsonString, jsonPath.date);

		String slaId = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.slaId));

		String violationRefValue = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationRefValue));

		String violationCommentLocale0 = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale0));

		String violationCommentLocale1 = String
				.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale1));

		GenericUtils.validateAssertNotNull(href, "HREF");

		GenericUtils.validateAssertEquals("Termination", slaId, "SLAID");
		
		GenericUtils.validateAssertEqualsFromDB(JSONUtils.getGMTStartDate().split("T")[0], date, "DATE");

		for (int i = 0; i < itemNo; i++) {

			GetEarlyRenewalPenalty itemJsonPath = new GetEarlyRenewalPenalty(i);
			consequenceItemNo = i + 1;

			String consequenceType = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceType));

			String consequenceAdjAmtName = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceAdjAmtName));

			String consequenceAdjAmtValue = JSONUtils.checkValue(jsonString, jsonPath.consequenceAdjAmtValue);

			String consequenceBillingCode = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingCode));

			String consequenceBillingRevCode = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingRevCode));

			String consequenceLoyaltyType = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceLoyaltyType));
			String itemID = DBUtils.getItemType(consequenceLoyaltyType);

			String consequenceAmt = JSONUtils.checkValue(jsonString, jsonPath.consequenceAmt);

			String consequenceTypeCode = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceTypeCode));

			
			if(consequenceType.equals("CreditAdjustment"))
			{
				Reporting.logReporter(Status.INFO,
						"---Consequence Item No =" + consequenceItemNo + " : " + consequenceTypeCode);
				switch (consequenceTypeCode) {

				case "ACTIVATIONBILLCREDIT":
					billingCode = "ACTCR";
					billingRev = "ACTCH";
					typeCode = "ACTIVATIONBILLCREDIT";
					break;
					
				case "RENEWALBILLCREDIT":
					billingCode = "RCCC";
					billingRev = "RCAC";
					typeCode = "RENEWALBILLCREDIT";
					break;
				}
				
				GenericUtils.validateAssertEquals("CreditAdjustment", consequenceType, "TYPE");
				
				GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

				GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
						"BILLING REVERSAL CODE");

				GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");
				
			}
			else
			{	
				Reporting.logReporter(Status.INFO,
						"---Consequence Item No =" + consequenceItemNo + " : " + consequenceLoyaltyType);

			resultSetRewardAcc = statementRewardAcc
					.executeQuery("select * from reward_account rewAcc inner join reward_txn rewTxn "
							+ " on rewAcc.REWARD_ACCOUNT_ID = rewTxn.REWARD_ACCOUNT_ID "
							+ "And rewAcc.REWARD_PROGRAM_TYP_ID='" + itemID
							+ "' and rewAcc.BUSINESS_OBJECT_ID =" + subscriptionID
							+ " and rewTxn.REWARD_TXN_RSN_ID='1'");
			switch (consequenceLoyaltyType) {

			case "ACCESSORYFINANCE":
				billingCode = "AFTC";
				billingRev = "AFCR";
				typeCode = "AFDB";//"AFECF"
				break;
				
			case "BIB" :
			billingCode = "BIBTC";
			billingRev = "BIBCR";
			typeCode = "BIBDB";
			if (paymentMech.equals("BIB_TELUS_PENDING") || paymentMech.equals("TRADE_IN_PENDING"))
			{
				billingCode = "BIBPAY";
				typeCode="BIBPDB";
			}
			break;
			
			case "HARDWARE":
				billingCode = "UPDBC";
				billingRev = "UPDBCR";
				typeCode = "HWHB";
				break;

			case "FINANCE":
				billingCode = "UPDF";
				billingRev = "UPDFCR";
				typeCode = "FINEPB";
				break;

			case "TAB":
				billingCode = "TABBCA";
				billingRev = "ATABCA";
				typeCode = "TABDB";
				break;

			case "HWS":
				billingCode = "PHCCAN";
				billingRev = "APHCCA";
				typeCode = "HWSDB";
				break;
				
			case "ACTIVATIONBILLCREDIT":
				billingCode = "BCLBC";
				billingRev = "BCLBR";
				typeCode = "ACBDB";
				break;
				
			case "RENEWALBILLCREDIT":
				billingCode = "RCBD";
				billingRev = "RCBC";
				typeCode = "RCBDB";
				break;
			}
			if(consequenceTypeCode.contains("ECF"))
			{
				typeCode="AFECF";
				
			}
			GenericUtils.validateAssertNotNull(violationRefValue, "VIOLATION_REF_VALUE");

			GenericUtils.validateAssertEquals("Charge", consequenceType, "TYPE");

			GenericUtils.validateAssertEquals("0", consequenceAdjAmtValue, "ADJUSTMENT_AMT");

			GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

			GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
					"BILLING REVERSAL CODE");

			GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");

			
			while (resultSetRewardAcc.next()) {

				if(consequenceAmt.equals("0"))
				{
				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
						consequenceAmt, "AMOUNT");
				}
				else if(consequenceTypeCode.contains("ECF"))
				{
				GenericUtils.validateAssertEquals("100", consequenceAmt, "AMOUNT");
				}
				else if (paymentMech.equals("BIB_TELUS_PENDING") || paymentMech.equals("TRADE_IN_PENDING"))
				{
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_AMT")),
							"-"+consequenceAmt, "AMOUNT");	
					
				}
				else {
				GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
						"-"+consequenceAmt, "AMOUNT");	
				}
			}

			GenericUtils.validateAssertEquals("EN_CA", violationCommentLocale0, "COMMENT_0");

			GenericUtils.validateAssertEquals("FR_CA", violationCommentLocale1, "COMMENT_1");

			}
		}

	}

		public static void responseDBCheckMigrationPenalty(String jsonString, String subscriptionID, int itemNo, String paymentMech)
				throws SQLException {

			Statement statementRewardAcc = null;
			statementRewardAcc = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardAcc = null;
			
			Statement statementRewardTxn = null;
			statementRewardTxn = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardTxn = null;

			String payMechId = null;
			String typeCode = null;
			String billingCode = null;
			String billingRev = null;
			int consequenceItemNo = 0;
			
			switch(paymentMech)
			{
			case"NA":
				payMechId="1";
				break;
				
			case"BILL":
				payMechId="101";
				break;
				
			case"BIB_TELUS_PENDING":
			case"TRADE_IN_PENDING":
				payMechId="217";
				break;
				
			}

			GetEarlyRenewalPenalty jsonPath = new GetEarlyRenewalPenalty();

			String href = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.href));

			String date = JSONUtils.checkValue(jsonString, jsonPath.date);

			String slaId = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.slaId));

			String violationRefValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationRefValue));

			String violationCommentLocale0 = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale0));

			String violationCommentLocale1 = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale1));

			GenericUtils.validateAssertNotNull(href, "HREF");

			GenericUtils.validateAssertEquals("Migration", slaId, "SLAID");
			
			GenericUtils.validateAssertEqualsFromDB(JSONUtils.getGMTStartDate().split("T")[0], date, "DATE");

			for (int i = 0; i < itemNo; i++) {

				GetEarlyRenewalPenalty itemJsonPath = new GetEarlyRenewalPenalty(i);
				consequenceItemNo = i + 1;

				String consequenceType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceType));

				String consequenceAdjAmtName = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceAdjAmtName));

				String consequenceAdjAmtValue = JSONUtils.checkValue(jsonString, jsonPath.consequenceAdjAmtValue);

				String consequenceBillingCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingCode));

				String consequenceBillingRevCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingRevCode));

				String consequenceLoyaltyType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceLoyaltyType));
				String itemID = DBUtils.getItemType(consequenceLoyaltyType);

				String consequenceAmt = JSONUtils.checkValue(jsonString, jsonPath.consequenceAmt);

				String consequenceTypeCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceTypeCode));

				
				if(consequenceType.equals("CreditAdjustment"))
				{
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceTypeCode);
					switch (consequenceTypeCode) {

					case "ACTIVATIONBILLCREDIT":
						billingCode = "ACTCR";
						billingRev = "ACTCH";
						typeCode = "ACTIVATIONBILLCREDIT";
						break;
						
					case "RENEWALBILLCREDIT":
						billingCode = "RCCC";
						billingRev = "RCAC";
						typeCode = "RENEWALBILLCREDIT";
						break;
					}
					
					GenericUtils.validateAssertEquals("CreditAdjustment", consequenceType, "TYPE");
					
					GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

					GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
							"BILLING REVERSAL CODE");

					GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");
					
				}
				else
				{	
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceLoyaltyType);

				resultSetRewardAcc = statementRewardAcc
						.executeQuery("select * from reward_account rewAcc inner join reward_txn rewTxn "
								+ " on rewAcc.REWARD_ACCOUNT_ID = rewTxn.REWARD_ACCOUNT_ID "
								+ "And rewAcc.REWARD_PROGRAM_TYP_ID='" + itemID
								+ "' and rewAcc.BUSINESS_OBJECT_ID =" + subscriptionID
								+ " and rewTxn.REWARD_TXN_RSN_ID="+payMechId);
				switch (consequenceLoyaltyType) {

				case "ACCESSORYFINANCE":
					billingCode = "AFTC";
					billingRev = "AFCR";
					typeCode = "AFDB";//"AFECF"
					break;
					
				case "BIB" :
				billingCode = "BIBTC";
				billingRev = "BIBCR";
				typeCode = "BIBDB";
				if (paymentMech.equals("BIB_TELUS_PENDING") || paymentMech.equals("TRADE_IN_PENDING"))
				{
					billingCode = "BIBPAY";
					typeCode="BIBPDB";
				}
				break;
				
				case "HARDWARE":
					billingCode = "DBLBC";
					billingRev = "DBLBR";
					typeCode = "HWDB";
					break;

				case "FINANCE":
					billingCode = "DBMF";
					billingRev = "DBMFCR";
					typeCode = "FINDB";
					break;

				case "TAB":
					billingCode = "TABBCA";
					billingRev = "ATABCA";
					typeCode = "TABDB";
					break;

				case "HWS":
					billingCode = "PHCCAN";
					billingRev = "APHCCA";
					typeCode = "HWSDB";
					break;
					
				case "ACTIVATIONBILLCREDIT":
					billingCode = "BCLBC";
					billingRev = "BCLBR";
					typeCode = "ACBDB";
					break;
					
				/*case "ACTIVATIONBILLCREDIT":
					billingCode = "CBFC";
					billingRev = "CBFR";
					typeCode = "ACBECF";
					break;*/
					
				case "RENEWALBILLCREDIT":
					billingCode = "RCBD";
					billingRev = "RCBC";
					typeCode = "RCBDB";
					break;
				}
				if(consequenceTypeCode.contains("ECF"))
				{
					typeCode="AFECF";
					
				}
				GenericUtils.validateAssertNotNull(violationRefValue, "VIOLATION_REF_VALUE");

				GenericUtils.validateAssertEquals("Charge", consequenceType, "TYPE");

				GenericUtils.validateAssertEquals("0", consequenceAdjAmtValue, "ADJUSTMENT_AMT");

				GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

				GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
						"BILLING REVERSAL CODE");

				GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");

				
				while (resultSetRewardAcc.next()) {

					if(consequenceAmt.equals("0"))
					{
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
							consequenceAmt, "AMOUNT");
					}
					else if(consequenceTypeCode.contains("ECF"))
					{
					GenericUtils.validateAssertEquals("100", consequenceAmt, "AMOUNT");
					}
					else if ( paymentMech.equals("BILL") || paymentMech.equals("NA"))
					{
						GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
								"-"+consequenceAmt, "AMOUNT");	
						
					}
					else if (paymentMech.equals("TRADE_IN_PENDING") ||paymentMech.equals("BIB_TELUS_PENDING"))
					{
						GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_AMT")),
								consequenceAmt, "AMOUNT");	
						
					}
					else {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
							"-"+consequenceAmt, "AMOUNT");	
					}
				}

				GenericUtils.validateAssertEquals("EN_CA", violationCommentLocale0, "COMMENT_0");

				GenericUtils.validateAssertEquals("FR_CA", violationCommentLocale1, "COMMENT_1");

				}
			}

		}


		public static void responseDBCheckReturnAdjustment(String jsonString, String subscriptionID, int itemNo, String paymentMech)
				throws SQLException {

			Statement statementRewardAcc = null;
			statementRewardAcc = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardAcc = null;
			
			Statement statementRewardTxn = null;
			statementRewardTxn = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardTxn = null;

			String typeCode = null;
			String billingCode = null;
			String billingRev = null;
			int consequenceItemNo = 0;

			GetEarlyRenewalPenalty jsonPath = new GetEarlyRenewalPenalty();

			String href = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.href));

			String date = JSONUtils.checkValue(jsonString, jsonPath.date);

			String slaId = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.slaId));

			String violationRefValue = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationRefValue));

			String violationCommentLocale0 = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale0));

			String violationCommentLocale1 = String
					.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.violationCommentLocale1));

			GenericUtils.validateAssertNotNull(href, "HREF");

			GenericUtils.validateAssertEquals("ReturnAdjustment", slaId, "SLAID");
			
			GenericUtils.validateAssertEqualsFromDB(JSONUtils.getGMTStartDate().split("T")[0], date, "DATE");

			for (int i = 0; i < itemNo; i++) {

				GetEarlyRenewalPenalty itemJsonPath = new GetEarlyRenewalPenalty(i);
				consequenceItemNo = i + 1;

				String consequenceType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceType));

				String consequenceAdjAmtName = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceAdjAmtName));

				String consequenceAdjAmtValue = JSONUtils.checkValue(jsonString, jsonPath.consequenceAdjAmtValue);

				String consequenceBillingCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingCode));

				String consequenceBillingRevCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingRevCode));

				String consequenceLoyaltyType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceLoyaltyType));
				String itemID = DBUtils.getItemType(consequenceLoyaltyType);

				String consequenceAmt = JSONUtils.checkValue(jsonString, jsonPath.consequenceAmt);

				String consequenceTypeCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceTypeCode));

				
				if(consequenceType.equals("CreditAdjustment"))
				{
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceTypeCode);
					switch (consequenceTypeCode) {

					case "ACTIVATIONBILLCREDIT":
						billingCode = "ACTCR";
						billingRev = "ACTCH";
						typeCode = "ACTIVATIONBILLCREDIT";
						break;
						
					case "RENEWALBILLCREDIT":
						billingCode = "RCCC";
						billingRev = "RCAC";
						typeCode = "RENEWALBILLCREDIT";
						break;
					}
					
					GenericUtils.validateAssertEquals("CreditAdjustment", consequenceType, "TYPE");
					
					GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

					GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
							"BILLING REVERSAL CODE");

					GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");
					
				}
				else
				{	
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceLoyaltyType);

				resultSetRewardAcc = statementRewardAcc
						.executeQuery("select * from reward_account rewAcc inner join reward_txn rewTxn "
								+ " on rewAcc.REWARD_ACCOUNT_ID = rewTxn.REWARD_ACCOUNT_ID "
//								+ "And rewAcc.REWARD_PROGRAM_TYP_ID='" + itemID
								+ "' and rewAcc.BUSINESS_OBJECT_ID =" + subscriptionID
								+ " and rewTxn.REWARD_TXN_RSN_ID=''");
				switch (consequenceLoyaltyType) {

				case "ACCESSORYFINANCE":
					billingCode = "AFTC";
					billingRev = "AFCR";
					typeCode = "AFDB";//"AFECF"
					break;
					
				case "BIB" :
				billingCode = "BIBTC";
				billingRev = "BIBCR";
				typeCode = "BIBDB";
				if (paymentMech.equals("BIB_TELUS_PENDING") || paymentMech.equals("TRADE_IN_PENDING"))
				{
					billingCode = "BIBPAY";
					typeCode="BIBPDB";
				}
				break;
				
				case "HARDWARE":
					billingCode = "DBLBC";
					billingRev = "DBLBR";
					typeCode = "HWDB";
					break;

				case "FINANCE":
					billingCode = "UPDF";
					billingRev = "UPDFCR";
					typeCode = "FINEPB";
					break;

				case "TAB":
					billingCode = "TABBCA";
					billingRev = "ATABCA";
					typeCode = "TABDB";
					break;

				case "HWS":
					billingCode = "PHCCAN";
					billingRev = "APHCCA";
					typeCode = "HWSDB";
					break;
					
				case "ACTIVATIONBILLCREDIT":
					billingCode = "BCLBC";
					billingRev = "BCLBR";
					typeCode = "ACBDB";
					break;
					
				/*case "ACTIVATIONBILLCREDIT":
					billingCode = "CBFC";
					billingRev = "CBFR";
					typeCode = "ACBECF";
					break;*/
					
				case "RENEWALBILLCREDIT":
					billingCode = "RCBD";
					billingRev = "RCBC";
					typeCode = "RCBDB";
					break;
				}
				if(consequenceTypeCode.contains("ECF"))
				{
					typeCode="AFECF";
					
				}
				GenericUtils.validateAssertNotNull(violationRefValue, "VIOLATION_REF_VALUE");

				GenericUtils.validateAssertEquals("Charge", consequenceType, "TYPE");

				GenericUtils.validateAssertEquals("0", consequenceAdjAmtValue, "ADJUSTMENT_AMT");

				GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

				GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
						"BILLING REVERSAL CODE");

				GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");

				
				while (resultSetRewardAcc.next()) {

					if(consequenceAmt.equals("0"))
					{
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
							consequenceAmt, "AMOUNT");
					}
					else if(consequenceTypeCode.contains("ECF"))
					{
					GenericUtils.validateAssertEquals("100", consequenceAmt, "AMOUNT");
					}
					else if (paymentMech.equals("BIB_TELUS_PENDING") || paymentMech.equals("TRADE_IN_PENDING") || paymentMech.equals("BILL"))
					{
						GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_AMT")),
								"-"+consequenceAmt, "AMOUNT");	
						
					}
					else {
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
							"-"+consequenceAmt, "AMOUNT");	
					}
				}

				GenericUtils.validateAssertEquals("EN_CA", violationCommentLocale0, "COMMENT_0");

				GenericUtils.validateAssertEquals("FR_CA", violationCommentLocale1, "COMMENT_1");

				}
			}

		}

			public static void responseDBCheckResumePenalty(String jsonString, String subscriptionID, int itemNo) throws SQLException {

			Statement statementRewardAcc = null;
			statementRewardAcc = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardAcc = null;
			
			Statement statementRewardTxn = null;
			statementRewardTxn = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardTxn = null;

			String typeCode = null;
			String billingCode = null;
			String billingRev = null;
			int consequenceItemNo = 0;

			GetEarlyRenewalPenalty jsonPath = new GetEarlyRenewalPenalty();

			String href = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.href));

			String date = JSONUtils.checkValue(jsonString, jsonPath.date);

			String slaId = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.slaId));

			GenericUtils.validateAssertNotNull(href, "HREF");

			GenericUtils.validateAssertEquals("ResumeAdjustment", slaId, "SLAID");
			
			GenericUtils.validateAssertEqualsFromDB(JSONUtils.getGMTStartDate().split("T")[0], date, "DATE");

			for (int i = 0; i < itemNo; i++) {

				GetEarlyRenewalPenalty itemJsonPath = new GetEarlyRenewalPenalty(i);
				consequenceItemNo = i + 1;

				String consequenceType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceType));

				String consequenceBillingCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingCode));

				String consequenceBillingRevCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingRevCode));

				String consequenceTypeCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceTypeCode));

		
				{	
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceTypeCode);

			
				switch (consequenceTypeCode) {
				
				case "HWDB":
					billingCode = "DBLBC";
					billingRev = "DBLBR";
					typeCode = "HWDB";
					break;
					
				case "BIBDB" :
					billingCode = "BIBTC";
					billingRev = "BIBCR";
					typeCode = "BIBDB";
				break;

				case "FINDB":
					billingCode = "DBMF";
					billingRev = "DBMFCR";
					typeCode = "FINDB";
					break;

				case "TABDB":
					billingCode = "TABBCA";
					billingRev = "ATABCA";
					typeCode = "TABDB";
					break;

				case "HWSDB":
					billingCode = "PHCCAN";
					billingRev = "APHCCA";
					typeCode = "HWSDB";
					break;
					
				case "ACBDB":
					billingCode = "BCLBC";
					billingRev = "BCLBR";
					typeCode = "ACBDB";
					break;
							
				case "RCBDB":
					billingCode = "RCBD";
					billingRev = "RCBC";
					typeCode = "RCBDB";
					break;
				}
				if(consequenceTypeCode.contains("ECF"))
				{
					billingCode = "CBFC";
					billingRev = "CBFR";
					typeCode="ECF";
					
				}
				
				GenericUtils.validateAssertEquals("ChargeAdjustment", consequenceType, "TYPE");

				GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

				GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
						"BILLING REVERSAL CODE");

				GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");

																																																																																																		
				}
			}

			
		}

		public static void responseDBCheckChangeServPenalty(String jsonStringActv, String jsonString, String subscriptionID, int itemNo) throws SQLException {
			
			Statement statementRewardAcc = null;
			statementRewardAcc = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardAcc = null;
			
			Statement statementRewardTxn = null;
			statementRewardTxn = DBUtils.Conn.createStatement();
			ResultSet resultSetRewardTxn = null;

			String typeCode = null;
			String billingCode = null;
			String billingRev = null;
			int consequenceItemNo = 0;
			int subscriptionsNo=0;
			

			GetEarlyRenewalPenalty jsonPath = new GetEarlyRenewalPenalty();

			String href = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.href));

			String date = JSONUtils.checkValue(jsonString, jsonPath.date);

			String slaId = String.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.slaId));


			GenericUtils.validateAssertNotNull(href, "HREF");

			GenericUtils.validateAssertEquals("ServiceChange", slaId, "SLAID");
			
			GenericUtils.validateAssertEqualsFromDB(JSONUtils.getGMTStartDate().split("T")[0], date, "DATE");
			
			
			for (int i = 0; i < itemNo; i++) {

				GetEarlyRenewalPenalty itemJsonPath = new GetEarlyRenewalPenalty(i);
				consequenceItemNo = i + 1;

				String consequenceType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceType));

				String consequenceAdjAmtName = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceAdjAmtName));

				String consequenceAdjAmtValue = JSONUtils.checkValue(jsonString, jsonPath.consequenceAdjAmtValue);

				String consequenceBillingCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingCode));

				String consequenceBillingRevCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceBillingRevCode));

				String consequenceLoyaltyType = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceLoyaltyType));
				String itemID = DBUtils.getItemType(consequenceLoyaltyType);

				String consequenceAmt = JSONUtils.checkValue(jsonString, jsonPath.consequenceAmt);

				String consequenceTypeCode = String
						.valueOf(JSONUtils.getJSONKeyValueUsingJsonPath(jsonString, jsonPath.consequenceTypeCode));

				
				if(consequenceType.equals("CreditAdjustment"))
				{
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceTypeCode);
					switch (consequenceTypeCode) {

					case "ACTIVATIONBILLCREDIT":
						billingCode = "ACTCR";
						billingRev = "ACTCH";
						typeCode = "ACTIVATIONBILLCREDIT";
						break;
						
					case "RENEWALBILLCREDIT":
						billingCode = "RCCC";
						billingRev = "RCAC";
						typeCode = "RENEWALBILLCREDIT";
						break;
					}
					
					GenericUtils.validateAssertEquals("CreditAdjustment", consequenceType, "TYPE");
					
					GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

					GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
							"BILLING REVERSAL CODE");

					GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");
					
				}
				else
				{	
					Reporting.logReporter(Status.INFO,
							"---Consequence Item No =" + consequenceItemNo + " : " + consequenceLoyaltyType);

				resultSetRewardAcc = statementRewardAcc
						.executeQuery("select * from reward_account rewAcc inner join reward_txn rewTxn "
								+ " on rewAcc.REWARD_ACCOUNT_ID = rewTxn.REWARD_ACCOUNT_ID "
								+ "And rewAcc.REWARD_PROGRAM_TYP_ID='" + itemID
								+ "' and rewAcc.BUSINESS_OBJECT_ID =" + subscriptionID);
				
				switch (consequenceLoyaltyType) {

				case "ACCESSORYFINANCE":
					billingCode = "AFTC";
					billingRev = "AFCR";
					typeCode = "AFDB";//"AFECF"
					break;
					
				case "BIB" :
				billingCode = "BIBTC";
				billingRev = "BIBCR";
				typeCode = "BIBDB";
				break;
				
				case "HARDWARE":
					billingCode = "DBLBC";
					billingRev = "DBLBR";
					typeCode = "HWDB";
					break;

				case "FINANCE":
					billingCode = "DBMF";
					billingRev = "DBMFCR";
					typeCode = "FINDB";
					break;

				case "TAB":
					billingCode = "TABMSC";
					billingRev = "ATABMS";
					typeCode = "TABMSC";
					break;

				case "HWS":
					billingCode = "PHCMSC";
					billingRev = "APHCMS";
					typeCode = "HWSMSC";
					break;
					
				case "ACTIVATIONBILLCREDIT":
					billingCode = "BCLBC";
					billingRev = "BCLBR";
					typeCode = "ACBDB";
					break;
					
				/*case "ACTIVATIONBILLCREDIT":
					billingCode = "CBFC";
					billingRev = "CBFR";
					typeCode = "ACBECF";
					break;*/
					
				case "RENEWALBILLCREDIT":
					billingCode = "RCBD";
					billingRev = "RCBC";
					typeCode = "RCBDB";
					break;
				}
				if(consequenceTypeCode.contains("ECF"))
				{
					typeCode="AFECF";
					
				}
				GenericUtils.validateAssertEquals("Charge", consequenceType, "TYPE");

				GenericUtils.validateAssertEquals("0", consequenceAdjAmtValue, "ADJUSTMENT_AMT");

				GenericUtils.validateAssertEquals(String.valueOf(billingCode), consequenceBillingCode, "BILLING CODE");

				GenericUtils.validateAssertEquals(String.valueOf(billingRev), consequenceBillingRevCode,
						"BILLING REVERSAL CODE");

				GenericUtils.validateAssertEquals(String.valueOf(typeCode), consequenceTypeCode, "TYPE CODE");

				
				while (resultSetRewardAcc.next()) {

					if(consequenceAmt.equals("0"))
					{
					GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
							consequenceAmt, "AMOUNT");
					}	
					else
					{
						GenericUtils.validateAssertEqualsFromDB(String.valueOf(resultSetRewardAcc.getInt("CURRENCY_BAL_AMT")),
								"-"+consequenceAmt, "AMOUNT");	
						
					}
				}
				}
			}
		
			
		}





}
