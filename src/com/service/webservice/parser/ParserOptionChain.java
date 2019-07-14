package com.service.webservice.parser;

import java.util.ArrayList;
import java.util.List;

import com.service.webservice.modal.OptionChainItemModal;
import com.service.webservice.modal.OptionChainModal;
import com.service.webservice.utilities.Utils;

public class ParserOptionChain {
	public static List<String> tableRowList = new ArrayList<String>();
	public static List<String> tableDetailList = new ArrayList<String>();
	public static String expiryDate = Utils.DEFAULT;
	public static OptionChainModal optionChainModal = null;
	public static List<OptionChainModal> optionChainList = new ArrayList<OptionChainModal>();

	public static void optionChainParser(String responseData) {
		if (Utils.isValidString(responseData)) {
			responseData = responseData.replaceAll("\\s", "");//Remove Space
			expiryDate = parseExpiryDate(responseData);
			if (responseData.contains("<divclass=\"opttbldata\">")) {
				String subStringResponse = responseData.substring(
						responseData.indexOf("<divclass=\"opttbldata\">"),
						responseData.indexOf("</table></div>"));
				subStringResponse = subStringResponse.replace(subStringResponse
						.substring(subStringResponse.indexOf("<thead>"),
								subStringResponse.indexOf("</thead>")), "");
				subStringResponse = subStringResponse
						.replace(
								"<divclass=\"opttbldata\"><tableid=\"octable\"width=\"100%\"border=\"0\"cellpadding=\"0\"cellspacing=\"0\"></thead>",
								"");
				String[] arrOfStr = subStringResponse.split("</tr>");

				for (int i = 0; i < arrOfStr.length; i++) {
					if (Utils.isValidString(arrOfStr[i])) {
						arrOfStr[i] = arrOfStr[i].replace("<tr>", "");
//						tableRowList.add(arrOfStr[i]);
					}
					OptionChainItemModal optionChainItem = parseOptionChainRow(arrOfStr[i]);
					System.out.println("################# optionChainItem : "+optionChainItem.getStrikePrice());
					if(optionChainItem.getStrikePrice().equals("11750.00")){
						System.out.println("################# optionChainItem value: "+optionChainItem.getCallLastTradingPrice());
					}
					if(optionChainItem != null && Utils.isValidString(optionChainItem.getStrikePrice())){
						optionChainModal = new OptionChainModal();
						optionChainModal.setStrickPrice(optionChainItem.getStrikePrice());
						optionChainModal.setExpiryDate(expiryDate);
						optionChainModal.setOptionChainItem(optionChainItem);
						optionChainList.add(optionChainModal);
					}
				}
			}
		}
//		System.out.println("################# optionChainList length : "+optionChainList.size());
	}
	
	private static String parseExpiryDate(String responseData){
		String spanElement = responseData.substring(responseData.indexOf("<span>ExpiryDate"), responseData.indexOf("</span></form>"));
		String selectElement = spanElement.substring(spanElement.indexOf("<selectstyle=\"width:100px\"id=\"date\"name=\"date\"onchange=\"document.forms['ocForm'].submit();\"class=\"goodTextBox\">"), spanElement.indexOf("</select>"));
		selectElement = selectElement.replace("<selectstyle=\"width:100px\"id=\"date\"name=\"date\"onchange=\"document.forms['ocForm'].submit();\"class=\"goodTextBox\">", "");
		String[] arrOfStr = selectElement.split("</option>"); 
		String expiryDate = "";
		for (int i = 0; i < arrOfStr.length; i++) {
			if(arrOfStr[i].contains("selected")){
				expiryDate = arrOfStr[i].replace(arrOfStr[i].substring(arrOfStr[i].indexOf("<"), arrOfStr[i].indexOf(">")), "").replace(">", "");
				System.out.println("################# :expiryDate "+expiryDate);
			}
		}
		return expiryDate;
	}

