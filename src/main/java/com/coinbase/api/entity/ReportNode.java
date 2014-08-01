package com.coinbase.api.entity;

import java.io.Serializable;

public class ReportNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3996102881697459153L;
    private Report _Report;

    public Report getReport() {
        return _Report;
    }

    public void setReport(Report Report) {
        _Report = Report;
    }
}
