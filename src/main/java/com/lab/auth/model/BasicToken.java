package com.lab.auth.model;

import lombok.Getter;

import java.util.Base64;

@Getter
public class BasicToken {

    private String id;
    private String password;

    private BasicToken(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public static BasicToken of(String credential) {

        String id;
        String password;

        String plain = new String(Base64.getDecoder().decode(credential));
        String data[] = plain.split(":");
        id = data[0];
        password = data[1];

        return new BasicToken(id, password);
    }
}
