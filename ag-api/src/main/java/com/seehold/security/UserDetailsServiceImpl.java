// UserDetailsServiceImpl.java
package com.seehold.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehold.entity.User;
import com.seehold.enums.StatusEnum;
import com.seehold.exception.BusinessException;
import com.seehold.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 支持用户名或邮箱登录
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, username);

        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        if (user.getStatus() == StatusEnum.DISABLED) {
            throw new BusinessException("账号已被禁用");
        }

        // 加载权限
        List<String> permissions = userMapper.selectPermissionNamesByUserId(user.getId());

        return UserDetailsImpl.build(user, permissions);
    }
}