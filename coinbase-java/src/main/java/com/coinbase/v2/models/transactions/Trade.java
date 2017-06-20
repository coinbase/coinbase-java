package com.coinbase.v2.models.transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Trade {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_method")
    @Expose
    private PaymentMethod paymentMethod;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;
    @SerializedName("fees")
    @Expose
    private List<Fee> fees = new ArrayList<Fee>();
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
    @SerializedName("fee")
    @Expose
    private Fee fee;
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("total")
    @Expose
    private Amount total;
    @SerializedName("subtotal")
    @Expose
    private Amount subtotal;
    @SerializedName("committed")
    @Expose
    private Boolean committed;
    @SerializedName("payout_at")
    @Expose
    private String payoutAt;
    @SerializedName("instant")
    @Expose
    private Boolean instant;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The paymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod The payment_method
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return The transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * @param transaction The transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * @return The fees
     */
    public List<Fee> getFees() {
        return fees;
    }

    /**
     * @param fees The fees
     */
    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource The resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @return The resourcePath
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @param resourcePath The resource_path
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * @return The amount
     */
    public Amount getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     * @return The total
     */
    public Amount getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Amount total) {
        this.total = total;
    }

    /**
     * @return The subtotal
     */
    public Amount getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal The subtotal
     */
    public void setSubtotal(Amount subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return The committed
     */
    public Boolean getCommitted() {
        return committed;
    }

    /**
     * @param committed The committed
     */
    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

    /**
     * @return The payoutAt
     */
    public String getPayoutAt() {
        return payoutAt;
    }

    /**
     * @param payoutAt The payout_at
     */
    public void setPayoutAt(String payoutAt) {
        this.payoutAt = payoutAt;
    }

    /**
     * @return The instant
     */
    public Boolean getInstant() {
        return instant;
    }

    /**
     * @param instant The instant
     */
    public void setInstant(Boolean instant) {
        this.instant = instant;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }
}