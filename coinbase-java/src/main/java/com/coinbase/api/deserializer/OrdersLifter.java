package com.coinbase.api.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.api.entity.Order;
import com.coinbase.api.entity.OrderNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class OrdersLifter extends StdConverter<List<OrderNode>, List<Order>> {

    public List<Order> convert(List<OrderNode> nodes) {
	ArrayList<Order> result = new ArrayList<Order>();
	
	for (OrderNode node : nodes) {
	    result.add(node.getOrder());
	}
	
	return result;
    }

}
