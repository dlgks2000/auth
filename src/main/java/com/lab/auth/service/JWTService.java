package com.lab.auth.service;

import com.lab.auth.domain.token.*;
import com.lab.auth.domain.user.User;
import com.lab.auth.domain.user.UserService;
import com.lab.auth.model.Token;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JWTService {
    public static final String CLAIM_TOKEN_TYPE = "tokenType";
    private String secret = "qwlkkdfopwmdokfmqwejtlaskdjfi";
    private final byte[] secretKey = Base64.getEncoder().encode(secret.getBytes());

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public Token allocateToken(String userId) {

        // user 검증
        User user = userService.findUser(userId);

        String accessToken = makeJwt(userId, TokenType.Access);
        String refreshToken = makeJwt(userId, TokenType.Refresh);

        accessTokenRepository.save(AccessTokenEntity.builder()
                .id(user.getId())
                .token(accessToken)
                .expired(Integer.valueOf((TokenType.Access.getExpireIn() / 1000) + ""))
                .build());

        refreshTokenRepository.save(RefreshTokenEntity.builder()
                .id(user.getId())
                .token(refreshToken)
                .expired(Integer.valueOf((TokenType.Refresh.getExpireIn() / 1000) + ""))
                .build());

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("bearer")
                .expiresIn(60 * 60l)
                .build();
    }

    public boolean verifyToken(String jwt) throws ExpiredJwtException {

        if (jwt == null || jwt.isEmpty()) {
            return false;
        }

        try {
            Claims claims = extractClaims(jwt);

            String tokenType = (String) claims.get(CLAIM_TOKEN_TYPE);
            if (!tokenType.equals(TokenType.Access.getCode())) {
                throw new Exception("invalid token type (tokenType=" + tokenType + ")");
            }

            AccessTokenEntity accessTokenEntity = accessTokenRepository.findById(claims.getSubject()).orElseThrow( ()->
                    new Exception("token not found")
            );

            if( ! accessTokenEntity.getToken().equals(jwt) ) {
                throw new Exception("invalid token");
            }

        } catch (ExpiredJwtException e) {
            throw e;
        } catch (RuntimeException e) {
            return false;
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public String makeJwt(String id, TokenType tokenType) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + tokenType.getExpireIn());
        Key signingKey = new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(JwsHeader.TYPE, Header.JWT_TYPE);
        headerMap.put(JwsHeader.ALGORITHM, signatureAlgorithm.getValue());

        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, id);
        claims.put(CLAIM_TOKEN_TYPE, tokenType.getCode());

        JwtBuilder builder = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public Claims extractClaims(String jwt) throws RuntimeException {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims;
    }

    public JSONObject getPayload(String jwt) {

        String[] data = jwt.split("\\."); //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
        String base64Payload = data[1];
        String payload = new String(Base64.getDecoder().decode(base64Payload));
        JSONObject result = new JSONObject(payload);
        return result;
    }

    public Token refreshToken(String jwt) throws Exception {
        // valid refreshToken
        Claims claims = extractClaims(jwt);
        String tokenType = (String) claims.get(CLAIM_TOKEN_TYPE);
        if (!tokenType.equals(TokenType.Refresh.getCode())) {
            throw new Exception("invalid token type (tokenType=" + tokenType + ")");
        }

        // check userInfo
        String userId = claims.getSubject();
        User user = userService.findUser(userId);
        if (user == null) {
            throw new Exception("not found user (userId=" + userId + ")");
        }

        // check refreshToken
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(userId).orElseThrow(()->
                new Exception("token not found"));

        if( !refreshTokenEntity.getToken().equals(jwt)) {
            throw new Exception("invalid token");
        }

        // create or update token
        Token token = allocateToken(user.getId());

        return token;
    }
}
