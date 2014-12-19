package com.coinbase.api.entity;

import java.io.Serializable;

import com.coinbase.api.deserializer.ErrorsCollector;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Response implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3392031826211907894L;
    private Boolean _success;
    private String _error;
    private String _errors;
    private int _totalCount;
    private int _numPages;
    private int _currentPage;
    
    public void setError(String error) {
        _error = error;
    }

    public String getErrors() {
	if (_error != null) {
	    if (_errors != null) {
		return _error + ", " + _errors;
	    } else {
		return _error;
	    }
	} else {
	    return _errors;
	}
    }

    @JsonDeserialize(converter=ErrorsCollector.class)
    public void setErrors(String errors) {
        _errors = errors;
    }

    public int getTotalCount() {
        return _totalCount;
    }

    public void setTotalCount(int totalCount) {
        _totalCount = totalCount;
    }

    public int getNumPages() {
        return _numPages;
    }

    public void setNumPages(int numPages) {
        _numPages = numPages;
    }

    public int getCurrentPage() {
        return _currentPage;
    }

    public void setCurrentPage(int currentPage) {
        _currentPage = currentPage;
    }

    public Boolean isSuccess() {
        return _success;
    }

    public void setSuccess(Boolean success) {
        this._success = success;
    }

    public boolean hasErrors() {
	return _error != null || _errors != null;
    }

}
