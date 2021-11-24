package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.RltmInqu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RltmInquRepository extends JpaRepository<RltmInqu, Integer> {
    public List<RltmInqu> findAllByStateOrderByBeginTimDesc(String state);

    public RltmInqu findOneByUsrIdAndSysCdAndBeginTim(String usrId, int sysCd, Date beginTim);

    public List<RltmInqu> findAllListByUsrId(String usrId);

    public List<RltmInqu> findAllByUsrIdAndSysCd(String usrId, int sysCd);

    public List<RltmInqu> findAllByUsrIdAndSysCdAndState(String usrId, int sysCd, String state);

    public List<RltmInqu> findAllByCoCdAndSysCd(String coCd, int sysCd);

    public List<RltmInqu> findAllByCoCdAndSysCdAndState(String coCd, int sysCd, String state);

    public List<RltmInqu> findAllByCoCdAndStateOrderByBeginTimDesc(String coCd, String state);

    public List<RltmInqu> findAllListByAnswrrAndState(String usrId, String state);

    public int countByCoCdAndAnswrr(String coCd, String answrr);

    List<RltmInqu> findAllByCoCdAndStateAndAnswrrOrderByBeginTimDesc(String coCd, String state, String answrr);

    List<RltmInqu> findAllByUsrIdAndState(String usrId, String 진행중);

    List<RltmInqu> findAllByAnswrrAndState(String usrId, String 진행중);

    List<RltmInqu> findTop7ByCoCdAndStateOrderByBeginTimDesc(String coCd, String 진행중);
}
