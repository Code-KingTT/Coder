package com.coder.mapper;

import com.coder.dto.RoleMenuQueryDTO;
import com.coder.entity.RoleMenu;
import com.coder.vo.RoleMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单Mapper接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Mapper
public interface RoleMenuMapper {

    /**
     * 插入角色菜单关联
     *
     * @param roleMenu 角色菜单实体
     * @return 影响行数
     */
    int insert(RoleMenu roleMenu);

    /**
     * 批量插入角色菜单关联
     *
     * @param roleMenus 角色菜单列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<RoleMenu> roleMenus);

    /**
     * 根据ID删除角色菜单关联（逻辑删除）
     *
     * @param id 关联ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 根据角色ID删除菜单关联（逻辑删除）
     *
     * @param roleId 角色ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") Long roleId, @Param("updateBy") Long updateBy);

    /**
     * 根据菜单ID删除角色关联（逻辑删除）
     *
     * @param menuId 菜单ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteByMenuId(@Param("menuId") Long menuId, @Param("updateBy") Long updateBy);

    /**
     * 根据ID查询角色菜单关联
     *
     * @param id 关联ID
     * @return 角色菜单实体
     */
    RoleMenu selectById(@Param("id") Long id);

    /**
     * 查询角色菜单关联
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     * @return 角色菜单实体
     */
    RoleMenu selectByRoleIdAndMenuId(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    /**
     * 分页查询角色菜单关联列表
     *
     * @param queryDTO 查询条件
     * @return 角色菜单关联列表
     */
    List<RoleMenuVO> selectPageList(RoleMenuQueryDTO queryDTO);

    /**
     * 查询角色菜单关联总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long selectCount(RoleMenuQueryDTO queryDTO);

    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据菜单ID查询角色ID列表
     *
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByMenuId(@Param("menuId") Long menuId);

    /**
     * 检查角色是否拥有菜单
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     * @return 是否存在
     */
    boolean existsByRoleIdAndMenuId(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}