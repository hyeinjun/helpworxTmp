package com.benit.helpworx.login.controller;

import com.benit.helpworx.common.jwt.Jwt;
import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.login.dto.UsrAuthInfoDto;
import com.benit.helpworx.login.dto.UsrInfoDto;
import com.benit.helpworx.login.resource.LogInInfoResource;
import com.benit.helpworx.login.resource.UsrInfoResource;
import com.benit.helpworx.login.service.LogInService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin(origins = {"*"},allowCredentials = "true")
@RestController
@RequestMapping(value="")
public class LogInController {

    @Autowired
    private LogInService logInService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @ApiOperation("로그인")
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity LogIn(@RequestHeader final HttpHeaders header, @RequestBody LogInInfoResource logInInfoResource, HttpServletRequest request) throws Exception {

        UsrInfoDto usrInfoDto=logInService.logInService(logInInfoResource.toDto());

        //유효한 유저 정보일 때 토큰 발행
        if(usrInfoDto!=null&&usrInfoDto.isUseYn()) {

            Jwt tokens=null;

            //리프레시 토큰이 없거나 유효기간이 지난 경우, 다른 사람의 리프레시 토큰을 가지고 있는 경우에는 두 토큰 모두 다시 발행한다.
            //유효한 본인의 리프레시 토큰이 있는 경우는 액세스 토큰만 재발행한다.
            if(logInInfoResource.getRefreshToken()==null
                    || !jwtTokenUtil.validateRefreshToken(logInInfoResource.getRefreshToken())
                    || !jwtTokenUtil.extractUsrIdRefreshToken(logInInfoResource.getRefreshToken())
                        .equals(logInInfoResource.getUsrId())){

                tokens = jwtTokenUtil.generateToken(usrInfoDto);

            }else{
                tokens= jwtTokenUtil.makeReJwt(logInInfoResource.getRefreshToken());
            }

            if(tokens!=null) {
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }else{
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }

        if(usrInfoDto==null) {
            return new ResponseEntity<>("wrong id or pwd", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("expired user",HttpStatus.BAD_REQUEST);
        }

    }

//    @ApiOperation("로그아웃")
//    @GetMapping("/logout")
//    public void Logout(HttpServletRequest request){
//
//        //세션대신 accessToken 날리는 걸로 바꿔야 함
//        HttpSession session= request.getSession();
//
//
//        session.invalidate();
//
//    }

    /**
     * 로그인, 토큰부여 성공시 해당하는 사용자의 정보를 프론트에 가져간다.
     *
     * @param header
     * @return
     */
    @ApiOperation("사용자 정보 가져오기")
    @GetMapping("/usr-info")
    public ResponseEntity usrInfo(@RequestHeader final HttpHeaders header){

        //헤더에서 유저 아이디를 추출한다.
        String accessToken= jwtTokenUtil.resolveToken(header);
        String usrId=jwtTokenUtil.extractUsrIdAccessToken(accessToken);

        UsrInfoDto usrInfoDto=logInService.getInfoService(usrId);
        UsrAuthInfoDto usrAuthInfoDto=logInService.getAuthService(usrInfoDto.getUsrId());

        //권한정보와 유저정보를 response로 담아 보낸다.
        UsrInfoResource usrInfoResource=new UsrInfoResource();

        usrInfoDto.toResource(usrInfoResource);
        usrAuthInfoDto.toResource(usrInfoResource);

        return new ResponseEntity(usrInfoResource,HttpStatus.OK);
    }

    @ApiOperation("사용자 회사 정보 가져오기")
    @GetMapping("/cocd-info")
    public ResponseEntity usrCoCd(@RequestHeader final HttpHeaders header){

        String accessToken= jwtTokenUtil.resolveToken(header);
        String coCd=jwtTokenUtil.extractCoCd(accessToken);

        return new ResponseEntity(coCd, HttpStatus.OK);

    }

}
