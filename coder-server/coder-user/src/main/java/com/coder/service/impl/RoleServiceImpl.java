package com.coder.service.impl;

import com.coder.constant.Constants;
import com.coder.dto.RoleCreateDTO;
import com.coder.dto.RoleQueryDTO;
import com.coder.dto.RoleUpdateDTO;
import com.coder.entity.Role;
import com.coder.exception.BusinessException;
import com.coder.mapper.RoleMapper;
import com.coder.result.ResultCode;
import com.coder.service.RoleService;
import com.coder.utils.RedisUtils;
import com.coder.utils.StrUtils;
import com.coder.vo.RoleVO;
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
import java.util.concurrent.TimeUnit;

/**
 * 角色服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateDTO createDTO) {
        log.info("创建角色，角色编码：{}", createDTO.getRoleCode());

        // 检查角色编码是否已存在
        if (checkRoleCodeExists(createDTO.getRoleCode())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS, "角色编码已存在");
        }

        // 构建角色实体
        Role role = new Role();
        BeanUtils.copyProperties(createDTO, role);

        // 设置创建信息
        Long operatorId = createDTO.getOperatorId() != null ? createDTO.getOperatorId() : 1L;
        role.setCreateInfo(operatorId);

        int result = roleMapper.insert(role);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色创建失败");
        }

        // 清除角色相关缓存
        clearRoleCache(role.getId());

        log.info("角色创建成功，角色ID：{}", role.getId());
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRole(Long id) {
        log.info("删除角色，角色ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色不存在");
        }

        int result = roleMapper.deleteById(id, 1L); // 系统默认操作人
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色删除失败");
        }

        // 清除角色相关缓存
        clearRoleCache(id);

        log.info("角色删除成功，角色ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRole(RoleUpdateDTO updateDTO) {
        log.info("更新角色，角色ID：{}", updateDTO.getId());

        if (updateDTO.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        Role existRole = roleMapper.selectById(updateDTO.getId());
        if (existRole == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色不存在");
        }

        // 如果更新角色编码，检查是否重复
        if (StrUtils.isNotBlank(updateDTO.getRoleCode()) 
            && !updateDTO.getRoleCode().equals(existRole.getRoleCode())) {
            if (checkRoleCodeExists(updateDTO.getRoleCode())) {
                throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS, "角色编码已存在");
            }
        }

        // 构建更新实体
        Role role = new Role();
        BeanUtils.copyProperties(updateDTO, role);
        
        // 设置更新信息
        Long operatorId = updateDTO.getOperatorId() != null ? updateDTO.getOperatorId() : 1L;
        role.setUpdateInfo(operatorId);

        int result = roleMapper.updateById(role);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色更新失败");
        }

        // 清除角色相关缓存
        clearRoleCache(updateDTO.getId());

        log.info("角色更新成功，角色ID：{}", updateDTO.getId());
        return true;
    }

    @Override
    public RoleVO getRoleById(Long id) {
        log.info("查询角色，角色ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "role:" + id;
        RoleVO cachedRole = redisUtils.get(cacheKey, RoleVO.class);
        if (cachedRole != null) {
            log.debug("从缓存获取角色信息，角色ID：{}", id);
            return cachedRole;
        }

        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "角色不存在");
        }

        RoleVO roleVO = convertToVO(role);

        // 缓存角色信息，过期时间30分钟
        redisUtils.set(cacheKey, roleVO, 30, TimeUnit.MINUTES);

        return roleVO;
    }

    @Override
    public PageInfo<RoleVO> getRoleList(RoleQueryDTO queryDTO) {
        log.info("分页查询角色列表，查询条件：{}", queryDTO);

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<Role> roleList = roleMapper.selectPageList(queryDTO);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);

        if (StringUtils.isEmpty(roleList)) {
            return new PageInfo<>();
        }

        List<RoleVO> voList = new ArrayList<>();
        for (Role role : roleList) {
            voList.add(convertToVO(role));
        }

        // 构建返回的分页信息
        PageInfo<RoleVO> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);

        return result;
    }

    @Override
    public Long getRoleCount(RoleQueryDTO queryDTO) {
        log.info("查询角色总数，查询条件：{}", queryDTO);
        return roleMapper.selectCount(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchRoles(List<Long> ids) {
        log.info("批量删除角色，角色ID列表：{}", ids);

        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID列表不能为空");
        }

        int result = roleMapper.deleteBatchByIds(ids, 1L); // 系统默认操作人
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量删除角色失败");
        }

        // 清除角色相关缓存
        for (Long id : ids) {
            clearRoleCache(id);
        }

        log.info("批量删除角色成功，删除数量：{}", result);
        return true;
    }

    @Override
    public Boolean checkRoleCodeExists(String roleCode) {
        if (StrUtils.isBlank(roleCode)) {
            return false;
        }

        Role role = roleMapper.selectByRoleCode(roleCode);
        return role != null;
    }

    @Override
    public List<RoleVO> getEnabledRoles() {
        log.info("查询所有启用的角色");

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_PREFIX + "enabled_roles";
        List<RoleVO> cachedRoles = redisUtils.get(cacheKey, List.class);
        if (cachedRoles != null) {
            log.debug("从缓存获取启用角色列表");
            return cachedRoles;
        }

        List<Role> roleList = roleMapper.selectEnabledRoles();
        List<RoleVO> voList = new ArrayList<>();
        for (Role role : roleList) {
            voList.add(convertToVO(role));
        }

        // 缓存启用角色列表，过期时间1小时
        redisUtils.set(cacheKey, voList, 1, TimeUnit.HOURS);

        return voList;
    }

    @Override
    public List<RoleVO> getRolesByUserId(Long userId) {
        log.info("根据用户ID查询角色列表，用户ID：{}", userId);

        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        List<Role> roleList = roleMapper.selectRolesByUserId(userId);
        List<RoleVO> voList = new ArrayList<>();
        for (Role role : roleList) {
            voList.add(convertToVO(role));
        }

        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableRole(Long id) {
        log.info("启用角色，角色ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        Role role = new Role();
        role.setId(id);
        role.setStatus(Constants.ENABLED);
        role.setUpdateInfo(1L);

        int result = roleMapper.updateById(role);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色启用失败");
        }

        // 清除角色相关缓存
        clearRoleCache(id);

        log.info("角色启用成功，角色ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disableRole(Long id) {
        log.info("禁用角色，角色ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "角色ID不能为空");
        }

        Role role = new Role();
        role.setId(id);
        role.setStatus(Constants.DISABLED);
        role.setUpdateInfo(1L);

        int result = roleMapper.updateById(role);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "角色禁用失败");
        }

        // 清除角色相关缓存
        clearRoleCache(id);

        log.info("角色禁用成功，角色ID：{}", id);
        return true;
    }

    /**
     * 转换为VO对象
     */
    private RoleVO convertToVO(Role role) {
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);

        // 设置描述字段
        vo.setStatusDesc(getStatusDesc(role.getStatus()));

        return vo;
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
     * 清除角色相关缓存
     */
    private void clearRoleCache(Long roleId) {
        if (roleId != null) {
            String roleInfoKey = Constants.CacheKey.USER_PREFIX + "role:" + roleId;
            String enabledRolesKey = Constants.CacheKey.USER_PREFIX + "enabled_roles";

            redisUtils.delete(roleInfoKey);
            redisUtils.delete(enabledRolesKey);

            log.debug("清除角色缓存，角色ID：{}", roleId);
        }
    }
}