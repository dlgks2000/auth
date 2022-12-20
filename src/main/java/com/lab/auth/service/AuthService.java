package com.lab.auth.service;

import com.lab.auth.domain.user.User;
import com.lab.auth.domain.user.UserService;
import com.lab.auth.model.Token;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTService jwtService;
    private final UserService userService;

    public Token allocateToken(String userId, String password) throws Exception {

        // user 검증
        User user = userService.findUser(userId);

        if( user == null ) {
            throw new Exception("user not found");
        }

        if( !user.getPassword().equals(password)) {
            throw new Exception("user not found");
        }

        return jwtService.allocateToken(userId);
    }

    public Token refreshToken(String refreshToken) throws Exception {

        return jwtService.refreshToken(refreshToken);
    }

    public boolean verifyToken(String jwt) throws ExpiredJwtException {

        return jwtService.verifyToken(jwt);
    }

}
