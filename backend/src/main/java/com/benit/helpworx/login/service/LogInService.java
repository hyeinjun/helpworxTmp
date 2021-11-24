package com.benit.helpworx.login.service;

import com.benit.helpworx.login.dto.LogInInfoDto;
import com.benit.helpworx.login.dto.UsrAuthInfoDto;
import com.benit.helpworx.login.dto.UsrInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogInService {
    public UsrInfoDto logInService(LogInInfoDto logInInfoDto);
    public UsrInfoDto getInfoService(String usrId);
    public UsrAuthInfoDto getAuthService(String usrId);
}
