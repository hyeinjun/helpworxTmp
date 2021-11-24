package com.benit.helpworx.notfc.resource;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotfcResource {
    private int notfcId;
    private String Msg;
    private String trgetUrl;
}
