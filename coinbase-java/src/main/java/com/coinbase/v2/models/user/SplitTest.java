package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class SplitTest {

    @SerializedName("test")
    @Expose
    private String test;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("is_tracked")
    @Expose
    private Boolean isTracked;

    /**
     *
     * @return
     *     The test
     */
    public String getTest() {
        return test;
    }

    /**
     *
     * @param test
     *     The test
     */
    public void setTest(String test) {
        this.test = test;
    }

    /**
     *
     * @return
     *     The group
     */
    public String getGroup() {
        return group;
    }

    /**
     *
     * @param group
     *     The group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     *
     * @return
     *     The isTracked
     */
    public Boolean getIsTracked() {
        return isTracked;
    }

    /**
     *
     * @param isTracked
     *     The is_tracked
     */
    public void setIsTracked(Boolean isTracked) {
        this.isTracked = isTracked;
    }

}
