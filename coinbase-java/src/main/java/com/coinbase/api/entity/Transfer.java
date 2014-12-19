package com.coinbase.api.entity;

import java.io.Serializable;
import java.util.HashMap;

import org.joda.money.Money;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Transfer implements Serializable {

    public enum Type {
	SELL("Sell"),
	BUY("Buy");
	
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

    public enum Status {
	CREATED("created"),
	PENDING("Pending"),
	COMPLETE("Complete"),
	CANCELED("Canceled"),
	REVERSED("Reversed");
	
	private String _value;
	private Status(String value) { this._value = value; }
	
	@JsonValue
	public String toString() { return this._value; }
	
	@JsonCreator
	public static Status create(String val) {
	    for (Status status : Status.values()) {
		if (status.toString().equalsIgnoreCase(val)) {
		    return status;
		}
	    }
	    return null;
	}
    }

    /**
     * 
     */
    private static final long serialVersionUID = -5717063284463576652L;
    private Type _type;
    private Status _status;
    private String _code;
    private DateTime _createdAt;
    private DateTime _payoutDate;
    private HashMap<String, Money> _fees;
    private String _transactionId;
    private Money _btc;
    private Money _subtotal;
    private Money _total;
    private String _description;
    
    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        _type = type;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code;
    }

    public DateTime getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        _createdAt = createdAt;
    }

    public DateTime getPayoutDate() {
        return _payoutDate;
    }

    public void setPayoutDate(DateTime payoutDate) {
        _payoutDate = payoutDate;
    }

    public HashMap<String, Money> getFees() {
        return _fees;
    }

    @JsonDeserialize(as=HashMap.class, keyAs=String.class, contentAs=Money.class )
    public void setFees(HashMap<String, Money> fees) {
        _fees = fees;
    }

    public String getTransactionId() {
        return _transactionId;
    }

    public void setTransactionId(String transactionId) {
        _transactionId = transactionId;
    }

    public Money getBtc() {
        return _btc;
    }

    public void setBtc(Money btc) {
        _btc = btc;
    }

    public Money getSubtotal() {
        return _subtotal;
    }

    public void setSubtotal(Money subtotal) {
        _subtotal = subtotal;
    }

    public Money getTotal() {
        return _total;
    }

    public void setTotal(Money total) {
        _total = total;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

}
