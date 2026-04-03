// RoleController.java
package com.seehold.controller;

import com.seehold.result.Result;
import com.seehold.dto.RoleDTO;
import com.seehold.service.RoleService;
import com.seehold.vo.PageVO;
import com.seehold.vo.RoleVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('role:list')")
    public Result<PageVO<RoleVO>> list(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword) {
        return Result.success(roleService.getRoleList(current, size, keyword));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('role:list')")
    public Result<List<RoleVO>> getAll() {
        return Result.success(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:view')")
    public Result<RoleVO> getById(@PathVariable Long id) {
        return Result.success(roleService.getRoleById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public Result<RoleVO> create(@Valid @RequestBody RoleDTO roleDTO) {
        return Result.success(roleService.createRole(roleDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<RoleVO> update(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        return Result.success(roleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:assign-perm')")
    public Result<Void> assignPermissions(
            @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('role:update')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        roleService.updateStatus(id, status);
        return Result.success();
    }
}