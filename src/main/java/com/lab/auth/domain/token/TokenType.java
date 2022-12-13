package com.lab.auth.domain.token;

import lombok.Getter;

@Getter
public enum TokenType {

    Access("access", 1000 * 60 * 60), // 1 hour
    Refresh("refresh", 1000 * 60 * 60 * 6) // 6 hour
    ;

    private String code;
    private Long expireIn;

    TokenType(String code, long expireIn) {
        this.code = code;
        this.expireIn = expireIn;
    }
}
