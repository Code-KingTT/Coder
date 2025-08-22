package com.coder.service.impl;

import com.coder.dto.FileRecordCreateDTO;
import com.coder.dto.FileRecordQueryDTO;
import com.coder.entity.File;
import com.coder.entity.FileRecord;
import com.coder.exception.BusinessException;
import com.coder.mapper.FileMapper;
import com.coder.mapper.FileRecordMapper;
import com.coder.result.ResultCode;
import com.coder.service.FileRecordService;
import com.coder.utils.BeanUtils;
import com.coder.utils.StrUtils;
import com.coder.vo.FileRecordVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件操作记录服务实现类
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Service
public class FileRecordServiceImpl implements FileRecordService {

    @Resource
    private FileRecordMapper fileRecordMapper;

    @Resource
    private FileMapper fileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFileRecord(FileRecordCreateDTO createDTO) {
        log.info("创建文件操作记录，文件ID：{}，用户ID：{}，操作类型：{}", 
                createDTO.getFileId(), createDTO.getUserId(), createDTO.getActionType());

        // 验证文件是否存在
        File file = fileMapper.selectById(createDTO.getFileId());
        if (file == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "文件不存在");
        }

        // 验证用户是否存在
        validateUserExists(createDTO.getUserId());

        // 构建文件操作记录实体
        FileRecord fileRecord = new FileRecord();
        BeanUtils.copyProperties(createDTO, fileRecord);

        // 设置创建信息
        Long operatorId = createDTO.getOperatorId() != null ? createDTO.getOperatorId() : createDTO.getUserId();
        fileRecord.setCreateTime(LocalDateTime.now());
        fileRecord.setUpdateTime(LocalDateTime.now());
        fileRecord.setCreateBy(operatorId);
        fileRecord.setUpdateBy(operatorId);
        fileRecord.setDeleted(0);

