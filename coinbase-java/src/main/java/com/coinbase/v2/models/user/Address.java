
package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Address {

    @SerializedName("line1")
    @Expose
    private String line1;
    @SerializedName("line2")
    @Expose
    private String line2;
    @SerializedName("line3")
    @Expose
    private Object line3;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("country")
    @Expose
    private Country country;

    /**
     * 
     * @return
     *     The line1
     */
    public String getLine1() {
        return line1;
    }

    /**
     * 
     * @param line1
     *     The line1
     */
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
     * 
     * @return
     *     The line2
     */
    public String getLine2() {
        return line2;
    }

    /**
     * 
     * @param line2
     *     The line2
     */
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    /**
     * 
     * @return
     *     The line3
     */
    public Object getLine3() {
        return line3;
    }

    /**
     * 
     * @param line3
     *     The line3
     */
    public void setLine3(Object line3) {
        this.line3 = line3;
    }

    /**
     * 
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 
     * @param postalCode
     *     The postal_code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * 
     * @return
     *     The country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

}
