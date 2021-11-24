package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.SrRcptTreat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface SrRcptTreatRepository extends JpaRepository<SrRcptTreat, Integer> {

    public boolean existsBySrId(int srId);

    @Transactional
    public void deleteBySrId(int srId);

    public SrRcptTreat getOneBySrId(int srId);

    public Optional<SrRcptTreat> findBySrId(int srId);
}
