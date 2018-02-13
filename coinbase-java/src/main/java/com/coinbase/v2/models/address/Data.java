
package com.coinbase.v2.models.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Data {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("callback_url")
    @Expose
    private String callbackUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("legacy_address")
    @Expose
    private String legacyAddress;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("resource_path")
    @Expose
    private String resourcePath;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("uri_scheme")
    @Expose
    private String uriScheme;
    @SerializedName("warning_details")
    @Expose
    private String warningDetails;
    @SerializedName("warning_title")
    @Expose
    private String warningTitle;

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     *     The callbackUrl
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     *
     * @param callbackUrl
     *     The callbackUrl
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
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

     *     The legacy_address
     */
    public String getLegacyAddress() {
        return legacyAddress;
    }

    /**
     *
     * @param legacyAddress
     *     The legacyAddress
     */
    public void setLegacyAddress(String legacyAddress) {
        this.legacyAddress = legacyAddress;
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
     * @return network
     *     The network
     */
    public String getNetwork() {
        return network;
    }

    /**
     *
     * @param network
     *     The network
     */
    public void setNetwork(String network) {
        this.network = network;
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
     * @return  uri_scheme
     *     The uriScheme
     */
    public String getUriScheme() {
        return uriScheme;
    }

    /**
     *
     * @param uriScheme
     *     The uriScheme
     */
    public void setUriScheme(String uriScheme) {
        this.uriScheme = uriScheme;
    }

    /**
     *
     * @return warning_details
     *     The warningDetails
     */
    public String getWarningDetails() {
        return warningDetails;
    }

    /**
     *
     * @param warningDetails
     *     The warningDetails
     */
    public void setWarningDetails(String warningDetails) {
        this.warningDetails = warningDetails;
    }

    /**
     *
     * @return warningTitle
     *     The warningTitle
     */
    public String getWarningTitle() {
        return warningTitle;
    }

    /**
     *
     * @param warningTitle
     *     The warningTitle
     */
    public void setWarningTitle(String warningTitle) {
        this.warningTitle = warningTitle;
    }
}
