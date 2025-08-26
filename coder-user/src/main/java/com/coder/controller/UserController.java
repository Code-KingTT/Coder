package com.coder.controller;

import com.coder.annotation.RequiresPermission;
import com.coder.context.UserContext;
import com.coder.dto.UserCreateDTO;
import com.coder.dto.UserQueryDTO;
import com.coder.dto.UserUpdateDTO;
import com.coder.result.Result;
import com.coder.result.ResultCode;
import com.coder.service.UserService;
import com.coder.vo.UserPermissionVO;
import com.coder.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * 用户控制器
 *
 * @author Sunset
 * @date 2025-08-15
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/user")
@Api(tags = "用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/validate-password")
    @ApiOperation("验证用户密码")
    public Result<Boolean> validatePassword(
            @ApiParam(value = "用户名", required = true)
            @RequestParam @NotNull(message = "用户名不能为空") String username,
            @ApiParam(value = "密码", required = true)
            @RequestParam @NotNull(message = "密码不能为空") String password) {
        Boolean result = userService.validatePassword(username, password);
        return Result.success("验证完成", result);
    }

    @PostMapping("/create")
    @ApiOperation("创建用户")
    public Result<Long> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        Long userId = userService.createUser(createDTO);
        return Result.success("用户创建成功", userId);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除用户")
    public Result<Void> deleteUser(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功");
    }

    @PutMapping("/update")
    @ApiOperation("更新用户")
    public Result<Void> updateUser(@Valid @RequestBody UserUpdateDTO updateDTO) {
        userService.updateUser(updateDTO);
        return Result.success("用户更新成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询用户")
    @RequiresPermission("system:user:query")
    public Result<UserVO> getUserById(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success("查询成功", userVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询用户列表")
    public Result<PageInfo<UserVO>> getUserList(UserQueryDTO queryDTO) {
        PageInfo<UserVO> pageInfo = userService.getUserList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除用户")
    public Result<Void> deleteBatchUsers(
            @ApiParam(value = "用户ID列表", required = true)
            @RequestBody @NotEmpty(message = "用户ID列表不能为空") List<Long> ids) {
        userService.deleteBatchUsers(ids);
        return Result.success("批量删除成功");
    }

    @GetMapping("/check-username")
    @ApiOperation("检查用户名是否存在")
    public Result<Boolean> checkUsernameExists(
            @ApiParam(value = "用户名", required = true)
            @RequestParam @NotNull(message = "用户名不能为空") String username) {
        Boolean exists = userService.checkUsernameExists(username);
        return Result.success("查询成功", exists);
    }

    @GetMapping("/get-by-username")
    @ApiOperation("根据用户名查询用户")
    public Result<UserVO> getUserByUsername(
            @ApiParam(value = "用户名", required = true)
            @RequestParam @NotNull(message = "用户名不能为空") String username) {
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success("查询成功", userVO);
    }

    @GetMapping("/{userId}/permission-info")
    @ApiOperation("根据用户ID查询用户完整权限信息")
    public Result<UserPermissionVO> getUserPermissionInfo(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        UserPermissionVO permissionVO = userService.getUserPermissionInfo(userId);
        return Result.success("查询成功", permissionVO);
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return UserVO 用户VO
     */
    @GetMapping("/get-by-email")
    @ApiOperation("根据邮箱查询用户")
    public Result<UserVO> getUserByEmail(
            @ApiParam(value = "邮箱", required = true)
            @RequestParam @NotNull(message = "邮箱不能为空") String email) {
        UserVO userVO = userService.getUserByEmail(email);
        return Result.success("查询成功", userVO);
    }

    /**
     * 检查邮箱是否占用
     *
     * @param email 邮箱
     * @return 是否占用
     */
    @GetMapping("/check-email")
    @ApiOperation("检查邮箱是否占用")
     public Result<Boolean> checkEmailExists(
             @ApiParam(value = "邮箱", readOnly = true)
             @RequestParam @NotNull(message = "邮箱不能为空") String email) {
         Boolean exists = userService.checkEmailExists(email);
         if (exists) {
             return Result.success("邮箱可用", exists);
         } else {
             return Result.failed(ResultCode.EMAIL_ALREADY_REGISTER);
         }
     }

    /**
     * 根据邮箱修改密码
     *
     * @param email 邮箱
     * @param password 密码
     * @return 是否修改成功
     */
    @GetMapping("/update-password-by-email")
     public Result<Boolean> updatePasswordByEmail(
             @ApiParam(value = "邮箱", required = true)
             @RequestParam String email,
             @ApiParam(value = "密码", required = true)
             @RequestParam String password) {

         Boolean result = userService.updatePasswordByEmail(email, password);
         return Result.success("密码修改成功", result);

     }

}