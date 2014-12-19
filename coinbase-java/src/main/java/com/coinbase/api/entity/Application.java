package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Application implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8979526541382258919L;
    private String _id;
    private DateTime _createdAt;
    private String _name;
    private String _redirectUri;
    private Integer _numUsers;
    private String _clientId;
    private String _clientSecret;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public DateTime getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        _createdAt = createdAt;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getRedirectUri() {
        return _redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        _redirectUri = redirectUri;
    }

    public Integer getNumUsers() {
        return _numUsers;
    }

    public void setNumUsers(Integer numUsers) {
        _numUsers = numUsers;
    }

    public String getClientId() {
        return _clientId;
    }

    public void setClientId(String clientId) {
        _clientId = clientId;
    }

    public String getClientSecret() {
        return _clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        _clientSecret = clientSecret;
    }
}
