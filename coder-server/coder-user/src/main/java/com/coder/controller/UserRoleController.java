package com.coder.controller;

import com.coder.dto.UserRoleAssignDTO;
import com.coder.dto.UserRoleQueryDTO;
import com.coder.result.Result;
import com.coder.service.UserRoleService;
import com.coder.vo.UserRoleDetailVO;
import com.coder.vo.UserRoleVO;
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
 * 用户角色关联控制器
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/user-role")
@Api(tags = "用户角色管理")
public class UserRoleController {

    @Resource
    private UserRoleService userRoleService;

    @PostMapping("/assign")
    @ApiOperation("分配用户角色")
    public Result<Void> assignUserRoles(@Valid @RequestBody UserRoleAssignDTO assignDTO) {
        userRoleService.assignUserRoles(assignDTO);
        return Result.success("用户角色分配成功");
    }

    @DeleteMapping("/unassign")
    @ApiOperation("取消用户角色分配")
    public Result<Void> unassignUserRoles(
            @ApiParam(value = "用户ID", required = true)
            @RequestParam @NotNull(message = "用户ID不能为空") Long userId,
            @ApiParam(value = "角色ID列表", required = true)
            @RequestBody @NotEmpty(message = "角色ID列表不能为空") List<Long> roleIds) {
        userRoleService.unassignUserRoles(userId, roleIds);
        return Result.success("取消用户角色分配成功");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除用户角色关联")
    public Result<Void> deleteUserRole(
            @ApiParam(value = "关联ID", required = true)
            @PathVariable @NotNull(message = "关联ID不能为空") Long id) {
        userRoleService.deleteUserRole(id);
        return Result.success("用户角色关联删除成功");
    }

    @DeleteMapping("/user/{userId}")
    @ApiOperation("根据用户ID删除所有角色关联")
    public Result<Void> deleteUserRolesByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        userRoleService.deleteUserRolesByUserId(userId);
        return Result.success("用户角色关联删除成功");
    }

    @DeleteMapping("/role/{roleId}")
    @ApiOperation("根据角色ID删除所有用户关联")
    public Result<Void> deleteUserRolesByRoleId(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        userRoleService.deleteUserRolesByRoleId(roleId);
        return Result.success("用户角色关联删除成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询用户角色关联")
    public Result<UserRoleVO> getUserRoleById(
            @ApiParam(value = "关联ID", required = true)
            @PathVariable @NotNull(message = "关联ID不能为空") Long id) {
        UserRoleVO userRoleVO = userRoleService.getUserRoleById(id);
        return Result.success("查询成功", userRoleVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询用户角色关联列表")
    public Result<PageInfo<UserRoleVO>> getUserRoleList(UserRoleQueryDTO queryDTO) {
        PageInfo<UserRoleVO> pageInfo = userRoleService.getUserRoleList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @GetMapping("/user/{userId}/roles")
    @ApiOperation("根据用户ID查询角色ID列表")
    public Result<List<Long>> getRoleIdsByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        return Result.success("查询成功", roleIds);
    }

    @GetMapping("/role/{roleId}/users")
    @ApiOperation("根据角色ID查询用户ID列表")
    public Result<List<Long>> getUserIdsByRoleId(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        List<Long> userIds = userRoleService.getUserIdsByRoleId(roleId);
        return Result.success("查询成功", userIds);
    }

    @GetMapping("/user/{userId}/detail")
    @ApiOperation("根据用户ID查询用户角色详情")
    public Result<UserRoleDetailVO> getUserRoleDetail(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        UserRoleDetailVO detail = userRoleService.getUserRoleDetail(userId);
        return Result.success("查询成功", detail);
    }

    @GetMapping("/check")
    @ApiOperation("检查用户是否拥有角色")
    public Result<Boolean> checkUserHasRole(
            @ApiParam(value = "用户ID", required = true)
            @RequestParam @NotNull(message = "用户ID不能为空") Long userId,
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId) {
        Boolean hasRole = userRoleService.checkUserHasRole(userId, roleId);
        return Result.success("查询成功", hasRole);
    }

    @GetMapping("/check-code")
    @ApiOperation("检查用户是否拥有指定角色编码")
    public Result<Boolean> checkUserHasRoleCode(
            @ApiParam(value = "用户ID", required = true)
            @RequestParam @NotNull(message = "用户ID不能为空") Long userId,
            @ApiParam(value = "角色编码", required = true)
            @RequestParam @NotNull(message = "角色编码不能为空") String roleCode) {
        Boolean hasRole = userRoleService.checkUserHasRoleCode(userId, roleCode);
        return Result.success("查询成功", hasRole);
    }

    @PostMapping("/batch-assign-users")
    @ApiOperation("批量分配用户到角色")
    public Result<Void> batchAssignUsersToRole(
            @ApiParam(value = "用户ID列表", required = true)
            @RequestBody @NotEmpty(message = "用户ID列表不能为空") List<Long> userIds,
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId) {
        userRoleService.batchAssignUsersToRole(userIds, roleId);
        return Result.success("批量分配用户角色成功");
    }

    @DeleteMapping("/batch-unassign-users")
    @ApiOperation("批量取消用户角色分配")
    public Result<Void> batchUnassignUsersFromRole(
            @ApiParam(value = "用户ID列表", required = true)
            @RequestBody @NotEmpty(message = "用户ID列表不能为空") List<Long> userIds,
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId) {
        userRoleService.batchUnassignUsersFromRole(userIds, roleId);
        return Result.success("批量取消用户角色分配成功");
    }
}