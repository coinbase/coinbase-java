package com.coinbase.api.entity;

public class RecurringPaymentResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -1740844042364630330L;
    private RecurringPayment _recurringPayment;

    public RecurringPayment getRecurringPayment() {
        return _recurringPayment;
    }

    public void setRecurringPayment(RecurringPayment recurringPayment) {
        _recurringPayment = recurringPayment;
    }
}
