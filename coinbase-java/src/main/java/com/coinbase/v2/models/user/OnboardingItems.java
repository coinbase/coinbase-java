package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OnboardingItems {

    @SerializedName("screen")
    @Expose
    private String screen;
    @SerializedName("required")
    @Expose
    private Boolean required;

    /**
     *
     * @return
     *     The name of the screen to show
     */
    public String getScreen() {
        return screen;
    }

    /**
     *
     * @param screen
     *     The name of the screen to show
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }

    /**
     *
     * @return
     *     {@code true} if this is required to show, else {@code false}
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     *
     * @param required
     *     {@code true} if this is required to show, else {@code false}
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }
}
