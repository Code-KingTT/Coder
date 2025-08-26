package com.coder.controller;

import com.coder.dto.RoleCreateDTO;
import com.coder.dto.RoleQueryDTO;
import com.coder.dto.RoleUpdateDTO;
import com.coder.result.Result;
import com.coder.service.RoleService;
import com.coder.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色控制器
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/role")
@Api(tags = "角色管理")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/create")
    @ApiOperation("创建角色")
    public Result<Long> createRole(@Valid @RequestBody RoleCreateDTO createDTO) {
        Long roleId = roleService.createRole(createDTO);
        return Result.success("角色创建成功", roleId);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除角色")
    public Result<Void> deleteRole(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long id) {
        roleService.deleteRole(id);
        return Result.success("角色删除成功");
    }

    @PutMapping("/update")
    @ApiOperation("更新角色")
    public Result<Void> updateRole(@Valid @RequestBody RoleUpdateDTO updateDTO) {
        roleService.updateRole(updateDTO);
        return Result.success("角色更新成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询角色")
    public Result<RoleVO> getRoleById(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long id) {
        RoleVO roleVO = roleService.getRoleById(id);
        return Result.success("查询成功", roleVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询角色列表")
    public Result<PageInfo<RoleVO>> getRoleList(RoleQueryDTO queryDTO) {
        PageInfo<RoleVO> pageInfo = roleService.getRoleList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除角色")
    public Result<Void> deleteBatchRoles(
            @ApiParam(value = "角色ID列表", required = true)
            @RequestBody @NotEmpty(message = "角色ID列表不能为空") List<Long> ids) {
        roleService.deleteBatchRoles(ids);
        return Result.success("批量删除成功");
    }

    @GetMapping("/check-code")
    @ApiOperation("检查角色编码是否存在")
    public Result<Boolean> checkRoleCodeExists(
            @ApiParam(value = "角色编码", required = true)
            @RequestParam @NotNull(message = "角色编码不能为空") String roleCode) {
        Boolean exists = roleService.checkRoleCodeExists(roleCode);
        return Result.success("查询成功", exists);
    }

    @GetMapping("/enabled")
    @ApiOperation("查询所有启用的角色")
    public Result<List<RoleVO>> getEnabledRoles() {
        List<RoleVO> roles = roleService.getEnabledRoles();
        return Result.success("查询成功", roles);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID查询角色列表")
    public Result<List<RoleVO>> getRolesByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<RoleVO> roles = roleService.getRolesByUserId(userId);
        return Result.success("查询成功", roles);
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("启用角色")
    public Result<Void> enableRole(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long id) {
        roleService.enableRole(id);
        return Result.success("角色启用成功");
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("禁用角色")
    public Result<Void> disableRole(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long id) {
        roleService.disableRole(id);
        return Result.success("角色禁用成功");
    }
}