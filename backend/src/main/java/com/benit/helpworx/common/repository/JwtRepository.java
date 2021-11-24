package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRepository extends JpaRepository<RefreshToken,Integer> {
    public boolean existsByToken(String token);
}
