
package com.coinbase.v2.models.transfers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

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
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("total")
    @Expose
    private Amount total;
    @SerializedName("subtotal")
    @Expose
    private Amount subtotal;
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
    @SerializedName("committed")
    @Expose
    private Boolean committed;
    @SerializedName("instant")
    @Expose
    private Boolean instant;
    @SerializedName("is_first_buy")
    @Expose
    private Boolean isFirstBuy;
    @SerializedName("fee")
    @Expose
    private Amount fee;
    @SerializedName("payout_at")
    @Expose
    private String payoutAt;
    @SerializedName("requires_completion_step")
    @Expose
    private Boolean requiresCompletionStep;
    @SerializedName("secure3d_verification")
    @Expose
    private Secure3dVerification secure3dVerification;
    @SerializedName("fee_explanation_url")
    @Expose
    private String feeExplanationUrl;
    @SerializedName("payment_method_fee")
    @Expose
    private Amount paymentMethodFee;
    @SerializedName("hold_days")
    @Expose
    private Integer holdDays;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The paymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 
     * @param paymentMethod
     *     The payment_method
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 
     * @return
     *     The transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * 
     * @param transaction
     *     The transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * 
     * @return
     *     The amount
     */
    public Amount getAmount() {
        return amount;
    }

    /**
     * 
     * @param amount
     *     The amount
     */
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     * 
     * @return
     *     The total
     */
    public Amount getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(Amount total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The subtotal
     */
    public Amount getSubtotal() {
        return subtotal;
    }

    /**
     * 
     * @param subtotal
     *     The subtotal
     */
    public void setSubtotal(Amount subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt
     *     The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 
     * @return
     *     The resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * 
     * @param resource
     *     The resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 
     * @return
     *     The resourcePath
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * 
     * @param resourcePath
     *     The resource_path
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * 
     * @return
     *     The committed
     */
    public Boolean getCommitted() {
        return committed;
    }

    /**
     * 
     * @param committed
     *     The committed
     */
    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

    /**
     * 
     * @return
     *     The instant
     */
    public Boolean getInstant() {
        return instant;
    }

    /**
     * 
     * @param instant
     *     The instant
     */
    public void setInstant(Boolean instant) {
        this.instant = instant;
    }

    /**
     *
     * @return
     *     The isFirstBuy
     */
    public Boolean getIsFirstBuy() {
        return isFirstBuy;
    }

    /**
     *
     * @param instant
     *     The isFirstBuy
     */
    public void setIsFirstBuy(Boolean isFirstBuy) {
        this.isFirstBuy = isFirstBuy;
    }

    /**
     * 
     * @return
     *     The fees
     */
    public Amount getFee() {
        return fee;
    }

    /**
     * 
     * @param fees
     *     The fees
     */
    public void setFee(Amount fee) {
        this.fee = fee;
    }

    /**
     * 
     * @return
     *     The payoutAt
     */
    public String getPayoutAt() {
        return payoutAt;
    }

    /**
     * 
     * @param payoutAt
     *     The payout_at
     */
    public void setPayoutAt(String payoutAt) {
        this.payoutAt = payoutAt;
    }

    /**
     *
     * @return
     *     The requiresCompletionStep
     */
    public Boolean getRequiresCompletionStep() {
        return requiresCompletionStep;
    }

    /**
     *
     * @param requiresCompletionStep
     *     The requires_completion_step
     */
    public void setRequiresCompletionStep(Boolean requiresCompletionStep) {
        this.requiresCompletionStep = requiresCompletionStep;
    }

    /**
     *
     * @return
     *     The secure3dVerification
     */
    public Secure3dVerification getSecure3dVerification() {
        return secure3dVerification;
    }

    /**
     *
     * @param secure3dVerification
     *     The secure3d_verification
     */
    public void setSecure3dVerification(Secure3dVerification secure3dVerification) {
        this.secure3dVerification = secure3dVerification;
    }

    /**
     *
     * @return
     *     The feeExplanationUrl
     */
    public String getFeeExplanationUrl() {
        return feeExplanationUrl;
    }

    /**
     *
     * @param feeExplanationUrl
     *     The feeExplanationUrl
     */
    public void setFeeExplanationUrl(String feeExplanationUrl) {
        this.feeExplanationUrl = feeExplanationUrl;
    }

    /**
     *
     * @return
     *     The paymentMethodFee
     */
    public Amount getPaymentMethodFee() {
        return paymentMethodFee;
    }

    /**
     *
     * @param paymentMethodFee
     *     The paymentMethodFee
     */
    public void setPaymentMethodFee(Amount paymentMethodFee) {
        this.paymentMethodFee = paymentMethodFee;
    }

    /**
     * @return
     *     The number of days the funds will be on hold before the customer can withdraw/send
     */
    public Integer getHoldDays() {
        return holdDays;
    }

    /**
     * @param hold days
     *     The hold days
     */
    public void setHoldDays(Integer holdDays) {
        this.holdDays = holdDays;
    }
}
