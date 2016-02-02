package com.coinbase.entity;

import com.coinbase.deserializer.OrdersLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class OrdersResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 2721898279676340667L;
    private List<Order> _orders;
    
    public List<Order> getOrders() {
	return _orders;
    }

    @JsonDeserialize(converter=OrdersLifter.class)
    public void setOrders(List<Order> orders) {
	_orders = orders;
    }
}
