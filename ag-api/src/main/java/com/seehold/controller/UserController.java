// UserController.java
package com.seehold.controller;

import com.seehold.result.Result;
import com.seehold.dto.UserDTO;
import com.seehold.security.UserDetailsImpl;
import com.seehold.service.UserService;
import com.seehold.vo.PageVO;
import com.seehold.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:list')")
    public Result<PageVO<UserVO>> list(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword) {
        return Result.success(userService.getUserList(current, size, keyword));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:view')")
    public Result<UserVO> getById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public Result<UserVO> create(@Valid @RequestBody UserDTO userDTO) {
        return Result.success(userService.createUser(userDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<UserVO> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return Result.success(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('user:assign-role')")
    public Result<Void> assignRoles(
            @PathVariable Long id,
            @RequestBody List<Long> roleIds,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.assignRoles(id, roleIds, userDetails.getId());
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }
}