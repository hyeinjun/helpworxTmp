package com.benit.helpworx.comng.service;

import com.benit.helpworx.common.entity.Co;
import com.benit.helpworx.comng.dto.CoDto;
import com.benit.helpworx.common.repository.CoRepository;
import com.benit.helpworx.comng.dto.CoListDto;
import com.benit.helpworx.comng.dto.CoNmDto;
import com.benit.helpworx.comng.dto.SearchCoInfoDto;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyManagementServiceImpl implements CompanyManagementService{

    @Autowired
    private CoRepository coRepository;

    /**
     * 회사 등록
     *
     * @param coDto
     */
    @Override
    @Transactional
    public void insertCompany(CoDto coDto) throws ParseException {

        Co co=coDto.toEntity();

        //개인정보 암호화
        co.setBizmanNo(infoEncoding(co.getBizmanNo()));
        co.setCoAddr(infoEncoding(co.getCoAddr()));

        System.out.println("insert company");
        coRepository.save(co);
    }

    /**
     * 회사 상세 정보 조회
     *
     * @param coCd
     * @return
     */
    @Override
    @Transactional
    public CoDto detailCompany(String coCd) throws ParseException {

        Co co=coRepository.findById(coCd).orElseThrow(()->new IllegalArgumentException("없는 회사 입니다. 회사 코드 = "+coCd));

        System.out.println("detail company");

        CoDto coDto=co.toDto(Optional.of(co));

        //개인정보 복호화
        coDto.setBizmanNo(infoDecoding(coDto.getBizmanNo()));
        coDto.setCoAddr(infoDecoding(coDto.getCoAddr()));
        return coDto;
    }


    /**
     * 회사 목록 조회 - 페이징 처리 o
     *
     * @param searchCoInfoDto
     * @return
     */
    @Override
    @Transactional
    public CoListDto listCompany(SearchCoInfoDto searchCoInfoDto) throws ParseException {
        if(searchCoInfoDto.getPage()==null){
            searchCoInfoDto.setPage(new Integer(1));
        }

        QueryResults<Co> results=coRepository.findDynamicQuery(searchCoInfoDto);
        List<Co> cos=results.getResults();

        List<CoDto> coDtoList=new ArrayList<>();
        for(Co co: cos){
            coDtoList.add(co.toDto(Optional.of(co)));
        }

        System.out.println("list company");
        return CoListDto.builder()
                .coDtos(coDtoList)
                .totalCount(results.getTotal())
                .build();
    }

    /**
     * 회사 삭제
     *
     * @param coCd
     */
    @Override
    @Transactional
    public void deleteCompany(String coCd) {
        coRepository.deleteById(coCd);
    }

    /**
     * 회사 수정
     *
     * @param coDto
     */
    @Override
    @Transactional
    public void updateCompany(CoDto coDto) throws ParseException {

        System.out.println("here?");
        Co co=coRepository.findById(coDto.getCoCd())
                .orElseThrow(()->new IllegalArgumentException("없는 회사 입니다. 회사 코드 = "+coDto.getCoCd()));

        //개인정보 암호화
        if(coDto.getBizmanNo()!=null) {
            coDto.setBizmanNo(infoEncoding(coDto.getBizmanNo()));
        }
        if(coDto.getCoAddr()!=null) {
            coDto.setCoAddr(infoEncoding(coDto.getCoAddr()));
        }
        System.out.println("update company");
        co.update(coDto);
    }

    /**
     * 회사 이름 목록 조회
     *
     * @return
     */
    @Override
    @Transactional
    public List<CoNmDto> listCompanyName() {
        List<Co> cos= coRepository.findAll(Sort.by(Sort.Direction.ASC,"coNm"));

        List<CoNmDto> coNmDtos=new ArrayList<>();

        for(Co co:cos){
            coNmDtos.add(co.toNmDto());
        }

        return coNmDtos;
    }

    /**
     * 회사 휴면 전환
     *
     * @param coCd
     */
    @Override
    @Transactional
    public void changeCoUse(String coCd){
        Co co=coRepository.findById(coCd)
                .orElseThrow(()->new IllegalArgumentException("없는 회사 입니다. 회사 코드 = "+coCd));

        co.setUseYn(false);
    }

    /**
     * 모든 회사 코드 리턴
     * @return
     */
    @Override
    public List<String> getAllCoCd() {
        return coRepository.findAllCoCd();
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
            byte[] decoded=Base64.getDecoder().decode(info);
            return new String(decoded,StandardCharsets.UTF_8);
        }
        return null;
    }


}
