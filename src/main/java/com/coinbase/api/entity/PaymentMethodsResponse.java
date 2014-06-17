package com.coinbase.api.entity;

import java.util.List;

public class PaymentMethodsResponse extends Response {
    private String _defaultBuy;
    private String _defaultSell;
    private List<PaymentMethodNode> _paymentMethods;

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

    public List<PaymentMethodNode> getPaymentMethods() {
        return _paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodNode> paymentMethods) {
        _paymentMethods = paymentMethods;
    }
}
