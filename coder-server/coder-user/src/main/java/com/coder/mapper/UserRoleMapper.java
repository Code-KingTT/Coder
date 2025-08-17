package com.coder.mapper;

import com.coder.dto.UserRoleQueryDTO;
import com.coder.entity.UserRole;
import com.coder.vo.UserRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色Mapper接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Mapper
public interface UserRoleMapper {

    /**
     * 插入用户角色关联
     *
     * @param userRole 用户角色实体
     * @return 影响行数
     */
    int insert(UserRole userRole);

    /**
     * 批量插入用户角色关联
     *
     * @param userRoles 用户角色列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<UserRole> userRoles);

    /**
     * 根据ID删除用户角色关联（逻辑删除）
     *
     * @param id 关联ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 根据用户ID删除角色关联（逻辑删除）
     *
     * @param userId 用户ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId, @Param("updateBy") Long updateBy);

    /**
     * 根据角色ID删除用户关联（逻辑删除）
     *
     * @param roleId 角色ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") Long roleId, @Param("updateBy") Long updateBy);

    /**
     * 根据ID查询用户角色关联
     *
     * @param id 关联ID
     * @return 用户角色实体
     */
    UserRole selectById(@Param("id") Long id);

    /**
     * 查询用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 用户角色实体
     */
    UserRole selectByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 分页查询用户角色关联列表
     *
     * @param queryDTO 查询条件
     * @return 用户角色关联列表
     */
    List<UserRoleVO> selectPageList(UserRoleQueryDTO queryDTO);

    /**
     * 查询用户角色关联总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long selectCount(UserRoleQueryDTO queryDTO);

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户ID列表
     *
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 检查用户是否拥有角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否存在
     */
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}