package com.coder.service;

import com.coder.dto.UserCreateDTO;
import com.coder.dto.UserQueryDTO;
import com.coder.dto.UserUpdateDTO;
import com.coder.vo.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author Sunset
 * @date 2025-08-15
 */
public interface UserService {

    /**
     * 创建用户
     *
     * @param createDTO 创建用户DTO
     * @return 用户ID
     */
    Long createUser(UserCreateDTO createDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean deleteUser(Long id);

    /**
     * 更新用户
     *
     * @param updateDTO 更新用户DTO
     * @return 是否成功
     */
    Boolean updateUser(UserUpdateDTO updateDTO);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long id);

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 分页用户列表
     */
    PageInfo<UserVO> getUserList(UserQueryDTO queryDTO);

    /**
     * 查询用户总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long getUserCount(UserQueryDTO queryDTO);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    Boolean deleteBatchUsers(List<Long> ids);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    Boolean checkUsernameExists(String username);
}