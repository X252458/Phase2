package com.telus.rcms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.aventstack.extentreports.Status;
import com.test.reporting.Reporting;
import com.test.utils.DatesUtils;
import com.test.utils.SystemProperties;

import com.aventstack.extentreports.Status;
import com.telus.api.test.utils.APIJava;
import com.telus.rcms.jsonPathLibrary.All;
import com.test.reporting.Reporting;
import com.test.ui.actions.WebDriverSteps;

public class DBUtils {

	static String connectionString = null;
	public static Connection Conn = null;
	static Statement Stmt = null;
	static ResultSet Resultset = null;
	static ResultSet Resultset2 = null;

	public static void dbConnect() throws SQLException {

		System.out.println("-------- DB Connect  ------");

		String environment = SystemProperties.EXECUTION_ENVIRONMENT;

		String connectionStringPT148 = "jdbc:oracle:thin:@//RWRDPT-SCAN.CORP.ADS:41521/RWRDPTSV2.WORLD";
		String connectionStringPT168 = "jdbc:oracle:thin:@//RWRDPV-SCAN.CORP.ADS:41521/RWRDPV.WORLD";
		String connectionStringPT140 = "jdbc:oracle:thin:@//RWRDPV-SCAN.CORP.ADS:41521/RWRDPSSV1.WORLD";

		if (environment.equals("PT148")) {
			connectionString = connectionStringPT148;
		} else if (environment.equals("PT168")) {
			connectionString = connectionStringPT168;
		} else if (environment.equals("PT140")) {
			connectionString = connectionStringPT140;
		}
		try {
			// Returns the Class object associated with the class
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException exception) {
			System.out.println("Oracle Driver Class Not found Exception: " + exception.toString());
		}

		// Set connection timeout. Make sure you set this correctly as per your need
		DriverManager.setLoginTimeout(5);
		
		

		try {
			// Attempts to establish a connection
			Conn = DriverManager.getConnection(connectionString, "X241410", "X241410");
			Reporting.logReporter(Status.INFO, "------------DB connected successfully------------");
			System.out.println("Oracle JDBC Driver Successfully Registered! Let's make connection now");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void dbDisConnect() throws SQLException {

		try {
			if (!Conn.isClosed()) {
				Resultset = null;
				Conn.close();
				Conn = null;
			}
		} catch (SQLException e) {
			Reporting.logReporter(Status.INFO, "Exception during DB Disconnect");

		}
		Reporting.logReporter(Status.INFO, "DB Disconnected successfully");
	}

	/*
	 * To verify whether particular AccountID is available in DB or not.
	 */

	public static String getProductOfferID(String offerID) {

		String offerValue = null;

		switch (offerID) {
		case "AOM":
			offerValue = "1";
			break;
			
		case "OOM":
			offerValue = "2";
			break;
			
		case "PURCHASE":
			offerValue = "2";
			break;
		}
		return offerValue;
	}
	
	public static String getNullEndDate(String endDate,String startDate) {

		if(endDate==null)
			endDate=startDate;
		else if(endDate!=null)
		endDate=endDate;
		
		return endDate;
	}
		
	public static String getNullCode(String code) {

		if(code.equals("null")||code.equals("NA"))
			code="0";
		else if(code!=null)
			code=code;

		return code;
	}
	
	
	public static String convertYN(String value) {

		String convertedValue = null;
//Yet to check on this		
		if(value.equals("null") || value.equals("NA") || value.equalsIgnoreCase("false") )
			convertedValue="N";
		else if(value.equalsIgnoreCase("true")) 
			convertedValue="Y"; 
		else
			convertedValue=value;
		
		return convertedValue;
	}

	public static String getPaymentMech(String payment) {

		String paymentMechId = null;
		
		switch (payment) {
		
		case "BILL":
			paymentMechId = "1";
			break;
			
		case "CREDITCARD":
			paymentMechId = "2";
			break;
			
		case "TRADE_IN":
			paymentMechId = "5";
			break;

		case "BIB_TELUS_PENDING":
			paymentMechId = "6";
			break;
		
		case "TRADE_IN_PENDING":
			paymentMechId = "7";
			break;
		}
		
		return paymentMechId;
	}

	public static String getItemName(String ItemType) throws SQLException {

		String ItemTypeID = null;

		switch (ItemType) {
		case "HWDB":
			ItemTypeID = "1";
			break;
			
		case "BIBDB":
			ItemTypeID = "9";
			break;
			
		case "FINDB":
			ItemTypeID = "7";
			break;

		case "ACBDB":
			ItemTypeID = "4";
			break;

		}
		return ItemTypeID;
	}
	public static String getItemType(String ItemType) throws SQLException {

		String ItemTypeID = null;

		switch (ItemType) {
		case "HARDWARE":
			ItemTypeID = "1";
			break;
			
		case "ACTIVATIONBILLCREDIT":
			ItemTypeID = "4";
			break;

		case "FINANCE":
			ItemTypeID = "7";
			break;

		case "BIB":
			ItemTypeID = "9";
			break;

		case "PRECREDIT":
			ItemTypeID = "11";
			break;

		case "RENEWALBILLCREDIT":
			ItemTypeID = "12";
			break;
			
		case "TAB":
			ItemTypeID = "13";
			break;

		case "HWS":
			ItemTypeID = "14";
			break;

		case "TIASSETCREDIT":
			ItemTypeID = "15";
			break;

		case "TIPROMOCREDIT":
			ItemTypeID = "16";
			break;
			
	case "ACCESSORYFINANCE":
			ItemTypeID = "17";
			break;
		
		case "PRESOC":
			ItemTypeID="10";
			
		}
		return ItemTypeID;
	}

	public static Boolean DBAccountIDAvailability(String accountID) throws SQLException {
		Boolean DBAccIDAvailability = true;
		String DBaccID = null;
		callDBConnect();
		Stmt = Conn.createStatement();
		Resultset = Stmt.executeQuery(
				"SELECT DISTINCT BAN FROM CUSTOMER_SERVICE_AGREEMENT " + "WHERE BAN ='" + accountID + "'");
		while (Resultset.next()) {
		DBaccID = String.valueOf(Resultset.getInt("BAN"));
		}
		if(DBaccID==null)
		{
		DBAccIDAvailability = false;
		}
		return DBAccIDAvailability;
	}

	/*
	 * To verify whether particular SubscriptionID is available in DB or not.
	 */
	public static Boolean DBSubscriptionIDAvailability(String subscriptionID) throws SQLException {
		
		Boolean DBSubIDAvailability = true;
		String DBSubscriptionID = null;
		callDBConnect();
		Stmt = Conn.createStatement();
		Resultset = Stmt.executeQuery("SELECT DISTINCT SUBSCRIPTION_ID FROM CUSTOMER_SERVICE_AGREEMENT "
				+ "WHERE SUBSCRIPTION_ID ='" + subscriptionID + "'");
		while (Resultset.next()) {
		DBSubscriptionID = String.valueOf(Resultset.getInt("SUBSCRIPTION_ID"));
		}
		if(DBSubscriptionID==null)
		{
			DBSubIDAvailability = false;
		}
		return DBSubIDAvailability;
	}

	/*
	 * To verify whether particular SubscriptionNumber is available in DB or not.
	 */
	public static Boolean DBSubscriptionNumberAvailability(String subscriberNum) throws SQLException {
		Boolean DBSubNumAvailability = true;
		String DBsubscriberNumber = null;
		callDBConnect();
		Stmt = Conn.createStatement();
		Resultset = Stmt.executeQuery("SELECT DISTINCT subscriber_no FROM CUSTOMER_SERVICE_AGREEMENT "
				+ "WHERE SUBSCRIBER_NO ='" + subscriberNum + "'");
		while (Resultset.next()) {
		DBsubscriberNumber = String.valueOf(Resultset.getInt("SUBSCRIBER_NO"));
		}
		if(DBsubscriberNumber==null)
		{
			DBSubNumAvailability = false;
		}
		return DBSubNumAvailability;
	}

	/*
	 * To Convert Result Set to HashMAP
	 */
	public static Boolean convertResultSetToHashMap() throws SQLException {
		Boolean stat = true;
		List<Map<String, ?>> results = new ArrayList<Map<String, ?>>();

		callDBConnect();
		Stmt = Conn.createStatement();
		Resultset = Stmt.executeQuery("SELECT * FROM CUSTOMER_SERVICE_AGREEMENT where subscriber_no ='8499117748'");

		if (Resultset.next()) {
			Reporting.logReporter(Status.INFO, "");
		}

		try {

			ResultSetMetaData md = Resultset.getMetaData();
			int columns = md.getColumnCount();

			System.out.println(Resultset.getFetchSize());
			while (Resultset.next()) {

				for (int i = 1; i <= columns; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = Resultset.getString(i);
					System.out.print(columnValue + " " + md.getColumnName(i));
				}

			}

		} catch (Exception e) {
			stat = false;

		}

		for (int i = 0; i < results.size(); i++) {
			System.out.print(" " + results.get(i).toString());
		}

		DBUtils.dbDisConnect();
		return stat;
	}

	public static void callDBConnect() throws SQLException {

		try {
			if (Conn.isClosed()) {
				dbConnect();
			}
		} catch (Exception e) {
			dbConnect();

		}

	}

	public static void callDBDisconnect() throws SQLException {

		if (!Conn.isClosed()) {
			Conn.close();
		}
	}

}
