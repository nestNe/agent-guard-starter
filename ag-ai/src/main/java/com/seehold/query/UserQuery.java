package com.seehold.query;

import com.seehold.enums.StatusEnum;
import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;

@Data
public class UserQuery {

    @ToolParam(required = false, description = "用户名")
    private String username;

    @ToolParam(required = false, description = "用户邮箱")
    private String email;

    @ToolParam(required = false, description = "用户状态：0-禁用，1-启用")
    private StatusEnum status;

    @ToolParam(required = false, description = "最后登入时间")
    private LocalDateTime lastLoginAt;

}
