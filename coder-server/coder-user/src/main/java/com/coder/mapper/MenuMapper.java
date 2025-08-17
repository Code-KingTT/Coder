package com.coder.mapper;

import com.coder.dto.MenuQueryDTO;
import com.coder.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Mapper
public interface MenuMapper {

    /**
     * 插入菜单
     *
     * @param menu 菜单实体
     * @return 影响行数
     */
    int insert(Menu menu);

    /**
     * 根据ID删除菜单（逻辑删除）
     *
     * @param id 菜单ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 更新菜单
     *
     * @param menu 菜单实体
     * @return 影响行数
     */
    int updateById(Menu menu);

    /**
     * 根据ID查询菜单
     *
     * @param id 菜单ID
     * @return 菜单实体
     */
    Menu selectById(@Param("id") Long id);

    /**
     * 根据权限标识查询菜单
     *
     * @param permission 权限标识
     * @return 菜单实体
     */
    Menu selectByPermission(@Param("permission") String permission);

    /**
     * 分页查询菜单列表
     *
     * @param queryDTO 查询条件
     * @return 菜单列表
     */
    List<Menu> selectPageList(MenuQueryDTO queryDTO);

    /**
     * 查询菜单总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long selectCount(MenuQueryDTO queryDTO);

    /**
     * 批量删除菜单（逻辑删除）
     *
     * @param ids 菜单ID列表
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 查询所有启用的菜单
     *
     * @return 菜单列表
     */
    List<Menu> selectEnabledMenus();

    /**
     * 根据父菜单ID查询子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<Menu> selectMenusByParentId(@Param("parentId") Long parentId);

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 检查菜单是否有子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单数量
     */
    int checkMenuHasChildren(@Param("parentId") Long parentId);
}