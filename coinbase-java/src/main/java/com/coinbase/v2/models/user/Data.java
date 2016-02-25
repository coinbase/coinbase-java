package com.coinbase.v2.models.user;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("state_verification_required")
    @Expose
    private Boolean stateVerificationRequired;
    @SerializedName("restricted_state")
    @Expose
    private Boolean restrictedState;

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
     * The stateVerificationRequired
     */
    public Boolean getStateVerificationRequired() {
        return stateVerificationRequired;
    }

    /**
     *
     * @param stateVerificationRequired
     * The state_verification_required
     */
    public void setStateVerificationRequired(Boolean stateVerificationRequired) {
        this.stateVerificationRequired = stateVerificationRequired;
    }

    /**
     *
     * @return
     * The restrictedState
     */
    public Boolean getRestrictedState() {
        return restrictedState;
    }

    /**
     *
     * @param restrictedState
     * The restricted_state
     */
    public void setRestrictedState(Boolean restrictedState) {
        this.restrictedState = restrictedState;
    }

}