package com.coinbase.api.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.api.entity.PaymentMethod;
import com.coinbase.api.entity.PaymentMethodNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class PaymentMethodsLifter extends StdConverter<List<PaymentMethodNode>, List<PaymentMethod>> {

    public List<PaymentMethod> convert(List<PaymentMethodNode> nodes) {
	ArrayList<PaymentMethod> result = new ArrayList<PaymentMethod>();
	
	for (PaymentMethodNode node : nodes) {
	    result.add(node.getPaymentMethod());
	}
	
	return result;
    }

}
