package com.seehold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seehold.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    void deleteByRoleId(@Param("roleId") Long roleId);
}