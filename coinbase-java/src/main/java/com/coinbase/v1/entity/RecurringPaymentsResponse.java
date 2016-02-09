package com.coinbase.v1.entity;

import com.coinbase.v1.deserializer.RecurringPaymentsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class RecurringPaymentsResponse extends Response {

    /**
     * 
     */
    private static final long serialVersionUID = 5547214480181926761L;
    private List<RecurringPayment> _recurringPayments;

    public List<RecurringPayment> getRecurringPayments() {
	return _recurringPayments;
    }

    @JsonDeserialize(converter=RecurringPaymentsLifter.class)
    public void setRecurringPayments(List<RecurringPayment> recurringPayments) {
	_recurringPayments = recurringPayments;
    }

}
