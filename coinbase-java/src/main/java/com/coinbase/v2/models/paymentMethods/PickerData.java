
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
     *     The institution_name
     */
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

}
