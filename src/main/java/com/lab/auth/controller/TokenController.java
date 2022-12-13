package com.lab.auth.controller;

import com.lab.auth.model.BasicToken;
import com.lab.auth.model.Token;
import com.lab.auth.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final AuthService authService;

    @PostMapping(ApiPath.Token.getToken)
    public ResponseEntity<Token> getToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) throws Exception {

        String data[] = authorization.split(" "); // basic

        BasicToken basicToken = BasicToken.of(data[1]);
        Token token = null;

        token = authService.allocateToken(basicToken.getId(), basicToken.getPassword());

        return ResponseEntity.ok(token);
    }

    @GetMapping(ApiPath.Token.verify)
    public ResponseEntity<Void> verify(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) throws Exception {

        String data[] = authorization.split(" "); // Bearer

        boolean result = false;

        try {
            result = authService.verifyToken(data[1]);
        } catch (ExpiredJwtException e) {
            throw new Exception("expired token");
        }

        if( !result ) {
            throw new Exception("invalid token");
        }

        return ResponseEntity
                .ok()
                .build();
    }


}
