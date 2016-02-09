package com.coinbase.v1.entity;

import com.coinbase.v1.deserializer.TransfersLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TransfersResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -812292521739103434L;
    private List<Transfer> _transfers;
    
    public List<Transfer> getTransfers() {
        return _transfers;
    }
    
    @JsonDeserialize(converter=TransfersLifter.class)
    public void setTransfers(List<Transfer> transfers) {
        _transfers = transfers;
    }
}
