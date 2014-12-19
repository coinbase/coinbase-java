package com.coinbase.api.entity;

import java.io.Serializable;

public class Contact implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7691519279895052424L;
    private String _email;

    public String getEmail() {
	return _email;
    }

    public void setEmail(String email) {
	_email = email;
    }
    
}
