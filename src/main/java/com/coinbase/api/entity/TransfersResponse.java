package com.coinbase.api.entity;

import java.util.List;

public class TransfersResponse extends Response {
    private List<TransferNode> _transfers;
    
    public List<TransferNode> getTransfers() {
        return _transfers;
    }

    public void setTransfers(List<TransferNode> transfers) {
        _transfers = transfers;
    }
}
