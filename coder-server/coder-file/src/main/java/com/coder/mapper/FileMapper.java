package com.coder.mapper;

import com.coder.dto.FileQueryDTO;
import com.coder.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件Mapper接口
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Mapper
public interface FileMapper {

    /**
     * 插入文件
     */
    int insert(File file);

    /**
     * 根据ID删除文件
     */
    int deleteById(@Param("id") Long id, @Param("updateBy") Long updateBy);

    /**
     * 批量删除文件
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 更新文件
     */
    int updateById(File file);

    /**
     * 根据ID查询文件
     */
    File selectById(@Param("id") Long id);

    /**
     * 根据MD5查询文件
     */
    File selectByMd5(@Param("fileMd5") String fileMd5);

    /**
     * 分页查询文件列表
     */
    List<File> selectPageList(FileQueryDTO queryDTO);

    /**
     * 查询文件总数
     */
    Long selectCount(FileQueryDTO queryDTO);

    /**
     * 更新文件统计数据
     */
    int updateFileStats(@Param("id") Long id, @Param("field") String field);
}