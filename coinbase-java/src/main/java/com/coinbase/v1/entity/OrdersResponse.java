package com.coinbase.v1.entity;

import com.coinbase.v1.deserializer.OrdersLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class OrdersResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 2721898279676340667L;
    private List<com.coinbase.v1.entity.Order> _orders;
    
    public List<com.coinbase.v1.entity.Order> getOrders() {
	return _orders;
    }

    @JsonDeserialize(converter=OrdersLifter.class)
    public void setOrders(List<com.coinbase.v1.entity.Order> orders) {
	_orders = orders;
    }
}
