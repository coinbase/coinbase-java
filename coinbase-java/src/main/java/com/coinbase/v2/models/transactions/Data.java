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
    @SerializedName("delayed_send_date")
    @Expose
    private String delayedSendDate;
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
    @SerializedName("idem")
    @Expose
    private String idem;
    @SerializedName("to")
    @Expose
    private Entity to;
    @SerializedName("from")
    @Expose
    private Entity from;
    @SerializedName("buy")
    @Expose
    private Trade buy;
    @SerializedName("sell")
    @Expose
    private Trade sell;
    @SerializedName("request")
    @Expose
    private Trade request;
    @SerializedName("transfer")
    @Expose
    private Trade transfer;
    @SerializedName("send")
    @Expose
    private Trade send;
    @SerializedName("fiat_deposit")
    @Expose
    private Trade fiatDeposit;
    @SerializedName("fiat_withdrawal")
    @Expose
    private Trade fiatWithdrawal;
    @SerializedName("exchange_deposit")
    @Expose
    private Trade exchangeDeposit;
    @SerializedName("exchange_withdrawal")
    @Expose
    private Trade exchangeWithdrawal;
    @SerializedName("vault_withdrawal")
    @Expose
    private Trade vaultWithdrawal;
    @SerializedName("instant_exchange")
    @Expose
    private Boolean instantExchange;
    @SerializedName("delayed")
    @Expose
    private Boolean delayed;
    @SerializedName("details")
    @Expose
    private Details details;
    @SerializedName("image")
    @Expose
    private Image image;

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
     * The delayedSendDate
     */
    public String getDelayedSendDate() {
        return delayedSendDate;
    }

    /**
     *
     * @param delayedSendDate
     * The delayedSendDate
     */
    public void setDelayedSendDate(String delayedSendDate) {
        this.delayedSendDate = delayedSendDate;
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
     * The to
     */
    public Entity getTo() {
        return to;
    }

    /**
     *
     * @param to
     * The to
     */
    public void setTo(Entity to) {
        this.to = to;
    }

    /**
     *
     * @return
     * The from
     */
    public Entity getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(Entity from) {
        this.from = from;
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
     * The request
     */
    public Trade getRequest() {
        return request;
    }

    /**
     *
     * @param request
     * The request
     */
    public void setRequest(Trade request) {
        this.request = request;
    }

    /**
     *
     * @return
     * The transfer
     */
    public Trade getTransfer() {
        return transfer;
    }

    /**
     *
     * @param transfer
     * The transfer
     */
    public void setTransfer(Trade transfer) {
        this.transfer = transfer;
    }

    /**
     *
     * @return
     * The send
     */
    public Trade getSend() {
        return send;
    }

    /**
     *
     * @param send
     * The send
     */
    public void setSend(Trade send) {
        this.send = send;
    }

    /**
     *
     * @return
     * The fiatWithdrawal
     */
    public Trade getFiatWithdrawal() {
        return fiatWithdrawal;
    }

    /**
     *
     * @param fiatWithdrawal
     * The fiat_withdrawal
     */
    public void setFiatWithdrawal(Trade fiatWithdrawal) {
        this.fiatWithdrawal = fiatWithdrawal;
    }

    /**
     *
     * @return
     * The fiatDeposit
     */
    public Trade getFiatDeposit() {
        return fiatDeposit;
    }

    /**
     *
     * @param fiatDeposit
     * The fiat_deposit
     */
    public void setFiatDeposit(Trade fiatDeposit) {
        this.fiatDeposit = fiatDeposit;
    }

    /**
     *
     * @return
     * The exchangeDeposit
     */
    public Trade getExchangeDeposit() {
        return exchangeDeposit;
    }

    /**
     *
     * @param exchangeDeposit
     * The exchange_deposit
     */
    public void setExchangeDeposit(Trade exchangeDeposit) {
        this.exchangeDeposit = exchangeDeposit;
    }

    /**
     *
     * @return
     * The exchangeWithdrawal
     */
    public Trade getExchangeWithdrawal() {
        return exchangeWithdrawal;
    }

    /**
     *
     * @param exchangeWithdrawal
     * The exchange_withdrawal
     */
    public void setExchangeWithdrawal(Trade exchangeWithdrawal) {
        this.exchangeWithdrawal = exchangeWithdrawal;
    }

    /**
     *
     * @return
     * The vaultWithdrawal
     */
    public Trade getVaultWithdrawal() {
        return vaultWithdrawal;
    }

    /**
     *
     * @param vaultWithdrawal
     * The vault_withdrawal
     */
    public void setVaultWithdrawal(Trade vaultWithdrawal) {
        this.vaultWithdrawal = vaultWithdrawal;
    }

    /**
     *
     * @return
     * The delayed
     */
    public Boolean getDelayed() {
        return delayed;
    }

    /**
     *
     * @param delayed
     * The delayed
     */
    public void setDelayed(Boolean delayed) {
        this.delayed = delayed;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}