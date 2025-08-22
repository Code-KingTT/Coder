package com.coder.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.coder.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * 文件实体类
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "File", description = "文件实体")
@TableName("sys_file")
public class File extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文件原始名称
     */
    @ApiModelProperty(value = "文件原始名称", example = "document.pdf", required = true)
    @NotBlank(message = "文件名称不能为空")
    @Size(max = 255, message = "文件名称长度不能超过255个字符")
    private String fileName;

    /**
     * 文件存储路径
     */
    @ApiModelProperty(value = "文件存储路径", example = "/uploads/2025/08/17/document.pdf", required = true)
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 500, message = "文件路径长度不能超过500个字符")
    private String filePath;

    /**
     * 文件访问URL
     */
    @ApiModelProperty(value = "文件访问URL", example = "https://example.com/files/document.pdf")
    @Size(max = 500, message = "文件URL长度不能超过500个字符")
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty(value = "文件大小（字节）", example = "1024000", required = true)
    @NotNull(message = "文件大小不能为空")
    @Min(value = 0, message = "文件大小不能小于0")
    private Long fileSize;

    /**
     * 文件类型（扩展名）
     */
    @ApiModelProperty(value = "文件类型（扩展名）", example = "pdf", required = true)
    @NotBlank(message = "文件类型不能为空")
    @Size(max = 50, message = "文件类型长度不能超过50个字符")
    private String fileType;

    /**
     * MIME类型
     */
    @ApiModelProperty(value = "MIME类型", example = "application/pdf")
    @Size(max = 100, message = "MIME类型长度不能超过100个字符")
    private String mimeType;

    /**
     * 文件MD5值
     */
    @ApiModelProperty(value = "文件MD5值", example = "d41d8cd98f00b204e9800998ecf8427e")
    @Size(max = 32, message = "MD5值长度不能超过32个字符")
    private String fileMd5;

    /**
     * 文件SHA1值
     */
    @ApiModelProperty(value = "文件SHA1值", example = "da39a3ee5e6b4b0d3255bfef95601890afd80709")
    @Size(max = 40, message = "SHA1值长度不能超过40个字符")
    private String fileSha1;

    /**
     * 文件分类：IMAGE-图片，DOCUMENT-文档，VIDEO-视频，AUDIO-音频，OTHER-其他
     */
    @ApiModelProperty(value = "文件分类", example = "DOCUMENT")
    @Size(max = 50, message = "文件分类长度不能超过50个字符")
    private String category;

    /**
     * 业务类型：AVATAR-头像，ATTACHMENT-附件，TEMP-临时文件等
     */
    @ApiModelProperty(value = "业务类型", example = "ATTACHMENT")
    @Size(max = 50, message = "业务类型长度不能超过50个字符")
    private String businessType;

    /**
     * 所属模块名称
     */
    @ApiModelProperty(value = "所属模块名称", example = "user")
    @Size(max = 50, message = "模块名称长度不能超过50个字符")
    private String moduleName;

    /**
     * 关联业务ID
     */
    @ApiModelProperty(value = "关联业务ID", example = "1001")
    private Long businessId;

    /**
     * 存储类型：LOCAL-本地存储，OSS-阿里云OSS，COS-腾讯云COS，QINIU-七牛云等
     */
    @ApiModelProperty(value = "存储类型", example = "LOCAL")
    @Size(max = 20, message = "存储类型长度不能超过20个字符")
    private String storageType;

    /**
     * 存储桶名称（云存储）
     */
    @ApiModelProperty(value = "存储桶名称", example = "my-bucket")
    @Size(max = 100, message = "存储桶名称长度不能超过100个字符")
    private String bucketName;

    /**
     * 存储路径（云存储）
     */
    @ApiModelProperty(value = "存储路径", example = "/files/2025/08/17/")
    @Size(max = 500, message = "存储路径长度不能超过500个字符")
    private String storagePath;

    /**
     * 分片大小（字节）
     */
    @ApiModelProperty(value = "分片大小（字节）", example = "5242880")
    @Min(value = 0, message = "分片大小不能小于0")
    private Integer chunkSize;

    /**
     * 总分片数
     */
    @ApiModelProperty(value = "总分片数", example = "10")
    @Min(value = 0, message = "总分片数不能小于0")
    private Integer totalChunks;

    /**
     * 分片上传ID（云存储）
     */
    @ApiModelProperty(value = "分片上传ID", example = "upload-123456")
    @Size(max = 100, message = "上传ID长度不能超过100个字符")
    private String uploadId;

    /**
     * 上传状态：0-上传中，1-上传完成，2-上传失败
     */
    @ApiModelProperty(value = "上传状态", example = "1")
    @Min(value = 0, message = "上传状态值不正确")
    @Max(value = 2, message = "上传状态值不正确")
    private Integer uploadStatus;

    /**
     * 文件状态：0-禁用，1-正常，2-待审核，3-审核失败
     */
    @ApiModelProperty(value = "文件状态", example = "1")
    @Min(value = 0, message = "文件状态值不正确")
    @Max(value = 3, message = "文件状态值不正确")
    private Integer status;

    /**
     * 下载次数
     */
    @ApiModelProperty(value = "下载次数", example = "100")
    @Min(value = 0, message = "下载次数不能小于0")
    private Integer downloadCount;

    /**
     * 查看次数
     */
    @ApiModelProperty(value = "查看次数", example = "500")
    @Min(value = 0, message = "查看次数不能小于0")
    private Integer viewCount;

    /**
     * 收藏次数
     */
    @ApiModelProperty(value = "收藏次数", example = "50")
    @Min(value = 0, message = "收藏次数不能小于0")
    private Integer favoriteCount;

    /**
     * 访问级别：1-公开，2-登录可见，3-私有
     */
    @ApiModelProperty(value = "访问级别", example = "1")
    @Min(value = 1, message = "访问级别值不正确")
    @Max(value = 3, message = "访问级别值不正确")
    private Integer accessLevel;

    /**
     * 文件所有者ID
     */
    @ApiModelProperty(value = "文件所有者ID", example = "1001")
    private Long ownerId;

    /**
     * 缩略图路径（图片/视频）
     */
    @ApiModelProperty(value = "缩略图路径", example = "/thumbnails/image_thumb.jpg")
    @Size(max = 500, message = "缩略图路径长度不能超过500个字符")
    private String thumbnailPath;

    /**
     * 时长（音频/视频，单位：秒）
     */
    @ApiModelProperty(value = "时长（秒）", example = "300")
    @Min(value = 0, message = "时长不能小于0")
    private Integer duration;

    /**
     * 宽度（图片/视频）
     */
    @ApiModelProperty(value = "宽度", example = "1920")
    @Min(value = 0, message = "宽度不能小于0")
    private Integer width;

    /**
     * 高度（图片/视频）
     */
    @ApiModelProperty(value = "高度", example = "1080")
    @Min(value = 0, message = "高度不能小于0")
    private Integer height;

    /**
     * 文件标签，多个标签用逗号分隔
     */
    @ApiModelProperty(value = "文件标签", example = "重要,工作,文档")
    @Size(max = 500, message = "文件标签长度不能超过500个字符")
    private String tags;
}