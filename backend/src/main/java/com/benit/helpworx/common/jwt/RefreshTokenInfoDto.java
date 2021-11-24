package com.benit.helpworx.common.jwt;

import com.benit.helpworx.common.entity.RefreshToken;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInfoDto {

    private Integer tokenId;

    private String token;

    private String usrId;

    public RefreshToken toEntity(){
        return RefreshToken.builder()
                .tokenId(this.tokenId)
                .token(this.token)
                .usrId(this.usrId)
                .build();
    }
}
