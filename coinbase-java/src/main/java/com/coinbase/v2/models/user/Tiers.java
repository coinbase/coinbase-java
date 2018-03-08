package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Tiers {

    @SerializedName("upgrade_button_text")
    @Expose
    private String upgradeButtonText;
    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("completed_description")
    @Expose
    private String completedDescription;

    /**
     *
     * @return
     *     The upgrade button text
     */
    public String getUpgradeButtonText() {
        return upgradeButtonText;
    }

    /**
     *
     * @param upgradeButtonText
     *     The upgrade button text
     */
    public void setUpgradeButtonText(String upgradeButtonText) {
        this.upgradeButtonText = upgradeButtonText;
    }

    /**
     *
     * @return
     *     The header
     */
    public String getHeader() {
        return header;
    }

    /**
     *
     * @param header
     *     The header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     *
     * @return
     *     The body
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     *     The body
     */
    public void setBody(String body) {
        this.body = body;
    }
    /**
     *
     * @return
     *     The completedDescription
     */
    public String getCompletedDescription() {
        return completedDescription;
    }

    /**
     *
     * @param completedDescription
     *     The completed_description
     */
    public void setCompletedDescription(String completedDescription) {
        this.completedDescription = completedDescription;
    }

}
