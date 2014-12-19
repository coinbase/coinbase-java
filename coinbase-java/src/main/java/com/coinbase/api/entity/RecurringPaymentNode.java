package com.coinbase.api.entity;

import java.io.Serializable;

public class RecurringPaymentNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6607883611045097912L;
    private RecurringPayment _recurringPayment;

    public RecurringPayment getRecurringPayment() {
	return _recurringPayment;
    }

    public void setRecurringPayment(RecurringPayment recurringPayment) {
	_recurringPayment = recurringPayment;
    }
}
