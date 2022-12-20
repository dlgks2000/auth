package com.lab.auth.domain.token;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "access")
public class AccessTokenEntity {

    @Id
    private String id;
    @Indexed
    private String token;
    @TimeToLive
    private int expired;

    @Builder
    public AccessTokenEntity(String id, String token, int expired) {
        this.id = id;
        this.token = token;
        this.expired = expired;
    }
}
