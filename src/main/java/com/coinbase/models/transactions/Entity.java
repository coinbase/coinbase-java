package com.coinbase.models.transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entity {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private Object username;
    @SerializedName("profile_location")
    @Expose
    private Object profileLocation;
    @SerializedName("profile_bio")
    @Expose
    private Object profileBio;
    @SerializedName("profile_url")
    @Expose
    private Object profileUrl;
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
    @SerializedName("address")
    @Expose
    private String address;

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
    public Object getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(Object username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The profileLocation
     */
    public Object getProfileLocation() {
        return profileLocation;
    }

    /**
     *
     * @param profileLocation
     * The profile_location
     */
    public void setProfileLocation(Object profileLocation) {
        this.profileLocation = profileLocation;
    }

    /**
     *
     * @return
     * The profileBio
     */
    public Object getProfileBio() {
        return profileBio;
    }

    /**
     *
     * @param profileBio
     * The profile_bio
     */
    public void setProfileBio(Object profileBio) {
        this.profileBio = profileBio;
    }

    /**
     *
     * @return
     * The profileUrl
     */
    public Object getProfileUrl() {
        return profileUrl;
    }

    /**
     *
     * @param profileUrl
     * The profile_url
     */
    public void setProfileUrl(Object profileUrl) {
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
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address= address;
    }

}
