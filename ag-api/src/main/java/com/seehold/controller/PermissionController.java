// PermissionController.java
package com.seehold.controller;

import com.seehold.result.Result;
import com.seehold.dto.PermissionDTO;
import com.seehold.service.PermissionService;
import com.seehold.vo.PageVO;
import com.seehold.vo.PermissionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('perm:list')")
    public Result<PageVO<PermissionVO>> list(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String module) {
        return Result.success(permissionService.getPermissionList(current, size, keyword, module));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('perm:list')")
    public Result<List<PermissionVO>> getAll() {
        return Result.success(permissionService.getAllPermissions());
    }

    @GetMapping("/by-module")
    @PreAuthorize("hasAuthority('perm:list')")
    public Result<Map<String, List<PermissionVO>>> getByModule() {
        return Result.success(permissionService.getPermissionsByModule());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('perm:view')")
    public Result<PermissionVO> getById(@PathVariable Long id) {
        return Result.success(permissionService.getPermissionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('perm:create')")
    public Result<PermissionVO> create(@Valid @RequestBody PermissionDTO permissionDTO) {
        return Result.success(permissionService.createPermission(permissionDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('perm:update')")
    public Result<PermissionVO> update(@PathVariable Long id, @Valid @RequestBody PermissionDTO permissionDTO) {
        return Result.success(permissionService.updatePermission(id, permissionDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('perm:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('perm:update')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        permissionService.updateStatus(id, status);
        return Result.success();
    }
}