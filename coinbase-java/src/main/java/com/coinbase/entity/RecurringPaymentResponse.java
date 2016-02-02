package com.coinbase.entity;

public class RecurringPaymentResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -1740844042364630330L;
    private com.coinbase.entity.RecurringPayment _recurringPayment;

    public com.coinbase.entity.RecurringPayment getRecurringPayment() {
        return _recurringPayment;
    }

    public void setRecurringPayment(com.coinbase.entity.RecurringPayment recurringPayment) {
        _recurringPayment = recurringPayment;
    }
}
