package com.coder.controller;

import com.coder.dto.MenuCreateDTO;
import com.coder.dto.MenuQueryDTO;
import com.coder.dto.MenuUpdateDTO;
import com.coder.result.Result;
import com.coder.service.MenuService;
import com.coder.vo.MenuTreeVO;
import com.coder.vo.MenuVO;
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
 * 菜单控制器
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/menu")
@Api(tags = "菜单管理")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("/create")
    @ApiOperation("创建菜单")
    public Result<Long> createMenu(@Valid @RequestBody MenuCreateDTO createDTO) {
        Long menuId = menuService.createMenu(createDTO);
        return Result.success("菜单创建成功", menuId);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除菜单")
    public Result<Void> deleteMenu(
            @ApiParam(value = "菜单ID", required = true)
            @PathVariable @NotNull(message = "菜单ID不能为空") Long id) {
        menuService.deleteMenu(id);
        return Result.success("菜单删除成功");
    }

    @PutMapping("/update")
    @ApiOperation("更新菜单")
    public Result<Void> updateMenu(@Valid @RequestBody MenuUpdateDTO updateDTO) {
        menuService.updateMenu(updateDTO);
        return Result.success("菜单更新成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询菜单")
    public Result<MenuVO> getMenuById(
            @ApiParam(value = "菜单ID", required = true)
            @PathVariable @NotNull(message = "菜单ID不能为空") Long id) {
        MenuVO menuVO = menuService.getMenuById(id);
        return Result.success("查询成功", menuVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询菜单列表")
    public Result<PageInfo<MenuVO>> getMenuList(MenuQueryDTO queryDTO) {
        PageInfo<MenuVO> pageInfo = menuService.getMenuList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除菜单")
    public Result<Void> deleteBatchMenus(
            @ApiParam(value = "菜单ID列表", required = true)
            @RequestBody @NotEmpty(message = "菜单ID列表不能为空") List<Long> ids) {
        menuService.deleteBatchMenus(ids);
        return Result.success("批量删除成功");
    }

    @GetMapping("/enabled")
    @ApiOperation("查询所有启用的菜单")
    public Result<List<MenuVO>> getEnabledMenus() {
        List<MenuVO> menus = menuService.getEnabledMenus();
        return Result.success("查询成功", menus);
    }

    @GetMapping("/tree")
    @ApiOperation("查询菜单树")
    public Result<List<MenuTreeVO>> getMenuTree() {
        List<MenuTreeVO> menuTree = menuService.getMenuTree();
        return Result.success("查询成功", menuTree);
    }

    @GetMapping("/children/{parentId}")
    @ApiOperation("根据父菜单ID查询子菜单")
    public Result<List<MenuVO>> getMenusByParentId(
            @ApiParam(value = "父菜单ID", required = true)
            @PathVariable @NotNull(message = "父菜单ID不能为空") Long parentId) {
        List<MenuVO> menus = menuService.getMenusByParentId(parentId);
        return Result.success("查询成功", menus);
    }

    @GetMapping("/role/{roleId}")
    @ApiOperation("根据角色ID查询菜单列表")
    public Result<List<MenuVO>> getMenusByRoleId(
            @ApiParam(value = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        List<MenuVO> menus = menuService.getMenusByRoleId(roleId);
        return Result.success("查询成功", menus);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID查询菜单列表")
    public Result<List<MenuVO>> getMenusByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<MenuVO> menus = menuService.getMenusByUserId(userId);
        return Result.success("查询成功", menus);
    }

    @GetMapping("/user/{userId}/tree")
    @ApiOperation("根据用户ID查询菜单树")
    public Result<List<MenuTreeVO>> getMenuTreeByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<MenuTreeVO> menuTree = menuService.getMenuTreeByUserId(userId);
        return Result.success("查询成功", menuTree);
    }

    @GetMapping("/user/{userId}/permissions")
    @ApiOperation("根据用户ID查询权限标识列表")
    public Result<List<String>> getPermissionsByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<String> permissions = menuService.getPermissionsByUserId(userId);
        return Result.success("查询成功", permissions);
    }

    @GetMapping("/check-children/{parentId}")
    @ApiOperation("检查菜单是否有子菜单")
    public Result<Boolean> checkMenuHasChildren(
            @ApiParam(value = "父菜单ID", required = true)
            @PathVariable @NotNull(message = "父菜单ID不能为空") Long parentId) {
        Boolean hasChildren = menuService.checkMenuHasChildren(parentId);
        return Result.success("查询成功", hasChildren);
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("启用菜单")
    public Result<Void> enableMenu(
            @ApiParam(value = "菜单ID", required = true)
            @PathVariable @NotNull(message = "菜单ID不能为空") Long id) {
        menuService.enableMenu(id);
        return Result.success("菜单启用成功");
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("禁用菜单")
    public Result<Void> disableMenu(
            @ApiParam(value = "菜单ID", required = true)
            @PathVariable @NotNull(message = "菜单ID不能为空") Long id) {
        menuService.disableMenu(id);
        return Result.success("菜单禁用成功");
    }
}