package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.deserializer.PaymentMethodsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PaymentMethodsResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 4752885593284986181L;
    private String _defaultBuy;
    private String _defaultSell;
    private List<PaymentMethod> _paymentMethods;

    public String getDefaultBuy() {
        return _defaultBuy;
    }

    public void setDefaultBuy(String defaultBuy) {
        _defaultBuy = defaultBuy;
    }

    public String getDefaultSell() {
        return _defaultSell;
    }

    public void setDefaultSell(String defaultSell) {
        _defaultSell = defaultSell;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return _paymentMethods;
    }

    @JsonDeserialize(converter=PaymentMethodsLifter.class)
    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        _paymentMethods = paymentMethods;
    }
}
