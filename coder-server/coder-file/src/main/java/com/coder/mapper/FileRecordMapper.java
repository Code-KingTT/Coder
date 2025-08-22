package com.coder.mapper;

import com.coder.dto.FileRecordQueryDTO;
import com.coder.entity.FileRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件操作记录Mapper接口
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Mapper
public interface FileRecordMapper {

    /**
     * 插入文件操作记录
     *
     * @param fileRecord 文件操作记录
     * @return 影响行数
     */
    int insert(FileRecord fileRecord);

    /**
     * 根据ID删除文件操作记录
     *
     * @param id       记录ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 批量删除文件操作记录
     *
     * @param ids      记录ID列表
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 根据文件ID删除相关记录
     *
     * @param fileId   文件ID
     * @param updateBy 更新人ID
     * @return 影响行数
     */
    int deleteByFileId(@Param("fileId") Long fileId, @Param("updateBy") Long updateBy);

    /**
     * 根据ID查询文件操作记录
     *
     * @param id 记录ID
     * @return 文件操作记录
     */
    FileRecord selectById(@Param("id") Long id);

    /**
     * 分页查询文件操作记录列表
     *
     * @param queryDTO 查询条件
     * @return 文件操作记录列表
     */
    List<FileRecord> selectPageList(FileRecordQueryDTO queryDTO);

    /**
     * 查询文件操作记录总数
     *
     * @param queryDTO 查询条件
     * @return 总数
     */
    Long selectCount(FileRecordQueryDTO queryDTO);

    /**
     * 根据文件ID查询操作记录
     *
     * @param fileId 文件ID
     * @return 操作记录列表
     */
    List<FileRecord> selectByFileId(@Param("fileId") Long fileId);

    /**
     * 根据用户ID查询操作记录
     *
     * @param userId 用户ID
     * @return 操作记录列表
     */
    List<FileRecord> selectByUserId(@Param("userId") Long userId);

    /**
     * 统计用户操作次数
     *
     * @param userId     用户ID
     * @param actionType 操作类型
     * @return 操作次数
     */
    Long countUserActions(@Param("userId") Long userId, @Param("actionType") String actionType);

    /**
     * 统计文件操作次数
     *
     * @param fileId     文件ID
     * @param actionType 操作类型
     * @return 操作次数
     */
    Long countFileActions(@Param("fileId") Long fileId, @Param("actionType") String actionType);
}