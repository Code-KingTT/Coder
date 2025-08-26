package com.coder.mapper;

import com.coder.entity.UserPrivacy;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户隐私信息Mapper接口
 *
 * @author Sunset
 * @since 2025-08-24
 */
@Mapper
public interface UserPrivacyMapper {

    /**
     * 新增用户隐私信息
     *
     * @param userPrivacy 用户隐私信息实体对象
     * @return 影响行数
     */
    int insertUserPrivacy(UserPrivacy userPrivacy);
}