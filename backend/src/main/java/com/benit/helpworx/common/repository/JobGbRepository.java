package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.JobGb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobGbRepository extends JpaRepository<JobGb,Integer> {
    List<JobGb> findAllBySysCd(int sysCd);
}
