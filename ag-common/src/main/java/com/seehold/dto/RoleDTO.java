package com.seehold.dto;

import com.seehold.enums.RoleScopeEnum;
import com.seehold.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {

    private Long id;

    @NotBlank(message = "角色标识不能为空")
    private String name;

    @NotBlank(message = "角色名称不能为空")
    private String label;

    private String description;

    private RoleScopeEnum scope;

    private StatusEnum status;

    private List<Long> permissionIds;
}