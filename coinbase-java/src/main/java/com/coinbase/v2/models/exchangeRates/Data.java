
package com.coinbase.v2.models.exchangeRates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("rates")
    @Expose
    private Map<String, String> rates;

    /**
     * 
     * @return
     *     The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 
     * @param currency
     *     The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 
     * @return
     *     The rates
     */
    public Map<String, String> getRates() {
        return rates;
    }

    /**
     * 
     * @param rates
     *     The rates
     */
    public void setRates(Map<String, String> rates) {
        this.rates = rates;
    }

}
