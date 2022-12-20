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

        String data[] = authorization.split(" "); // Basic
        if (!data[0].equals("Basic")) {
            throw new Exception("invalid authorization type (type=" + data[0] + ")");
        }

        BasicToken basicToken = BasicToken.of(data[1]);
        Token token = authService.allocateToken(basicToken.getId(), basicToken.getPassword());

        return ResponseEntity.ok(token);
    }

    @PostMapping(ApiPath.Token.refreshToken)
    public ResponseEntity<Token> refreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) throws Exception {

        String data[] = authorization.split(" "); // Bearer
        if (!data[0].equals("Bearer")) {
            throw new Exception("invalid authorization type (type=" + data[0] + ")");
        }

        String refreshToken = data[1];
        Token token = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(token);
    }

    @GetMapping(ApiPath.Token.verify)
    public ResponseEntity<Void> verify(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization) throws Exception {

        String data[] = authorization.split(" "); // Bearer
        if (!data[0].equals("Bearer")) {
            throw new Exception("invalid authorization type (type=" + data[0] + ")");
        }

        boolean result = false;

        try {
            result = authService.verifyToken(data[1]);
        } catch (ExpiredJwtException e) {
            throw new Exception("expired token");
        }

        if (!result) {
            throw new Exception("invalid token");
        }

        return ResponseEntity
                .ok()
                .build();
    }


}
