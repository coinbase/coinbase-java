package com.coinbase.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaymentMethodsResponse extends ResponseV2 {
    /**
     *
     */
    private static final long serialVersionUID = 4752885593284986181L;
    private String _defaultBuy;
    private String _defaultSell;

    @JsonProperty(value = "data")
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

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        _paymentMethods = paymentMethods;
    }
}
