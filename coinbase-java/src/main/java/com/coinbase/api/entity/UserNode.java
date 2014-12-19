package com.coinbase.api.entity;

import java.io.Serializable;

public class UserNode implements Serializable {

        private static final long serialVersionUID = -6543933340956648721L;
        private User _user;

	public User getUser() {
		return _user;
	}

	public void setUser(User user) {
		_user = user;
	}
	
}
