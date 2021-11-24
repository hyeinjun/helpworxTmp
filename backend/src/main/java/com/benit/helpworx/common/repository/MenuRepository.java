package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    public List<Menu> findAllByAccesAuthOrderByMenuId(String auth);
}
