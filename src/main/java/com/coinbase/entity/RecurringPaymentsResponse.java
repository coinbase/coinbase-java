package com.coinbase.entity;

import java.util.List;

import com.coinbase.deserializer.RecurringPaymentsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RecurringPaymentsResponse extends Response {

    /**
     * 
     */
    private static final long serialVersionUID = 5547214480181926761L;
    private List<com.coinbase.entity.RecurringPayment> _recurringPayments;

    public List<com.coinbase.entity.RecurringPayment> getRecurringPayments() {
	return _recurringPayments;
    }

    @JsonDeserialize(converter=RecurringPaymentsLifter.class)
    public void setRecurringPayments(List<com.coinbase.entity.RecurringPayment> recurringPayments) {
	_recurringPayments = recurringPayments;
    }

}
