package com.coder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件操作记录VO
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Data
@ApiModel(value = "FileRecordVO", description = "文件操作记录响应对象")
public class FileRecordVO {

    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "操作类型")
    private String actionType;

    @ApiModelProperty(value = "操作类型描述")
    private String actionTypeDesc;

    @ApiModelProperty(value = "操作描述")
    private String actionDesc;

    @ApiModelProperty(value = "扩展数据")
    private String extraData;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}