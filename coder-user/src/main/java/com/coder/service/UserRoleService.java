package com.coder.service;

import com.coder.dto.UserRoleAssignDTO;
import com.coder.dto.UserRoleQueryDTO;
import com.coder.vo.UserRoleDetailVO;
import com.coder.vo.UserRoleVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 用户角色服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface UserRoleService {

    /**
     * 分配用户角色
     *
     * @param assignDTO 分配用户角色DTO
     * @return 是否成功
     */
    Boolean assignUserRoles(UserRoleAssignDTO assignDTO);

    /**
     * 取消用户角色分配
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    Boolean unassignUserRoles(Long userId, List<Long> roleIds);

    /**
     * 删除用户角色关联
     *
     * @param id 关联ID
     * @return 是否成功
     */
    Boolean deleteUserRole(Long id);

    /**
     * 根据用户ID删除所有角色关联
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean deleteUserRolesByUserId(Long userId);

    /**
     * 根据角色ID删除所有用户关联
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean deleteUserRolesByRoleId(Long roleId);

    /**
     * 根据ID查询用户角色关联
     *
     * @param id 关联ID
     * @return 用户角色VO
     */
    UserRoleVO getUserRoleById(Long id);

    /**
     * 分页查询用户角色关联列表
     *
     * @param queryDTO 查询条件
     * @return 分页用户角色关联列表
     */
    PageInfo<UserRoleVO> getUserRoleList(UserRoleQueryDTO queryDTO);

    /**
     * 查询用户角色关联总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long getUserRoleCount(UserRoleQueryDTO queryDTO);

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 根据角色ID查询用户ID列表
     *
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> getUserIdsByRoleId(Long roleId);

    /**
     * 根据用户ID查询用户角色详情
     *
     * @param userId 用户ID
     * @return 用户角色详情VO
     */
    UserRoleDetailVO getUserRoleDetail(Long userId);

    /**
     * 检查用户是否拥有角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否拥有
     */
    Boolean checkUserHasRole(Long userId, Long roleId);

    /**
     * 检查用户是否拥有指定角色编码
     *
     * @param userId 用户ID
     * @param roleCode 角色编码
     * @return 是否拥有
     */
    Boolean checkUserHasRoleCode(Long userId, String roleCode);

    /**
     * 批量分配用户角色
     *
     * @param userIds 用户ID列表
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean batchAssignUsersToRole(List<Long> userIds, Long roleId);

    /**
     * 批量取消用户角色分配
     *
     * @param userIds 用户ID列表
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean batchUnassignUsersFromRole(List<Long> userIds, Long roleId);
}