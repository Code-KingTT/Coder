package com.coder.service;

import com.coder.dto.FileCreateDTO;
import com.coder.dto.FileQueryDTO;
import com.coder.dto.FileUpdateDTO;
import com.coder.dto.FileUploadDTO;
import com.coder.vo.FileUploadVO;
import com.coder.vo.FileVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务接口
 *
 * @author Sunset
 * @date 2025-8-22
 */
public interface FileService {

    /**
     * 创建文件
     */
    Long createFile(FileCreateDTO createDTO);

    /**
     * 删除文件
     */
    Boolean deleteFile(Long id);

    /**
     * 批量删除文件
     */
    Boolean deleteBatchFiles(List<Long> ids);

    /**
     * 更新文件
     */
    Boolean updateFile(FileUpdateDTO updateDTO);

    /**
     * 根据ID查询文件
     */
    FileVO getFileById(Long id);

    /**
     * 分页查询文件列表
     */
    PageInfo<FileVO> getFileList(FileQueryDTO queryDTO);

    /**
     * 根据MD5查询文件（秒传功能）
     */
    FileVO getFileByMd5(String fileMd5);

    /**
     * 增加文件下载次数
     */
    Boolean increaseDownloadCount(Long id);

    /**
     * 增加文件查看次数
     */
    Boolean increaseViewCount(Long id);

    /**
     * 增加文件收藏次数
     */
    Boolean increaseFavoriteCount(Long id);

    /**
     * 上传文件
     */
    FileUploadVO uploadFile(MultipartFile file, FileUploadDTO uploadDTO);
}