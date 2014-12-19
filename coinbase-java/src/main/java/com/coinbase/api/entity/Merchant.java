package com.coinbase.api.entity;

import java.io.Serializable;

public class Merchant implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2561008208513493704L;
    
    private String _companyName;
    private Logo _logo;
    
    public static class Logo implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1227765058838128026L;
        
        private String url;
        private String medium;
        private String small;
        
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getMedium() {
            return medium;
        }
        public void setMedium(String medium) {
            this.medium = medium;
        }
        public String getSmall() {
            return small;
        }
        public void setSmall(String small) {
            this.small = small;
        }
    }
    
    public String getCompanyName() {
        return _companyName;
    }
    public void setCompanyName(String companyName) {
        _companyName = companyName;
    }
    public Logo getLogo() {
        return _logo;
    }
    public void setLogo(Logo logo) {
        _logo = logo;
    }
}
