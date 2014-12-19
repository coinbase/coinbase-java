package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class OAuthCodeRequest implements Serializable {
    private static final long serialVersionUID = 3716938132337502204L;

    public static class Meta implements Serializable {
        private static final long serialVersionUID = -5468361596726979847L;

        public enum Period {
            DAILY("daily"),
            WEEKLY("weekly"),
            MONTHLY("monthly");

            private String _value;

            private Period(String value) {
                this._value = value;
            }

            @Override
            @JsonValue
            public String toString() {
                return this._value;
            }

            @JsonCreator
            public static Period create(String val) {
                for (Period period : Period.values()) {
                    if (period.toString().equalsIgnoreCase(val)) {
                        return period;
                    }
                }
                return null;
            }
        }

        private String _name;
        private Money _sendLimitAmount;
        private Period _sendLimitPeriod;

        public String getName() {
            return _name;
        }

        public void setName(String name) {
            _name = name;
        }

        public Money getSendLimitAmount() {
            return _sendLimitAmount;
        }

        public void setSendLimitAmount(Money sendLimitAmount) {
            _sendLimitAmount = sendLimitAmount;
        }

        public Period getSendLimitPeriod() {
            return _sendLimitPeriod;
        }

        public void setSendLimitPeriod(Period sendLimitPeriod) {
            _sendLimitPeriod = sendLimitPeriod;
        }

        public String getSendLimitCurrency() {
            if (_sendLimitAmount != null) {
                return _sendLimitAmount.getCurrencyUnit().getCurrencyCode();
            } else {
                return null;
            }
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
