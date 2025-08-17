package com.coder.service;

import com.coder.dto.RoleMenuAssignDTO;
import com.coder.dto.RoleMenuQueryDTO;
import com.coder.vo.RoleMenuDetailVO;
import com.coder.vo.RoleMenuVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色菜单服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface RoleMenuService {

    /**
     * 分配角色菜单
     *
     * @param assignDTO 分配角色菜单DTO
     * @return 是否成功
     */
    Boolean assignRoleMenus(RoleMenuAssignDTO assignDTO);

    /**
     * 取消角色菜单分配
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    Boolean unassignRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 删除角色菜单关联
     *
     * @param id 关联ID
     * @return 是否成功
     */
    Boolean deleteRoleMenu(Long id);

    /**
     * 根据角色ID删除所有菜单关联
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean deleteRoleMenusByRoleId(Long roleId);

    /**
     * 根据菜单ID删除所有角色关联
     *
     * @param menuId 菜单ID
     * @return 是否成功
     */
    Boolean deleteRoleMenusByMenuId(Long menuId);

    /**
     * 根据ID查询角色菜单关联
     *
     * @param id 关联ID
     * @return 角色菜单VO
     */
    RoleMenuVO getRoleMenuById(Long id);

    /**
     * 分页查询角色菜单关联列表
     *
     * @param queryDTO 查询条件
     * @return 分页角色菜单关联列表
     */
    PageInfo<RoleMenuVO> getRoleMenuList(RoleMenuQueryDTO queryDTO);

    /**
     * 查询角色菜单关联总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long getRoleMenuCount(RoleMenuQueryDTO queryDTO);

    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 根据菜单ID查询角色ID列表
     *
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByMenuId(Long menuId);

    /**
     * 根据角色ID查询角色菜单详情
     *
     * @param roleId 角色ID
     * @return 角色菜单详情VO
     */
    RoleMenuDetailVO getRoleMenuDetail(Long roleId);

    /**
     * 检查角色是否拥有菜单
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     * @return 是否拥有
     */
    Boolean checkRoleHasMenu(Long roleId, Long menuId);

    /**
     * 检查角色是否拥有指定权限
     *
     * @param roleId 角色ID
     * @param permission 权限标识
     * @return 是否拥有
     */
    Boolean checkRoleHasPermission(Long roleId, String permission);

    /**
     * 批量分配菜单到角色
     *
     * @param menuIds 菜单ID列表
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean batchAssignMenusToRole(List<Long> menuIds, Long roleId);

    /**
     * 批量取消菜单角色分配
     *
     * @param menuIds 菜单ID列表
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean batchUnassignMenusFromRole(List<Long> menuIds, Long roleId);

    /**
     * 复制角色权限
     *
     * @param sourceRoleId 源角色ID
     * @param targetRoleId 目标角色ID
     * @return 是否成功
     */
    Boolean copyRoleMenus(Long sourceRoleId, Long targetRoleId);
}