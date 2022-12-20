package com.lab.auth.controller;

public interface ApiPath {

    String basePath = "/auth/v1.0";

    public interface Token {
        String basePath = ApiPath.basePath + "/token";
        String getToken = basePath;
        String refreshToken = basePath+"/refresh";
        String verify = basePath + "/verify";
    }
}
