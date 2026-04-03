package com.seehold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seehold.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT r.* FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1 " +
            "AND (ur.expires_at IS NULL OR ur.expires_at > NOW())")
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    @Select("SELECT p.id, p.name, p.label, p.module, p.status " +
            "FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.status = 1")
    List<Role> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}