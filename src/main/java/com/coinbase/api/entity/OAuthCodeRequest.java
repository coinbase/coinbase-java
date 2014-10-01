package com.coinbase.api.entity;

import java.io.Serializable;

public class OAuthCodeRequest implements Serializable {
    private static final long serialVersionUID = 3716938132337502204L;

    public static class Meta implements Serializable {
        private static final long serialVersionUID = -5468361596726979847L;

        private String _name;

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }
    }

    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String token;
    private String scope;
    private String redirectUri;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    private Meta meta;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
