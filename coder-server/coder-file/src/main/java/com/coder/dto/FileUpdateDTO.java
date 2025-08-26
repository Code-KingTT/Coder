package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 文件更新DTO
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@ApiModel(value = "FileUpdateDTO", description = "文件更新请求对象")
public class FileUpdateDTO {

    @ApiModelProperty(value = "文件ID", required = true)
    @NotNull(message = "文件ID不能为空")
    private Long id;

    @ApiModelProperty(value = "文件名称", example = "updated_document.pdf")
    @Size(max = 255, message = "文件名称长度不能超过255个字符")
    private String fileName;

    @ApiModelProperty(value = "文件访问URL", example = "https://example.com/files/updated_document.pdf")
    @Size(max = 500, message = "文件URL长度不能超过500个字符")
    private String fileUrl;

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

    @ApiModelProperty(value = "缩略图路径", example = "/thumbnails/image_thumb.jpg")
    @Size(max = 500, message = "缩略图路径长度不能超过500个字符")
    private String thumbnailPath;

    @ApiModelProperty(value = "文件标签", example = "重要,工作,文档")
    @Size(max = 500, message = "文件标签长度不能超过500个字符")
    private String tags;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}