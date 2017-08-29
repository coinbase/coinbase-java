package com.coinbase.v2.models.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    public enum Type {
        @SerializedName("wallet")
        WALLET(),
        @SerializedName("vault")
        VAULT(),
        @SerializedName("multisig_vault")
        MULTISIG_VAULT(),
        @SerializedName("multisig")
        MULTISIG(),
        @SerializedName("fiat")
        FIAT(),
        UNKNOWN();

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static Type toType(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return UNKNOWN;
            }
        }
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("primary")
    @Expose
    private Boolean primary;
    private Type type;
    @SerializedName("currency")
    @Expose
    private Currency currency;
    @SerializedName("balance")
    @Expose
    private Balance balance;
    @SerializedName("native_balance")
    @Expose
    private NativeBalance nativeBalance;
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
    @SerializedName("ready")
    @Expose
    private Boolean ready;
    @SerializedName("active")
    @Expose
    private Boolean active;
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
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The primary
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     *
     * @param primary
     * The primary
     */
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    /**
     *
     * @return
     * The type
     */
    public Type getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     *
     * @return
     * The balance
     */
    public Balance getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     * The balance
     */
    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    /**
     *
     * @return
     * The nativeBalance
     */
    public NativeBalance getNativeBalance() {
        return nativeBalance;
    }

    /**
     *
     * @param nativeBalance
     * The native_balance
     */
    public void setNativeBalance(NativeBalance nativeBalance) {
        this.nativeBalance = nativeBalance;
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
     * The ready
     */
    public Boolean getReady() {
        return active;
    }

    /**
     *
     * @param ready
     * The ready
     */
    public void setReady(Boolean ready) {
        this.ready = ready;
    }


    /**
     *
     * @return
     * The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     *
     * @param active
     * The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}