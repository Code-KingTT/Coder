package com.coder.mapper;

import com.coder.dto.RoleQueryDTO;
import com.coder.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Mapper
public interface RoleMapper {

    /**
     * 插入角色
     *
     * @param role 角色实体
     * @return 影响行数
     */
    int insert(Role role);

    /**
     * 根据ID删除角色（逻辑删除）
     *
     * @param id 角色ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 更新角色
     *
     * @param role 角色实体
     * @return 影响行数
     */
    int updateById(Role role);

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色实体
     */
    Role selectById(@Param("id") Long id);

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色实体
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 分页查询角色列表
     *
     * @param queryDTO 查询条件
     * @return 角色列表
     */
    List<Role> selectPageList(RoleQueryDTO queryDTO);

    /**
     * 查询角色总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long selectCount(RoleQueryDTO queryDTO);

    /**
     * 批量删除角色（逻辑删除）
     *
     * @param ids 角色ID列表
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 查询所有启用的角色
     *
     * @return 角色列表
     */
    List<Role> selectEnabledRoles();

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);
}