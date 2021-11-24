package com.benit.helpworx.notfc.controller;

import com.benit.helpworx.menu.dto.MenuDto;
import com.benit.helpworx.notfc.dto.NotfcDto;
import com.benit.helpworx.notfc.resource.NotfcResource;
import com.benit.helpworx.notfc.service.NotfcService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = {"*"},allowCredentials = "true")
@RestController
@RequestMapping(value="")
public class NotfcController {
    @Autowired
    NotfcService notfcService;

    @ApiOperation("유저의 알림 조회")
    @GetMapping("/notfc")
    @ResponseBody
    public ResponseEntity GetNotfc(@RequestHeader final HttpHeaders header, @RequestParam String usrId) {
        List<NotfcDto> dto = notfcService.getNotfc(usrId);
        List<NotfcResource> ret = new ArrayList<>();
        if(dto.size()>0)
            dto.forEach(e -> ret.add(e.toResource()));
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    @ApiOperation("읽은 알림 삭제")
    @DeleteMapping("/notfc")
    @ResponseBody
    public ResponseEntity deleteNotfc(@RequestHeader final HttpHeaders header, @RequestParam int notfcId) {
        notfcService.deleteNotfc(notfcId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
