package com.benit.helpworx.common.repository;

import com.benit.helpworx.common.entity.InquLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InquLogRepository extends JpaRepository<InquLog, Integer> {
    List<InquLog> findAllByInquId(int inquId);

    InquLog findOneByInquIdAndInquDscrAndTim(int inquId, String inquDscr, Date tim);

    Optional<InquLog> findTop1ByInquIdOrderByTimDesc(int inquId);

    Optional<InquLog> findFirstByInquIdAndInquDscrLike(int inquId, String dscr);

    int countByInquIdAndState(int inquId, boolean b);

    int countByInquIdAndStateAndSndrNot(int inquId, boolean b, String usrId);
}
