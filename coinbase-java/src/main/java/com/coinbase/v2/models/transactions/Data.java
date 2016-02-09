package com.coinbase.v2.models.transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("native_amount")
    @Expose
    private NativeAmount nativeAmount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("resource_path")
    @Expose
    private String resourcePath;
    @SerializedName("network")
    @Expose
    private Network network;
    @SerializedName("to")
    @Expose
    private com.coinbase.v2.models.transactions.Entity to;
    @SerializedName("from")
    @Expose
    private com.coinbase.v2.models.transactions.Entity from;
    @SerializedName("instant_exchange")
    @Expose
    private Boolean instantExchange;
    @SerializedName("idem")
    @Expose
    private String idem;
    @SerializedName("buy")
    @Expose
    private Trade buy;
    @SerializedName("sell")
    @Expose
    private Trade sell;
    @SerializedName("delayed")
    @Expose
    private boolean delayed;
    @SerializedName("details")
    @Expose
    private Details details;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The amount
     */
    public Amount getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The nativeAmount
     */
    public NativeAmount getNativeAmount() {
        return nativeAmount;
    }

    /**
     *
     * @param nativeAmount
     * The native_amount
     */
    public void setNativeAmount(NativeAmount nativeAmount) {
        this.nativeAmount = nativeAmount;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The resource
     */
    public String getResource() {
        return resource;
    }

    /**
     *
     * @param resource
     * The resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     *
     * @return
     * The resourcePath
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     *
     * @param resourcePath
     * The resource_path
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     *
     * @return
     * The network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     *
     * @param network
     * The network
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     *
     * @return
     * The to
     */
    public com.coinbase.v2.models.transactions.Entity getTo() {
        return to;
    }

    /**
     *
     * @param to
     * The to
     */
    public void setTo(com.coinbase.v2.models.transactions.Entity to) {
        this.to = to;
    }

    /**
     *
     * @return
     * The from
     */
    public com.coinbase.v2.models.transactions.Entity getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(com.coinbase.v2.models.transactions.Entity from) {
        this.from = from;
    }

    /**
     *
     * @return
     * The instantExchange
     */
    public Boolean getInstantExchange() {
        return instantExchange;
    }

    /**
     *
     * @param instantExchange
     * The instant_exchange
     */
    public void setInstantExchange(Boolean instantExchange) {
        this.instantExchange = instantExchange;
    }

    /**
     *
     * @return
     * The idem
     */
    public String getIdem() {
        return idem;
    }

    /**
     *
     * @param idem
     * The idem
     */
    public void setIdem(String idem) {
        this.idem = idem;
    }

    /**
     *
     * @return
     * The buy
     */
    public Trade getBuy() {
        return buy;
    }

    /**
     *
     * @param buy
     * The buy
     */
    public void setBuy(Trade buy) {
        this.buy = buy;
    }

    /**
     *
     * @return
     * The sell
     */
    public Trade getSell() {
        return sell;
    }

    /**
     *
     * @param sell
     * The sell
     */
    public void setSell(Trade sell) {
        this.sell = sell;
    }

    /**
     *
     * @return
     * The delayed
     */
    public boolean getDelayed() {
        return delayed;
    }

    /**
     *
     * @param sell
     * The sell
     */
    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }


    /**
     *
     * @return
     * The details
     */
    public Details getDetails() {
        return details;
    }

    /**
     *
     * @param details
     * The details
     */
    public void setDetails(Details details) {
        this.details = details;
    }
}