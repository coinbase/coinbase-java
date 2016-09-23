
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Limits {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("buy")
    @Expose
    private List<Limit> buy = new ArrayList<Limit>();
    @SerializedName("instant_buy")
    @Expose
    private List<Limit> instantBuy = new ArrayList<Limit>();
    @SerializedName("sell")
    @Expose
    private List<Limit> sell = new ArrayList<Limit>();
    @SerializedName("deposit")
    @Expose
    private List<Limit> deposit = new ArrayList<Limit>();

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The buy
     */
    public List<Limit> getBuy() {
        return buy;
    }

    /**
     * 
     * @param buy
     *     The buy
     */
    public void setBuy(List<Limit> buy) {
        this.buy = buy;
    }

    /**
     * 
     * @return
     *     The instantBuy
     */
    public List<Limit> getInstantBuy() {
        return instantBuy;
    }

    /**
     * 
     * @param instantBuy
     *     The instant_buy
     */
    public void setInstantBuy(List<Limit> instantBuy) {
        this.instantBuy = instantBuy;
    }

    /**
     * 
     * @return
     *     The sell
     */
    public List<Limit> getSell() {
        return sell;
    }

    /**
     * 
     * @param sell
     *     The sell
     */
    public void setSell(List<Limit> sell) {
        this.sell = sell;
    }

    /**
     * 
     * @return
     *     The deposit
     */
    public List<Limit> getDeposit() {
        return deposit;
    }

    /**
     * 
     * @param deposit
     *     The deposit
     */
    public void setDeposit(List<Limit> deposit) {
        this.deposit = deposit;
    }

}
