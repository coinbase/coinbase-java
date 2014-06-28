package com.coinbase.api.entity;

public class ApplicationResponse extends Response {
    private Application _application;

    public Application getApplication() {
        return _application;
    }

    public void setApplication(Application application) {
        _application = application;
    }
}
