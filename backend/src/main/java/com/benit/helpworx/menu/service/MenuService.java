package com.benit.helpworx.menu.service;

import com.benit.helpworx.menu.dto.MenuDto;

import java.util.List;

public interface MenuService {
    public List<MenuDto> getMenusByAuth(String auth);
}
