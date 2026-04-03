package com.seehold.dto;

import com.seehold.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionDTO {

    private Long id;

    @NotBlank(message = "权限标识不能为空")
    private String name;

    @NotBlank(message = "权限名称不能为空")
    private String label;

    @NotBlank(message = "所属模块不能为空")
    private String module;

    private StatusEnum status;
}