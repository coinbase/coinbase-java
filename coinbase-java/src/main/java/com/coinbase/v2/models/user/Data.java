package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("profile_location")
    @Expose
    private String profileLocation;
    @SerializedName("profile_bio")
    @Expose
    private String profileBio;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("resource_path")
    @Expose
    private String resourcePath;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("native_currency")
    @Expose
    private String nativeCurrency;
    @SerializedName("bitcoin_unit")
    @Expose
    private String bitcoinUnit;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("restrictions")
    @Expose
    private List<String> restrictions = new ArrayList<String>();
    @SerializedName("feature_flags")
    @Expose
    private List<String> featureFlags = new ArrayList<String>();
    @SerializedName("split_test_groups")
    @Expose
    private List<SplitTest> splitTestGroups = new ArrayList<SplitTest>();
    @SerializedName("admin_flags")
    @Expose
    private List<String> adminFlags = new ArrayList<String>();
    @SerializedName("onboarding_items")
    @Expose
    private List<OnboardingItem> onboardingItems = new ArrayList<OnboardingItem>();
    @SerializedName("personal_details")
    @Expose
    private PersonalDetails personalDetails;
    @SerializedName("tiers")
    @Expose
    private Tiers tiers;
    @SerializedName("merchant")
    @Expose
    private Object merchant;
    @SerializedName("oauth")
    @Expose
    private Oauth oauth;
    @SerializedName("referral_id")
    @Expose
    private Object referralId;
    @SerializedName("access_privacy_rights")
    @Expose
    private Boolean accessPrivacyRights;

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
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The profileLocation
     */
    public String getProfileLocation() {
        return profileLocation;
    }

    /**
     *
     * @param profileLocation
     * The profile_location
     */
    public void setProfileLocation(String profileLocation) {
        this.profileLocation = profileLocation;
    }

    /**
     *
     * @return
     * The profileBio
     */
    public String getProfileBio() {
        return profileBio;
    }

    /**
     *
     * @param profileBio
     * The profile_bio
     */
    public void setProfileBio(String profileBio) {
        this.profileBio = profileBio;
    }

    /**
     *
     * @return
     * The profileUrl
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     *
     * @param profileUrl
     * The profile_url
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    /**
     *
     * @return
     * The avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     *
     * @param avatarUrl
     * The avatar_url
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     *     The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     *
     * @param timeZone
     *     The time_zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     *
     * @return
     *     The nativeCurrency
     */
    public String getNativeCurrency() {
        return nativeCurrency;
    }

    /**
     *
     * @param nativeCurrency
     *     The native_currency
     */
    public void setNativeCurrency(String nativeCurrency) {
        this.nativeCurrency = nativeCurrency;
    }

    /**
     *
     * @return
     *     The bitcoinUnit
     */
    public String getBitcoinUnit() {
        return bitcoinUnit;
    }

    /**
     *
     * @param bitcoinUnit
     *     The bitcoin_unit
     */
    public void setBitcoinUnit(String bitcoinUnit) {
        this.bitcoinUnit = bitcoinUnit;
    }

    /**
     *
     * @return
     *     The country
     */
    public Country getCountry() {
        return country;
    }

    /**
     *
     * @param country
     *     The country
     */
    public void setCountry(Country country) {
        this.country = country;
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
     *     The restrictions
     */
    public List<String> getRestrictions() {
        return restrictions;
    }

    /**
     *
     * @param restrictions
     *     The restrictions
     */
    public void setRestrictions(List<String> restrictions) {
        this.restrictions = restrictions;
    }

    /**
     *
     * @return
     *     The featureFlags
     */
    public List<String> getFeatureFlags() {
        return featureFlags;
    }

    /**
     *
     * @param featureFlags
     *     The feature_flags
     */
    public void setFeatureFlags(List<String> featureFlags) {
        this.featureFlags = featureFlags;
    }

    /**
     *
     * @return
     *     The splitTestGroups
     */
    public List<SplitTest> getSplitTestGroups() {
        return splitTestGroups;
    }

    /**
     *
     * @param splitTestGroups
     *     The split_test_groups
     */
    public void setSplitTestGroups(List<SplitTest> splitTestGroups) {
        this.splitTestGroups = splitTestGroups;
    }

    /**
     *
     * @return
     *     The adminFlags
     */
    public List<String> getAdminFlags() {
        return adminFlags;
    }

    /**
     *
     * @param adminFlags
     *     The admin_flags
     */
    public void setAdminFlags(List<String> adminFlags) {
        this.adminFlags = adminFlags;
    }

    /**
     *
     * @return
     *     List of onboarding items
     */
    public List<OnboardingItem> getOnboardingItems() {
        return onboardingItems;
    }

    /**
     *
     * @param onboardingItems
     *     Set onboardingitems
     */
    public void setOnboardingItems(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    /**
     *
     * @return
     *     The personalDetails
     */
    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    /**
     *
     * @param personalDetails
     *     The personal_details
     */
    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    /**
     *
     * @return
     *     The tiers
     */
    public Tiers getTiers() {
        return tiers;
    }

    /**
     *
     * @param tiers
     *     The tiers
     */
    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    /**
     *
     * @return
     *     The merchant
     */
    public Object getMerchant() {
        return merchant;
    }

    /**
     *
     * @param merchant
     *     The merchant
     */
    public void setMerchant(Object merchant) {
        this.merchant = merchant;
    }

    /**
     *
     * @return
     * The oauth
     */
    public Oauth getOauth() {
        return oauth;
    }

    /**
     *
     * @param oauth
     * The oauth
     */
    public void setOauth(Oauth oauth) {
        this.oauth = oauth;
    }

    /**
     *
     * @return
     * The referralId
     */
    public Object getReferralId() {
        return referralId;
    }

    /**
     *
     * @param referralId
     * The referral_id
     */
    public void setReferralId(Object referralId) {
        this.referralId = referralId;
    }

    /**
     *
     * @return
     * {@code true} if user should see Privacy Rights and Email Settings;
     * {@code false} otherwise.
     */
    public Boolean getAccessPrivacyRights() {
        return accessPrivacyRights;
    }

    /**
     *
     * @param accessPrivacyRights
     * {@code true} if user should see Privacy Rights and Email Settings;
     * {@code false} otherwise.
     */
    public void setAccessPrivacyRights(Boolean accessPrivacyRights) {
        this.accessPrivacyRights = accessPrivacyRights;
    }
}