package com.benit.helpworx.notfc.dto;

import com.benit.helpworx.notfc.resource.NotfcResource;
import lombok.*;

import java.util.Date;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotfcDto {
    private String func;
    private int notfcId;
    private String sbjct;
    private String state;
    private String trgetUrl;


    public NotfcResource toResource() {
        String Msg = this.func+" - '"+ this.sbjct+"' 글이 "+this.state+"되었습니다.";
        return NotfcResource.builder()
                .notfcId(this.notfcId)
                .Msg(Msg)
                .trgetUrl(this.trgetUrl).build();
    }

}
