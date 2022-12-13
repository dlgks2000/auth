package com.lab.auth.service;

import com.lab.auth.domain.token.TokenType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTServiceTest {

    @Autowired
    JWTService service;

    @Test
    public void makeJwt(){

        String id = "user";
        TokenType tokenType = TokenType.Access;
        String jwt = service.makeJwt(id, tokenType);
        System.out.println(jwt);
        JSONObject payload = service.getPayload(jwt);
        System.out.println("payload: " + payload.toString());
    }
}