package com.lab.auth.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}
