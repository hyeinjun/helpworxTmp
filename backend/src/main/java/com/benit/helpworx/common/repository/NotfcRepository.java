package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Notfc;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface NotfcRepository extends JpaRepository<Notfc,Integer> {

    @Transactional
    void deleteAllByTrgetId(int trgetId);

    List<Notfc> findAllByUsrIdAndFuncOrderByNotfcTimDesc(String usrId, String sr);

    List<Notfc> findAllByUsrIdOrderByNotfcTimDesc(String usrId);
}
