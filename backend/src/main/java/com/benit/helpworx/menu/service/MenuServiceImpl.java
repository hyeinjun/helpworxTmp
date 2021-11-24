package com.benit.helpworx.menu.service;

import com.benit.helpworx.common.entity.Menu;
import com.benit.helpworx.common.repository.MenuRepository;
import com.benit.helpworx.main.resource.SystemResource;
import com.benit.helpworx.menu.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    /**
     * auth로 menu 조회 후 리턴
     * @param auth
     * @return
     */
    @Override
    public List<MenuDto> getMenusByAuth(String auth) {
        System.out.println("SERVICE");
        List<Menu> menus = menuRepository.findAllByAccesAuthOrderByMenuId(auth);
        System.out.println("MENUS "+menus.toString());
        List<MenuDto> ret = new ArrayList<>();

        menus.forEach( e -> ret.add(e.toDto()));
        System.out.println("RET "+ret.toString());
        return ret;
    }
}
