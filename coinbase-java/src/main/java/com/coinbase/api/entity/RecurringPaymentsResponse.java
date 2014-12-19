package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.deserializer.RecurringPaymentsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