        int result = fileRecordMapper.insert(fileRecord);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "文件操作记录创建失败");
        }

        log.info("文件操作记录创建成功，记录ID：{}", fileRecord.getId());
        return fileRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFileRecord(Long id) {
        log.info("删除文件操作记录，记录ID：{}", id);

        // 检查记录是否存在
        FileRecord fileRecord = fileRecordMapper.selectById(id);
        if (fileRecord == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "文件操作记录不存在");
        }

        int result = fileRecordMapper.deleteById(id, 1L);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "文件操作记录删除失败");
        }

        log.info("文件操作记录删除成功，记录ID：{}", id);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBatchFileRecords(List<Long> ids) {
        log.info("批量删除文件操作记录，记录ID列表：{}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "记录ID列表不能为空");
        }

        int result = fileRecordMapper.deleteBatchByIds(ids, 1L);
        if (result <= 0) {
            throw new BusinessException(ResultCode.OPERATION_FAILED, "批量删除文件操作记录失败");
        }

        log.info("批量删除文件操作记录成功，删除数量：{}", result);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByFileId(Long fileId) {
        log.info("根据文件ID删除相关记录，文件ID：{}", fileId);

        int result = fileRecordMapper.deleteByFileId(fileId, 1L);
        log.info("根据文件ID删除相关记录完成，文件ID：{}，删除数量：{}", fileId, result);
        return Boolean.TRUE;
    }

    @Override
    public FileRecordVO getFileRecordById(Long id) {
        log.debug("根据ID查询文件操作记录，记录ID：{}", id);

        FileRecord fileRecord = fileRecordMapper.selectById(id);
        if (fileRecord == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "文件操作记录不存在");
        }

        return convertToVO(fileRecord);
    }

    @Override
    public PageInfo<FileRecordVO> getFileRecordList(FileRecordQueryDTO queryDTO) {
        log.debug("分页查询文件操作记录列表，查询条件：{}", queryDTO);

        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询数据
        List<FileRecord> recordList = fileRecordMapper.selectPageList(queryDTO);
        PageInfo<FileRecord> pageInfo = new PageInfo<>(recordList);

        // 转换为VO
        List<FileRecordVO> voList = recordList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageInfo<FileRecordVO> result = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, result);
        result.setList(voList);

        return result;
    }

    @Override
    public List<FileRecordVO> getRecordsByFileId(Long fileId) {
        log.debug("根据文件ID查询操作记录，文件ID：{}", fileId);

        List<FileRecord> recordList = fileRecordMapper.selectByFileId(fileId);
        return recordList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileRecordVO> getRecordsByUserId(Long userId) {
        log.debug("根据用户ID查询操作记录，用户ID：{}", userId);

        List<FileRecord> recordList = fileRecordMapper.selectByUserId(userId);
        return recordList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUserActions(Long userId, String actionType) {
        log.debug("统计用户操作次数，用户ID：{}，操作类型：{}", userId, actionType);

        return fileRecordMapper.countUserActions(userId, actionType);
    }

    @Override
    public Long countFileActions(Long fileId, String actionType) {
        log.debug("统计文件操作次数，文件ID：{}，操作类型：{}", fileId, actionType);

        return fileRecordMapper.countFileActions(fileId, actionType);
    }

    @Override
    public void recordUploadAction(Long fileId, Long userId) {
        log.debug("记录文件上传操作，文件ID：{}，用户ID：{}", fileId, userId);
        createActionRecord(fileId, userId, "UPLOAD", "用户上传了文件");
    }

    @Override
    public void recordDownloadAction(Long fileId, Long userId) {
        log.debug("记录文件下载操作，文件ID：{}，用户ID：{}", fileId, userId);
        createActionRecord(fileId, userId, "DOWNLOAD", "用户下载了文件");
    }

    @Override
    public void recordViewAction(Long fileId, Long userId) {
        log.debug("记录文件查看操作，文件ID：{}，用户ID：{}", fileId, userId);
        createActionRecord(fileId, userId, "VIEW", "用户查看了文件");
    }

    @Override
    public void recordFavoriteAction(Long fileId, Long userId) {
        log.debug("记录文件收藏操作，文件ID：{}，用户ID：{}", fileId, userId);
        createActionRecord(fileId, userId, "FAVORITE", "用户收藏了文件");
    }

    @Override
    public void recordUnfavoriteAction(Long fileId, Long userId) {
        log.debug("记录文件取消收藏操作，文件ID：{}，用户ID：{}", fileId, userId);
        createActionRecord(fileId, userId, "UNFAVORITE", "用户取消收藏了文件");
    }

    @Override
    public void recordDeleteAction(Long fileId, Long userId) {
        log.debug("记录文件删除操作，文件ID：{}，用户ID：{}", fileId, userId);
        createActionRecord(fileId, userId, "DELETE", "用户删除了文件");
    }

    /**
     * 创建操作记录
     *
     * @param fileId     文件ID
     * @param userId     用户ID
     * @param actionType 操作类型
     * @param actionDesc 操作描述
     */
    private void createActionRecord(Long fileId, Long userId, String actionType, String actionDesc) {
        try {
            FileRecordCreateDTO createDTO = new FileRecordCreateDTO();
            createDTO.setFileId(fileId);
            createDTO.setUserId(userId);
            createDTO.setActionType(actionType);
            createDTO.setActionDesc(actionDesc);
            createDTO.setOperatorId(userId);

            createFileRecord(createDTO);
        } catch (Exception e) {
            log.warn("创建文件操作记录失败，文件ID：{}，用户ID：{}，操作类型：{}", fileId, userId, actionType, e);
            // 记录操作失败不影响主业务流程
        }
    }

    /**
     * 验证用户是否存在
     *
     * @param userId 用户ID
     */
    private void validateUserExists(Long userId) {
        try {
            // userServiceClient.checkUserExists(userId);
        } catch (Exception e) {
            log.warn("验证用户存在性失败，用户ID：{}", userId, e);
            // 这里可以选择忽略或抛出异常，根据业务需求决定
        }
    }

    /**
     * 转换为VO对象
     *
     * @param fileRecord 文件操作记录实体
     * @return FileRecordVO
     */
    private FileRecordVO convertToVO(FileRecord fileRecord) {
        FileRecordVO vo = new FileRecordVO();
        BeanUtils.copyProperties(fileRecord, vo);

        // 设置描述信息
        vo.setActionTypeDesc(getActionTypeDesc(fileRecord.getActionType()));

        // 获取文件名称
        try {
            File file = fileMapper.selectById(fileRecord.getFileId());
            if (file != null) {
                vo.setFileName(file.getFileName());
            }
        } catch (Exception e) {
            log.warn("获取文件信息失败，文件ID：{}", fileRecord.getFileId(), e);
        }

        return vo;
    }

    /**
     * 获取操作类型描述
     */
    private String getActionTypeDesc(String actionType) {
        if (StrUtils.isBlank(actionType)) {
            return "";
        }
        switch (actionType) {
            case "UPLOAD": return "上传";
            case "DOWNLOAD": return "下载";
            case "VIEW": return "查看";
            case "FAVORITE": return "收藏";
            case "UNFAVORITE": return "取消收藏";
            case "DELETE": return "删除";
            default: return actionType;
        }
    }
}