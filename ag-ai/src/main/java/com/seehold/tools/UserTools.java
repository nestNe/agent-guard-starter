package com.seehold.tools;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.seehold.entity.*;
import com.seehold.mapper.*;
import com.seehold.query.UserQuery;
import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component

@AllArgsConstructor
public class UserTools {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;


    @Tool(description = "根据查询条件查询用户列表")
    public List<User> userQuery(@ToolParam(description = "用户查询的条件") UserQuery query) {
        if (query == null) {
            return List.of();
        }

        QueryWrapper<User> qw = new QueryWrapper<>();

        qw.eq(StringUtils.isNotBlank(query.getUsername()), "username", query.getUsername())
                .likeRight(StringUtils.isNotBlank(query.getEmail()), "email", query.getEmail())
                .eq(query.getStatus() != null, "status", query.getStatus())
                .ge(query.getLastLoginAt() != null, "last_login_at", query.getLastLoginAt());

        return userMapper.selectList(qw);
    }

    @Tool(description = "根据用户ID查询用户所属角色")
    public Role userRoles(@ToolParam(description = "用户ID") Long userId) {
        if (userId == null) {
            return null;
        }
        QueryWrapper<UserRole> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        UserRole userRole = userRoleMapper.selectOne(qw);
        if (userRole == null || userRole.getRoleId() == null)
            return null;
        return roleMapper.selectById(userRole.getRoleId());
    }

    @Tool(description = "根据角色查询该角色对应的权限有哪些")
    public List<Permission> rolePermissions(@ToolParam(description = "角色ID") Long roleId) {
        if (roleId == null) {
            return List.of();
        }
        //1.角色-权限
        List<RolePermission> rpList = rolePermissionMapper.selectList(
                new QueryWrapper<RolePermission>().eq("role_id", roleId)
        );

        if (rpList == null || rpList.isEmpty())
            return List.of();

        List<Long> ids = rpList.stream()
                .map(RolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()//去重
                .toList();

        return permissionMapper.selectBatchIds(ids);
    }


}
