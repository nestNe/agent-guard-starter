package com.seehold.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum StatusEnum {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    @EnumValue
    private final int value;

    @JsonValue
    private final String label;

    StatusEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }
}