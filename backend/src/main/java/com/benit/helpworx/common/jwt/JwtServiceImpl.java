package com.benit.helpworx.common.jwt;

import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.common.repository.JwtRepository;
import com.benit.helpworx.common.repository.UsrRepository;
import com.benit.helpworx.login.dto.UsrInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService{

    @Autowired
    private JwtRepository jwtRepository;

    @Autowired
    private UsrRepository usrRepository;

    /**
     * 리프레시 토큰 저장
     *
     * @param refreshTokenInfoDto
     */
    @Override
    @Transactional
    public void saveRefreshTokenService(RefreshTokenInfoDto refreshTokenInfoDto) {
        jwtRepository.save(refreshTokenInfoDto.toEntity());
    }

    /**
     * 저장된 토큰의 유저 정보를 가져온다.
     *
     * @param usrId
     * @return
     */
    @Override
    @Transactional
    public UsrInfoDto getUsrInfo(String usrId) {

        Optional<Usr> usr=usrRepository.findById(usrId);
        return usr.get().toInfoDto(usr);
    }

    /**
     * 해당 토큰의 존재여부를 확인한다.
     *
     * @param token
     * @return
     */
    @Override
    @Transactional
    public boolean checkValidTokenService(String token) {
        return jwtRepository.existsByToken(token);
    }
}
