package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.Sr;
import com.benit.helpworx.sr.dto.IAvgTimeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SrRepository extends JpaRepository<Sr, Integer> {

    public boolean existsBySrId(int srId);

    Sr getOneBySrId(int srId);

    Optional<Sr> findBySrId(int srId);

    int countBySysCd(int sysCd);

    int countByCoCdAndSysCd(String coCd, int sysCd);

    int countBySysCdAndReqGb(int sysCd, String reqGb);

    int countByCoCdAndSysCdAndReqGb(String coCd, int sysCd, String reqGb);

    int countBySysCdAndRegTimBetween(int sysCd, Date thisMmStart, Date thisMmEnd);

    int countByCoCdAndSysCdAndRegTimBetween(String coCd, int sysCd, Date thisMmStart, Date thisMmEnd);

    List<Sr> findAllByStateOrStateOrderByRegTimDesc(String state, String state2);

    List<Sr> findAllByUsrIdAndStateOrUsrIdAndStateOrderByRegTimDesc(String usrId, String state1, String usrId1, String state2);

    List<Sr> findAllByCoCdAndStateOrCoCdAndStateOrderByRegTimDesc(String coCd, String state1, String coCd1, String state2);

    Sr findByUsrIdAndRegTim(String usrId, Date regTim);

    /**
     * 월별 평균 처리시간 sql
     * native query 사용
     *
     * @param coCd
     * @param sysCd
     * @return
     */
    @Query(value = "select TO_CHAR(td.date, 'YYYY-MM') AS yearMonth" +
            ",coalesce(EXTRACT(day FROM AVG(join_sr.treat_end_tim-join_sr.treat_begin_tim)),0) AS day" +
            ",coalesce(EXTRACT(HOUR FROM AVG(join_sr.treat_end_tim-join_sr.treat_begin_tim)),0) AS hour" +
            ",coalesce(EXTRACT(MINUTE FROM AVG(join_sr.treat_end_tim-join_sr.treat_begin_tim)),0) AS minute\n" +
            "FROM PUBLIC.temp_date td\n" +
            "LEFT OUTER JOIN (SELECT srrt.treat_begin_tim, srrt.treat_end_tim, sr.reg_tim FROM public.sr sr INNER JOIN public.sr_rcpt_treat srrt ON sr.sr_id = srrt.sr_id where sr.co_cd = ?1 AND sr.sys_cd = ?2) as join_sr\n" +
            "ON td.date = DATE(join_sr.reg_tim)\n" +
            "WHERE td.date between DATE_TRUNC('month', CURRENT_TIMESTAMP+'-6 months') and CURRENT_TIMESTAMP\n" +
            "GROUP BY yearMonth ORDER BY yearMonth",
            nativeQuery = true)
    List<IAvgTimeDto> getAvgTreatRqmtTime(String coCd, int sysCd);

    /**
     * 월별 평균 접수시간 sql
     * native query 사용
     * 
     * @param coCd
     * @param sysCd
     * @return
     */
    @Query(value = "select TO_CHAR(td.date, 'YYYY-MM') AS yearMonth" +
            ",coalesce(EXTRACT(day FROM AVG(join_sr.treat_begin_tim-join_sr.reg_tim)),0) AS day" +
            ",coalesce(EXTRACT(HOUR FROM AVG(join_sr.treat_begin_tim-join_sr.reg_tim)),0) AS hour" +
            ",coalesce(EXTRACT(MINUTE FROM AVG(join_sr.treat_begin_tim-join_sr.reg_tim)),0) AS minute\n" +
            "FROM PUBLIC.temp_date td\n" +
            "LEFT OUTER JOIN (SELECT srrt.treat_begin_tim, srrt.treat_end_tim, sr.reg_tim FROM public.sr sr INNER JOIN public.sr_rcpt_treat srrt ON sr.sr_id = srrt.sr_id where sr.co_cd = ?1 AND sr.sys_cd = ?2) as join_sr\n" +
            "ON td.date = DATE(join_sr.reg_tim)\n" +
            "WHERE td.date between DATE_TRUNC('month', CURRENT_TIMESTAMP+'-6 months') and CURRENT_TIMESTAMP\n" +
            "GROUP BY yearMonth ORDER BY yearMonth",
            nativeQuery = true)
    List<IAvgTimeDto> getAvgRcptRqmtTime(String coCd, int sysCd);
}
