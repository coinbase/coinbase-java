package com.coinbase.api.entity;

import java.io.Serializable;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.coinbase.api.deserializer.MoneyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class User implements Serializable {

    private static final long serialVersionUID = 7772234749971786319L;
    private String _id;
    private String _name;
    private String _email;
    private Boolean _email_verified;
    private String _timeZone;
    private CurrencyUnit _nativeCurrency;
    private Money _balance;
    private Integer _buyLevel;
    private Integer _sellLevel;
    private Money _buyLimit;
    private Money _sellLimit;
    private String _password;
    private String _referrerId;
    private String _receiveAddress;
    private Merchant _merchant;
    private String _avatarUrl;
    private String _username;
    private String _bitcoin_units;
    private String _location;
    private String _profile_description;
    private Boolean _accept_agreement;

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getReferrerId() {
        return _referrerId;
    }

    public void setReferrerId(String referrerId) {
        _referrerId = referrerId;
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

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public Boolean getEmailVerified() {
        return _email_verified;
    }

    public void setEmailVerified(Boolean email_verified) {
        this._email_verified = email_verified;
    }

    public String getTimeZone() {
        return _timeZone;
    }

    public void setTimeZone(String timeZone) {
        _timeZone = timeZone;
    }

    public CurrencyUnit getNativeCurrency() {
        return _nativeCurrency;
    }

    public void setNativeCurrency(CurrencyUnit nativeCurrency) {
        _nativeCurrency = nativeCurrency;
    }

    public Money getBalance() {
        return _balance;
    }

    @JsonDeserialize(using=MoneyDeserializer.class)
    public void setBalance(Money balance) {
        _balance = balance;
    }

    public Integer getBuyLevel() {
        return _buyLevel;
    }

    public void setBuyLevel(Integer buyLevel) {
        _buyLevel = buyLevel;
    }

    public Integer getSellLevel() {
        return _sellLevel;
    }

    public void setSellLevel(Integer sellLevel) {
        _sellLevel = sellLevel;
    }

    public Money getBuyLimit() {
        return _buyLimit;
    }

    @JsonDeserialize(using=MoneyDeserializer.class)
    public void setBuyLimit(Money buyLimit) {
        _buyLimit = buyLimit;
    }

    public Money getSellLimit() {
        return _sellLimit;
    }

    @JsonDeserialize(using=MoneyDeserializer.class)
    public void setSellLimit(Money sellLimit) {
        _sellLimit = sellLimit;
    }

    public String getReceiveAddress() {
        return _receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        _receiveAddress = receiveAddress;
    }

    public Merchant getMerchant() {
        return _merchant;
    }

    public void setMerchant(Merchant merchant) {
        _merchant = merchant;
    }

    public void setAvatarUrl(String avatarUrl) {
        this._avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return _avatarUrl;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getUsername() {
        return _username;
    }

    public String getBitcoinUnits() {
        return _bitcoin_units;
    }

    public void setBitcoinUnits(String bitcoin_units) {
        this._bitcoin_units = bitcoin_units;
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }

    public String getProfileDescription() {
        return _profile_description;
    }

    public void setProfileDescription(String profile_description) {
        this._profile_description = profile_description;
    }

    public Boolean getAcceptAgreement() {
        return _accept_agreement;
    }

    public void setAcceptAgreement(Boolean accept_agreement) {
        this._accept_agreement = accept_agreement;
    }
}
