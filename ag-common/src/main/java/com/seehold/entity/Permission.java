package com.seehold.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.seehold.enums.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("permissions")
public class Permission extends BaseEntity {

    private String name;

    private String label;

    private String module;

    private StatusEnum status;
}