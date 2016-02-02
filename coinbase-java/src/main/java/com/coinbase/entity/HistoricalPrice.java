package com.coinbase.entity;

import org.joda.money.Money;
import org.joda.time.DateTime;

import java.io.Serializable;

public class HistoricalPrice implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7026579109955379515L;
    private DateTime _time;
    private Money _spotPrice;

    public DateTime getTime() {
        return _time;
    }

    public void setTime(DateTime time) {
        _time = time;
    }

    public Money getSpotPrice() {
        return _spotPrice;
    }

    public void setSpotPrice(Money spotPrice) {
        _spotPrice = spotPrice;
    }
}
