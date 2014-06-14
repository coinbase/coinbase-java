package com.coinbase.api.entity;

import java.util.HashMap;
import java.util.List;

import org.joda.money.Money;

import com.coinbase.api.deserializer.FeesCollector;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Quote {

    private HashMap<String, Money> _fees;
    private Money _subtotal;
    private Money _total;

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

    public HashMap<String, Money> getFees() {
        return _fees;
    }

    @JsonDeserialize(converter=FeesCollector.class )
    public void setFees(HashMap<String, Money> fees) {
        _fees = fees;
    }

}
