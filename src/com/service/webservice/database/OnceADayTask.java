package com.service.webservice.database;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.service.webservice.service.ServiceOptionChain;
import com.service.webservice.utilities.StockMarketConstants;

public class OnceADayTask extends TimerTask {
    private final static long ONCE_PER_DAY = 1000*60*60*24;
    private MysqlCon mSqlCon = new MysqlCon();
    static Timer timer = new Timer();


    @Override
    public void run() {
//        long currentTime = System.currentTimeMillis();
//        long stopTime = currentTime + (ONCE_PER_DAY*5);//provide the 2hrs time it should execute 1000*60*60*2
//          while(stopTime != System.currentTimeMillis()){
              // Do your Job Here
        	  try {
				String responseData = ServiceOptionChain.sendGET(StockMarketConstants.NIFTY50_URL);
				mSqlCon.insertDataInOptionChain(StockMarketConstants.TABLE_OPTIONCHAIN_MASTER);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("#################### Start Job");
            System.out.println("#################### End Job"+System.currentTimeMillis());
//          }
    }
    private static Date getTomorrowMorning7PM(){
    	Calendar c1 = Calendar.getInstance();
    	c1.set(Calendar.HOUR_OF_DAY, 21);
    	c1.set(Calendar.MINUTE, 23);

           return c1.getTime();
      }
    //call this method from your servlet init method
    public static void startTask(){
    	OnceADayTask task = new OnceADayTask();
        timer.schedule(task,getTomorrowMorning7PM(),ONCE_PER_DAY);// for your case u need to give 1000*60*60*24
//        timer.schedule(task, 1000, 10*1000);
    }

}
