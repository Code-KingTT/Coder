package com.coder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件VO
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@ApiModel(value = "FileVO", description = "文件响应对象")
public class FileVO {

    @ApiModelProperty(value = "文件ID")
    private Long id;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件存储路径")
    private String filePath;

    @ApiModelProperty(value = "文件访问URL")
    private String fileUrl;

    @ApiModelProperty(value = "文件大小（字节）")
    private Long fileSize;

    @ApiModelProperty(value = "文件大小描述")
    private String fileSizeDesc;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "MIME类型")
    private String mimeType;

    @ApiModelProperty(value = "文件MD5值")
    private String fileMd5;

    @ApiModelProperty(value = "文件SHA1值")
    private String fileSha1;

    @ApiModelProperty(value = "文件分类")
    private String category;

    @ApiModelProperty(value = "文件分类描述")
    private String categoryDesc;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型描述")
    private String businessTypeDesc;

    @ApiModelProperty(value = "所属模块名称")
    private String moduleName;

    @ApiModelProperty(value = "关联业务ID")
    private Long businessId;

    @ApiModelProperty(value = "存储类型")
    private String storageType;

    @ApiModelProperty(value = "存储类型描述")
    private String storageTypeDesc;

    @ApiModelProperty(value = "存储桶名称")
    private String bucketName;

    @ApiModelProperty(value = "存储路径")
    private String storagePath;

    @ApiModelProperty(value = "分片大小")
    private Integer chunkSize;

    @ApiModelProperty(value = "总分片数")
    private Integer totalChunks;

    @ApiModelProperty(value = "上传ID")
    private String uploadId;

    @ApiModelProperty(value = "上传状态")
    private Integer uploadStatus;

    @ApiModelProperty(value = "上传状态描述")
    private String uploadStatusDesc;

    @ApiModelProperty(value = "文件状态")
    private Integer status;

    @ApiModelProperty(value = "文件状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "下载次数")
    private Integer downloadCount;

    @ApiModelProperty(value = "查看次数")
    private Integer viewCount;

    @ApiModelProperty(value = "收藏次数")
    private Integer favoriteCount;

    @ApiModelProperty(value = "访问级别")
    private Integer accessLevel;

    @ApiModelProperty(value = "访问级别描述")
    private String accessLevelDesc;

    @ApiModelProperty(value = "文件所有者ID")
    private Long ownerId;

    @ApiModelProperty(value = "文件所有者名称")
    private String ownerName;

    @ApiModelProperty(value = "缩略图路径")
    private String thumbnailPath;

    @ApiModelProperty(value = "时长（秒）")
    private Integer duration;

    @ApiModelProperty(value = "宽度")
    private Integer width;

    @ApiModelProperty(value = "高度")
    private Integer height;

    @ApiModelProperty(value = "文件标签")
    private String tags;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}