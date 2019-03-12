package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ReferralMoney {

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    @SerializedName("referral_threshold")
    @Expose
    private String referralThreshold;

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getReferralThreshold() {
        return referralThreshold;
    }
}
