package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 文件上传DTO
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@ApiModel(value = "FileUploadDTO", description = "文件上传请求对象")
public class FileUploadDTO {

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

    @ApiModelProperty(value = "访问级别：1-公开，2-登录可见，3-私有", example = "1")
    private Integer accessLevel = 1;

    @ApiModelProperty(value = "文件所有者ID", example = "1001")
    private Long ownerId;

    @ApiModelProperty(value = "文件标签", example = "重要,工作,文档")
    @Size(max = 500, message = "文件标签长度不能超过500个字符")
    private String tags;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}