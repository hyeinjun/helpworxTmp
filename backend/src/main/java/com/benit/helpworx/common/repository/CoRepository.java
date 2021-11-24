package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Co;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoRepository extends JpaRepository<Co,String>, CoRepositoryCustom {
    @Query(value = "SELECT * FROM co WHERE cont_end_dt>current_date ORDER BY cont_end_dt asc", nativeQuery = true)
    List<Co> findByEndDt(Pageable pageable);

    @Query(value = "SELECT * FROM co WHERE cont_begin_dt<current_date ORDER BY cont_begin_dt desc", nativeQuery = true)
    List<Co> findByBeginDt(Pageable pageable);

    @Query(value = "SELECT co_cd FROM co ORDER BY co_cd", nativeQuery = true)
    List<String> findAllCoCd();
}
