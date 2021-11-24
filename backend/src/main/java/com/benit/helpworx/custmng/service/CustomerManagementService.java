package com.benit.helpworx.custmng.service;

import com.benit.helpworx.custmng.dto.SearchUsrInfoDto;
import com.benit.helpworx.custmng.dto.UsrAuthDto;
import com.benit.helpworx.custmng.dto.UsrDto;
import com.benit.helpworx.custmng.dto.UsrListDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerManagementService {
    public void insertCustomer(UsrDto usrDto);
    public UsrListDto listCustomer(SearchUsrInfoDto searchUsrInfoDto);
    public UsrDto detailCustomer(String usrId);
    public void deleteCustomer(String usrId);
    public void updateCustomer(UsrDto usrDto);

    public void insertCustomerAuth(UsrAuthDto usrAuthDto);
    public void updateCustomerAuth(UsrAuthDto usrAuthDto);
    public UsrAuthDto getCustomerAuth(String usrId);
    public void deleteCustomerAuth(String usrId);

    public Long countCustomer(String coCd);
    public void changeCustomerUse(String usrId);

}

