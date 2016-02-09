package com.coinbase.v1.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMethodResponse extends ResponseV2 {

    private static final long serialVersionUID = 9092224278859996820L;

    @JsonProperty(value = "data")
    private com.coinbase.v1.entity.PaymentMethod _paymentMethod;

    public com.coinbase.v1.entity.PaymentMethod getPaymentMethod() {
        return _paymentMethod;
    }

    public void setPaymentMethod(com.coinbase.v1.entity.PaymentMethod paymentMethod) {
        this._paymentMethod = paymentMethod;
    }
}
