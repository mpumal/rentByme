package com.oriental.service.model;

import lombok.Data;

@Data
public class APIResponse {

    private int status;
    private String message;
    private String parameter;
    private AccessToken accessTokens;

    public APIResponse() {
    }

    public APIResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public APIResponse(int status, String message, String parameter) {
        this.status = status;
        this.message = message;
        this.parameter = parameter;
    }

    public APIResponse(int status, String message, AccessToken accessTokens) {
        this.status = status;
        this.message = message;
        this.accessTokens = accessTokens;
    }

    public APIResponse(int status, String message, String parameter, AccessToken accessTokens) {
        this.status = status;
        this.message = message;
        this.parameter = parameter;
        this.accessTokens = accessTokens;
    }
}
