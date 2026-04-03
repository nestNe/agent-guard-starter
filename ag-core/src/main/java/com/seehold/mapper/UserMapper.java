package com.seehold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seehold.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT r.name FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 1 " +
            "AND (ur.expires_at IS NULL OR ur.expires_at > NOW())")
    List<String> selectRoleNamesByUserId(@Param("userId") Long userId);

    @Select("SELECT p.name FROM permissions p " +
            "INNER JOIN role_permissions rp ON p.id = rp.permission_id " +
            "INNER JOIN user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND p.status = 1 " +
            "AND (ur.expires_at IS NULL OR ur.expires_at > NOW())")
    List<String> selectPermissionNamesByUserId(@Param("userId") Long userId);
}