// PermissionService.java
package com.seehold.service;

import com.seehold.dto.PermissionDTO;
import com.seehold.vo.PageVO;
import com.seehold.vo.PermissionVO;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    PageVO<PermissionVO> getPermissionList(Long current, Long size, String keyword, String module);

    List<PermissionVO> getAllPermissions();

    Map<String, List<PermissionVO>> getPermissionsByModule();

    PermissionVO getPermissionById(Long id);

    PermissionVO createPermission(PermissionDTO permissionDTO);

    PermissionVO updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);

    void updateStatus(Long id, Integer status);
}