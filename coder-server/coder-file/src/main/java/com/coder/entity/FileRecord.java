package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.coder.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 文件操作记录实体类
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "FileRecord", description = "文件操作记录实体")
@TableName("sys_file_record")
public class FileRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @ApiModelProperty(value = "文件ID", example = "1001", required = true)
    @NotNull(message = "文件ID不能为空")
    private Long fileId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "2001", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 操作类型：UPLOAD-上传，DOWNLOAD-下载，VIEW-查看，FAVORITE-收藏，UNFAVORITE-取消收藏，DELETE-删除
     */
    @ApiModelProperty(value = "操作类型", example = "DOWNLOAD", required = true)
    @NotBlank(message = "操作类型不能为空")
    @Size(max = 20, message = "操作类型长度不能超过20个字符")
    private String actionType;

    /**
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述", example = "用户下载了文件")
    @Size(max = 200, message = "操作描述长度不能超过200个字符")
    private String actionDesc;

    /**
     * 扩展数据（JSON格式）
     */
    @ApiModelProperty(value = "扩展数据", example = "{\"downloadSize\": 1024}")
    private String extraData;
}