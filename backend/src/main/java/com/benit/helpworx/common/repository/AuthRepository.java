package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Auth;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthRepository extends JpaRepository<Auth,Integer> {
    List<Auth> findAllByUsrId(String usrId);
    void deleteAllByUsrIdAndAuth(String usrId, String Auth);
    void deleteAllByUsrId(String usrId);
    Boolean existsByUsrIdAndAuth(String usrId, String Auth);
}
