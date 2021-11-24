package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.InfoModReq;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InfoModReqRepository extends JpaRepository<InfoModReq, Integer>, InfoModReqRepositoryCustom {
    @Query(value = "SELECT * FROM info_mod_req WHERE state!='완료' ORDER BY req_tim DESC", nativeQuery = true)
    List<InfoModReq> findByStateAndTim(Pageable pageable);
}
