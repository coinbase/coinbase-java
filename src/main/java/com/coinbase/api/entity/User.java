package com.coinbase.api.entity;

import org.joda.money.Money;

import com.coinbase.api.deserializer.MoneyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class User {
	
	private String _id;
	private String _name;
	private String _email;
	private String _timeZone;
	private String _nativeCurrency;
	private Money _balance;
	private Integer _buyLevel;
	private Integer _sellLevel;
	private Money _buyLimit;
	private Money _sellLimit;
	private String _password;
	private String _referrerId;
	private String _receiveAddress;

	public String getPassword() {
	    return _password;
	}

	public void setPassword(String password) {
	    _password = password;
	}

	public String getReferrerId() {
	    return _referrerId;
	}

	public void setReferrerId(String referrerId) {
	    _referrerId = referrerId;
	}

	public String getId() {
		return _id;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getEmail() {
		return _email;
	}
	
	public void setEmail(String email) {
		_email = email;
	}
	
	public String getTimeZone() {
		return _timeZone;
	}
	
	public void setTimeZone(String timeZone) {
		_timeZone = timeZone;
	}
	
	public String getNativeCurrency() {
		return _nativeCurrency;
	}
	
	public void setNativeCurrency(String nativeCurrency) {
		_nativeCurrency = nativeCurrency;
	}
	
	public Money getBalance() {
		return _balance;
	}
	
	@JsonDeserialize(using=MoneyDeserializer.class)
	public void setBalance(Money balance) {
		_balance = balance;
	}
	
	public Integer getBuyLevel() {
		return _buyLevel;
	}
	
	public void setBuyLevel(Integer buyLevel) {
		_buyLevel = buyLevel;
	}
	
	public Integer getSellLevel() {
		return _sellLevel;
	}
	
	public void setSellLevel(Integer sellLevel) {
		_sellLevel = sellLevel;
	}
	
	public Money getBuyLimit() {
		return _buyLimit;
	}
	
	@JsonDeserialize(using=MoneyDeserializer.class)
	public void setBuyLimit(Money buyLimit) {
		_buyLimit = buyLimit;
	}
	
	public Money getSellLimit() {
		return _sellLimit;
	}
	
	@JsonDeserialize(using=MoneyDeserializer.class)
	public void setSellLimit(Money sellLimit) {
		_sellLimit = sellLimit;
	}

	public String getReceiveAddress() {
	    return _receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
	    _receiveAddress = receiveAddress;
	}
	
}
