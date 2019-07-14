package com.service.webservice.modal;

import com.service.webservice.utilities.Utils;

public class OptionChainModal {
	private String strickPrice = Utils.DEFAULT;
	private String stockSymbol = Utils.DEFAULT;
	private String expiryDate = Utils.DEFAULT;
	private OptionChainItemModal optionChainItem= null;
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getStrickPrice() {
		return strickPrice;
	}
	public void setStrickPrice(String strickPrice) {
		this.strickPrice = strickPrice;
	}
	public OptionChainItemModal getOptionChainItem() {
		return optionChainItem;
	}
	public void setOptionChainItem(OptionChainItemModal optionChainItem) {
		this.optionChainItem = optionChainItem;
	}
	
}
