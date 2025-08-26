package com.coder.service.impl;

import com.coder.constant.Constants;
import com.coder.dto.UserRoleAssignDTO;
import com.coder.dto.UserRoleQueryDTO;
import com.coder.entity.UserRole;
import com.coder.exception.BusinessException;
import com.coder.mapper.UserRoleMapper;
import com.coder.result.ResultCode;
import com.coder.service.RoleService;
import com.coder.service.UserRoleService;
import com.coder.utils.RedisUtils;
import com.coder.vo.RoleVO;
import com.coder.vo.UserRoleDetailVO;
import com.coder.vo.UserRoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignUserRoles(UserRoleAssignDTO assignDTO) {
        log.info("分配用户角色，用户ID：{}，角色ID列表：{}", assignDTO.getUserId(), assignDTO.getRoleIds());

        if (assignDTO.getUserId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        if (StringUtils.isEmpty(assignDTO.getRoleIds())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID列表不能为空");
        }

        // 先删除该用户的所有角色关联
        deleteUserRolesByUserId(assignDTO.getUserId());

        // 批量插入新的角色关联
        List<UserRole> userRoles = new ArrayList<>();
        Long operatorId = assignDTO.getOperatorId() != null ? assignDTO.getOperatorId() : 1L;

        for (Long roleId : assignDTO.getRoleIds()) {
            UserRole userRole = new UserRole(assignDTO.getUserId(), roleId);
            userRole.setCreateInfo(operatorId);
            userRoles.add(userRole);
        }

        int result = userRoleMapper.insertBatch(userRoles);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "用户角色分配失败");
        }

        // 清除用户相关缓存
        clearUserRoleCache(assignDTO.getUserId());

        log.info("用户角色分配成功，用户ID：{}，分配数量：{}", assignDTO.getUserId(), result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unassignUserRoles(Long userId, List<Long> roleIds) {
        log.info("取消用户角色分配，用户ID：{}，角色ID列表：{}", userId, roleIds);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        if (StringUtils.isEmpty(roleIds)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID列表不能为空");
        }

        // 删除指定的用户角色关联
        int result = 0;
        for (Long roleId : roleIds) {
            UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(userId, roleId);
            if (userRole != null) {
                result += userRoleMapper.deleteById(userRole.getId(), 1L);
            }
        }

        // 清除用户相关缓存
        clearUserRoleCache(userId);

        log.info("取消用户角色分配成功，用户ID：{}，取消数量：{}", userId, result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRole(Long id) {
        log.info("删除用户角色关联，关联ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "关联ID不能为空");
        }

        UserRole userRole = userRoleMapper.selectById(id);
        if (userRole == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户角色关联不存在");
        }

        int result = userRoleMapper.deleteById(id, 1L);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "用户角色关联删除失败");
        }

        // 清除用户相关缓存
        clearUserRoleCache(userRole.getUserId());

        log.info("用户角色关联删除成功，关联ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRolesByUserId(Long userId) {
        log.info("根据用户ID删除所有角色关联，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        int result = userRoleMapper.deleteByUserId(userId, 1L);

        // 清除用户相关缓存
        clearUserRoleCache(userId);

        log.info("根据用户ID删除角色关联成功，用户ID：{}，删除数量：{}", userId, result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserRolesByRoleId(Long roleId) {
        log.info("根据角色ID删除所有用户关联，角色ID：{}", roleId);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 获取受影响的用户ID列表，用于清除缓存
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(roleId);

        int result = userRoleMapper.deleteByRoleId(roleId, 1L);

        // 清除相关用户的缓存
        for (Long userId : userIds) {
            clearUserRoleCache(userId);
        }

        log.info("根据角色ID删除用户关联成功，角色ID：{}，删除数量：{}", roleId, result);
        return true;
    }

    @Override
    public UserRoleVO getUserRoleById(Long id) {
        log.info("查询用户角色关联，关联ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "关联ID不能为空");
        }

        UserRole userRole = userRoleMapper.selectById(id);
        if (userRole == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户角色关联不存在");
        }

        // 这里需要通过查询获取完整信息，简化处理
        UserRoleQueryDTO queryDTO = new UserRoleQueryDTO();
        queryDTO.setPageNum(1);
        queryDTO.setPageSize(1);
        
        PageInfo<UserRoleVO> pageInfo = getUserRoleList(queryDTO);
        if (pageInfo.getList().isEmpty()) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户角色关联不存在");
        }

        return pageInfo.getList().get(0);
    }

    @Override
    public PageInfo<UserRoleVO> getUserRoleList(UserRoleQueryDTO queryDTO) {
        log.info("分页查询用户角色关联列表，查询条件：{}", queryDTO);

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<UserRoleVO> userRoleList = userRoleMapper.selectPageList(queryDTO);
        PageInfo<UserRoleVO> pageInfo = new PageInfo<>(userRoleList);

        return pageInfo;
    }

    @Override
    public Long getUserRoleCount(UserRoleQueryDTO queryDTO) {
        log.info("查询用户角色关联总数，查询条件：{}", queryDTO);
        return userRoleMapper.selectCount(queryDTO);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        log.info("根据用户ID查询角色ID列表，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        return userRoleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public List<Long> getUserIdsByRoleId(Long roleId) {
        log.info("根据角色ID查询用户ID列表，角色ID：{}", roleId);

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        return userRoleMapper.selectUserIdsByRoleId(roleId);
    }

    @Override
    public UserRoleDetailVO getUserRoleDetail(Long userId) {
        log.info("根据用户ID查询用户角色详情，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "role_detail:" + userId;
        UserRoleDetailVO cached = redisUtils.get(cacheKey, UserRoleDetailVO.class);
        if (cached != null) {
            log.debug("从缓存获取用户角色详情，用户ID：{}", userId);
            return cached;
        }

        // 查询用户的角色ID列表
        List<Long> roleIds = getRoleIdsByUserId(userId);
        
        UserRoleDetailVO detail = new UserRoleDetailVO();
        detail.setUserId(userId);
        detail.setRoleIds(roleIds);

        if (!roleIds.isEmpty()) {
            // 查询角色详情
            List<RoleVO> roles = new ArrayList<>();
            List<String> roleNames = new ArrayList<>();
            
            for (Long roleId : roleIds) {
                RoleVO role = roleService.getRoleById(roleId);
                if (role != null) {
                    roles.add(role);
                    roleNames.add(role.getRoleName());
                }
            }
            
            detail.setRoles(roles);
            detail.setRoleNames(String.join(",", roleNames));
        }

        // 缓存用户角色详情，过期时间30分钟
        redisUtils.set(cacheKey, detail, 30, java.util.concurrent.TimeUnit.MINUTES);

        return detail;
    }

    @Override
    public Boolean checkUserHasRole(Long userId, Long roleId) {
        if (userId == null || roleId == null) {
            return false;
        }

        return userRoleMapper.existsByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public Boolean checkUserHasRoleCode(Long userId, String roleCode) {
        if (userId == null || StringUtils.isEmpty(roleCode)) {
            return false;
        }

        List<RoleVO> roles = roleService.getRolesByUserId(userId);
        return roles.stream().anyMatch(role -> roleCode.equals(role.getRoleCode()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAssignUsersToRole(List<Long> userIds, Long roleId) {
        log.info("批量分配用户到角色，用户ID列表：{}，角色ID：{}", userIds, roleId);

        if (StringUtils.isEmpty(userIds)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID列表不能为空");
        }

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 批量插入用户角色关联
        List<UserRole> userRoles = new ArrayList<>();
        for (Long userId : userIds) {
            // 检查关联是否已存在
            if (!checkUserHasRole(userId, roleId)) {
                UserRole userRole = new UserRole(userId, roleId);
                userRole.setCreateInfo(1L);
                userRoles.add(userRole);
            }
        }

        if (userRoles.isEmpty()) {
            log.info("所有用户已拥有该角色，无需分配");
            return true;
        }

        int result = userRoleMapper.insertBatch(userRoles);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量分配用户角色失败");
        }

        // 清除相关用户的缓存
        for (Long userId : userIds) {
            clearUserRoleCache(userId);
        }

        log.info("批量分配用户角色成功，分配数量：{}", result);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUnassignUsersFromRole(List<Long> userIds, Long roleId) {
        log.info("批量取消用户角色分配，用户ID列表：{}，角色ID：{}", userIds, roleId);

        if (StringUtils.isEmpty(userIds)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID列表不能为空");
        }

        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 批量删除用户角色关联
        int result = 0;
        for (Long userId : userIds) {
            UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(userId, roleId);
            if (userRole != null) {
                result += userRoleMapper.deleteById(userRole.getId(), 1L);
            }
        }

        // 清除相关用户的缓存
        for (Long userId : userIds) {
            clearUserRoleCache(userId);
        }

        log.info("批量取消用户角色分配成功，取消数量：{}", result);
        return true;
    }

    /**
     * 清除用户角色相关缓存
     */
    private void clearUserRoleCache(Long userId) {
        if (userId != null) {
            String roleDetailKey = Constants.CacheKey.USER_PREFIX + "role_detail:" + userId;
            String userMenuTreeKey = Constants.CacheKey.USER_PREFIX + "menu_tree:" + userId;
            String userPermissionsKey = Constants.CacheKey.USER_PREFIX + "permissions:" + userId;

            redisUtils.delete(roleDetailKey);
            redisUtils.delete(userMenuTreeKey);
            redisUtils.delete(userPermissionsKey);

            log.debug("清除用户角色缓存，用户ID：{}", userId);
        }
    }
}