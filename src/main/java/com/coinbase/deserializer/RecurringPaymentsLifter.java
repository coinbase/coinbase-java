package com.coinbase.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.entity.RecurringPayment;
import com.coinbase.entity.RecurringPaymentNode;
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
