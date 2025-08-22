package com.coder.client;

import com.coder.result.Result;
import com.coder.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务客户端
 *
 * @author Sunset
 * @date 2025-8-22
 */
@FeignClient(name = "coder-user", path = "/coder")
public interface UserServiceClient {

    /**
     * 根据用户ID查询用户信息
     */
    @GetMapping("/user/get/{userId}")
    Result<UserVO> getUserById(@PathVariable("userId") Long userId);

    /**
     * 检查用户是否存在
     */
    @GetMapping("/user/exists")
    Result<Boolean> checkUserExists(@RequestParam("userId") Long userId);
}