	private static OptionChainItemModal parseOptionChainRow(String optionChainRow) {
		OptionChainItemModal optionChainItem = null;
		if (Utils.isValidString(optionChainRow)) {
			optionChainItem = new OptionChainItemModal();
			String[] arrOfStr = optionChainRow.split("</td>");
			boolean isCallFlag = true;
			for (int i = 0; i < arrOfStr.length; i++) {
				if (Utils.isValidString(arrOfStr[i])) {
					arrOfStr[i] = arrOfStr[i]
							.replace(
									"<!--<td><ahref=\"javascript:popup1('','','1')\">Quote</a></td><td><ahref=\"javascript:popup1('','','','','CE')\"><imgsrc=\"/images/print3.gif\"></a></td>-->",
									"");
					arrOfStr[i] = arrOfStr[i]
							.replace(
									"<!--<td><ahref=\"javascript:popup1('','','1')\">Quote</a></td><td><ahref=\"javascript:popup1('','','','','PE')\"><imgsrc=\"/images/print3.gif\"></a></td>-->",
									"");
					arrOfStr[i] = arrOfStr[i].replace("<td>", "");
					// System.out.println("#########################  "+arrOfStr[i].toString());
					if ((arrOfStr[i].contains("ylwbg")
							|| arrOfStr[i].contains("nobg")
							|| arrOfStr[i].contains("grybg"))
							&& !arrOfStr[i].contains("Arial,Helvetica,sans-serif")) {
						if (arrOfStr[i].contains("<ahref")) {
							if (arrOfStr[i].contains("<b>")) {
								arrOfStr[i] = arrOfStr[i].substring(arrOfStr[i].indexOf("<b>"),
												arrOfStr[i].indexOf("</b>"));
								arrOfStr[i] = arrOfStr[i]+"</b>";
								arrOfStr[i] = arrOfStr[i].replace("<b>", "").replace("</b>", "");
//								arrOfStr[i] = arrOfStr[i].replace("<b>", "<StrikePrice>").replace("</b>", "</StrikePrice>");
								optionChainItem.setStrikePrice(arrOfStr[i]);
								isCallFlag = false;
								tableDetailList.add(arrOfStr[i]);
//								System.out
//										.println("#########################  Strick Price : "
//												+ arrOfStr[i]);
							}else{
								arrOfStr[i] = arrOfStr[i].replace("<!--End-->", "");
								if(arrOfStr[i].contains("nobg")){
									if(arrOfStr[i].contains("-->")){
										arrOfStr[i] = arrOfStr[i].replace(arrOfStr[i].substring(arrOfStr[i].indexOf("<tdclass=\"nobg\">"), arrOfStr[i].indexOf("-->")), "");
									}else{
										arrOfStr[i] = arrOfStr[i].replace("<tdclass=\"nobg\">", "");
									}
								
								arrOfStr[i] = arrOfStr[i].replace("-->","");
								arrOfStr[i] = arrOfStr[i].replace(arrOfStr[i].substring(arrOfStr[i].indexOf("<ahref="),arrOfStr[i].indexOf(">")), "");
								arrOfStr[i] =arrOfStr[i].substring(arrOfStr[i].indexOf(">"),arrOfStr[i].indexOf("<"));
								arrOfStr[i] = arrOfStr[i].replace(">","");
								}else{
									if(arrOfStr[i].contains("-->")){
										arrOfStr[i] = arrOfStr[i].replace(arrOfStr[i].substring(arrOfStr[i].indexOf("<tdclass=\"ylwbg\">"), arrOfStr[i].indexOf("-->")), "");
										arrOfStr[i] = arrOfStr[i].replace("-->","");
									}else{
										arrOfStr[i] = arrOfStr[i].replace("<tdclass=\"ylwbg\">", "");
									}
									arrOfStr[i] = arrOfStr[i].replace(arrOfStr[i].substring(arrOfStr[i].indexOf("<ahref="),arrOfStr[i].indexOf(">")), "");
									arrOfStr[i] =arrOfStr[i].substring(arrOfStr[i].indexOf(">"),arrOfStr[i].indexOf("<"));
									arrOfStr[i] = arrOfStr[i].replace(">","");
								}
								if(isCallFlag){
									setCallOptionChainItem(i, arrOfStr[i], optionChainItem);
								}else{
									setPutOptionChainItem(i, arrOfStr[i], optionChainItem);
								}
							}
						} else {
							arrOfStr[i] = arrOfStr[i]+"</td>";
							arrOfStr[i] = arrOfStr[i].substring(arrOfStr[i].indexOf("<tdclass="),arrOfStr[i].indexOf( "</td>"));
							arrOfStr[i] = arrOfStr[i].replace(arrOfStr[i].substring(arrOfStr[i].indexOf("<"),arrOfStr[i].indexOf( ">"))+">", "");
//							System.out.println("############arrOfStr[i] :  "+arrOfStr[i]);
							if(isCallFlag){
								
								setCallOptionChainItem(i, arrOfStr[i], optionChainItem);
//								 = "<Call>"+arrOfStr[i]+"</Call>";
							}else{
								setPutOptionChainItem(i, arrOfStr[i], optionChainItem);
//								arrOfStr[i] = "<Put>"+arrOfStr[i]+"</Put>";
							}
						}
					}
				}

			}
		}
		return optionChainItem;
	}
	
	private static void setCallOptionChainItem(int index, String item, OptionChainItemModal optionChainItem){
		if(item.equals("-")){
			item = "0";
		}
		item = item.replaceAll(",", "");
//		System.out.println("Call Index : "+index+" : Value : "+item);
		
		  switch (index){
		  case 3: optionChainItem.setCallOpenInterest(item);
		  break;
		  case 4: optionChainItem.setCallChangeInOpenInterest(item);
		  break;
		  case 5: optionChainItem.setCallVolume(item);
		  break;
		  case 6: optionChainItem.setCallImpliedVolatility(item);
		  break;
		  case 7: optionChainItem.setCallLastTradingPrice(item);
		  break;
		  case 8: optionChainItem.setCallNetChange(item);
		  break;
		  case 9: optionChainItem.setCallBidQuantity(item);
		  break;
		  case 10: optionChainItem.setCallBidPrice(item);
		  break;
		  case 11: optionChainItem.setCallAskPrice(item);
		  break;
		  case 12: optionChainItem.setCallAskQuantity(item);
		  break;
		  default: 
		  }
	}
	private static void setPutOptionChainItem(int index, String item, OptionChainItemModal optionChainItem){
		if(item.equals("-")){
			item = "0";
		}
		item = item.replaceAll(",", "");
//	System.out.println("Put Index : "+index+" : Value : "+item);
	
		  switch (index){
		  case 14: optionChainItem.setPutBidQuantity(item);
		  break;
		  case 15: optionChainItem.setPutBidPrice(item);
		  break;
		  case 16: optionChainItem.setPutAskPrice(item);
		  break;
		  case 17: optionChainItem.setPutAskQuantity(item);
		  break;
		  case 18: optionChainItem.setPutNetChange(item);
		  break;
		  case 19: optionChainItem.setPutLastTradingPrice(item);
		  break;
		  case 20: optionChainItem.setPutImpliedVolatility(item);
		  break;
		  case 21: optionChainItem.setPutVolume(item);
		  break;
		  case 22: optionChainItem.setPutChangeInOpenInterest(item);
		  break;
		  case 23: optionChainItem.setPutOpenInterest(item);
		  break;
		  }
	}
}
