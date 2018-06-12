
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    public enum Type {
        @SerializedName("ach_bank_account")
        ACH_BANK_ACCOUNT(),
        @SerializedName("credit_card")
        CREDIT_CARD(),
        @SerializedName("debit_card")
        DEBIT_CARD(),
        @SerializedName("secure3d_card")
        SECURE_3DS(),
        @SerializedName("worldpay_card")
        WORLDPAY_CARD(),
        @SerializedName("sepa_bank_account")
        SEPA_BANK_ACCOUNT(),
        @SerializedName("fiat_account")
        FIAT_ACCOUNT(),
        @SerializedName("ideal_bank_account")
        IDEAL_BANK(),
        @SerializedName("xfers_account")
        XFERS(),
        @SerializedName("bank_wire")
        BANK_WIRE(),
        @SerializedName("paypal_account")
        PAYPAL_ACCOUNT(),
        UNKNOWN();

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum VerificationMethod {
        @SerializedName("cdv")
        CDV(),
        @SerializedName("ach_setup_session")
        ACH_SETUP_SESSION(),
        UNKNOWN();

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum CDVStatus {
        @SerializedName("ready")
        READY(),
        @SerializedName("in_progress")
        IN_PROGRESS(),
        UNKNOWN();

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    @SerializedName("id")
    @Expose
    private String id;
    private Type type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("primary_buy")
    @Expose
    private Boolean primaryBuy;
    @SerializedName("primary_sell")
    @Expose
    private Boolean primarySell;
    @SerializedName("allow_buy")
    @Expose
    private Boolean allowBuy;
    @SerializedName("allow_sell")
    @Expose
    private Boolean allowSell;
    @SerializedName("allow_deposit")
    @Expose
    private Boolean allowDeposit;
    @SerializedName("allow_withdraw")
    @Expose
    private Boolean allowWithdraw;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("fiat_account")
    @Expose
    private FiatAccount fiatAccount;
    @SerializedName("limits")
    @Expose
    private Limits limits;
    @SerializedName("picker_data")
    @Expose
    private PickerData pickerData;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("recurring_options")
    @Expose
    private List<Object> recurringOptions = new ArrayList<Object>();

    @SerializedName("verification_method")
    @Expose
    private VerificationMethod verificationMethod;
    private CDVStatus cdvStatus;

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
     *     The type
     */
    public Type getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(Type type) {
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
     *     The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 
     * @param currency
     *     The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }


    /**
     *
     * @return
     *     The iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     *
     * @param iconUrl
     *     The iconUrl
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     *
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The primaryBuy
     */
    public Boolean getPrimaryBuy() {
        return primaryBuy;
    }

    /**
     * 
     * @param primaryBuy
     *     The primary_buy
     */
    public void setPrimaryBuy(Boolean primaryBuy) {
        this.primaryBuy = primaryBuy;
    }

    /**
     * 
     * @return
     *     The primarySell
     */
    public Boolean getPrimarySell() {
        return primarySell;
    }

    /**
     * 
     * @param primarySell
     *     The primary_sell
     */
    public void setPrimarySell(Boolean primarySell) {
        this.primarySell = primarySell;
    }

    /**
     * 
     * @return
     *     The allowBuy
     */
    public Boolean getAllowBuy() {
        return allowBuy;
    }

    /**
     * 
     * @param allowBuy
     *     The allow_buy
     */
    public void setAllowBuy(Boolean allowBuy) {
        this.allowBuy = allowBuy;
    }

    /**
     * 
     * @return
     *     The allowSell
     */
    public Boolean getAllowSell() {
        return allowSell;
    }

    /**
     * 
     * @param allowSell
     *     The allow_sell
     */
    public void setAllowSell(Boolean allowSell) {
        this.allowSell = allowSell;
    }

    /**
     *
     * @return
     *     The allowDeposit
     */
    public Boolean getAllowDeposit() {
        return allowDeposit;
    }

    /**
     *
     * @param allowDeposit
     *     The allow_deposit
     */
    public void setAllowDeposit(Boolean allowDeposit) {
        this.allowDeposit = allowDeposit;
    }

    /**
     *
     * @return
     *     The allowWithdraw
     */
    public Boolean getAllowWithdraw() {
        return allowWithdraw;
    }

    /**
     *
     * @param allowWithdraw
     *     The allow_withdraw
     */
    public void setAllowWithdraw(Boolean allowWithdraw) {
        this.allowWithdraw = allowWithdraw;
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
     *     The fiatAccount
     */
    public FiatAccount getFiatAccount() {
        return fiatAccount;
    }

    /**
     * 
     * @param fiatAccount
     *     The fiat_account
     */
    public void setFiatAccount(FiatAccount fiatAccount) {
        this.fiatAccount = fiatAccount;
    }

    /**
     *
     * @return
     *     The limits
     */
    public Limits getLimits() {
        return limits;
    }

    /**
     *
     * @param limits
     *     The limits
     */
    public void setLimits(Limits limits) {
        this.limits = limits;
    }

    /**
     *
     * @return
     *     The pickerData
     */
    public PickerData getPickerData() {
        return pickerData;
    }

    /**
     *
     * @param pickerData
     *     The picker_data
     */
    public void setPickerData(PickerData pickerData) {
        this.pickerData = pickerData;
    }


    /**
     * 
     * @return
     *     The verified
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * 
     * @param verified
     *     The verified
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * 
     * @return
     *     The recurringOptions
     */
    public List<Object> getRecurringOptions() {
        return recurringOptions;
    }

    /**
     * 
     * @param recurringOptions
     *     The recurring_options
     */
    public void setRecurringOptions(List<Object> recurringOptions) {
        this.recurringOptions = recurringOptions;
    }

    /**
     * 
     * @return
     *     The verificationMethod
     */
    public VerificationMethod getVerificationMethod() {
        return verificationMethod;
    }

    /**
     * 
     * @param verificationMethod
     *     The verificationMethod
     */
    public void setVerificationMethod(VerificationMethod verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    /**
     * 
     * @return
     *     The cdvStatus
     */
    public CDVStatus getCdvStatus() {
        return cdvStatus;
    }

    /**
     * 
     * @param cdvStatus
     *     The cdv_status
     */
    public void setCdvStatus(CDVStatus cdvStatus) {
        this.cdvStatus = cdvStatus;
    }
}
