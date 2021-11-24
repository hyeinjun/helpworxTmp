package com.benit.helpworx.sr.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SrStatusBarChartDataSetResource {

    // reqGb
    private String label;

    // company 순서대로 count 배열
    private Long[] data;
}
