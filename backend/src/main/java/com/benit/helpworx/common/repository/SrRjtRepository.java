package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.SrRjt;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface SrRjtRepository extends JpaRepository<SrRjt,Integer> {
    @Transactional
    void deleteBySrId(int srId);

    SrRjt findBySrId(int srId);
}
