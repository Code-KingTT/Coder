package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 文件查询DTO
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@ApiModel(value = "FileQueryDTO", description = "文件查询请求对象")
public class FileQueryDTO {

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能大于100")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "文件名称", example = "document")
    @Size(max = 255, message = "文件名称长度不能超过255个字符")
    private String fileName;

    @ApiModelProperty(value = "文件类型", example = "pdf")
    @Size(max = 50, message = "文件类型长度不能超过50个字符")
    private String fileType;

    @ApiModelProperty(value = "文件分类", example = "DOCUMENT")
    @Size(max = 50, message = "文件分类长度不能超过50个字符")
    private String category;

    @ApiModelProperty(value = "业务类型", example = "ATTACHMENT")
    @Size(max = 50, message = "业务类型长度不能超过50个字符")
    private String businessType;

    @ApiModelProperty(value = "所属模块名称", example = "user")
    @Size(max = 50, message = "模块名称长度不能超过50个字符")
    private String moduleName;

    @ApiModelProperty(value = "关联业务ID", example = "1001")
    private Long businessId;

    @ApiModelProperty(value = "存储类型", example = "LOCAL")
    @Size(max = 20, message = "存储类型长度不能超过20个字符")
    private String storageType;

    @ApiModelProperty(value = "上传状态", example = "1")
    @Min(value = 0, message = "上传状态值不正确")
    @Max(value = 2, message = "上传状态值不正确")
    private Integer uploadStatus;

    @ApiModelProperty(value = "文件状态", example = "1")
    @Min(value = 0, message = "文件状态值不正确")
    @Max(value = 3, message = "文件状态值不正确")
    private Integer status;

    @ApiModelProperty(value = "访问级别", example = "1")
    @Min(value = 1, message = "访问级别值不正确")
    @Max(value = 3, message = "访问级别值不正确")
    private Integer accessLevel;

    @ApiModelProperty(value = "文件所有者ID", example = "1001")
    private Long ownerId;

    @ApiModelProperty(value = "文件标签", example = "重要")
    @Size(max = 500, message = "文件标签长度不能超过500个字符")
    private String tags;

    @ApiModelProperty(value = "创建开始时间", example = "2025-08-01 00:00:00")
    private String createTimeStart;

    @ApiModelProperty(value = "创建结束时间", example = "2025-08-31 23:59:59")
    private String createTimeEnd;
}