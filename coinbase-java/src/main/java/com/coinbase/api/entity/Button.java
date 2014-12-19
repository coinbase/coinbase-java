package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Button implements Serializable {

    public enum Type {
	BUY_NOW("buy_now"),
	DONATION("donation"),
	SUBSCRIPTION("subscription");
	
	private String _value;
	private Type(String value) { this._value = value; }
	
	@JsonValue
	public String toString() { return this._value; }
	
	@JsonCreator
	public static Type create(String val) {
	    for (Type type : Type.values()) {
		if (type.toString().equalsIgnoreCase(val)) {
		    return type;
		}
	    }
	    return null;
	}
    }
    
    public enum Repeat {
	NEVER("never"),
	DAILY("daily"),
	WEEKLY("weekly"),
	BIWEEKLY("every_two_weeks"),
	MONTHLY("monthly"),
	QUARTERLY("quarterly"),
	YEARLY("yearly");
	
	private String _value;
	private Repeat(String value) { this._value = value; }
	
	@JsonValue
	public String toString() { return this._value; }
	
	@JsonCreator
	public static Repeat create(String val) {
	    for (Repeat repeat : Repeat.values()) {
		if (repeat.toString().equalsIgnoreCase(val)) {
		    return repeat;
		}
	    }
	    return null;
	}
    }
    
    public enum Style {
	BUY_NOW_LARGE("buy_now_large"),
	BUY_NOW_SMALL("buy_now_small"),
	DONATION_LARGE("donation_large"),
	DONATION_SMALL("donation_small"),
	SUBSCRIPTION_LARGE("subscription_large"),
	SUBSCRIPTION_SMALL("subscription_small"),
	CUSTOM_LARGE("custom_large"),
	CUSTOM_SMALL("custom_small"),
	NONE("none");
	
	private String _value;
	private Style(String value) { this._value = value; }
	
	@JsonValue
	public String toString() { return this._value; }
	
	@JsonCreator
	public static Style create(String val) {
	    for (Style style : Style.values()) {
		if (style.toString().equalsIgnoreCase(val)) {
		    return style;
		}
	    }
	    return null;
	}
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = -5470904374812796853L;
    private String _name;
    private String _priceString;
    private String _priceCurrencyIso;
    private Type _type;
    private Style _style;
    private String _text;
    private String _description;
    private String _custom;
    private Boolean _customSecure;
    private String _callbackUrl;
    private String _successUrl;
    private String _cancelUrl;
    private String _infoUrl;
    private Boolean _autoRedirect;
    // TODO variable price if it gets fixed
    private Boolean _choosePrice;
    private Boolean _includeAddress;
    private Boolean _includeEmail;
    private Money _price;
    private String _code;
    private Repeat _repeat;
    
    public Repeat getRepeat() {
        return _repeat;
    }
    public void setRepeat(Repeat repeat) {
        _repeat = repeat;
    }
    public String getId() {
        return _code;
    }
    public void setId(String id) {
        _code = id;
    }
    public String getCode() {
        return _code;
    }
    public void setCode(String code) {
        _code = code;
    }
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }
    public String getPriceString() {
        return _priceString;
    }
    public void setPriceString(String priceString) {
        _priceString = priceString;
    }
    public String getPriceCurrencyIso() {
        return _priceCurrencyIso;
    }
    public void setPriceCurrencyIso(String priceCurrencyIso) {
        _priceCurrencyIso = priceCurrencyIso;
    }
    public Type getType() {
        return _type;
    }
    public void setType(Type type) {
        _type = type;
    }
    public Style getStyle() {
        return _style;
    }
    public void setStyle(Style style) {
        _style = style;
    }
    public String getText() {
        return _text;
    }
    public void setText(String text) {
        _text = text;
    }
    public String getDescription() {
        return _description;
    }
    public void setDescription(String description) {
        _description = description;
    }
    public String getCustom() {
        return _custom;
    }
    public void setCustom(String custom) {
        _custom = custom;
    }
    public Boolean getCustomSecure() {
        return _customSecure;
    }
    public void setCustomSecure(Boolean customSecure) {
        _customSecure = customSecure;
    }
    public String getCallbackUrl() {
        return _callbackUrl;
    }
    public void setCallbackUrl(String customUrl) {
        _callbackUrl = customUrl;
    }
    public String getSuccessUrl() {
        return _successUrl;
    }
    public void setSuccessUrl(String successUrl) {
        _successUrl = successUrl;
    }
    public String getCancelUrl() {
        return _cancelUrl;
    }
    public void setCancelUrl(String cancelUrl) {
        _cancelUrl = cancelUrl;
    }
    public String getInfoUrl() {
        return _infoUrl;
    }
    public void setInfoUrl(String infoUrl) {
        _infoUrl = infoUrl;
    }
    public Boolean getAutoRedirect() {
        return _autoRedirect;
    }
    public void setAutoRedirect(Boolean autoRedirect) {
        _autoRedirect = autoRedirect;
    }
    public Boolean getChoosePrice() {
        return _choosePrice;
    }
    public void setChoosePrice(Boolean choosePrice) {
        _choosePrice = choosePrice;
    }
    public Boolean getIncludeAddress() {
        return _includeAddress;
    }
    public void setIncludeAddress(Boolean includeAddress) {
        _includeAddress = includeAddress;
    }
    public Boolean getIncludeEmail() {
        return _includeEmail;
    }
    public void setIncludeEmail(Boolean includeEmail) {
        _includeEmail = includeEmail;
    }
    public Money getPrice() {
        return _price;
    }
    public void setPrice(Money price) {
        _price = price;
        if (price != null) {
            setPriceString(price.getAmount().toPlainString());
            setPriceCurrencyIso(price.getCurrencyUnit().getCurrencyCode());
        } else {
            setPriceString(null);
            setPriceCurrencyIso(null);
        }
    }
}
