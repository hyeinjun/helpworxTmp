package com.benit.helpworx.custmng.service;

import com.benit.helpworx.common.entity.Auth;
import com.benit.helpworx.common.entity.Usr;
import com.benit.helpworx.common.repository.AuthRepository;
import com.benit.helpworx.custmng.dto.SearchUsrInfoDto;
import com.benit.helpworx.custmng.dto.UsrAuthDto;
import com.benit.helpworx.custmng.dto.UsrDto;
import com.benit.helpworx.common.repository.UsrRepository;
import com.benit.helpworx.custmng.dto.UsrListDto;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

    @Autowired
    private UsrRepository usrRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 유저 등록
     *
     * @param usrDto
     */
    @Override
    @Transactional
    public void insertCustomer(UsrDto usrDto) {
        Usr usr=usrDto.toEntity();
        System.out.println("insert cust");

        //패스워드 암호화
        usr.setPwd(pwdEncoding(usr.getPwd()));

        //개인정보 암호화
        usr.setEmail(infoEncoding(usr.getEmail()));
        usr.setEmpno(infoEncoding(usr.getEmpno()));
        usr.setTelNo(infoEncoding(usr.getTelNo()));
        usrRepository.save(usr);
    }

    /**
     * 유저 목록 조회 - 페이징 o
     *
     * @param searchUsrInfoDto
     * @return
     */
    @Override
    @Transactional
    public UsrListDto listCustomer(SearchUsrInfoDto searchUsrInfoDto) {

        if(searchUsrInfoDto.getPage()==null){
            searchUsrInfoDto.setPage(new Integer(1));
        }

        QueryResults<Usr> results=usrRepository.findDynamicQueryUser(searchUsrInfoDto);
        List<Usr> users=results.getResults();

        List<UsrDto> usrDtos=new ArrayList<>();

        for(Usr usr: users){
            usrDtos.add(usr.toDto(Optional.of(usr)));
        }

        UsrListDto usrListDto= UsrListDto.builder()
                .usrDtos(usrDtos)
                .totalCount(results.getTotal())
                .build();

        return usrListDto;
    }

    /**
     * 유저 상세 정보 조회
     *
     * @param usrId
     * @return
     */
    @Override
    @Transactional
    public UsrDto detailCustomer(String usrId) {
        Usr usr=usrRepository.findById(usrId)
                .orElseThrow(()->new IllegalArgumentException("없는 회원 입니다. 회원 아이디 = "+usrId));

        UsrDto usrDto =usr.toDto(Optional.of(usr));

//        개인정보 복호화
        usrDto.setEmail(infoDecoding(usrDto.getEmail()));
        usrDto.setEmpno(infoDecoding(usrDto.getEmpno()));
        usrDto.setTelNo(infoDecoding(usrDto.getTelNo()));
        System.out.println(usrDto.getEmail());

        System.out.println("detail customer");
        return usrDto;
    }

    /**
     * 유저 삭제
     *
     * @param usrId
     */
    @Override
    @Transactional
    public void deleteCustomer(String usrId) {
        System.out.println("delete customer :"+usrId);
        if(usrRepository.existsById(usrId)) {
            usrRepository.deleteById(usrId);
        }

    }

    /**
     * 유저 권한 삭제
     *
     * @param usrId
     */
    @Override
    @Transactional
    public void deleteCustomerAuth(String usrId){
        authRepository.deleteAllByUsrId(usrId);
    }

    /**
     * 유저 정보 수정
     *
     * @param usrDto
     */
    @Override
    @Transactional
    public void updateCustomer(UsrDto usrDto) {

        Usr usr=usrRepository.findById(usrDto.getUsrId())
                .orElseThrow(()->new IllegalArgumentException("없는 회원 입니다. 회원 아이디 = "+usrDto.getUsrId()));

        System.out.println("update customer");

        //패스워드 암호화
        if(usrDto.getPwd()!=null&&!usrDto.getPwd().equals("")) {
            usrDto.setPwd(pwdEncoding(usrDto.getPwd()));
        }

        //개인정보 암호화
        if(usrDto.getEmail()!=null) {
            usrDto.setEmail(infoEncoding(usrDto.getEmail()));
        };
        if(usrDto.getEmpno()!=null) {
            usrDto.setEmpno(infoEncoding(usrDto.getEmpno()));
        }
        if(usrDto.getTelNo()!=null) {
            usrDto.setTelNo(infoEncoding(usrDto.getTelNo()));
        }

        usr.update(usrDto);

    }

    /**
     * 유저 휴면 전환
     *
     * @param usrId
     */
    @Override
    @Transactional
    public void changeCustomerUse(String usrId) {

        Usr usr=usrRepository.findById(usrId)
                .orElseThrow(()->new IllegalArgumentException("없는 회원 입니다. 회원 아이디 = "+usrId));

        System.out.println("change customer use");

        usr.setUseYn(false);

    }
    /**
     * 유저 권한 등록
     *
     * @param usrAuthDto
     */
    @Override
    @Transactional
    public void insertCustomerAuth(UsrAuthDto usrAuthDto) {

        //고객 권한 등록
        if(usrAuthDto.isUserYn()) {
            authRepository.save(usrAuthDto.toAuth("user"));
        }

        //운영자 권한 등록
        if(usrAuthDto.isOperatorYn()) {
            authRepository.save(usrAuthDto.toAuth("operator"));
        }

        //관리자 권한 등록
        if(usrAuthDto.isManagerYn()){
                authRepository.save(usrAuthDto.toAuth("manager"));
        }
    }

    /**
     * 유저 권한 수정
     *
     * @param usrAuthDto
     */
    @Override
    @Transactional
    public void updateCustomerAuth(UsrAuthDto usrAuthDto) {

        //고객 권한 수정
        if(usrAuthDto.isUserYn()){
            if(!authRepository.existsByUsrIdAndAuth(usrAuthDto.getUsrId(), "user")){
                authRepository.save(usrAuthDto.toAuth("user"));
            }
        }else{
            if(authRepository.existsByUsrIdAndAuth(usrAuthDto.getUsrId(), "user")){
                authRepository.deleteAllByUsrIdAndAuth(usrAuthDto.getUsrId(), "user");
            }
        }

        //운영자 권한 수정
        if(usrAuthDto.isOperatorYn()){
            if(!authRepository.existsByUsrIdAndAuth(usrAuthDto.getUsrId(), "operator")){
                authRepository.save(usrAuthDto.toAuth("operator"));
            }
        }else{
            if(authRepository.existsByUsrIdAndAuth(usrAuthDto.getUsrId(), "operator")){
                authRepository.deleteAllByUsrIdAndAuth(usrAuthDto.getUsrId(), "operator");
            }
        }

        //관리자 권한 수정
        if(usrAuthDto.isManagerYn()){
            if(!authRepository.existsByUsrIdAndAuth(usrAuthDto.getUsrId(), "manager")){
                authRepository.save(usrAuthDto.toAuth("manager"));
            }
        }else{
            if(authRepository.existsByUsrIdAndAuth(usrAuthDto.getUsrId(), "manager")){
                authRepository.deleteAllByUsrIdAndAuth(usrAuthDto.getUsrId(), "manager");
            }
        }

    }

    /**
     * 유저 권한 목록 조회
     *
     * @param usrId
     * @return
     */
    @Override
    @Transactional
    public UsrAuthDto getCustomerAuth(String usrId) {
        UsrAuthDto usrAuthDto=new UsrAuthDto();
        usrAuthDto.setUsrId(usrId);

        if(authRepository.existsByUsrIdAndAuth(usrId,"user")){
            usrAuthDto.setUserYn(true);
        }

        if(authRepository.existsByUsrIdAndAuth(usrId,"operator")){
            usrAuthDto.setOperatorYn(true);
        }

        if(authRepository.existsByUsrIdAndAuth(usrId,"manager")){
            usrAuthDto.setManagerYn(true);
        }

        return usrAuthDto;
    }

    /**
     * 회사 당 고객 수 조회
     *
     * @param coCd
     * @return
     */
    @Override
    @Transactional
    public Long countCustomer(String coCd) {
        System.out.println("customer count");
        return usrRepository.countByCoCd(coCd);
    }

    /**
     * 패스워드 암호화
     *
     * @param pwd
     * @return
     */
    public String pwdEncoding(String pwd){
        return passwordEncoder.encode(pwd);
    }

    /**
     * 개인정보 암호화
     *
     * @param info
     * @return
     */
    public String infoEncoding(String info){
        if(info!=null) {
            byte[] message = info.getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().encodeToString(message);
        }
        return null;
    }

    /**
     *  개인정보 복호화
     *
     * @param info
     * @return
     */
    public String infoDecoding(String info){
        if(info!=null) {
            byte[] decoded = Base64.getDecoder().decode(info);
            return new String(decoded, StandardCharsets.UTF_8);
        }
        return null;
    }
}
