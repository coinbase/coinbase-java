package com.coinbase.api.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class OAuthTokensRequest implements Serializable {

    public enum GrantType {
        AUTHORIZATION_CODE("authorization_code"),
        REFRESH_TOKEN("refresh_token");
        
        private String _value;
        private GrantType(String value) { this._value = value; }
        
        @JsonValue
        public String toString() { return this._value; }
        
        @JsonCreator
        public static GrantType create(String val) {
            for (GrantType type : GrantType.values()) {
                if (type.toString().equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    private static final long serialVersionUID = 1267065019984284435L;
    
    private String clientId;
    private String clientSecret;
    private GrantType grantType;
    private String refreshToken;
    private String code;
    private String redirectUri;
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public GrantType getGrantType() {
        return grantType;
    }
    
    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
