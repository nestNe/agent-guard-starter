// AuthController.java
package com.seehold.controller;

import com.seehold.result.Result;
import com.seehold.dto.LoginDTO;
import com.seehold.dto.RegisterDTO;
import com.seehold.security.UserDetailsImpl;
import com.seehold.service.AuthService;
import com.seehold.vo.LoginVO;
import com.seehold.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return Result.success(authService.register(registerDTO));
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return Result.success(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(userDetails.getId());
        return Result.success();
    }

    @GetMapping("/me")
    public Result<UserVO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 这里可以从缓存或重新查询获取完整信息
        return Result.success();
    }
}