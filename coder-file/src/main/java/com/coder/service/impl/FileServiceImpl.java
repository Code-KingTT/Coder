package com.coder.service.impl;

import com.coder.client.UserServiceClient;
import com.coder.dto.FileCreateDTO;
import com.coder.dto.FileQueryDTO;
import com.coder.dto.FileUpdateDTO;
import com.coder.entity.File;
import com.coder.exception.BusinessException;
import com.coder.mapper.FileMapper;
import com.coder.result.ResultCode;
import com.coder.service.FileService;
import com.coder.utils.BeanUtils;
import com.coder.utils.StrUtils;
import com.coder.vo.FileVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coder.config.FileConfig;
import com.coder.dto.FileUploadDTO;
import com.coder.service.FileRecordService;
import com.coder.utils.FileUtils;
import com.coder.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件服务实现类
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private UserServiceClient userServiceClient;

    @Resource
    private FileConfig fileConfig;

    @Resource
    private FileRecordService fileRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadVO uploadFile(MultipartFile file, FileUploadDTO uploadDTO) {
        log.info("开始上传文件，原始文件名：{}", file.getOriginalFilename());

        try {
            // 1. 文件基本验证
            validateFile(file);

            // 2. 计算文件MD5，检查是否已存在（秒传功能）
            String fileMd5 = FileUtils.calculateMD5(file);
            FileVO existFile = getFileByMd5(fileMd5);
            if (existFile != null) {
                log.info("文件已存在，执行秒传，MD5：{}", fileMd5);
                return buildUploadVO(existFile);
            }

            // 3. 生成文件存储信息
            String originalFileName = file.getOriginalFilename();
            String fileExtension = FileUtils.getFileExtension(originalFileName);
            String uniqueFileName = FileUtils.generateUniqueFileName(originalFileName);
            String storagePath = FileUtils.generateStoragePath();
            String fullStoragePath = fileConfig.getStorage().getLocal().getRootPath() + storagePath;
            String filePath = fullStoragePath + "/" + uniqueFileName;

            // 4. 创建存储目录
            FileUtils.createDirectories(fullStoragePath);

            // 5. 保存文件到磁盘
            Path destPath = Paths.get(filePath);
            Files.copy(file.getInputStream(), destPath);

            // 6. 构建文件创建DTO
            FileCreateDTO createDTO = buildFileCreateDTO(file, uploadDTO, fileMd5,
                    originalFileName, fileExtension, uniqueFileName, storagePath, filePath);

            // 7. 保存文件记录到数据库
            Long fileId = createFile(createDTO);

            // 8. 记录上传操作（如果FileRecordService存在）
            recordUploadAction(fileId, uploadDTO.getOperatorId());

            // 9. 构建返回结果
            FileUploadVO result = new FileUploadVO();
            result.setFileId(fileId);
            result.setFileName(originalFileName);
            result.setFileSize(file.getSize());
            result.setFileType(fileExtension);
            result.setFileUrl(buildFileUrl(storagePath, uniqueFileName));
            result.setFileMd5(fileMd5);
            result.setUploadStatus(1);

            log.info("文件上传成功，文件ID：{}，存储路径：{}", fileId, filePath);
            return result;

        } catch (Exception e) {
            log.error("文件上传失败，原始文件名：{}", file.getOriginalFilename(), e);
            throw new BusinessException(ResultCode.OPERATION_FAILED, "文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 验证上传文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "上传文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > fileConfig.getUpload().getMaxFileSize()) {
            throw new BusinessException(ResultCode.PARAM_ERROR,
                    "文件大小超过限制，最大允许：" + formatFileSize(fileConfig.getUpload().getMaxFileSize()));
        }

        // 检查文件类型
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "文件名不能为空");
        }

        String fileExtension = FileUtils.getFileExtension(fileName);
        if (fileConfig.getUpload().getEnableTypeCheck()) {
            if (fileConfig.getUpload().getAllowedTypes() != null &&
                    !fileConfig.getUpload().getAllowedTypes().contains(fileExtension)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "不支持的文件类型：" + fileExtension);
            }

            if (fileConfig.getUpload().getForbiddenTypes() != null &&
                    fileConfig.getUpload().getForbiddenTypes().contains(fileExtension)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "禁止上传的文件类型：" + fileExtension);
            }
        }
    }


    /**
     * 构建文件创建DTO
     */
    private FileCreateDTO buildFileCreateDTO(MultipartFile file, FileUploadDTO uploadDTO,
                                             String fileMd5, String originalFileName, String fileExtension,
                                             String uniqueFileName, String storagePath, String filePath) {

        FileCreateDTO createDTO = new FileCreateDTO();
        createDTO.setFileName(originalFileName);
        createDTO.setFilePath(filePath);
        createDTO.setFileUrl(buildFileUrl(storagePath, uniqueFileName));
        createDTO.setFileSize(file.getSize());
        createDTO.setFileType(fileExtension);
        createDTO.setMimeType(FileUtils.getMimeType(originalFileName));
        createDTO.setFileMd5(fileMd5);
        createDTO.setCategory(uploadDTO.getCategory());
        createDTO.setBusinessType(uploadDTO.getBusinessType());
        createDTO.setModuleName(uploadDTO.getModuleName());
        createDTO.setBusinessId(uploadDTO.getBusinessId());
        createDTO.setStorageType("LOCAL");
        createDTO.setStoragePath(storagePath);
        createDTO.setUploadStatus(1);
        createDTO.setAccessLevel(uploadDTO.getAccessLevel());
        createDTO.setOwnerId(uploadDTO.getOwnerId());
        createDTO.setTags(uploadDTO.getTags());
        createDTO.setRemark(uploadDTO.getRemark());
        createDTO.setOperatorId(uploadDTO.getOperatorId());

        return createDTO;
    }

    /**
     * 构建文件访问URL
     */
    private String buildFileUrl(String storagePath, String fileName) {
        return fileConfig.getAccess().getDomain() +
                fileConfig.getAccess().getPathPrefix() + "/" +
                storagePath + "/" + fileName;
    }

    /**
     * 构建上传结果VO（秒传场景）
     */
    private FileUploadVO buildUploadVO(FileVO fileVO) {
        FileUploadVO result = new FileUploadVO();
        result.setFileId(fileVO.getId());
        result.setFileName(fileVO.getFileName());
        result.setFileSize(fileVO.getFileSize());
        result.setFileType(fileVO.getFileType());
        result.setFileUrl(fileVO.getFileUrl());
        result.setFileMd5(fileVO.getFileMd5());
        result.setUploadStatus(fileVO.getUploadStatus());
        return result;
    }

    /**
     * 记录上传操作
     */
    private void recordUploadAction(Long fileId, Long operatorId) {
        try {
            if (operatorId != null && fileRecordService != null) {
                // 这里可以调用FileRecordService记录上传操作
                // fileRecordService.recordUploadAction(fileId, operatorId);
            }
        } catch (Exception e) {
            log.warn("记录上传操作失败，文件ID：{}，操作人ID：{}", fileId, operatorId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFile(FileCreateDTO createDTO) {
        log.info("创建文件，文件名称：{}", createDTO.getFileName());

        // 验证文件所有者是否存在
        if (createDTO.getOwnerId() != null) {
            validateUserExists(createDTO.getOwnerId());
        }

        // 构建文件实体
        File file = new File();
        BeanUtils.copyProperties(createDTO, file);

        // 设置默认值
        if (file.getStorageType() == null) {
            file.setStorageType("LOCAL");
        }
        if (file.getUploadStatus() == null) {
            file.setUploadStatus(1);
        }
        if (file.getStatus() == null) {
            file.setStatus(1);
        }
        if (file.getAccessLevel() == null) {
            file.setAccessLevel(1);
        }
        if (file.getDownloadCount() == null) {
            file.setDownloadCount(0);
        }
        if (file.getViewCount() == null) {
            file.setViewCount(0);
        }
        if (file.getFavoriteCount() == null) {
            file.setFavoriteCount(0);
        }

        // 设置创建信息
        Long operatorId = createDTO.getOperatorId() != null ? createDTO.getOperatorId() : 1L;
        file.setCreateTime(LocalDateTime.now());
        file.setUpdateTime(LocalDateTime.now());
        file.setCreateBy(operatorId);
        file.setUpdateBy(operatorId);
        file.setDeleted(0);

        int result = fileMapper.insert(file);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "文件创建失败");
        }

        log.info("文件创建成功，文件ID：{}", file.getId());
        return file.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFile(Long id) {
        log.info("删除文件，文件ID：{}", id);

        // 检查文件是否存在
        File file = fileMapper.selectById(id);
        if (file == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "文件不存在");
        }

        int result = fileMapper.deleteById(id, 1L);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "文件删除失败");
        }

        log.info("文件删除成功，文件ID：{}", id);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchFiles(List<Long> ids) {
        log.info("批量删除文件，文件ID列表：{}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "文件ID列表不能为空");
        }

        int result = fileMapper.deleteBatchByIds(ids, 1L);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量删除文件失败");
        }

        log.info("批量删除文件成功，删除数量：{}", result);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFile(FileUpdateDTO updateDTO) {
        log.info("更新文件，文件ID：{}", updateDTO.getId());

        // 检查文件是否存在
        File existFile = fileMapper.selectById(updateDTO.getId());
        if (existFile == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "文件不存在");
        }

        // 构建更新实体
        File file = new File();
        BeanUtils.copyProperties(updateDTO, file);

        // 设置更新信息
        Long operatorId = updateDTO.getOperatorId() != null ? updateDTO.getOperatorId() : 1L;
        file.setUpdateTime(LocalDateTime.now());
        file.setUpdateBy(operatorId);

        int result = fileMapper.updateById(file);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "文件更新失败");
        }

        log.info("文件更新成功，文件ID：{}", updateDTO.getId());
        return Boolean.TRUE;
    }

    @Override
    public FileVO getFileById(Long id) {
        log.debug("根据ID查询文件，文件ID：{}", id);

        File file = fileMapper.selectById(id);
        if (file == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "文件不存在");
        }

        return convertToVO(file);
    }

    @Override
    public PageInfo<FileVO> getFileList(FileQueryDTO queryDTO) {
        log.debug("分页查询文件列表，查询条件：{}", queryDTO);

        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询数据
        List<File> fileList = fileMapper.selectPageList(queryDTO);
        PageInfo<File> pageInfo = new PageInfo<>(fileList);

        // 转换为VO
        List<FileVO> voList = fileList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageInfo<FileVO> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);

        return result;
    }

    @Override
    public FileVO getFileByMd5(String fileMd5) {
        log.debug("根据MD5查询文件，MD5：{}", fileMd5);

        if (StrUtils.isBlank(fileMd5)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "文件MD5不能为空");
        }

        File file = fileMapper.selectByMd5(fileMd5);
        if (file == null) {
            return null;
        }

        return convertToVO(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean increaseDownloadCount(Long id) {
        log.debug("增加文件下载次数，文件ID：{}", id);

        int result = fileMapper.updateFileStats(id, "download_count");
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "更新下载次数失败");
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean increaseViewCount(Long id) {
        log.debug("增加文件查看次数，文件ID：{}", id);

        int result = fileMapper.updateFileStats(id, "view_count");
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "更新查看次数失败");
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean increaseFavoriteCount(Long id) {
        log.debug("增加文件收藏次数，文件ID：{}", id);

        int result = fileMapper.updateFileStats(id, "favorite_count");
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "更新收藏次数失败");
        }

        return Boolean.TRUE;
    }



    /**
     * 验证用户是否存在
     *
     * @param userId 用户ID
     */
    private void validateUserExists(Long userId) {
        try {
            userServiceClient.checkUserExists(userId);
        } catch (Exception e) {
            log.warn("验证用户存在性失败，用户ID：{}", userId, e);
            // 这里可以选择忽略或抛出异常，根据业务需求决定
        }
    }

    /**
     * 转换为VO对象
     *
     * @param file 文件实体
     * @return FileVO
     */
    private FileVO convertToVO(File file) {
        FileVO vo = new FileVO();
        BeanUtils.copyProperties(file, vo);

        // 设置描述信息
        vo.setFileSizeDesc(formatFileSize(file.getFileSize()));
        vo.setCategoryDesc(getCategoryDesc(file.getCategory()));
        vo.setBusinessTypeDesc(getBusinessTypeDesc(file.getBusinessType()));
        vo.setStorageTypeDesc(getStorageTypeDesc(file.getStorageType()));
        vo.setUploadStatusDesc(getUploadStatusDesc(file.getUploadStatus()));
        vo.setStatusDesc(getStatusDesc(file.getStatus()));
        vo.setAccessLevelDesc(getAccessLevelDesc(file.getAccessLevel()));

        return vo;
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long fileSize) {
        if (fileSize == null || fileSize == 0) {
            return "0 B";
        }

        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize.doubleValue();

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }

    /**
     * 获取分类描述
     */
    private String getCategoryDesc(String category) {
        if (StrUtils.isBlank(category)) {
            return "";
        }
        switch (category) {
            case "IMAGE": return "图片";
            case "DOCUMENT": return "文档";
            case "VIDEO": return "视频";
            case "AUDIO": return "音频";
            case "OTHER": return "其他";
            default: return category;
        }
    }

    /**
     * 获取业务类型描述
     */
    private String getBusinessTypeDesc(String businessType) {
        if (StrUtils.isBlank(businessType)) {
            return "";
        }
        switch (businessType) {
            case "AVATAR": return "头像";
            case "ATTACHMENT": return "附件";
            case "TEMP": return "临时文件";
            default: return businessType;
        }
    }

    /**
     * 获取存储类型描述
     */
    private String getStorageTypeDesc(String storageType) {
        if (StrUtils.isBlank(storageType)) {
            return "";
        }
        switch (storageType) {
            case "LOCAL": return "本地存储";
            case "OSS": return "阿里云OSS";
            case "COS": return "腾讯云COS";
            case "QINIU": return "七牛云";
            default: return storageType;
        }
    }

    /**
     * 获取上传状态描述
     */
    private String getUploadStatusDesc(Integer uploadStatus) {
        if (uploadStatus == null) {
            return "";
        }
        switch (uploadStatus) {
            case 0: return "上传中";
            case 1: return "上传完成";
            case 2: return "上传失败";
            default: return "未知";
        }
    }

    /**
     * 获取文件状态描述
     */
    private String getStatusDesc(Integer status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 0: return "禁用";
            case 1: return "正常";
            case 2: return "待审核";
            case 3: return "审核失败";
            default: return "未知";
        }
    }

    /**
     * 获取访问级别描述
     */
    private String getAccessLevelDesc(Integer accessLevel) {
        if (accessLevel == null) {
            return "";
        }
        switch (accessLevel) {
            case 1: return "公开";
            case 2: return "登录可见";
            case 3: return "私有";
            default: return "未知";
        }
    }
}