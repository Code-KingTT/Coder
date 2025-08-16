package com.coder.service.impl;

import com.coder.constant.Constants;
import com.coder.dto.UserCreateDTO;
import com.coder.dto.UserQueryDTO;
import com.coder.dto.UserUpdateDTO;
import com.coder.entity.User;
import com.coder.exception.BusinessException;
import com.coder.mapper.UserMapper;
import com.coder.result.ResultCode;
import com.coder.service.UserService;
import com.coder.utils.DateUtils;
import com.coder.utils.EncryptUtils;
import com.coder.utils.RedisUtils;
import com.coder.utils.StrUtils;
import com.coder.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 *
 * @author Sunset
 * @date 2025-08-15
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtils redisUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateDTO createDTO) {
        log.info("创建用户，用户名：{}", createDTO.getUsername());

        // 检查用户名是否已存在
        if (checkUsernameExists(createDTO.getUsername())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS, "用户名已存在");
        }

        // 构建用户实体
        User user = new User();
        BeanUtils.copyProperties(createDTO, user);

        // 密码加密
        String salt = EncryptUtils.uuid().replace("-", "");
        String encodedPassword = passwordEncoder.encode(createDTO.getPassword() + salt);
        user.setPassword(encodedPassword);
        user.setSalt(salt);

        // 设置默认值
        user.setStatus(Constants.ENABLED);
        user.setLoginCount(0);
        user.setFailedLoginCount(0);
        user.setTwoFactorEnabled(Constants.NO);
        user.setSource("REGISTER");
        user.setDeleted(Constants.NOT_DELETED);

        // 设置创建信息
        user.setCreateTime(DateUtils.now());
        user.setCreateBy(createDTO.getOperatorId() != null ? createDTO.getOperatorId() : 1L);

        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "用户创建失败");
        }

        // 清除用户相关缓存
        clearUserCache(user.getId());

        log.info("用户创建成功，用户ID：{}", user.getId());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUser(Long id) {
        log.info("删除用户，用户ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户不存在");
        }

        int result = userMapper.deleteById(id, 1L); // 系统默认操作人
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "用户删除失败");
        }

        // 清除用户相关缓存
        clearUserCache(id);

        log.info("用户删除成功，用户ID：{}", id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(UserUpdateDTO updateDTO) {
        log.info("更新用户，用户ID：{}", updateDTO.getId());

        if (updateDTO.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        User existUser = userMapper.selectById(updateDTO.getId());
        if (existUser == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户不存在");
        }

        // 构建更新实体
        User user = new User();
        BeanUtils.copyProperties(updateDTO, user);
        user.setUpdateTime(DateUtils.now());
        user.setUpdateBy(updateDTO.getOperatorId() != null ? updateDTO.getOperatorId() : 1L);

        int result = userMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "用户更新失败");
        }

        // 清除用户相关缓存
        clearUserCache(updateDTO.getId());

        log.info("用户更新成功，用户ID：{}", updateDTO.getId());
        return true;
    }

    @Override
    public UserVO getUserById(Long id) {
        log.info("查询用户，用户ID：{}", id);

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        // 先从缓存获取
        String cacheKey = Constants.CacheKey.USER_INFO + id;
        UserVO cachedUser = redisUtils.get(cacheKey, UserVO.class);
        if (cachedUser != null) {
            log.debug("从缓存获取用户信息，用户ID：{}", id);
            return cachedUser;
        }

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXISTS, "用户不存在");
        }

        UserVO userVO = convertToVO(user);

        // 缓存用户信息，过期时间30分钟
        redisUtils.set(cacheKey, userVO, 30, TimeUnit.MINUTES);

        return userVO;
    }

    @Override
    public PageInfo<UserVO> getUserList(UserQueryDTO queryDTO) {
        log.info("分页查询用户列表，查询条件：{}", queryDTO);

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<User> userList = userMapper.selectPageList(queryDTO);
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        if (StringUtils.isEmpty(userList)) {
            return new PageInfo<>();
        }

        List<UserVO> voList = new ArrayList<>();
        for (User user : userList) {
            voList.add(convertToVO(user));
        }

        // 构建返回的分页信息
        PageInfo<UserVO> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);

        return result;
    }

    @Override
    public Long getUserCount(UserQueryDTO queryDTO) {
        log.info("查询用户总数，查询条件：{}", queryDTO);
        return userMapper.selectCount(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchUsers(List<Long> ids) {
        log.info("批量删除用户，用户ID列表：{}", ids);

        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID列表不能为空");
        }

        int result = userMapper.deleteBatchByIds(ids, 1L); // 系统默认操作人
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量删除用户失败");
        }

        // 清除用户相关缓存
        for (Long id : ids) {
            clearUserCache(id);
        }

        log.info("批量删除用户成功，删除数量：{}", result);
        return true;
    }

    @Override
    public Boolean checkUsernameExists(String username) {
        if (StrUtils.isBlank(username)) {
            return false;
        }

        User user = userMapper.selectByUsername(username);
        return user != null;
    }

    /**
     * 转换为VO对象
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        // 设置描述字段
        vo.setGenderDesc(getGenderDesc(user.getGender()));
        vo.setStatusDesc(getStatusDesc(user.getStatus()));

        return vo;
    }

    /**
     * 获取性别描述
     */
    private String getGenderDesc(Integer gender) {
        if (gender == null) {
            return "未知";
        }
        switch (gender) {
            case 1: return "男";
            case 2: return "女";
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
            case 1: return "正常";
            case 2: return "锁定";
            default: return "未知";
        }
    }

    /**
     * 清除用户相关缓存
     */
    private void clearUserCache(Long userId) {
        if (userId != null) {
            String userInfoKey = Constants.CacheKey.USER_INFO + userId;
            String userTokenKey = Constants.CacheKey.USER_TOKEN + userId;
            String userLoginKey = Constants.CacheKey.USER_LOGIN + userId;

            redisUtils.delete(userInfoKey);
            redisUtils.delete(userTokenKey);
            redisUtils.delete(userLoginKey);

            log.debug("清除用户缓存，用户ID：{}", userId);
        }
    }
}