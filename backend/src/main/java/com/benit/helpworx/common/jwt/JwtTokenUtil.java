package com.benit.helpworx.common.jwt;

import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.login.dto.UsrInfoDto;
import com.benit.helpworx.login.resource.UsrInfoResource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

public class JwtTokenUtil {

    @Autowired
    private JwtService jwtService;

    private final Key ACCESS_KEY;
    private final Key REFRESH_KEY;
    private final int ACCESS_EXP_TIME = 30*60*1000;
    private final int REFRESH_EXP_TIME = 7*24*60*60*1000;


    public JwtTokenUtil(String accessSecret,String refreshSecret){
        this.ACCESS_KEY= Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.REFRESH_KEY=Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    /**
     * AccessToken 생성
     *
     * @param usrInfoDto
     * @return
     */
    public String createAccessToken(UsrInfoDto usrInfoDto){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, ACCESS_EXP_TIME);
        Date expiryDate =  calendar.getTime();

        String token= Jwts.builder()
                .claim("usrId",usrInfoDto.getUsrId())
                .claim("nm",usrInfoDto.getNm())
                .claim("coCd",usrInfoDto.getCoCd())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(ACCESS_KEY, SignatureAlgorithm.HS256)
                .compact();

        return token;

    }

    /**
     * RefreshToken 생성
     *
     * @param usrInfoDto
     * @return
     */
    public String createRefreshToken(UsrInfoDto usrInfoDto){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, REFRESH_EXP_TIME);
        Date expiryDate =  calendar.getTime();

        String token= Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("usrId",usrInfoDto.getUsrId())
                .signWith(REFRESH_KEY, SignatureAlgorithm.HS256)
                .compact();

        //refreshtoken의 정보를 DB에 저장.
        this.saveRefreshToken(token);

        return token;
    }

    /**
     * Refresh Token의 정보를 DB에 저장
     *
     * @param token
     */
    public void saveRefreshToken(String token){

        RefreshTokenInfoDto refreshTokenInfoDto=RefreshTokenInfoDto.builder()
                .token(token)
                .usrId(this.extractUsrIdRefreshToken(token))
                .build();

        jwtService.saveRefreshTokenService(refreshTokenInfoDto);
    }

    /**
     * Access Token 유효성 검사
     *
     * @param authToken
     * @return
     * @throws JwtException
     */
    public boolean validateAccessToken(String authToken) throws JwtException{
        try{
            Jwts.parserBuilder()
                .setSigningKey(ACCESS_KEY)
        .build()
        .parseClaimsJws(authToken);
            return true;

        } catch(JwtException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Refresh Token 유효성 검사
     *
     * @param authToken
     * @return
     * @throws JwtException
     */
    public boolean validateRefreshToken(String authToken) throws JwtException{
        try{
            Jwts.parserBuilder()
                    .setSigningKey(REFRESH_KEY)
                    .build()
                    .parseClaimsJws(authToken);

            return true;

        } catch(JwtException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Refresh Token 재발행
     *
     * @param refreshToken
     * @return
     * @throws Exception
     */
    public Jwt makeReJwt(String refreshToken) throws Exception{

        String usrId=this.extractUsrIdRefreshToken(refreshToken);
        System.out.println(refreshToken+"   :   "+usrId);
        UsrInfoDto usrInfoDto=jwtService.getUsrInfo(usrId);

        //리프레시 토큰의 유효성 확인
        if(validateRefreshToken(refreshToken)
        &&jwtService.checkValidTokenService(refreshToken)){// 유효할 경우 accesstoken만 재발행
            return new Jwt(createAccessToken(usrInfoDto),refreshToken);
        }else if(!validateRefreshToken(refreshToken)){//유효하지 않으면 전체 재발행
            return this.generateToken(usrInfoDto);
        }else{
            return null;
        }
    }

    /**
     * Access, Refresh Token 생성
     *
     * @param usrInfoDto
     * @return
     */
    public Jwt generateToken(UsrInfoDto usrInfoDto) {

        String accessToken = this.createAccessToken(usrInfoDto);
        String refreshToken = this.createRefreshToken(usrInfoDto);

        return new Jwt(accessToken, refreshToken);
    }

    /**
     * Access Token에서 User Id 추출
     *
     * @param token
     * @return
     * @throws JwtException
     */
    public String extractUsrIdAccessToken(String token) throws JwtException{
        String usrId=null;
        try{
            usrId=Jwts.parserBuilder()
                    .setSigningKey(ACCESS_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("usrId").toString();

        }catch (Exception e){
            e.printStackTrace();
        }
        return usrId;
    }

    /**
     * Refresh Token에서 User Id 추출
     *
     * @param token
     * @return
     * @throws JwtException
     */
    public String extractUsrIdRefreshToken(String token) throws JwtException{
        String usrId=null;
        try{
            usrId=Jwts.parserBuilder()
                    .setSigningKey(REFRESH_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("usrId").toString();

        }catch (Exception e){
            e.printStackTrace();
        }
        return usrId;
    }

    /**
     * User name 추출
    *
     * @param token
     * @return
     * @throws JwtException
     */
    public String extractnm(String token) throws JwtException{
        String usrNm=null;
        try{
            usrNm=Jwts.parserBuilder()
                    .setSigningKey(ACCESS_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("nm").toString();

        }catch (Exception e){
            e.printStackTrace();
        }
        return usrNm;
    }

    /**
     * Co Cd 추출
     *
     * @param token
     * @return
     * @throws JwtException
     */
    public String extractCoCd(String token) throws JwtException{
        String coCd=null;
        try{
            coCd=Jwts.parserBuilder()
                    .setSigningKey(ACCESS_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("coCd").toString();

        }catch (Exception e){
            e.printStackTrace();
        }
        return coCd;
    }

    /**
     * 헤더에서 토큰 값 추출 : interceptor에서 체크
     *
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("accessToken").substring("Bearer ".length());
    }

    /**
     * 헤더에서 토큰 값 추출
     *
     * @param headers
     * @return
     */
    public String resolveToken(HttpHeaders headers){
        return headers.get("accessToken").get(0).substring("Bearer ".length());
    }

}
