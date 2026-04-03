package com.seehold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seehold.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}