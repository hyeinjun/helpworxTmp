package com.benit.helpworx.notfc.service;

import com.benit.helpworx.common.entity.Notfc;
import com.benit.helpworx.common.repository.InfoModReqRepository;
import com.benit.helpworx.common.repository.NotfcRepository;
import com.benit.helpworx.common.repository.SrRepository;
import com.benit.helpworx.notfc.dto.NotfcDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotfcServiceImpl implements NotfcService {
    @Autowired
    private NotfcRepository notfcRepository;
    @Autowired
    private SrRepository srRepository;
    @Autowired
    private InfoModReqRepository infoModReqRepository;

    /**
     * 미확인 알림 목록 조회
     * @param usrId
     * @return
     */
    @Override
    public List<NotfcDto> getNotfc(String usrId) {
        List<Notfc> notfcList = notfcRepository.findAllByUsrIdOrderByNotfcTimDesc(usrId);
        List<NotfcDto> ret = new ArrayList<>();
        if(notfcList.size()<=0) return ret;
        for (int idx = 0; idx < notfcList.size(); idx++) {
            Notfc notfc = notfcList.get(idx);
            if(notfc.getFunc().equals("sr")){
                String sbjct = srRepository.findBySrId(notfc.getTrgetId()).orElse(null).getSbjct();
                if (sbjct.length()>20) sbjct = sbjct.substring(0,20);
                ret.add(notfc.toDto(sbjct));
            }else if(notfc.getFunc().equals("정보수정요청")){
                String sbjct=infoModReqRepository.findById(notfc.getTrgetId()).orElse(null).getReqSbjct();
                if (sbjct.length()>20) sbjct = sbjct.substring(0,20);
                ret.add(notfc.toDto(sbjct));
            }
        }

        return ret;
    }

    /**
     * 읽은 알림과 trgetId가 같은 알림 삭제 처리
     * @param notfcId
     */
    @Override
    public void deleteNotfc(int notfcId) {
        Notfc notfc = notfcRepository.findById(notfcId).orElse(null);
        int srId = notfc.getTrgetId();
        notfcRepository.deleteAllByTrgetId(srId);
    }
}
