package com.coinbase.api.entity;

public class ReportResponse extends Response {
    private Report _report;

    public Report getReport() {
        return _report;
    }

    public void setReport(Report report) {
        _report = report;
    }
}
