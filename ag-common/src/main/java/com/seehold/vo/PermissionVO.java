package com.seehold.vo;

import com.seehold.enums.StatusEnum;
import lombok.Data;

@Data
public class PermissionVO {

    private Long id;
    private String name;
    private String label;
    private String module;
    private StatusEnum status;
}