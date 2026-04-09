// AuthServiceImpl.java
package com.seehold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seehold.dto.LoginDTO;
import com.seehold.dto.RegisterDTO;
import com.seehold.entity.Role;
import com.seehold.entity.User;
import com.seehold.enums.StatusEnum;
import com.seehold.exception.BusinessException;
import com.seehold.exception.UnauthorizedException;
import com.seehold.mapper.RoleMapper;
import com.seehold.mapper.UserMapper;
import com.seehold.mapper.UserRoleMapper;
import com.seehold.service.AuthService;
import com.seehold.constant.RedisKeyConstant;
import com.seehold.utils.JwtUtil;
import com.seehold.vo.LoginVO;
import com.seehold.vo.UserVO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${seehold.default.role-name}")
    private String roleName = "user";


    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getAccount())
                .or()
                .eq(User::getEmail, loginDTO.getAccount());

        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        if (user.getStatus() == StatusEnum.DISABLED) {
            throw new UnauthorizedException("账号已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成Token
        String accessToken = jwtUtil.generateToken(user.getId(), user.getUsername(), false);
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getUsername(), true);

        // 存储到Redis
        String userTokenKey = RedisKeyConstant.USER_TOKEN_PREFIX + user.getId();
        redisTemplate.opsForValue().set(userTokenKey, accessToken, 1, TimeUnit.DAYS);

        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setExpiresIn(86400L);
        loginVO.setUserInfo(buildUserVO(user));

        return loginVO;
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException("刷新令牌已过期");
        }

        Claims claims = jwtUtil.parseToken(refreshToken);
        if (!"refresh".equals(claims.get("type"))) {
            throw new UnauthorizedException("无效的刷新令牌");
        }

        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get("username", String.class);

        // 生成新的访问令牌
        String newAccessToken = jwtUtil.generateToken(userId, username, false);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(newAccessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setExpiresIn(86400L);

        return loginVO;
    }

    @Override
    @Transactional
    public UserVO register(RegisterDTO registerDTO) {
        // 检查用户名
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱
        wrapper.clear();
        wrapper.eq(User::getEmail, registerDTO.getEmail());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setStatus(StatusEnum.ENABLED);

        userMapper.insert(user);

        // 设置默认角色
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq("name", roleName);
        Role defaultRole = roleMapper.selectOne(qw);
        if (defaultRole != null) {
            userRoleMapper.insertUserRole(user.getId(), defaultRole.getId(), "0");
        }
        return buildUserVO(user);
    }

    @Override
    public void logout(Long userId) {
        String userTokenKey = RedisKeyConstant.USER_TOKEN_PREFIX + userId;
        String token = (String) redisTemplate.opsForValue().get(userTokenKey);
        if (token != null) {
            // 加入黑名单
            redisTemplate.opsForValue().set(
                    RedisKeyConstant.TOKEN_BLACKLIST_PREFIX + token,
                    "logout",
                    1,
                    TimeUnit.DAYS
            );
            redisTemplate.delete(userTokenKey);
        }
    }

    private UserVO buildUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setLastLoginAt(user.getLastLoginAt());
        vo.setCreatedAt(user.getCreatedAt());

        // 加载角色和权限
        List<String> roles = userMapper.selectRoleNamesByUserId(user.getId());
        List<String> permissions = userMapper.selectPermissionNamesByUserId(user.getId());
        vo.setRoles(roles);
        vo.setPermissions(permissions);

        return vo;
    }
}