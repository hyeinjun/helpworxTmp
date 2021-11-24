package com.benit.helpworx.sr.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
@AllArgsConstructor
public class RqmtTimeLineChartResource {

    private String[] label;

    private double[] rcptData;

    private double[] treatData;
}
