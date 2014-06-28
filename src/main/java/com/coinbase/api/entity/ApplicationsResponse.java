package com.coinbase.api.entity;

import java.util.List;

public class ApplicationsResponse extends Response {
    private List<Application> _applications;

    public List<Application> getApplications() {
        return _applications;
    }

    public void setApplications(List<Application> applications) {
        _applications = applications;
    }
}
