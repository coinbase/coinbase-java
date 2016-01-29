package com.coinbase.api.entity;

import org.joda.time.DateTime;

import java.io.Serializable;

public class HistoricPrice implements Serializable {

    private static final long serialVersionUID = 7221585030849430461L;

    private DateTime _time;
    private Float _price;

    public DateTime getTime() {
        return _time;
    }

    public void setTime(DateTime time) {
        _time = time;
    }

    public Float getPrice() {
        return _price;
    }

    public void setPrice(Float price) {
        _price = price;
    }
}
