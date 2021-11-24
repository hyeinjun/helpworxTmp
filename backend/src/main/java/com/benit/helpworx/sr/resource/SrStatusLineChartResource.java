package com.benit.helpworx.sr.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SrStatusLineChartResource {

    private String[] label;

    private long[] data;
}
