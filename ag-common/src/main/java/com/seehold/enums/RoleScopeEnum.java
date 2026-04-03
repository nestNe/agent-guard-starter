package com.seehold.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoleScopeEnum {
    BACKEND(1, "后台"),
    FRONTEND(2, "前台");

    @EnumValue
    private final int value;

    @JsonValue
    private final String label;

    RoleScopeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }
}