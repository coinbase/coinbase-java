package com.coinbase.v1.deserializer;

import com.coinbase.v1.entity.RecurringPayment;
import com.coinbase.v1.entity.RecurringPaymentNode;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

public class RecurringPaymentsLifter extends StdConverter<List<RecurringPaymentNode>, List<RecurringPayment>> {

    public List<RecurringPayment> convert(List<RecurringPaymentNode> nodes) {
	ArrayList<RecurringPayment> result = new ArrayList<RecurringPayment>();
	
	for (RecurringPaymentNode node : nodes) {
	    result.add(node.getRecurringPayment());
	}
	
	return result;
    }

}
