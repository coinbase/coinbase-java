package com.coinbase.api.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.api.entity.RecurringPayment;
import com.coinbase.api.entity.RecurringPaymentNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class RecurringPaymentsLifter extends StdConverter<List<RecurringPaymentNode>, List<RecurringPayment>> {

    public List<RecurringPayment> convert(List<RecurringPaymentNode> nodes) {
	ArrayList<RecurringPayment> result = new ArrayList<RecurringPayment>();
	
	for (RecurringPaymentNode node : nodes) {
	    result.add(node.getRecurringPayment());
	}
	
	return result;
    }

}
