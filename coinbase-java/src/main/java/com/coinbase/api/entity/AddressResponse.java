package com.coinbase.api.entity;

public class AddressResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -1558569647051796730L;
    private String _address;
    private String _callbackUrl;
    private String _label;

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
}
