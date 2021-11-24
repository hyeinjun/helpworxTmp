package com.benit.helpworx.common.jwt;

import com.benit.helpworx.login.dto.UsrInfoDto;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    public void saveRefreshTokenService(RefreshTokenInfoDto refreshTokenInfoDto);
    public boolean checkValidTokenService(String token);
    public UsrInfoDto getUsrInfo(String usrId);
}
