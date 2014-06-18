package com.coinbase.api.entity;

import java.util.List;

public class RecurringPaymentsResponse extends Response {

    private List<RecurringPaymentNode> _recurringPayments;

    public List<RecurringPaymentNode> getRecurringPayments() {
	return _recurringPayments;
    }

    public void setRecurringPayments(List<RecurringPaymentNode> recurringPayments) {
	_recurringPayments = recurringPayments;
    }

}
