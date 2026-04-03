// RoleServiceImpl.java
package com.seehold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehold.dto.RoleDTO;
import com.seehold.entity.Permission;
import com.seehold.entity.Role;
import com.seehold.entity.RolePermission;
import com.seehold.enums.StatusEnum;
import com.seehold.exception.BusinessException;
import com.seehold.mapper.PermissionMapper;
import com.seehold.mapper.RoleMapper;
import com.seehold.mapper.RolePermissionMapper;
import com.seehold.service.RoleService;
import com.seehold.vo.PageVO;
import com.seehold.vo.PermissionVO;
import com.seehold.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public PageVO<RoleVO> getRoleList(Long current, Long size, String keyword) {
        Page<Role> page = new Page<>(current, size);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Role::getName, keyword)
                    .or()
                    .like(Role::getLabel, keyword);
        }

        wrapper.orderByDesc(Role::getCreatedAt);
        roleMapper.selectPage(page, wrapper);

        List<RoleVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(current, size, page.getTotal(), records);
    }

    @Override
    public List<RoleVO> getAllRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, StatusEnum.ENABLED);
        List<Role> roles = roleMapper.selectList(wrapper);
        return roles.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public RoleVO getRoleById(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return convertToVO(role);
    }

    @Override
    @Transactional
    public RoleVO createRole(RoleDTO roleDTO) {
        // 检查角色标识
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, roleDTO.getName());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色标识已存在");
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        roleMapper.insert(role);

        // 分配权限
        if (roleDTO.getPermissionIds() != null && !roleDTO.getPermissionIds().isEmpty()) {
            assignPermissions(role.getId(), roleDTO.getPermissionIds());
        }

        return convertToVO(role);
    }

    @Override
    @Transactional
    public RoleVO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 检查角色标识是否被其他角色使用
        if (!role.getName().equals(roleDTO.getName())) {
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Role::getName, roleDTO.getName())
                    .ne(Role::getId, id);
            if (roleMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("角色标识已存在");
            }
        }

        role.setName(roleDTO.getName());
        role.setLabel(roleDTO.getLabel());
        role.setDescription(roleDTO.getDescription());
        role.setScope(roleDTO.getScope());
        role.setStatus(roleDTO.getStatus());
        roleMapper.updateById(role);

        // 更新权限
        if (roleDTO.getPermissionIds() != null) {
            assignPermissions(id, roleDTO.getPermissionIds());
        }

        return convertToVO(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleId(id);

        // 删除角色
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 删除旧的权限
        rolePermissionMapper.deleteByRoleId(roleId);

        // 分配新的权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permissionId);
                rolePermissionMapper.insert(rp);
            }
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        role.setStatus(status == 1 ? StatusEnum.ENABLED : StatusEnum.DISABLED);
        roleMapper.updateById(role);
    }

    private RoleVO convertToVO(Role role) {
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);

        // 加载权限
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, role.getId());
        List<RolePermission> rps = rolePermissionMapper.selectList(wrapper);

        if (!rps.isEmpty()) {
            List<Long> permissionIds = rps.stream()
                    .map(RolePermission::getPermissionId)
                    .collect(Collectors.toList());

            List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
            List<PermissionVO> permissionVOs = permissions.stream().map(p -> {
                PermissionVO pv = new PermissionVO();
                BeanUtils.copyProperties(p, pv);
                return pv;
            }).collect(Collectors.toList());

            vo.setPermissions(permissionVOs);
        }

        return vo;
    }
}