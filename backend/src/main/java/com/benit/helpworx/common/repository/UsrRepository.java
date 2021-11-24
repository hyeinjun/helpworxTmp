package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsrRepository extends JpaRepository<Usr, String>, UsrRepositoryCustom {
    public Long countByCoCd(String coCd);
}
