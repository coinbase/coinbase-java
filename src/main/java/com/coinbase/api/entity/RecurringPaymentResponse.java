package com.coinbase.api.entity;

public class RecurringPaymentResponse extends Response {
    private RecurringPayment _recurringPayment;

    public RecurringPayment getRecurringPayment() {
        return _recurringPayment;
    }

    public void setRecurringPayment(RecurringPayment recurringPayment) {
        _recurringPayment = recurringPayment;
    }
}
