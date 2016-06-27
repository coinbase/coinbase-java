package com.coinbase.v1.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public class PaymentMethod implements Serializable {

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

    /**
     *
     */
    private static final long serialVersionUID = -3574818318535801143L;
    private String _id;
    private String _name;
    private Boolean _allowBuy;
    private Boolean _allowSell;
    private Boolean _allowDeposit;
    private Boolean _allowWithdraw;
    private Boolean _primaryBuy;
    private Boolean _primarySell;
    private Account _account;
    private Account _fiatAccount;
    private String _currency;
    private Type _type;
    private Boolean _verified;
    private String _bankName;
    private String _iban;
    private String _swift;
    private VerificationMethod _verificationMethod;
    private CDVStatus _cdvStatus;
    private IAVStatus _iavStatus;
    private IAVField[] _iavFields;

    private String _uuid;

    public String getUuid() {
        return _uuid;
    }

    public void setUuid(String uuid) {
        this._uuid = uuid;
    }


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Boolean allowBuy() {
        return _allowBuy;
    }

    public void setAllowBuy(Boolean allowBuy) {
        _allowBuy = allowBuy;
    }

    public Boolean allowSell() {
        return _allowSell;
    }

    public void setAllowSell(Boolean allowSell) {
        _allowSell = allowSell;
    }

    public Boolean allowDeposit() {
        return _allowDeposit;
    }

    public void setAllowDeposit(Boolean allowDeposit) {
        _allowDeposit = allowDeposit;
    }

    public Boolean allowWithdraw() {
        return _allowWithdraw;
    }

    public void setAllowWithdraw(Boolean allowWithdraw) {
        _allowWithdraw = allowWithdraw;
    }

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public Account getFiatAccount() {
        return _fiatAccount;
    }

    public void setFiatAccount(Account account) {
        this._fiatAccount = account;
    }

    public String getCurrency() {
        return _currency;
    }

    public void setCurrency(String currency) {
        this._currency = currency;
    }

    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        this._type = type;
    }

    public Boolean getVerified() {
        return _verified;
    }

    public void setVerified(Boolean verified) {
        this._verified = verified;
    }

    public String getBankName() {
        return _bankName;
    }

    public void setBankName(String bankName) {
        this._bankName = bankName;
    }

    public String getIban() {
        return _iban;
    }

    public void setIban(String iban) {
        this._iban = iban;
    }

    public String getSwift() {
        return _swift;
    }

    public void setSwift(String swift) {
        this._swift = swift;
    }

    public Boolean getPrimaryBuy() {
        return _primaryBuy;
    }

    public void setPrimaryBuy(Boolean primaryBuy) {
        this._primaryBuy = primaryBuy;
    }

    public Boolean getPrimarySell() {
        return _primarySell;
    }

    public void setPrimarySell(Boolean primarySell) {
        this._primarySell = primarySell;
    }

    public VerificationMethod getVerificationMethod() {
        return _verificationMethod;
    }

    public void setVerificationMethod(VerificationMethod verificationMethod) {
        this._verificationMethod = verificationMethod;
    }

    public IAVStatus getIavStatus() {
        return _iavStatus;
    }

    public void setIavStatus(IAVStatus iavStatus) {
        this._iavStatus = iavStatus;
    }

    public CDVStatus getCdvStatus() {
        return _cdvStatus;
    }

    public void setCdvStatus(CDVStatus cdvStatus) {
        this._cdvStatus = cdvStatus;
    }

    public IAVField[] getIavFields() {
        return _iavFields;
    }

    public void setIavFields(IAVField[] iavFields) {
        this._iavFields = iavFields;
    }
}
