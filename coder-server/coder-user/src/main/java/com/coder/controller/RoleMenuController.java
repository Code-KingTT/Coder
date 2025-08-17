package com.coder.controller;

import com.coder.dto.RoleMenuAssignDTO;
import com.coder.dto.RoleMenuQueryDTO;
import com.coder.result.Result;
import com.coder.service.RoleMenuService;
import com.coder.vo.RoleMenuDetailVO;
import com.coder.vo.RoleMenuVO;
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
 * 角色菜单关联控制器
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/role-menu")
@Api(tags = "角色菜单管理")
public class RoleMenuController {

    @Resource
    private RoleMenuService roleMenuService;

    @PostMapping("/assign")
    @ApiOperation("分配角色菜单")
    public Result<Void> assignRoleMenus(@Valid @RequestBody RoleMenuAssignDTO assignDTO) {
        roleMenuService.assignRoleMenus(assignDTO);
        return Result.success("角色菜单分配成功");
    }

    @DeleteMapping("/unassign")
    @ApiOperation("取消角色菜单分配")
    public Result<Void> unassignRoleMenus(
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId,
            @ApiParam(value = "菜单ID列表", required = true)
            @RequestBody @NotEmpty(message = "菜单ID列表不能为空") List<Long> menuIds) {
        roleMenuService.unassignRoleMenus(roleId, menuIds);
        return Result.success("取消角色菜单分配成功");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除角色菜单关联")
    public Result<Void> deleteRoleMenu(
            @ApiParam(value = "关联ID", required = true)
            @PathVariable @NotNull(message = "关联ID不能为空") Long id) {
        roleMenuService.deleteRoleMenu(id);
        return Result.success("角色菜单关联删除成功");
    }

    @DeleteMapping("/role/{roleId}")
    @ApiOperation("根据角色ID删除所有菜单关联")
    public Result<Void> deleteRoleMenusByRoleId(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        roleMenuService.deleteRoleMenusByRoleId(roleId);
        return Result.success("角色菜单关联删除成功");
    }

    @DeleteMapping("/menu/{menuId}")
    @ApiOperation("根据菜单ID删除所有角色关联")
    public Result<Void> deleteRoleMenusByMenuId(
            @ApiParam(value = "菜单ID", required = true)
            @PathVariable @NotNull(message = "菜单ID不能为空") Long menuId) {
        roleMenuService.deleteRoleMenusByMenuId(menuId);
        return Result.success("角色菜单关联删除成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询角色菜单关联")
    public Result<RoleMenuVO> getRoleMenuById(
            @ApiParam(value = "关联ID", required = true)
            @PathVariable @NotNull(message = "关联ID不能为空") Long id) {
        RoleMenuVO roleMenuVO = roleMenuService.getRoleMenuById(id);
        return Result.success("查询成功", roleMenuVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询角色菜单关联列表")
    public Result<PageInfo<RoleMenuVO>> getRoleMenuList(RoleMenuQueryDTO queryDTO) {
        PageInfo<RoleMenuVO> pageInfo = roleMenuService.getRoleMenuList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @GetMapping("/role/{roleId}/menus")
    @ApiOperation("根据角色ID查询菜单ID列表")
    public Result<List<Long>> getMenuIdsByRoleId(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        List<Long> menuIds = roleMenuService.getMenuIdsByRoleId(roleId);
        return Result.success("查询成功", menuIds);
    }

    @GetMapping("/menu/{menuId}/roles")
    @ApiOperation("根据菜单ID查询角色ID列表")
    public Result<List<Long>> getRoleIdsByMenuId(
            @ApiParam(value = "菜单ID", required = true)
            @PathVariable @NotNull(message = "菜单ID不能为空") Long menuId) {
        List<Long> roleIds = roleMenuService.getRoleIdsByMenuId(menuId);
        return Result.success("查询成功", roleIds);
    }

    @GetMapping("/role/{roleId}/detail")
    @ApiOperation("根据角色ID查询角色菜单详情")
    public Result<RoleMenuDetailVO> getRoleMenuDetail(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        RoleMenuDetailVO detail = roleMenuService.getRoleMenuDetail(roleId);
        return Result.success("查询成功", detail);
    }

    @GetMapping("/check")
    @ApiOperation("检查角色是否拥有菜单")
    public Result<Boolean> checkRoleHasMenu(
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId,
            @ApiParam(value = "菜单ID", required = true)
            @RequestParam @NotNull(message = "菜单ID不能为空") Long menuId) {
        Boolean hasMenu = roleMenuService.checkRoleHasMenu(roleId, menuId);
        return Result.success("查询成功", hasMenu);
    }

    @GetMapping("/check-permission")
    @ApiOperation("检查角色是否拥有指定权限")
    public Result<Boolean> checkRoleHasPermission(
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId,
            @ApiParam(value = "权限标识", required = true)
            @RequestParam @NotNull(message = "权限标识不能为空") String permission) {
        Boolean hasPermission = roleMenuService.checkRoleHasPermission(roleId, permission);
        return Result.success("查询成功", hasPermission);
    }

    @PostMapping("/batch-assign-menus")
    @ApiOperation("批量分配菜单到角色")
    public Result<Void> batchAssignMenusToRole(
            @ApiParam(value = "菜单ID列表", required = true)
            @RequestBody @NotEmpty(message = "菜单ID列表不能为空") List<Long> menuIds,
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId) {
        roleMenuService.batchAssignMenusToRole(menuIds, roleId);
        return Result.success("批量分配菜单权限成功");
    }

    @DeleteMapping("/batch-unassign-menus")
    @ApiOperation("批量取消菜单角色分配")
    public Result<Void> batchUnassignMenusFromRole(
            @ApiParam(value = "菜单ID列表", required = true)
            @RequestBody @NotEmpty(message = "菜单ID列表不能为空") List<Long> menuIds,
            @ApiParam(value = "角色ID", required = true)
            @RequestParam @NotNull(message = "角色ID不能为空") Long roleId) {
        roleMenuService.batchUnassignMenusFromRole(menuIds, roleId);
        return Result.success("批量取消菜单权限分配成功");
    }

    @PostMapping("/copy")
    @ApiOperation("复制角色权限")
    public Result<Void> copyRoleMenus(
            @ApiParam(value = "源角色ID", required = true)
            @RequestParam @NotNull(message = "源角色ID不能为空") Long sourceRoleId,
            @ApiParam(value = "目标角色ID", required = true)
            @RequestParam @NotNull(message = "目标角色ID不能为空") Long targetRoleId) {
        roleMenuService.copyRoleMenus(sourceRoleId, targetRoleId);
        return Result.success("角色权限复制成功");
    }
}