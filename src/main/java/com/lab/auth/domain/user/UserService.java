package com.lab.auth.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDataHandler dataHandler;

    public User findUser( String id ){

        return dataHandler.findUser(id);
    }
}
