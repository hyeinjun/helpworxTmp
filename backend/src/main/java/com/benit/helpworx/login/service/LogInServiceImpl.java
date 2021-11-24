package com.benit.helpworx.login.service;

import com.benit.helpworx.common.entity.Auth;
import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.common.repository.AuthRepository;
import com.benit.helpworx.common.repository.UsrRepository;
import com.benit.helpworx.login.dto.LogInInfoDto;
import com.benit.helpworx.login.dto.UsrAuthInfoDto;
import com.benit.helpworx.login.dto.UsrInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LogInServiceImpl implements LogInService{

    @Autowired
    private UsrRepository usrRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 로그인 서비스
     * 아이디와 패스워드를 대조하여 확인
     *
     * @param logInInfoDto
     * @return
     */
    @Override
    @Transactional
    public UsrInfoDto logInService(LogInInfoDto logInInfoDto) {

        //아이디가 존재하는 경우에만 대조 진행
        if(usrRepository.existsById(logInInfoDto.getUsrId())) {

            Optional<Usr> usr = usrRepository.findById(logInInfoDto.getUsrId());

            //id, pwd의 db정보와 입력된 정보를 대조
            if (usr.get().getUsrId().equals(logInInfoDto.getUsrId())
                    && passwordEncoder.matches(logInInfoDto.getPwd(),usr.get().getPwd())){
                return usr.get().toInfoDto(usr);
            }
        }

        return null;
    }

    /**
     * 유저 정보를 가져온다.
     *
     * @param usrId
     * @return
     */
    @Override
    @Transactional
    public UsrInfoDto getInfoService(String usrId) {
        Usr usr = usrRepository.findById(usrId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다. 유저 아이디 = " + usrId));
        return usr.toInfoDto(Optional.of(usr));
    }

    /**
     * 유저의 권한 정보를 가져온다.
     *
     * @param usrId
     * @return
     */
    @Override
    @Transactional
    public UsrAuthInfoDto getAuthService(String usrId) {

        List<Auth> auths= authRepository.findAllByUsrId(usrId);
        UsrAuthInfoDto usrAuthInfoDto=new UsrAuthInfoDto();

        //권한 정보를 boolean으로 가져간다.
        //security 권한검사 사용시 바뀔 예정
        for(Auth auth:auths){
            if(auth.getAuth().equals("user")){
                usrAuthInfoDto.setUsrAuth(true);
            }else if(auth.getAuth().equals("manager")){
                usrAuthInfoDto.setManagerAuth(true);
            }else if(auth.getAuth().equals("operator")){
                usrAuthInfoDto.setOperatorAuth(true);
            }
        }

        return usrAuthInfoDto;
    }

}
