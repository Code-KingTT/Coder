package com.coder.client;

import com.coder.dto.RegisterUserDTO;
import com.coder.result.Result;
import com.coder.vo.UserPermissionVO;
import com.coder.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务客户端
 *
 * @author Sunset
 * @date 2025-8-17
 */
@FeignClient(name = "coder-user", path = "/coder")
public interface UserServiceClient {

    /**
     * 验证用户密码
     */
    @PostMapping("/user/validate-password")
    Result<Boolean> validatePassword(@RequestParam("username") String username,
                                     @RequestParam("password") String password);

    /**
     * 根据用户名查询用户信息
     * 注意：需要在用户模块添加这个接口
     */
    @GetMapping("/user/get-by-username")
    Result<UserVO> getUserByUsername(@RequestParam("username") String username);

    /**
     * 根据用户ID查询用户信息
     */
    @GetMapping("/user/get/{userId}")
    Result<UserVO> getUserById(@PathVariable("userId") Long userId);

    /**
     * 根据用户ID查询用户完整权限信息
     * 注意：需要在用户模块添加这个接口
     */
    @GetMapping("/user/{userId}/permission-info")
    Result<UserPermissionVO> getUserPermissionInfo(@PathVariable("userId") Long userId);

    /**
     * 根据用户ID查询权限标识列表
     */
    @GetMapping("/menu/user/{userId}/permissions")
    Result<List<String>> getPermissionsByUserId(@PathVariable("userId") Long userId);

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/user/check-username")
    Result<Boolean> checkUsernameExists(@RequestParam("username") String username);

    // 在 coder-auth/src/main/java/com/coder/client/UserServiceClient.java 中添加：

    /**
     * 创建用户（注册功能）
     */
    @PostMapping("/user/register")
    Result<Long> createUser(@RequestBody RegisterUserDTO registerUserDTO);
}