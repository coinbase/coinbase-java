package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Order implements Serializable {

    public enum Status {
	COMPLETED("completed"),
	CANCELED("canceled"),
	EXPIRED("expired"),
	NEW("new"),
	MISPAID("mispaid"),
	REFUNDED("refunded");
	
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
    private static final long serialVersionUID = -8221149393524031079L;
    private String _id;
    private DateTime _createdAt;
    private Status _status;
    private Money _totalBtc;
    private Money _totalNative;
    private String _custom;
    private String _receiveAddress;
    private Button _button;
    private String _refundAddress;
    private Transaction _transaction;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public DateTime getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        _createdAt = createdAt;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public Money getTotalBtc() {
        return _totalBtc;
    }

    public void setTotalBtc(Money totalBtc) {
        _totalBtc = totalBtc;
    }

    public Money getTotalNative() {
        return _totalNative;
    }

    public void setTotalNative(Money totalNative) {
        _totalNative = totalNative;
    }

    public String getCustom() {
        return _custom;
    }

    public void setCustom(String custom) {
        _custom = custom;
    }

    public String getReceiveAddress() {
        return _receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        _receiveAddress = receiveAddress;
    }

    public Button getButton() {
        return _button;
    }

    public void setButton(Button button) {
        _button = button;
    }

    public String getRefundAddress() {
        return _refundAddress;
    }

    public void setRefundAddress(String refundAddress) {
        _refundAddress = refundAddress;
    }

    public Transaction getTransaction() {
        return _transaction;
    }

    public void setTransaction(Transaction transaction) {
        _transaction = transaction;
    }

}
