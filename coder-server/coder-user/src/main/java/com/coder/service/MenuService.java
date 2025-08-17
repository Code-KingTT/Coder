package com.coder.service;

import com.coder.dto.MenuCreateDTO;
import com.coder.dto.MenuQueryDTO;
import com.coder.dto.MenuUpdateDTO;
import com.coder.vo.MenuTreeVO;
import com.coder.vo.MenuVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface MenuService {

    /**
     * 创建菜单
     *
     * @param createDTO 创建菜单DTO
     * @return 菜单ID
     */
    Long createMenu(MenuCreateDTO createDTO);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    Boolean deleteMenu(Long id);

    /**
     * 更新菜单
     *
     * @param updateDTO 更新菜单DTO
     * @return 是否成功
     */
    Boolean updateMenu(MenuUpdateDTO updateDTO);

    /**
     * 根据ID查询菜单
     *
     * @param id 菜单ID
     * @return 菜单VO
     */
    MenuVO getMenuById(Long id);

    /**
     * 分页查询菜单列表
     *
     * @param queryDTO 查询条件
     * @return 分页菜单列表
     */
    PageInfo<MenuVO> getMenuList(MenuQueryDTO queryDTO);

    /**
     * 查询菜单总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long getMenuCount(MenuQueryDTO queryDTO);

    /**
     * 批量删除菜单
     *
     * @param ids 菜单ID列表
     * @return 是否成功
     */
    Boolean deleteBatchMenus(List<Long> ids);

    /**
     * 查询所有启用的菜单
     *
     * @return 菜单列表
     */
    List<MenuVO> getEnabledMenus();

    /**
     * 查询菜单树
     *
     * @return 菜单树列表
     */
    List<MenuTreeVO> getMenuTree();

    /**
     * 根据父菜单ID查询子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<MenuVO> getMenusByParentId(Long parentId);

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuVO> getMenusByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<MenuVO> getMenusByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树
     *
     * @param userId 用户ID
     * @return 菜单树列表
     */
    List<MenuTreeVO> getMenuTreeByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getPermissionsByUserId(Long userId);

    /**
     * 检查菜单是否有子菜单
     *
     * @param parentId 父菜单ID
     * @return 是否有子菜单
     */
    Boolean checkMenuHasChildren(Long parentId);

    /**
     * 启用菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    Boolean enableMenu(Long id);

    /**
     * 禁用菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    Boolean disableMenu(Long id);
}