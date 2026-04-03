package com.seehold.vo;

import com.seehold.enums.RoleScopeEnum;
import com.seehold.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleVO {

    private Long id;
    private String name;
    private String label;
    private String description;
    private RoleScopeEnum scope;
    private StatusEnum status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private List<PermissionVO> permissions;
}