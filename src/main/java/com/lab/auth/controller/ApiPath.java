package com.lab.auth.controller;

public interface ApiPath {

    String BasePath = "/auth/v1.0";

    public interface Token {
        String getToken = BasePath + "/token";
        String verify = BasePath + "/verify";
    }
}
