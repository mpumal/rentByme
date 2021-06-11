package com.oriental.service.model;

import lombok.Data;

import java.util.List;

@Data
public class APIResponse {

    private int status;
    private String message;
    private String parameter;
    private Object object;
    private List<Object> objectList;

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

    public APIResponse(int status, String message, Object object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public APIResponse(int status, String message, List<Object> objectList) {
        this.status = status;
        this.message = message;
        this.objectList = objectList;
    }

    public APIResponse(int status, String message, String parameter, Object object) {
        this.status = status;
        this.message = message;
        this.parameter = parameter;
        this.object = object;
    }
}
