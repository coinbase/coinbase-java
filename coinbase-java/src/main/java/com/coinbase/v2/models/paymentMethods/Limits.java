
package com.coinbase.v2.models.paymentMethods;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Limits {

    @SerializedName("limit_type")
    @Expose
    private String limitType;
    @SerializedName("buy")
    @Expose
    private List<Buy> buy = new ArrayList<Buy>();
    @SerializedName("instant_buy")
    @Expose
    private List<InstantBuy> instantBuy = new ArrayList<InstantBuy>();
    @SerializedName("sell")
    @Expose
    private List<Sell> sell = new ArrayList<Sell>();
    @SerializedName("deposit")
    @Expose
    private List<Deposit> deposit = new ArrayList<Deposit>();

    /**
     * 
     * @return
     *     The limitType
     */
    public String getLimitType() {
        return limitType;
    }

    /**
     * 
     * @param limitType
     *     The limit_type
     */
    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    /**
     * 
     * @return
     *     The buy
     */
    public List<Buy> getBuy() {
        return buy;
    }

    /**
     * 
     * @param buy
     *     The buy
     */
    public void setBuy(List<Buy> buy) {
        this.buy = buy;
    }

    /**
     * 
     * @return
     *     The instantBuy
     */
    public List<InstantBuy> getInstantBuy() {
        return instantBuy;
    }

    /**
     * 
     * @param instantBuy
     *     The instant_buy
     */
    public void setInstantBuy(List<InstantBuy> instantBuy) {
        this.instantBuy = instantBuy;
    }

    /**
     * 
     * @return
     *     The sell
     */
    public List<Sell> getSell() {
        return sell;
    }

    /**
     * 
     * @param sell
     *     The sell
     */
    public void setSell(List<Sell> sell) {
        this.sell = sell;
    }

    /**
     * 
     * @return
     *     The deposit
     */
    public List<Deposit> getDeposit() {
        return deposit;
    }

    /**
     * 
     * @param deposit
     *     The deposit
     */
    public void setDeposit(List<Deposit> deposit) {
        this.deposit = deposit;
    }

}
