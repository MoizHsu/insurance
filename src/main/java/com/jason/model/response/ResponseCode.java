package com.jason.model.response;

public enum ResponseCode {
    // general response
    SUCCESS("1"),
    ERROR("0"),
    INTERNAL_ERROR("E500"),

    // model valiation
    DATA_INTEGRITY_ERROR("E001"),

    // query error
    QUERY_PARAMETER_ERROR("E4001"),

    // input error
    INPUT_PARAMETER_ERROR("E4002"),

    // token & security (6xxx)
    USER_NOT_EXIST("E6001"),
    USER_UNATHORIZED("E6002"),
    USER_IS_LOCKED("E6003");

    final String value;

    ResponseCode(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
