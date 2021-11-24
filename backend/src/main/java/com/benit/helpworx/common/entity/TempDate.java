package com.benit.helpworx.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 2020-01-01 ~ 2021-12-31 까지의 날짜 정보가 들어 있는 Table
 */
@Entity
@Table(name = "temp_date")
public class TempDate {

    @Id
    @Column(name = "date")
    private Date date;
}
