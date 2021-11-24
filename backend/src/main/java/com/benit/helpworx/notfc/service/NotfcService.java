package com.benit.helpworx.notfc.service;

import com.benit.helpworx.menu.dto.MenuDto;
import com.benit.helpworx.notfc.dto.NotfcDto;
import com.benit.helpworx.notfc.resource.NotfcResource;

import java.util.List;

public interface NotfcService {
    /**
     * usrId의 미확인 알림 목록
     * @param usrId
     * @return
     */
    List<NotfcDto> getNotfc(String usrId);

    /**
     * 알림 삭제 처리
     * @param notfcId
     */
    void deleteNotfc(int notfcId);
}
