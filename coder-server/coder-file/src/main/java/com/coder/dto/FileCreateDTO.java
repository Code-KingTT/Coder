package com.coder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 文件创建DTO
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@ApiModel(value = "FileCreateDTO", description = "文件创建请求对象")
public class FileCreateDTO {

    @ApiModelProperty(value = "文件名称", example = "document.pdf", required = true)
    @NotBlank(message = "文件名称不能为空")
    @Size(max = 255, message = "文件名称长度不能超过255个字符")
    private String fileName;

    @ApiModelProperty(value = "文件存储路径", example = "/uploads/2025/08/17/document.pdf", required = true)
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 500, message = "文件路径长度不能超过500个字符")
    private String filePath;

    @ApiModelProperty(value = "文件访问URL", example = "https://example.com/files/document.pdf")
    @Size(max = 500, message = "文件URL长度不能超过500个字符")
    private String fileUrl;

    @ApiModelProperty(value = "文件大小（字节）", example = "1024000", required = true)
    @NotNull(message = "文件大小不能为空")
    @Min(value = 0, message = "文件大小不能小于0")
    private Long fileSize;

    @ApiModelProperty(value = "文件类型（扩展名）", example = "pdf", required = true)
    @NotBlank(message = "文件类型不能为空")
    @Size(max = 50, message = "文件类型长度不能超过50个字符")
    private String fileType;

    @ApiModelProperty(value = "MIME类型", example = "application/pdf")
    @Size(max = 100, message = "MIME类型长度不能超过100个字符")
    private String mimeType;

    @ApiModelProperty(value = "文件MD5值", example = "d41d8cd98f00b204e9800998ecf8427e")
    @Size(max = 32, message = "MD5值长度不能超过32个字符")
    private String fileMd5;

    @ApiModelProperty(value = "文件SHA1值", example = "da39a3ee5e6b4b0d3255bfef95601890afd80709")
    @Size(max = 40, message = "SHA1值长度不能超过40个字符")
    private String fileSha1;

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

    @ApiModelProperty(value = "存储桶名称", example = "my-bucket")
    @Size(max = 100, message = "存储桶名称长度不能超过100个字符")
    private String bucketName;

    @ApiModelProperty(value = "存储路径", example = "/files/2025/08/17/")
    @Size(max = 500, message = "存储路径长度不能超过500个字符")
    private String storagePath;

    @ApiModelProperty(value = "分片大小（字节）", example = "5242880")
    @Min(value = 0, message = "分片大小不能小于0")
    private Integer chunkSize;

    @ApiModelProperty(value = "总分片数", example = "10")
    @Min(value = 0, message = "总分片数不能小于0")
    private Integer totalChunks;

    @ApiModelProperty(value = "分片上传ID", example = "upload-123456")
    @Size(max = 100, message = "上传ID长度不能超过100个字符")
    private String uploadId;

    @ApiModelProperty(value = "上传状态：0-上传中，1-上传完成，2-上传失败", example = "1")
    @Min(value = 0, message = "上传状态值不正确")
    @Max(value = 2, message = "上传状态值不正确")
    private Integer uploadStatus;

    @ApiModelProperty(value = "访问级别：1-公开，2-登录可见，3-私有", example = "1")
    @Min(value = 1, message = "访问级别值不正确")
    @Max(value = 3, message = "访问级别值不正确")
    private Integer accessLevel;

    @ApiModelProperty(value = "文件所有者ID", example = "1001")
    private Long ownerId;

    @ApiModelProperty(value = "缩略图路径", example = "/thumbnails/image_thumb.jpg")
    @Size(max = 500, message = "缩略图路径长度不能超过500个字符")
    private String thumbnailPath;

    @ApiModelProperty(value = "时长（秒）", example = "300")
    @Min(value = 0, message = "时长不能小于0")
    private Integer duration;

    @ApiModelProperty(value = "宽度", example = "1920")
    @Min(value = 0, message = "宽度不能小于0")
    private Integer width;

    @ApiModelProperty(value = "高度", example = "1080")
    @Min(value = 0, message = "高度不能小于0")
    private Integer height;

    @ApiModelProperty(value = "文件标签", example = "重要,工作,文档")
    @Size(max = 500, message = "文件标签长度不能超过500个字符")
    private String tags;

    @ApiModelProperty(value = "备注信息")
    @Size(max = 500, message = "备注信息长度不能超过500个字符")
    private String remark;

    @ApiModelProperty(value = "操作人ID", hidden = true)
    private Long operatorId;
}