package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 文件操作记录查询DTO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "FileRecordQueryDTO", description = "文件操作记录查询请求对象")
public class FileRecordQueryDTO {

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能大于100")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "文件ID", example = "1001")
    private Long fileId;

    @ApiModelProperty(value = "用户ID", example = "2001")
    private Long userId;

    @ApiModelProperty(value = "操作类型", example = "DOWNLOAD")
    @Size(max = 20, message = "操作类型长度不能超过20个字符")
    private String actionType;

    @ApiModelProperty(value = "操作描述", example = "下载")
    @Size(max = 200, message = "操作描述长度不能超过200个字符")
    private String actionDesc;

    @ApiModelProperty(value = "创建开始时间", example = "2025-08-01 00:00:00")
    private String createTimeStart;

    @ApiModelProperty(value = "创建结束时间", example = "2025-08-31 23:59:59")
    private String createTimeEnd;
}