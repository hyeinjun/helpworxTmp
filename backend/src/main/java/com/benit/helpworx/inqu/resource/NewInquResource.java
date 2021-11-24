package com.benit.helpworx.inqu.resource;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewInquResource {
    private int inquId;
    private String sbjct;
    private String inquUsrId;
    private String coCd;
    private String tim;
}
