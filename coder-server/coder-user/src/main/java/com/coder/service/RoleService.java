package com.coder.service;

import com.coder.dto.RoleCreateDTO;
import com.coder.dto.RoleQueryDTO;
import com.coder.dto.RoleUpdateDTO;
import com.coder.vo.RoleVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface RoleService {

    /**
     * 创建角色
     *
     * @param createDTO 创建角色DTO
     * @return 角色ID
     */
    Long createRole(RoleCreateDTO createDTO);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    Boolean deleteRole(Long id);

    /**
     * 更新角色
     *
     * @param updateDTO 更新角色DTO
     * @return 是否成功
     */
    Boolean updateRole(RoleUpdateDTO updateDTO);

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色VO
     */
    RoleVO getRoleById(Long id);

    /**
     * 分页查询角色列表
     *
     * @param queryDTO 查询条件
     * @return 分页角色列表
     */
    PageInfo<RoleVO> getRoleList(RoleQueryDTO queryDTO);

    /**
     * 查询角色总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long getRoleCount(RoleQueryDTO queryDTO);

    /**
     * 批量删除角色
     *
     * @param ids 角色ID列表
     * @return 是否成功
     */
    Boolean deleteBatchRoles(List<Long> ids);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @return 是否存在
     */
    Boolean checkRoleCodeExists(String roleCode);

    /**
     * 查询所有启用的角色
     *
     * @return 角色列表
     */
    List<RoleVO> getEnabledRoles();

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleVO> getRolesByUserId(Long userId);

    /**
     * 启用角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    Boolean enableRole(Long id);

    /**
     * 禁用角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    Boolean disableRole(Long id);
}