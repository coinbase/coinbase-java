package com.coinbase.api.entity;

import java.io.Serializable;

public class PaymentMethodNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3467480965918383878L;
    private PaymentMethod _paymentMethod;

    public PaymentMethod getPaymentMethod() {
        return _paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        _paymentMethod = paymentMethod;
    }
}
