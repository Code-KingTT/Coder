package com.coder.service;

import com.coder.dto.FileRecordCreateDTO;
import com.coder.dto.FileRecordQueryDTO;
import com.coder.vo.FileRecordVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 文件操作记录服务接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
public interface FileRecordService {

    /**
     * 创建文件操作记录
     *
     * @param createDTO 创建DTO
     * @return 记录ID
     */
    Long createFileRecord(FileRecordCreateDTO createDTO);

    /**
     * 删除文件操作记录
     *
     * @param id 记录ID
     * @return 是否成功
     */
    Boolean deleteFileRecord(Long id);

    /**
     * 批量删除文件操作记录
     *
     * @param ids 记录ID列表
     * @return 是否成功
     */
    Boolean deleteBatchFileRecords(List<Long> ids);

    /**
     * 根据文件ID删除相关记录
     *
     * @param fileId 文件ID
     * @return 是否成功
     */
    Boolean deleteByFileId(Long fileId);

    /**
     * 根据ID查询文件操作记录
     *
     * @param id 记录ID
     * @return 文件操作记录VO
     */
    FileRecordVO getFileRecordById(Long id);

    /**
     * 分页查询文件操作记录列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<FileRecordVO> getFileRecordList(FileRecordQueryDTO queryDTO);

    /**
     * 根据文件ID查询操作记录
     *
     * @param fileId 文件ID
     * @return 操作记录列表
     */
    List<FileRecordVO> getRecordsByFileId(Long fileId);

    /**
     * 根据用户ID查询操作记录
     *
     * @param userId 用户ID
     * @return 操作记录列表
     */
    List<FileRecordVO> getRecordsByUserId(Long userId);

    /**
     * 统计用户操作次数
     *
     * @param userId     用户ID
     * @param actionType 操作类型
     * @return 操作次数
     */
    Long countUserActions(Long userId, String actionType);

    /**
     * 统计文件操作次数
     *
     * @param fileId     文件ID
     * @param actionType 操作类型
     * @return 操作次数
     */
    Long countFileActions(Long fileId, String actionType);

    /**
     * 记录文件上传操作
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     */
    void recordUploadAction(Long fileId, Long userId);

    /**
     * 记录文件下载操作
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     */
    void recordDownloadAction(Long fileId, Long userId);

    /**
     * 记录文件查看操作
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     */
    void recordViewAction(Long fileId, Long userId);

    /**
     * 记录文件收藏操作
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     */
    void recordFavoriteAction(Long fileId, Long userId);

    /**
     * 记录文件取消收藏操作
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     */
    void recordUnfavoriteAction(Long fileId, Long userId);

    /**
     * 记录文件删除操作
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     */
    void recordDeleteAction(Long fileId, Long userId);
}