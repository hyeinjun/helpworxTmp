package com.benit.helpworx.common.jwt;

import lombok.Getter;

@Getter
public class Jwt {

    private final String accessToken;
    private final String refreshToken;

    public Jwt(String accessToken,String refreshToken){
        super();
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }

}
