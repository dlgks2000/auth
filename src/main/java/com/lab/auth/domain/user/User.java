package com.lab.auth.domain.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Setter( value = AccessLevel.NONE )
    private String id;
    private String password;

}
