
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class PickerData {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("card_last4")
    @Expose
    private String cardLast4;
    @SerializedName("card_network")
    @Expose
    private String cardNetwork;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("institution_name")
    @Expose
    private String institutionName;
    @SerializedName("institution_code")
    @Expose
    private String institutionCode;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("swift")
    @Expose
    private String swift;
    @SerializedName("paypal_email")
    @Expose
    private String paypalEmail;
    @SerializedName("paypal_owner")
    @Expose
    private String paypalOwner;

    /**
     * 
     * @return
     *     The symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 
     * @param symbol
     *     The symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * 
     * @return
     *     The cardLast4
     */
    public String getCardLast4() {
        return cardLast4;
    }

    /**
     * 
     * @param cardLast4
     *     The card_last4
     */
    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    /**
     * 
     * @return
     *     The cardNetwork
     */
    public String getCardNetwork() {
        return cardNetwork;
    }

    /**
     * 
     * @param cardNetwork
     *     The card_network
     */
    public void setCardNetwork(String cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    /**
     * 
     * @return
     *     The cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * 
     * @param cardType
     *     The card_type
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     *
     * @return
     *     The institutionName
     */
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     *
     * @param institutionName
     *     The institutionName
     */
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    /**
     *
     * @return
     *     The institutionCode
     */
    public String getInstitutionCode() {
        return institutionCode;
    }

    /**
     *
     * @param institutionCode
     *     The institutionCode
     */
    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    /**
     *
     * @return
     *     The accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     *
     * @param accountName
     *     The accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 
     * @return
     *     The accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * 
     * @param accountNumber
     *     The accountNumber
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     *
     * @return
     *     The accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     *
     * @param accountType
     *     The accountType
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     *
     * @return
     *     The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @param customerName
     *     The customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @return
     *     The swift
     */
    public String getSwift() {
        return swift;
    }

    /**
     *
     * @param swift
     *     The swift
     */
    public void setSwift(String swift) {
        this.swift = swift;
    }

    /**
     *
     * @return
     *      The paypalEmail
     */
    public String getPaypalEmail() {
        return paypalEmail;
    }

    /**
     *
     * @param paypalEmail
     *      The paypalEmail
     */
    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    /**
     *
     * @return
     *     The paypalOwner
     */
    public String getPaypalOwner() {
        return paypalOwner;
    }

    /**
     *
     * @param paypalOwner
     *      Thie paypalOwner
     */
    public void setPaypalOwner(String paypalOwner) {
        this.paypalOwner = paypalOwner;
    }
}
