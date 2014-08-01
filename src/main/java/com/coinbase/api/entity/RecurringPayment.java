package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class RecurringPayment implements Serializable {

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

    public enum StartType {
	ON("on"),
	NOW("now");
	
	private String _value;
	private StartType(String value) { this._value = value; }
	
	@JsonValue
	public String toString() { return this._value; }
	
	@JsonCreator
	public static StartType create(String val) {
	    for (StartType type : StartType.values()) {
		if (type.toString().equalsIgnoreCase(val)) {
		    return type;
		}
	    }
	    return null;
	}
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7769736829519464743L;
    
    public static final Integer INDEFINITE = -1;
    
    private String _id;
    private DateTime _createdAt;
    private Status _status;
    private String _custom;
    private Button _button;
    private String _to;
    private String _from;
    private Integer _timesRun;
    private Integer _times;
    private Button.Repeat _repeat;
    private DateTime _lastRun;
    private DateTime _nextRun;
    private String _notes;
    private String _description;
    private Money _amount;
    private StartType _startType;

    // TODO add type

    public String getTo() {
        return _to;
    }

    public void setTo(String to) {
        _to = to;
    }

    public String getFrom() {
        return _from;
    }

    public void setFrom(String from) {
        _from = from;
    }

    public Integer getTimesRun() {
        return _timesRun;
    }

    public void setTimesRun(Integer timesRun) {
        _timesRun = timesRun;
    }

    public Integer getTimes() {
        return _times;
    }

    public void setTimes(Integer times) {
        _times = times;
    }

    public Button.Repeat getRepeat() {
        return _repeat;
    }

    public void setRepeat(Button.Repeat repeat) {
        _repeat = repeat;
    }

    public DateTime getLastRun() {
        return _lastRun;
    }

    public void setLastRun(DateTime lastRun) {
        _lastRun = lastRun;
    }

    public DateTime getNextRun() {
        return _nextRun;
    }

    public void setNextRun(DateTime nextRun) {
        _nextRun = nextRun;
    }

    public String getNotes() {
        return _notes;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public Money getAmount() {
        return _amount;
    }

    public void setAmount(Money amount) {
        _amount = amount;
    }

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

    public StartType getStartType() {
	return _startType;
    }

    public void setStartType(StartType startType) {
	_startType = startType;
    }

}
