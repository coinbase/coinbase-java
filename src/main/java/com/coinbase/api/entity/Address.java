package com.coinbase.api.entity;

import org.joda.time.DateTime;

public class Address {
    
    private String _address;
    private String _callbackUrl;
    private String _label;
    private DateTime _createdAt;

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public String getCallbackUrl() {
        return _callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        _callbackUrl = callbackUrl;
    }

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public DateTime getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        _createdAt = createdAt;
    }
  
}
