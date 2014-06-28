package com.coinbase.api.entity;

import org.joda.money.Money;
import org.joda.time.DateTime;

public class HistoricalPrice {
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
