
package com.coinbase.v2.models.paymentMethods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    public enum Type {
        ACH_BANK_ACCOUNT("ach_bank_account"),
        CREDIT_CARD("credit_card"),
        DEBIT_CARD("debit_card"),
        SEPA_BANK_ACCOUNT("sepa_bank_account"),
        FIAT_ACCOUNT("fiat_account"),
        BANK_WIRE("bank_wire"),
        BANK_ACCOUNT("bank_account"),
        COINBASE_ACCOUNT("coinbase_account"),
        COINBASE_FIAT_ACCOUNT("coinbase_fiat_account"),
        FUTURE_MERCHANT_PAYOUT("future_merchant_payout"),
        SEPA_PAYMENT_METHOD("sepa_payment_method"),
        PAYPAL_ACCOUNT("paypal_account");

        private String _value;

        private Type(String value) {
            this._value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return this._value;
        }

        @JsonCreator
        public static Type create(String val) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum VerificationMethod {
        CDV("cdv"),
        IAV("iav"),
        ACH_SETUP_SESSION("ach_setup_session");

        private String _value;

        private VerificationMethod(String value) {
            this._value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return this._value;
        }

        @JsonCreator
        public static Type create(String val) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum CDVStatus {
        READY("ready"),
        IN_PROGRESS("in_progress");

        private String _value;

        private CDVStatus(String value) {
            this._value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return this._value;
        }

        @JsonCreator
        public static Type create(String val) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum IAVStatus {
        READY("ready"),
        UNAVAILABLE("unavailable"),
        MFA_REQUIRED("mfa_required"),
        IN_PROGRESS("in_progress"),
        FAILED("failed");

        private String _value;

        private IAVStatus(String value) {
            this._value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return this._value;
        }

        @JsonCreator
        public static Type create(String val) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(val)) {
                    return type;
                }
            }
            return null;
        }
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("currency")
    @Expose
    private String currency;
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
    private Boolean allowDeposit;
    @SerializedName("allow_deposit")
    @Expose
    private Boolean allowWithdraw;
    @SerializedName("allow_withdraw")
    @Expose
    private Boolean allowSell;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("fiat_account")
    @Expose
    private FiatAccount fiatAccount;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("recurring_options")
    @Expose
    private List<Object> recurringOptions = new ArrayList<Object>();
    @SerializedName("verification_method")
    @Expose
    private String verificationMethod;
    @SerializedName("cdv_status")
    @Expose
    private String cdvStatus;
    @SerializedName("iav_status")
    @Expose
    private String iavStatus;
    @SerializedName("iav_fields")
    @Expose
    private List<IavField> iavFields = new ArrayList<IavField>();

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
    public String getVerificationMethod() {
        return verificationMethod;
    }

    /**
     * 
     * @param verificationMethod
     *     The verification_method
     */
    public void setVerificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    /**
     * 
     * @return
     *     The cdvStatus
     */
    public String getCdvStatus() {
        return cdvStatus;
    }

    /**
     * 
     * @param cdvStatus
     *     The cdv_status
     */
    public void setCdvStatus(String cdvStatus) {
        this.cdvStatus = cdvStatus;
    }

    /**
     * 
     * @return
     *     The iavStatus
     */
    public String getIavStatus() {
        return iavStatus;
    }

    /**
     * 
     * @param iavStatus
     *     The iav_status
     */
    public void setIavStatus(String iavStatus) {
        this.iavStatus = iavStatus;
    }

    /**
     * 
     * @return
     *     The iavFields
     */
    public List<IavField> getIavFields() {
        return iavFields;
    }

    /**
     * 
     * @param iavFields
     *     The iav_fields
     */
    public void setIavFields(List<IavField> iavFields) {
        this.iavFields = iavFields;
    }

}
