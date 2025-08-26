package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 文件操作记录创建DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "FileRecordCreateDTO", description = "文件操作记录创建请求对象")
public class FileRecordCreateDTO {

    @ApiModelProperty(value = "文件ID", example = "1001", required = true)
    @NotNull(message = "文件ID不能为空")
    private Long fileId;

    @ApiModelProperty(value = "用户ID", example = "2001", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "操作类型", example = "DOWNLOAD", required = true)
    @NotBlank(message = "操作类型不能为空")
    @Size(max = 20, message = "操作类型长度不能超过20个字符")
    private String actionType;

    @ApiModelProperty(value = "操作描述", example = "用户下载了文件")
    @Size(max = 200, message = "操作描述长度不能超过200个字符")
    private String actionDesc;

    @ApiModelProperty(value = "扩展数据", example = "{\"downloadSize\": 1024}")
    private String extraData;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}