package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.deserializer.TransfersLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TransfersResponse extends Response {
    private List<Transfer> _transfers;
    
    public List<Transfer> getTransfers() {
        return _transfers;
    }
    
    @JsonDeserialize(converter=TransfersLifter.class)
    public void setTransfers(List<Transfer> transfers) {
        _transfers = transfers;
    }
}
