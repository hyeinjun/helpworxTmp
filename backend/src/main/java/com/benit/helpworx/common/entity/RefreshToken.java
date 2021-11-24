package com.benit.helpworx.common.entity;

import com.benit.helpworx.common.jwt.RefreshTokenInfoDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="refresh_token")
public class RefreshToken {

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="token_id")
    @NotNull
    private Integer tokenId;

    @Column(name="token")
    @NotNull
    private String token;

    @Column(name="usr_id")
    @NotNull
    private String usrId;

    public RefreshTokenInfoDto toDto(Optional<RefreshToken> refreshToken){
        return RefreshTokenInfoDto.builder()
                .tokenId(refreshToken.get().tokenId)
                .token(refreshToken.get().token)
                .usrId(refreshToken.get().usrId)
                .build();
    }
}
