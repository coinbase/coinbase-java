package com.coinbase.v1.entity;

public class ApplicationResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -8421060498323905062L;
    private Application _application;

    public Application getApplication() {
        return _application;
    }

    public void setApplication(Application application) {
        _application = application;
    }
}
