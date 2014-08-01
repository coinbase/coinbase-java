package com.coinbase.api.entity;

import java.io.Serializable;

public class Token implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5190861502452162126L;
    private String tokenId;
    private String address;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
