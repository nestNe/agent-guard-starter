package com.seehold.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehold.enums.RoleScopeEnum;
import com.seehold.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roles")
public class Role extends BaseEntity {

    private String name;

    private String label;

    private String description;

    private RoleScopeEnum scope;

    private StatusEnum status;

    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}