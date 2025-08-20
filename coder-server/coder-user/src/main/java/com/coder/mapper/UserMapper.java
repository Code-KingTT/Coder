package com.coder.mapper;

import com.coder.dto.UserQueryDTO;
import com.coder.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author Sunset
 * @date 2025-08-15
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户
     *
     * @param user 用户实体
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 根据ID删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 更新用户
     *
     * @param user 用户实体
     * @return 影响行数
     */
    int updateById(User user);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 用户列表
     */
    List<User> selectPageList(UserQueryDTO queryDTO);

    /**
     * 查询用户总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long selectCount(UserQueryDTO queryDTO);

    /**
     * 批量删除用户（逻辑删除）
     *
     * @param ids 用户ID列表
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户实体
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 根据邮箱修改用户密码
     *
     * @param email 邮箱
     * @param password 密码
     * @return 影响行数
     */
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String password, @Param("salt")  String salt);
}


