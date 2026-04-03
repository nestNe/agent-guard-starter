// UserServiceImpl.java
package com.seehold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehold.dto.UserDTO;
import com.seehold.entity.User;
import com.seehold.entity.UserRole;
import com.seehold.enums.StatusEnum;
import com.seehold.exception.BusinessException;
import com.seehold.mapper.UserMapper;
import com.seehold.mapper.UserRoleMapper;
import com.seehold.service.UserService;
import com.seehold.vo.PageVO;
import com.seehold.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageVO<UserVO> getUserList(Long current, Long size, String keyword) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getEmail, keyword);
        }

        wrapper.orderByDesc(User::getCreatedAt);
        userMapper.selectPage(page, wrapper);

        List<UserVO> records = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(current, size, page.getTotal(), records);
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional
    public UserVO createUser(UserDTO userDTO) {
        // 检查用户名
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱
        wrapper.clear();
        wrapper.eq(User::getEmail, userDTO.getEmail());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("邮箱已被注册");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPasswordHash(passwordEncoder.encode("123456")); // 默认密码
        userMapper.insert(user);

        return convertToVO(user);
    }

    @Override
    @Transactional
    public UserVO updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户名是否被其他用户使用
        if (!user.getUsername().equals(userDTO.getUsername())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, userDTO.getUsername())
                    .ne(User::getId, id);
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("用户名已存在");
            }
        }

        // 检查邮箱是否被其他用户使用
        if (!user.getEmail().equals(userDTO.getEmail())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, userDTO.getEmail())
                    .ne(User::getId, id);
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setAvatar(userDTO.getAvatar());
        user.setStatus(userDTO.getStatus());
        userMapper.updateById(user);

        return convertToVO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 删除用户角色关联
        userRoleMapper.deleteByUserId(id);

        // 删除用户
        userMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds, Long operatorId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 删除旧的角色
        userRoleMapper.deleteByUserId(userId);

        // 分配新角色
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setGrantedBy(operatorId);
                userRole.setGrantedAt(LocalDateTime.now());
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setStatus(status == 1 ? StatusEnum.ENABLED : StatusEnum.DISABLED);
        userMapper.updateById(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        // 加载角色和权限
        List<String> roles = userMapper.selectRoleNamesByUserId(user.getId());
        List<String> permissions = userMapper.selectPermissionNamesByUserId(user.getId());
        vo.setRoles(roles);
        vo.setPermissions(permissions);

        return vo;
    }
}