package com.benit.helpworx.common.entity;

import com.benit.helpworx.comng.dto.CoDto;
import com.benit.helpworx.menu.dto.MenuDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
public class Menu {
    @Id
    @Column(name = "menu_id")
    @NotNull
    private int menuId;

    @Column(name = "menu_nm")
    @NotNull
    private String menuNm;

    @Column(name = "acces_auth")
    @NotNull
    private String accesAuth;

    @Column(name = "router_addr")
    @NotNull
    private String routerAddr;

    public MenuDto toDto() {
        return MenuDto.builder()
                .menuNm(this.menuNm)
                .routerAddr(this.routerAddr)
                .build();
    }
}
