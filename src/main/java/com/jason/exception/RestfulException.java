package com.jason.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jason.model.response.ResponseCode;

/**
 * self-defined exception
 *
 * @author jason
 */
public class RestfulException extends RuntimeException {
    private ResponseCode responseCode;

    @JsonIgnore
    private String[] responseCodeParams;

    public RestfulException() {
        super();
    }

    public RestfulException(ResponseCode responseCode, Throwable cause, String... params) {
        super(responseCode.get(), cause);
        this.responseCode = responseCode;
        this.responseCodeParams = params;
    }

    public RestfulException(ResponseCode responseCode, String... params) {
        super(responseCode.get());
        this.responseCode = responseCode;
        this.responseCodeParams = params;
    }

    public ResponseCode getResponseCode() {
        return this.responseCode;
    }

    public String[] getResponseCodeParams() {
        return this.responseCodeParams;
    }
}
