package com.service.webservice.utilities;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Utils {

	public final static String SPACE = " ";
	public final static String DEFAULT = "";
	public final static String NEW_LINE = "\n";
	public final static String DEFAULT_NULL = "null";
	public final static String MODIFIED_NULL = "<null>";
	public static final String DEFAULT_NOT_APPLICABLE = "N/A";
	public static final String DEFAULT_NOT_SPECIFIED = "Not Specified";
	public static final String DEFAULT_NO_NAME = "No Name";
	public static final String COMMA_WITH_SPACE = ", ";
	public static final String NEXT_PAGE_TOKEN = "&pagetoken=";
	public static final String MAIL_DOT_COM = ".com";
	public static final String AT_THE_RATE = "@";
	public static final int REQUEST_CODE = 100;

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	/*********************************************************************
	 * @function isValidString()
	 * @param String
	 * @return boolean
	 * @description Check & return valid string
	 *********************************************************************/
	public static boolean isValidString(String testString) {
		try {
			if (testString != null
					&& !testString.trim().equalsIgnoreCase(
							DEFAULT_NOT_APPLICABLE)
					&& !testString.equalsIgnoreCase(DEFAULT_NOT_SPECIFIED)
					&& !testString.equalsIgnoreCase(DEFAULT_NULL)
					&& !testString.equalsIgnoreCase(MODIFIED_NULL)
					&& !testString.equalsIgnoreCase(DEFAULT)) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static String getCurrentTime() {
		long timeLong = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mma',' MMM d ");
		DateFormatSymbols symbols = new DateFormatSymbols();
		symbols.setAmPmStrings(new String[] { "AM", "PM" });
		dateFormat.setDateFormatSymbols(symbols);
		String systemTime = dateFormat.format(timeLong);

		return systemTime;
	}

	public static void setJsonValue(JSONObject json, String key, String value) {
		try {
			if (Utils.isValidString(value)) {
				json.put(key, value);
			} else {
				json.put(key, Utils.DEFAULT);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
