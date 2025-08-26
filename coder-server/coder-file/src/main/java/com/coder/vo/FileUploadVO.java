package com.coder.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传结果VO
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@ApiModel(value = "FileUploadVO", description = "文件上传结果对象")
public class FileUploadVO {

    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件访问URL")
    private String fileUrl;

    @ApiModelProperty(value = "文件MD5值")
    private String fileMd5;

    @ApiModelProperty(value = "上传状态")
    private Integer uploadStatus;
}