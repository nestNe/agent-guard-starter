// RoleService.java
package com.seehold.service;

import com.seehold.dto.RoleDTO;
import com.seehold.vo.PageVO;
import com.seehold.vo.RoleVO;

import java.util.List;

public interface RoleService {

    PageVO<RoleVO> getRoleList(Long current, Long size, String keyword);

    List<RoleVO> getAllRoles();

    RoleVO getRoleById(Long id);

    RoleVO createRole(RoleDTO roleDTO);

    RoleVO updateRole(Long id, RoleDTO roleDTO);

    void deleteRole(Long id);

    void assignPermissions(Long roleId, List<Long> permissionIds);

    void updateStatus(Long id, Integer status);
}