// PermissionServiceImpl.java
package com.seehold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehold.dto.PermissionDTO;
import com.seehold.entity.Permission;
import com.seehold.enums.StatusEnum;
import com.seehold.exception.BusinessException;
import com.seehold.mapper.PermissionMapper;
import com.seehold.service.PermissionService;
import com.seehold.vo.PageVO;
import com.seehold.vo.PermissionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public PageVO<PermissionVO> getPermissionList(Long current, Long size, String keyword, String module) {
        Page<Permission> page = new Page<>(current, size);
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Permission::getName, keyword)
                    .or()
                    .like(Permission::getLabel, keyword);
        }

        if (StringUtils.hasText(module)) {
            wrapper.eq(Permission::getModule, module);
        }

        wrapper.orderByDesc(Permission::getCreatedAt);
        permissionMapper.selectPage(page, wrapper);

        List<PermissionVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(current, size, page.getTotal(), records);
    }

    @Override
    public List<PermissionVO> getAllPermissions() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getStatus, StatusEnum.ENABLED);
        List<Permission> permissions = permissionMapper.selectList(wrapper);
        return permissions.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<PermissionVO>> getPermissionsByModule() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getStatus, StatusEnum.ENABLED);
        List<Permission> permissions = permissionMapper.selectList(wrapper);

        return permissions.stream()
                .map(this::convertToVO)
                .collect(Collectors.groupingBy(PermissionVO::getModule));
    }

    @Override
    public PermissionVO getPermissionById(Long id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        return convertToVO(permission);
    }

    @Override
    @Transactional
    public PermissionVO createPermission(PermissionDTO permissionDTO) {
        // 检查权限标识
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getName, permissionDTO.getName());
        if (permissionMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("权限标识已存在");
        }

        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permissionMapper.insert(permission);

        return convertToVO(permission);
    }

    @Override
    @Transactional
    public PermissionVO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        // 检查权限标识是否被其他权限使用
        if (!permission.getName().equals(permissionDTO.getName())) {
            LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Permission::getName, permissionDTO.getName())
                    .ne(Permission::getId, id);
            if (permissionMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("权限标识已存在");
            }
        }

        permission.setName(permissionDTO.getName());
        permission.setLabel(permissionDTO.getLabel());
        permission.setModule(permissionDTO.getModule());
        permission.setStatus(permissionDTO.getStatus());
        permissionMapper.updateById(permission);

        return convertToVO(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        permissionMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        permission.setStatus(status == 1 ? StatusEnum.ENABLED : StatusEnum.DISABLED);
        permissionMapper.updateById(permission);
    }

    private PermissionVO convertToVO(Permission permission) {
        PermissionVO vo = new PermissionVO();
        BeanUtils.copyProperties(permission, vo);
        return vo;
    }
}