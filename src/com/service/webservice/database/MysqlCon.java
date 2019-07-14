package com.service.webservice.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.service.webservice.modal.OptionChainItemModal;
import com.service.webservice.modal.OptionChainModal;
import com.service.webservice.parser.ParserOptionChain;
import com.service.webservice.service.ServiceOptionChain;
import com.service.webservice.utilities.Utils;

public class MysqlCon {
	public static int id = 0;
	public static String name = null;
	public static String place = null;
	public static String age = null;

	public static void main(String args[]) {
			 new Thread(new Runnable() {
			        public void run(){
			        	try {
			        		
							String responseData = ServiceOptionChain.sendGET("https://www.nseindia.com/live_market/dynaContent/live_watch/option_chain/optionKeys.jsp?segmentLink=17&instrument=OPTIDX&symbol=NIFTY");
//			        			String responseData = ServiceOptionChain.sendGET("https://www.nseindia.com/live_market/dynaContent/live_watch/option_chain/optionKeys.jsp?symbolCode=818&symbol=ITC&symbol=ITC&instrument=OPTSTK&date=-&segmentLink=17&segmentLink=17");
							System.out.println("################# optionChainList : "+ParserOptionChain.optionChainList.size());
							insertDataInOptionChain();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			    }).start();
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + "stockschema"
								+ "?useSSL=false", "root", "Johny@1986");
		} catch (Exception e) {
			System.out.println(e);
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
				String time = Utils.getCurrentTime();
				Connection conn = getConnection();
				PreparedStatement pst;
				try {
					for(int i =0;i < ParserOptionChain.optionChainList.size();i++ ){
						OptionChainModal optionChainModal = ParserOptionChain.optionChainList.get(i);
						OptionChainItemModal optionChainItem = optionChainModal.getOptionChainItem();
					
					pst = conn
							.prepareStatement("insert into tbl_optionchain ("
									+ "Stock_Symbol, "
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
									+ "ExpiryDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					pst.setString(1, "Nifty50");
					pst.setFloat(2, Float.parseFloat(optionChainItem.getStrikePrice()));
					pst.setFloat(3, Float.parseFloat(optionChainItem.getPutBidPrice()));
					pst.setFloat(4, Float.parseFloat(optionChainItem.getPutAskPrice()));
					pst.setFloat(5, Float.parseFloat(optionChainItem.getPutAskQuantity()));
					pst.setFloat(6, Float.parseFloat(optionChainItem.getPutBidQuantity()));
					pst.setFloat(7, Float.parseFloat(optionChainItem.getPutNetChange()));
					pst.setFloat(8, Float.parseFloat(optionChainItem.getPutLastTradingPrice()));
					pst.setFloat(9, Float.parseFloat(optionChainItem.getPutImpliedVolatility()));
					pst.setFloat(10, Float.parseFloat(optionChainItem.getPutVolume()));
					pst.setFloat(11, Float.parseFloat(optionChainItem.getPutChangeInOpenInterest()));
					pst.setFloat(12, Float.parseFloat(optionChainItem.getPutOpenInterest()));
					pst.setFloat(13, Float.parseFloat(optionChainItem.getCallBidPrice()));
					pst.setFloat(14, Float.parseFloat(optionChainItem.getCallAskPrice()));
					pst.setFloat(15, Float.parseFloat(optionChainItem.getCallAskQuantity()));
					pst.setFloat(16, Float.parseFloat(optionChainItem.getCallBidQuantity()));
					pst.setFloat(17, Float.parseFloat(optionChainItem.getCallNetChange()));
					pst.setFloat(18, Float.parseFloat(optionChainItem.getCallLastTradingPrice()));
					pst.setFloat(19, Float.parseFloat(optionChainItem.getCallImpliedVolatility()));
					pst.setFloat(20, Float.parseFloat(optionChainItem.getCallVolume()));
					pst.setFloat(21, Float.parseFloat(optionChainItem.getCallChangeInOpenInterest()));
					pst.setFloat(22, Float.parseFloat(optionChainItem.getCallOpenInterest()));
					pst.setString(23, time);
					pst.setString(24, optionChainModal.getExpiryDate());
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