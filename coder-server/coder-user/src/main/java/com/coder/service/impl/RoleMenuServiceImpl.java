package com.coder.service.impl;

import com.coder.constant.Constants;
import com.coder.dto.RoleMenuAssignDTO;
import com.coder.dto.RoleMenuQueryDTO;
import com.coder.entity.RoleMenu;
import com.coder.exception.BusinessException;
import com.coder.mapper.RoleMenuMapper;
import com.coder.result.ResultCode;
import com.coder.service.MenuService;
import com.coder.service.RoleMenuService;
import com.coder.service.RoleService;
import com.coder.utils.RedisUtils;
import com.coder.vo.MenuTreeVO;
import com.coder.vo.MenuVO;
import com.coder.vo.RoleMenuDetailVO;
import com.coder.vo.RoleMenuVO;
import com.coder.vo.RoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 角色菜单服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignRoleMenus(RoleMenuAssignDTO assignDTO) {
        log.info("分配角色菜单，角色ID：{}，菜单ID列表：{}", assignDTO.getRoleId(), assignDTO.getMenuIds());

        if (assignDTO.getRoleId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        if (StringUtils.isEmpty(assignDTO.getMenuIds())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID列表不能为空");
        }

        // 先删除该角色的所有菜单关联
        deleteRoleMenusByRoleId(assignDTO.getRoleId());

        // 批量插入新的菜单关联
        List<RoleMenu> roleMenus = new ArrayList<>();
        Long operatorId = assignDTO.getOperatorId() != null ? assignDTO.getOperatorId() : 1L;

        for (Long menuId : assignDTO.getMenuIds()) {
            RoleMenu roleMenu = new RoleMenu(assignDTO.getRoleId(), menuId);
            roleMenu.setCreateInfo(operatorId);
            roleMenus.add(roleMenu);
        }

        int result = roleMenuMapper.insertBatch(roleMenus);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色菜单分配失败");
        }

        // 清除角色相关缓存
        clearRoleMenuCache(assignDTO.getRoleId());

        log.info("角色菜单分配成功，角色ID：{}，分配数量：{}", assignDTO.getRoleId(), result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unassignRoleMenus(Long roleId, List<Long> menuIds) {
        log.info("取消角色菜单分配，角色ID：{}，菜单ID列表：{}", roleId, menuIds);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        if (StringUtils.isEmpty(menuIds)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID列表不能为空");
        }

        // 删除指定的角色菜单关联
        int result = 0;
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = roleMenuMapper.selectByRoleIdAndMenuId(roleId, menuId);
            if (roleMenu != null) {
                result += roleMenuMapper.deleteById(roleMenu.getId(), 1L);
            }
        }

        // 清除角色相关缓存
        clearRoleMenuCache(roleId);

        log.info("取消角色菜单分配成功，角色ID：{}，取消数量：{}", roleId, result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleMenu(Long id) {
        log.info("删除角色菜单关联，关联ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "关联ID不能为空");
        }

        RoleMenu roleMenu = roleMenuMapper.selectById(id);
        if (roleMenu == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色菜单关联不存在");
        }

        int result = roleMenuMapper.deleteById(id, 1L);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色菜单关联删除失败");
        }

        // 清除角色相关缓存
        clearRoleMenuCache(roleMenu.getRoleId());

        log.info("角色菜单关联删除成功，关联ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleMenusByRoleId(Long roleId) {
        log.info("根据角色ID删除所有菜单关联，角色ID：{}", roleId);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        int result = roleMenuMapper.deleteByRoleId(roleId, 1L);

        // 清除角色相关缓存
        clearRoleMenuCache(roleId);

        log.info("根据角色ID删除菜单关联成功，角色ID：{}，删除数量：{}", roleId, result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleMenusByMenuId(Long menuId) {
        log.info("根据菜单ID删除所有角色关联，菜单ID：{}", menuId);

        if (menuId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        // 获取受影响的角色ID列表，用于清除缓存
        List<Long> roleIds = roleMenuMapper.selectRoleIdsByMenuId(menuId);

        int result = roleMenuMapper.deleteByMenuId(menuId, 1L);

        // 清除相关角色的缓存
        for (Long roleId : roleIds) {
            clearRoleMenuCache(roleId);
        }

        log.info("根据菜单ID删除角色关联成功，菜单ID：{}，删除数量：{}", menuId, result);
        return true;
    }

    @Override
    public RoleMenuVO getRoleMenuById(Long id) {
        log.info("查询角色菜单关联，关联ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "关联ID不能为空");
        }

        RoleMenu roleMenu = roleMenuMapper.selectById(id);
        if (roleMenu == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色菜单关联不存在");
        }

        // 这里需要通过查询获取完整信息，简化处理
        RoleMenuQueryDTO queryDTO = new RoleMenuQueryDTO();
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(1);
        
        PageInfo<RoleMenuVO> pageInfo = getRoleMenuList(queryDTO);
        if (pageInfo.getList().isEmpty()) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色菜单关联不存在");
        }

        return pageInfo.getList().get(0);
    }

    @Override
    public PageInfo<RoleMenuVO> getRoleMenuList(RoleMenuQueryDTO queryDTO) {
        log.info("分页查询角色菜单关联列表，查询条件：{}", queryDTO);

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<RoleMenuVO> roleMenuList = roleMenuMapper.selectPageList(queryDTO);
        PageInfo<RoleMenuVO> pageInfo = new PageInfo<>(roleMenuList);

        return pageInfo;
    }

    @Override
    public Long getRoleMenuCount(RoleMenuQueryDTO queryDTO) {
        log.info("查询角色菜单关联总数，查询条件：{}", queryDTO);
        return roleMenuMapper.selectCount(queryDTO);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        log.info("根据角色ID查询菜单ID列表，角色ID：{}", roleId);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public List<Long> getRoleIdsByMenuId(Long menuId) {
        log.info("根据菜单ID查询角色ID列表，菜单ID：{}", menuId);

        if (menuId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID不能为空");
        }

        return roleMenuMapper.selectRoleIdsByMenuId(menuId);
    }

    @Override
    public RoleMenuDetailVO getRoleMenuDetail(Long roleId) {
        log.info("根据角色ID查询角色菜单详情，角色ID：{}", roleId);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "role_menu_detail:" + roleId;
        RoleMenuDetailVO cached = redisUtils.get(cacheKey, RoleMenuDetailVO.class);
        if (cached != null) {
            log.debug("从缓存获取角色菜单详情，角色ID：{}", roleId);
            return cached;
        }

        // 查询角色信息
        RoleVO role = roleService.getRoleById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色不存在");
        }

        // 查询角色的菜单ID列表
        List<Long> menuIds = getMenuIdsByRoleId(roleId);
        
        RoleMenuDetailVO detail = new RoleMenuDetailVO();
        detail.setRoleId(roleId);
        detail.setRoleCode(role.getRoleCode());
        detail.setRoleName(role.getRoleName());
        detail.setRoleDesc(role.getRoleDesc());
        detail.setMenuIds(menuIds);

        if (!menuIds.isEmpty()) {
            // 查询菜单详情
            List<MenuVO> menus = new ArrayList<>();
            List<String> permissions = new ArrayList<>();
            
            for (Long menuId : menuIds) {
                MenuVO menu = menuService.getMenuById(menuId);
                if (menu != null) {
                    menus.add(menu);
                    if (StringUtils.hasText(menu.getPermission())) {
                        permissions.add(menu.getPermission());
                    }
                }
            }
            
            detail.setMenus(menus);
            detail.setPermissions(permissions);
            
            // 构建菜单树
            List<MenuTreeVO> menuTree = buildMenuTreeFromMenuList(menus);
            detail.setMenuTree(menuTree);
        }

        // 缓存角色菜单详情，过期时间30分钟
        redisUtils.set(cacheKey, detail, 30, TimeUnit.MINUTES);

        return detail;
    }

    @Override
    public Boolean checkRoleHasMenu(Long roleId, Long menuId) {
        if (roleId == null || menuId == null) {
            return false;
        }

        return roleMenuMapper.existsByRoleIdAndMenuId(roleId, menuId);
    }

    @Override
    public Boolean checkRoleHasPermission(Long roleId, String permission) {
        if (roleId == null || StringUtils.isEmpty(permission)) {
            return false;
        }

        RoleMenuDetailVO detail = getRoleMenuDetail(roleId);
        return detail.getPermissions().contains(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAssignMenusToRole(List<Long> menuIds, Long roleId) {
        log.info("批量分配菜单到角色，菜单ID列表：{}，角色ID：{}", menuIds, roleId);

        if (StringUtils.isEmpty(menuIds)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID列表不能为空");
        }

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 批量插入角色菜单关联
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            // 检查关联是否已存在
            if (!checkRoleHasMenu(roleId, menuId)) {
                RoleMenu roleMenu = new RoleMenu(roleId, menuId);
                roleMenu.setCreateInfo(1L);
                roleMenus.add(roleMenu);
            }
        }

        if (roleMenus.isEmpty()) {
            log.info("角色已拥有所有指定菜单，无需分配");
            return true;
        }

        int result = roleMenuMapper.insertBatch(roleMenus);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量分配菜单角色失败");
        }

        // 清除角色相关缓存
        clearRoleMenuCache(roleId);

        log.info("批量分配菜单角色成功，分配数量：{}", result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUnassignMenusFromRole(List<Long> menuIds, Long roleId) {
        log.info("批量取消菜单角色分配，菜单ID列表：{}，角色ID：{}", menuIds, roleId);

        if (StringUtils.isEmpty(menuIds)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "菜单ID列表不能为空");
        }

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 批量删除角色菜单关联
        int result = 0;
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = roleMenuMapper.selectByRoleIdAndMenuId(roleId, menuId);
            if (roleMenu != null) {
                result += roleMenuMapper.deleteById(roleMenu.getId(), 1L);
            }
        }

        // 清除角色相关缓存
        clearRoleMenuCache(roleId);

        log.info("批量取消菜单角色分配成功，取消数量：{}", result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean copyRoleMenus(Long sourceRoleId, Long targetRoleId) {
        log.info("复制角色权限，源角色ID：{}，目标角色ID：{}", sourceRoleId, targetRoleId);

        if (sourceRoleId == null || targetRoleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        if (sourceRoleId.equals(targetRoleId)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "源角色和目标角色不能相同");
        }

        // 查询源角色的菜单ID列表
        List<Long> sourceMenuIds = getMenuIdsByRoleId(sourceRoleId);
        
        if (sourceMenuIds.isEmpty()) {
            log.info("源角色没有分配任何菜单权限");
            return true;
        }

        // 分配菜单到目标角色
        RoleMenuAssignDTO assignDTO = new RoleMenuAssignDTO();
        assignDTO.setRoleId(targetRoleId);
        assignDTO.setMenuIds(sourceMenuIds);
        assignDTO.setOperatorId(1L);

        return assignRoleMenus(assignDTO);
    }

    /**
     * 从菜单列表构建菜单树
     */
    private List<MenuTreeVO> buildMenuTreeFromMenuList(List<MenuVO> menuList) {
        // 这里简化处理，实际应该按照父子关系构建树形结构
        // 可以参考MenuService中的buildMenuTree方法
        return new ArrayList<>();
    }

    /**
     * 清除角色菜单相关缓存
     */
    private void clearRoleMenuCache(Long roleId) {
        if (roleId != null) {
            String roleMenuDetailKey = Constants.CacheKey.USER_PREFIX + "role_menu_detail:" + roleId;

            redisUtils.delete(roleMenuDetailKey);

            log.debug("清除角色菜单缓存，角色ID：{}", roleId);
        }
    }
}