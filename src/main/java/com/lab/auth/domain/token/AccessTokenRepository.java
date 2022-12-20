package com.lab.auth.domain.token;

import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, String> {

}
