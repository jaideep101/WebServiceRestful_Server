package com.service.webservice.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.axis.Handler;

import com.service.webservice.modal.OptionChainItemModal;
import com.service.webservice.modal.OptionChainModal;
import com.service.webservice.parser.ParserOptionChain;
import com.service.webservice.service.ServiceOptionChain;
import com.service.webservice.utilities.StockMarketConstants;
import com.service.webservice.utilities.Utils;

public class MysqlCon {
	public static int id = 0;
	public static String name = null;
	public static String place = null;
	public static String age = null;
	public static boolean isSchedulerStop = false;
	public static Timer timer = new Timer();
	public static void main(String args[]) {
		 // Instantiate Timer Object
		
		System.out.println("############### run timer");
		try {
			String responseData = ServiceOptionChain.sendGET(StockMarketConstants.NIFTY50_URL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("################# optionChainList : "+ParserOptionChain.optionChainList.size());
//		insertDataInOptionChain();
		
//		SchedularTask st = new SchedularTask(isSchedulerStop, timer); // Instantiate SheduledTask class
//		timer.schedule(st, 1000, 10*1000); // Create Repetitively task for every 1 secs
//		stopSchedular();
	}
	
	public static void stopSchedular() {
		class Demo extends TimerTask {
		      public void run() {
		            System.out.println("Hello World");
		            SchedularTask.mTimer = timer;
		            SchedularTask.isSchedularStop = true;
		            
		            timer.cancel();
		       }
		  }
		timer.schedule(new Demo(), 60*1000);
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + "stockschema"
								+ "?useSSL=false&autoReconnect=true", "root", "Johny@1986");
		} catch (Exception e) {
			System.out.println("###### : "+e);
		}
		return connection;
	}

	public static ResultSet getResultSet(String query, String schema) {
		Connection connection = getConnection();
		ResultSet resultSet = null;
		try {
			if (connection != null) {
				Statement stmt = connection.createStatement();
				resultSet = stmt.executeQuery("select * from stockmaster");
				while (resultSet.next()) {
					id = resultSet.getInt(1);
					System.out.println("ID : " + id);
					name = resultSet.getString(2);
					System.out.println("Name : " + name);
					place = resultSet.getString(3);
					System.out.println("place : " + place);
					age = resultSet.getString(4);
					System.out.println("age : " + age);
				}
				connection.close();
			}
			resultSet.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return resultSet;
	}
	public static boolean insertDataInOptionChain() {
		boolean flag = false;
		if(ParserOptionChain.optionChainList != null && !ParserOptionChain.optionChainList.isEmpty() ){
			if(ParserOptionChain.optionChainList.get(0) != null){
//				String time = Utils.getCurrentTime();
				String time = Long.toString(new Date().getTime());
				Connection conn = getConnection();
				PreparedStatement pst;
				try {
					for(int i =0;i < ParserOptionChain.optionChainList.size();i++ ){
						OptionChainModal optionChainModal = ParserOptionChain.optionChainList.get(i);
						OptionChainItemModal optionChainItem = optionChainModal.getOptionChainItem();
					
					pst = conn
							.prepareStatement("insert into tbl_optionchain ("
									+ "Stock_Symbol, "
									+ "Stock_CurrentPrice, "
									+ "Strike_Price, "
									+ "Put_Bid_price, "
									+ "Put_ask_Price, "
									+ "Put_ask_Qty, "
									+ "Put_Bid_Qty, "
									+ "Put_Ltp_netchange, "
									+ "Put_Ltp, "
									+ "Put_IV,  "
									+ "Put_volume, "
									+ "Put_ChangeInOpenInterest, "
									+ "Put_Openinterest, "
									+ "Call_Bid_price, "
									+ "Call_ask_Price, "
									+ "Call_ask_Qty, "
									+ "Call_Bid_Qty, "
									+ "Call_Ltp_netchange, "
									+ "Call_Ltp, "
									+ "Call_IV, "
									+ "Call_volume, "
									+ "Call_ChangeInOpenInterest, "
									+ "Call_Openinterest, "
									+ "LastUpdateDateTime, "
									+ "ExpiryDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					pst.setString(1, optionChainModal.getStockSymbol());
					pst.setFloat(2, Float.parseFloat(optionChainModal.getStrickCurrentPrice()));
					pst.setFloat(3, Float.parseFloat(optionChainItem.getStrikePrice()));
					pst.setFloat(4, Float.parseFloat(optionChainItem.getPutBidPrice()));
					pst.setFloat(5, Float.parseFloat(optionChainItem.getPutAskPrice()));
					pst.setFloat(6, Float.parseFloat(optionChainItem.getPutAskQuantity()));
					pst.setFloat(7, Float.parseFloat(optionChainItem.getPutBidQuantity()));
					pst.setFloat(8, Float.parseFloat(optionChainItem.getPutNetChange()));
					pst.setFloat(9, Float.parseFloat(optionChainItem.getPutLastTradingPrice()));
					pst.setFloat(10, Float.parseFloat(optionChainItem.getPutImpliedVolatility()));
					pst.setFloat(11, Float.parseFloat(optionChainItem.getPutVolume()));
					pst.setFloat(12, Float.parseFloat(optionChainItem.getPutChangeInOpenInterest()));
					pst.setFloat(13, Float.parseFloat(optionChainItem.getPutOpenInterest()));
					pst.setFloat(14, Float.parseFloat(optionChainItem.getCallBidPrice()));
					pst.setFloat(15, Float.parseFloat(optionChainItem.getCallAskPrice()));
					pst.setFloat(16, Float.parseFloat(optionChainItem.getCallAskQuantity()));
					pst.setFloat(17, Float.parseFloat(optionChainItem.getCallBidQuantity()));
					pst.setFloat(18, Float.parseFloat(optionChainItem.getCallNetChange()));
					pst.setFloat(19, Float.parseFloat(optionChainItem.getCallLastTradingPrice()));
					pst.setFloat(20, Float.parseFloat(optionChainItem.getCallImpliedVolatility()));
					pst.setFloat(21, Float.parseFloat(optionChainItem.getCallVolume()));
					pst.setFloat(22, Float.parseFloat(optionChainItem.getCallChangeInOpenInterest()));
					pst.setFloat(23, Float.parseFloat(optionChainItem.getCallOpenInterest()));
					pst.setString(24, time);
					pst.setString(25, optionChainModal.getExpiryDate());
					pst.execute();
					
					pst.close();
					}
					flag = true;
				} catch (SQLException e) {
					flag = false;
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
}