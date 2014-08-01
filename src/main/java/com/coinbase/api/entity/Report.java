package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Report implements Serializable {

    public enum Type {
        TRANSACTIONS("transactions"),
        ORDERS("orders"),
        TRANSFERS("transfers");
        
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
        ACTIVE("active"),
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
    
    public enum TimeRange {
        TODAY("today"),
        YESTERDAY("yesterday"),
        PAST_SEVEN("past_7"),
        PAST_THIRTY("past_30"),
        MONTH_TO_DATE("month_to_date"),
        LAST_FULL_MONTH("last_full_month"),
        YEAR_TO_DATE("year_to_date"),
        LAST_FULL_YEAR("last_full_year"),
        ALL("all"),
        CUSTOM("custom");
        
        private String _value;
        private TimeRange(String value) { this._value = value; }
        
        @JsonValue
        public String toString() { return this._value; }
        
        @JsonCreator
        public static TimeRange create(String val) {
            for (TimeRange timerange : TimeRange.values()) {
                if (timerange.toString().equalsIgnoreCase(val)) {
                    return timerange;
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
    private static final long serialVersionUID = -8462599652861673119L;
    
    public static final Integer INFINITE = Integer.valueOf(-1);

    private String id;
    private Type type;
    private String email;
    private String callbackUrl;
    private TimeRange timeRange;
    private StartType startType;
    private String nextRunDate;
    private String nextRunTime;
    private Repeat repeat;
    private Integer times;
    private DateTime lastRun;
    private DateTime nextRun;
    private String timeRangeStart;
    private String timeRangeEnd;
    private DateTime timeRangeStartDateTime;
    private DateTime timeRangeEndDateTime;
    private Status status;
    private Integer timesRun;
    private DateTime createdAt;

    public DateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public String getTimeRangeStart() {
        return timeRangeStart;
    }
    public void setTimeRangeStart(DateTime timeRangeStart) {
        this.timeRangeStartDateTime = timeRangeStart;
        this.timeRangeStart = timeRangeStart.toString("MM/dd/YYYY");
    }
    public String getTimeRangeEnd() {
        return timeRangeEnd;
    }
    public void setTimeRangeEnd(DateTime timeRangeEnd) {
        this.timeRangeEndDateTime = timeRangeEnd;
        this.timeRangeEnd = timeRangeEnd.toString("MM/dd/YYYY");
    }
    public DateTime getLastRun() {
        return lastRun;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCallbackUrl() {
        return callbackUrl;
    }
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    public TimeRange getTimeRange() {
        return timeRange;
    }
    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }
    public StartType getStartType() {
        return startType;
    }
    public void setStartType(StartType startType) {
        this.startType = startType;
    }
    public Repeat getRepeat() {
        return repeat;
    }
    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }
    public Integer getTimes() {
        return times;
    }
    public void setTimes(Integer times) {
        this.times = times;
    }
    public static Integer getInfinite() {
        return INFINITE;
    }
    public String getNextRunDate() {
        return nextRunDate;
    }
    public String getNextRunTime() {
        return nextRunTime;
    }
    public void setNextRun(DateTime time) {
        nextRun = time;
        nextRunDate = time.toString("MM/dd/YYYY");
        nextRunTime = time.toString("HH:mm");
    }
    public void setLastRun(DateTime time) {
        lastRun = time;
    }
    public DateTime getNextRun() {
        return nextRun;
    }
    public Integer getTimesRun() {
        return timesRun;
    }
    public void setTimesRun(Integer timesRun) {
        this.timesRun = timesRun;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}

