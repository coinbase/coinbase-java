package com.coinbase.api.entity;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class RecurringPayment {

    public enum Status {
	NEW("new"),
	ACTIVE("active"),
	PAUSED("paused"),
	CANCELED("canceled"),
	COMPLETED("completed");
	
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
    
    private String _id;
    private DateTime _createdAt;
    private Status _status;
    private String _custom;
    private Button _button;

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

    public String getCustom() {
        return _custom;
    }

    public void setCustom(String custom) {
        _custom = custom;
    }

    public Button getButton() {
        return _button;
    }

    public void setButton(Button button) {
        _button = button;
    }

}
