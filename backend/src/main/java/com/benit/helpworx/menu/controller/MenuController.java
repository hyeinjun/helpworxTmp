package com.benit.helpworx.menu.controller;

import com.benit.helpworx.common.jwt.JwtTokenUtil;
import com.benit.helpworx.menu.dto.MenuDto;
import com.benit.helpworx.menu.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"},allowCredentials = "true")
@RestController
@RequestMapping(value="")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 권한으로 메뉴 조회
     * @param header
     * @param auth
     * @return
     * @throws Exception
     */
    @ApiOperation("권한 별 메뉴 조회")
    @GetMapping("/menu")
    @ResponseBody
    public ResponseEntity GetMenus(@RequestHeader final HttpHeaders header, @RequestParam String auth) throws Exception {
        List<MenuDto> ret = menuService.getMenusByAuth(auth);
        return new ResponseEntity(ret, HttpStatus.OK);
    }


}
