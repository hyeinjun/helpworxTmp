package com.benit.helpworx.comng.service;

import com.benit.helpworx.comng.dto.CoDto;
import com.benit.helpworx.comng.dto.CoListDto;
import com.benit.helpworx.comng.dto.CoNmDto;
import com.benit.helpworx.comng.dto.SearchCoInfoDto;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface CompanyManagementService {

    public void insertCompany(CoDto coDto) throws ParseException;
    public CoDto detailCompany(String coCd) throws ParseException;
    public CoListDto listCompany(SearchCoInfoDto searchCoInfoDto) throws ParseException;
    public void deleteCompany(String coCd);
    public void updateCompany(CoDto coDto) throws ParseException;
    public List<CoNmDto> listCompanyName();
    public void changeCoUse(String coCd);
    public List<String> getAllCoCd();
    //public long countCompany();

}
