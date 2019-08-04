package com.service.webservice.database;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.service.webservice.parser.ParserOptionChain;
import com.service.webservice.service.ServiceOptionChain;
import com.service.webservice.utilities.StockMarketConstants;

public class SchedularTask extends TimerTask {
	public static boolean isSchedularStop = false;
	private MysqlCon mSqlCon = new MysqlCon();
	public static Timer mTimer = null;
	
	
	public SchedularTask(boolean isSchedularStop, Timer mTimer) {
		this.isSchedularStop = isSchedularStop;
		this.mTimer = mTimer;
	}
	// Add your task here
	public void run() {
		try {
			if(isSchedularStop) {
				if(mTimer != null) {
					mTimer.cancel();
					mTimer.purge();
				}
			}else {
				System.out.println("############### run timer");
				String responseData = ServiceOptionChain.sendGET(StockMarketConstants.NIFTY50_URL);
				System.out.println("################# optionChainList : "+ParserOptionChain.optionChainList.size());
				mSqlCon.insertDataInOptionChain(StockMarketConstants.TABLE_OPTIONCHAIN);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
