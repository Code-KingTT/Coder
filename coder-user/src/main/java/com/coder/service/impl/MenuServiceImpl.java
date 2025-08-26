package com.coder.service.impl;

import com.coder.constant.Constants;
import com.coder.dto.MenuCreateDTO;
import com.coder.dto.MenuQueryDTO;
import com.coder.dto.MenuUpdateDTO;
import com.coder.entity.Menu;
import com.coder.exception.BusinessException;
import com.coder.mapper.MenuMapper;
import com.coder.result.ResultCode;
import com.coder.service.MenuService;
import com.coder.utils.RedisUtils;
import com.coder.utils.StrUtils;
import com.coder.vo.MenuTreeVO;
import com.coder.vo.MenuVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMenu(MenuCreateDTO createDTO) {
        log.info("创建菜单，菜单名称：{}", createDTO.getMenuName());

        // 如果有权限标识，检查是否已存在
        if (StrUtils.isNotBlank(createDTO.getPermission())) {
            Menu existMenu = menuMapper.selectByPermission(createDTO.getPermission());
            if (existMenu != null) {
                throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS, "权限标识已存在");
            }
        }

        // 构建菜单实体
        Menu menu = new Menu();
        BeanUtils.copyProperties(createDTO, menu);

        // 设置创建信息
        Long operatorId = createDTO.getOperatorId() != null ? createDTO.getOperatorId() : 1L;
        menu.setCreateInfo(operatorId);

        int result = menuMapper.insert(menu);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "菜单创建失败");
        }

        // 清除菜单相关缓存
        clearMenuCache();

        log.info("菜单创建成功，菜单ID：{}", menu.getId());
        return menu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMenu(Long id) {
        log.info("删除菜单，菜单ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "菜单不存在");
        }

        // 检查是否有子菜单
        if (checkMenuHasChildren(id)) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "该菜单下存在子菜单，无法删除");
        }

        int result = menuMapper.deleteById(id, 1L); // 系统默认操作人
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "菜单删除失败");
        }

        // 清除菜单相关缓存
        clearMenuCache();

        log.info("菜单删除成功，菜单ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMenu(MenuUpdateDTO updateDTO) {
        log.info("更新菜单，菜单ID：{}", updateDTO.getId());

        if (updateDTO.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        Menu existMenu = menuMapper.selectById(updateDTO.getId());
        if (existMenu == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "菜单不存在");
        }

        // 如果更新权限标识，检查是否重复
        if (StrUtils.isNotBlank(updateDTO.getPermission()) 
            && !updateDTO.getPermission().equals(existMenu.getPermission())) {
            Menu permissionMenu = menuMapper.selectByPermission(updateDTO.getPermission());
            if (permissionMenu != null) {
                throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS, "权限标识已存在");
            }
        }

        // 构建更新实体
        Menu menu = new Menu();
        BeanUtils.copyProperties(updateDTO, menu);
        
        // 设置更新信息
        Long operatorId = updateDTO.getOperatorId() != null ? updateDTO.getOperatorId() : 1L;
        menu.setUpdateInfo(operatorId);

        int result = menuMapper.updateById(menu);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "菜单更新失败");
        }

        // 清除菜单相关缓存
        clearMenuCache();

        log.info("菜单更新成功，菜单ID：{}", updateDTO.getId());
        return true;
    }

    @Override
    public MenuVO getMenuById(Long id) {
        log.info("查询菜单，菜单ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "菜单不存在");
        }

        return convertToVO(menu);
    }

    @Override
    public PageInfo<MenuVO> getMenuList(MenuQueryDTO queryDTO) {
        log.info("分页查询菜单列表，查询条件：{}", queryDTO);

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<Menu> menuList = menuMapper.selectPageList(queryDTO);
        PageInfo<Menu> pageInfo = new PageInfo<>(menuList);

        if (StringUtils.isEmpty(menuList)) {
            return new PageInfo<>();
        }

        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
            voList.add(convertToVO(menu));
        }

        // 构建返回的分页信息
        PageInfo<MenuVO> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);

        return result;
    }

    @Override
    public Long getMenuCount(MenuQueryDTO queryDTO) {
        log.info("查询菜单总数，查询条件：{}", queryDTO);
        return menuMapper.selectCount(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchMenus(List<Long> ids) {
        log.info("批量删除菜单，菜单ID列表：{}", ids);

        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID列表不能为空");
        }

        // 检查是否有子菜单
        for (Long id : ids) {
            if (checkMenuHasChildren(id)) {
                throw new BusinessException(ResultCode.OPERATION_FAILED, "存在子菜单的菜单无法删除");
            }
        }

        int result = menuMapper.deleteBatchByIds(ids, 1L); // 系统默认操作人
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量删除菜单失败");
        }

        // 清除菜单相关缓存
        clearMenuCache();

        log.info("批量删除菜单成功，删除数量：{}", result);
        return true;
    }

    @Override
    public List<MenuVO> getEnabledMenus() {
        log.info("查询所有启用的菜单");

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "enabled_menus";
        List<MenuVO> cachedMenus = redisUtils.get(cacheKey, List.class);
        if (cachedMenus != null) {
            log.debug("从缓存获取启用菜单列表");
            return cachedMenus;
        }

        List<Menu> menuList = menuMapper.selectEnabledMenus();
        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
            voList.add(convertToVO(menu));
        }

        // 缓存启用菜单列表，过期时间1小时
        redisUtils.set(cacheKey, voList, 1, TimeUnit.HOURS);

        return voList;
    }

    @Override
    public List<MenuTreeVO> getMenuTree() {
        log.info("查询菜单树");

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "menu_tree";
        List<MenuTreeVO> cachedTree = redisUtils.get(cacheKey, List.class);
        if (cachedTree != null) {
            log.debug("从缓存获取菜单树");
            return cachedTree;
        }

        List<Menu> allMenus = menuMapper.selectEnabledMenus();
        List<MenuTreeVO> menuTree = buildMenuTree(allMenus, 0L);

        // 缓存菜单树，过期时间1小时
        redisUtils.set(cacheKey, menuTree, 1, TimeUnit.HOURS);

        return menuTree;
    }

    @Override
    public List<MenuVO> getMenusByParentId(Long parentId) {
        log.info("根据父菜单ID查询子菜单，父菜单ID：{}", parentId);

        if (parentId == null) {
            parentId = 0L;
        }

        List<Menu> menuList = menuMapper.selectMenusByParentId(parentId);
        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
            voList.add(convertToVO(menu));
        }

        return voList;
    }

    @Override
    public List<MenuVO> getMenusByRoleId(Long roleId) {
        log.info("根据角色ID查询菜单列表，角色ID：{}", roleId);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        List<Menu> menuList = menuMapper.selectMenusByRoleId(roleId);
        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
            voList.add(convertToVO(menu));
        }

        return voList;
    }

    @Override
    public List<MenuVO> getMenusByUserId(Long userId) {
        log.info("根据用户ID查询菜单列表，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        List<Menu> menuList = menuMapper.selectMenusByUserId(userId);
        List<MenuVO> voList = new ArrayList<>();
        for (Menu menu : menuList) {
            voList.add(convertToVO(menu));
        }

        return voList;
    }

    @Override
    public List<MenuTreeVO> getMenuTreeByUserId(Long userId) {
        log.info("根据用户ID查询菜单树，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "menu_tree:" + userId;
        List<MenuTreeVO> cachedTree = redisUtils.get(cacheKey, List.class);
        if (cachedTree != null) {
            log.debug("从缓存获取用户菜单树，用户ID：{}", userId);
            return cachedTree;
        }

        List<Menu> userMenus = menuMapper.selectMenusByUserId(userId);
        List<MenuTreeVO> menuTree = buildMenuTree(userMenus, 0L);

        // 缓存用户菜单树，过期时间30分钟
        redisUtils.set(cacheKey, menuTree, 30, TimeUnit.MINUTES);

        return menuTree;
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        log.info("根据用户ID查询权限标识列表，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "permissions:" + userId;
        List<String> cachedPermissions = redisUtils.get(cacheKey, List.class);
        if (cachedPermissions != null) {
            log.debug("从缓存获取用户权限列表，用户ID：{}", userId);
            return cachedPermissions;
        }

        List<String> permissions = menuMapper.selectPermissionsByUserId(userId);

        // 缓存用户权限列表，过期时间30分钟
        redisUtils.set(cacheKey, permissions, 30, TimeUnit.MINUTES);

        return permissions;
    }

    @Override
    public Boolean checkMenuHasChildren(Long parentId) {
        if (parentId == null) {
            return false;
        }
        return menuMapper.checkMenuHasChildren(parentId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableMenu(Long id) {
        log.info("启用菜单，菜单ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(Constants.ENABLED);
        menu.setUpdateInfo(1L);

        int result = menuMapper.updateById(menu);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "菜单启用失败");
        }

        // 清除菜单相关缓存
        clearMenuCache();

        log.info("菜单启用成功，菜单ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disableMenu(Long id) {
        log.info("禁用菜单，菜单ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(Constants.DISABLED);
        menu.setUpdateInfo(1L);

        int result = menuMapper.updateById(menu);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "菜单禁用失败");
        }

        // 清除菜单相关缓存
        clearMenuCache();

        log.info("菜单禁用成功，菜单ID：{}", id);
        return true;
    }

    /**
     * 构建菜单树
     */
    private List<MenuTreeVO> buildMenuTree(List<Menu> menuList, Long parentId) {
        List<MenuTreeVO> result = new ArrayList<>();
        
        // 按父ID分组
        Map<Long, List<Menu>> menuMap = menuList.stream()
            .collect(Collectors.groupingBy(Menu::getParentId));
        
        // 获取指定父ID的菜单
        List<Menu> parentMenus = menuMap.get(parentId);
        if (parentMenus == null) {
            return result;
        }
        
        for (Menu menu : parentMenus) {
            MenuTreeVO treeVO = convertToTreeVO(menu);
            
            // 递归查找子菜单
            List<MenuTreeVO> children = buildMenuTree(menuList, menu.getId());
            treeVO.setChildren(children);
            
            result.add(treeVO);
        }
        
        return result;
    }

    /**
     * 转换为VO对象
     */
    private MenuVO convertToVO(Menu menu) {
        MenuVO vo = new MenuVO();
        BeanUtils.copyProperties(menu, vo);

        // 设置描述字段
        vo.setMenuTypeDesc(getMenuTypeDesc(menu.getMenuType()));
        vo.setVisibleDesc(getVisibleDesc(menu.getVisible()));
        vo.setStatusDesc(getStatusDesc(menu.getStatus()));

        return vo;
    }

    /**
     * 转换为TreeVO对象
     */
    private MenuTreeVO convertToTreeVO(Menu menu) {
        MenuTreeVO vo = new MenuTreeVO();
        BeanUtils.copyProperties(menu, vo);

        // 设置描述字段
        vo.setMenuTypeDesc(getMenuTypeDesc(menu.getMenuType()));
        vo.setVisibleDesc(getVisibleDesc(menu.getVisible()));
        vo.setStatusDesc(getStatusDesc(menu.getStatus()));

        return vo;
    }

    /**
     * 获取菜单类型描述
     */
    private String getMenuTypeDesc(Integer menuType) {
        if (menuType == null) {
            return "未知";
        }
        switch (menuType) {
            case 1: return "目录";
            case 2: return "菜单";
            case 3: return "按钮";
            default: return "未知";
        }
    }

    /**
     * 获取显示状态描述
     */
    private String getVisibleDesc(Integer visible) {
        if (visible == null) {
            return "未知";
        }
        switch (visible) {
            case 0: return "隐藏";
            case 1: return "显示";
            default: return "未知";
        }
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0: return "禁用";
            case 1: return "启用";
            default: return "未知";
        }
    }

    /**
     * 清除菜单相关缓存
     */
    private void clearMenuCache() {
        String enabledMenusKey = Constants.CacheKey.USER_PREFIX + "enabled_menus";
        String menuTreeKey = Constants.CacheKey.USER_PREFIX + "menu_tree";

        redisUtils.delete(enabledMenusKey);
        redisUtils.delete(menuTreeKey);

        log.debug("清除菜单相关缓存");
    }
}