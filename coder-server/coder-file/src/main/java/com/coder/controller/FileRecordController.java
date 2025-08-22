package com.coder.controller;

import com.coder.dto.FileRecordCreateDTO;
import com.coder.dto.FileRecordQueryDTO;
import com.coder.result.Result;
import com.coder.service.FileRecordService;
import com.coder.vo.FileRecordVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 文件操作记录控制器
 *
 * @author Sunset
 * @date 2025-8-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/file/record")
@Api(tags = "文件操作记录管理")
public class FileRecordController {

    @Resource
    private FileRecordService fileRecordService;

    @PostMapping("/create")
    @ApiOperation("创建文件操作记录")
    public Result<Long> createFileRecord(@Valid @RequestBody FileRecordCreateDTO createDTO) {
        Long recordId = fileRecordService.createFileRecord(createDTO);
        return Result.success("文件操作记录创建成功", recordId);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除文件操作记录")
    public Result<Void> deleteFileRecord(
            @ApiParam(value = "记录ID", required = true)
            @PathVariable @NotNull(message = "记录ID不能为空") Long id) {
        fileRecordService.deleteFileRecord(id);
        return Result.success("文件操作记录删除成功");
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除文件操作记录")
    public Result<Void> deleteBatchFileRecords(@RequestBody List<Long> ids) {
        fileRecordService.deleteBatchFileRecords(ids);
        return Result.success("批量删除文件操作记录成功");
    }

    @DeleteMapping("/delete/by-file/{fileId}")
    @ApiOperation("根据文件ID删除相关记录")
    public Result<Void> deleteByFileId(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long fileId) {
        fileRecordService.deleteByFileId(fileId);
        return Result.success("删除文件相关记录成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询文件操作记录")
    public Result<FileRecordVO> getFileRecordById(
            @ApiParam(value = "记录ID", required = true)
            @PathVariable @NotNull(message = "记录ID不能为空") Long id) {
        FileRecordVO recordVO = fileRecordService.getFileRecordById(id);
        return Result.success("查询成功", recordVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询文件操作记录列表")
    public Result<PageInfo<FileRecordVO>> getFileRecordList(FileRecordQueryDTO queryDTO) {
        PageInfo<FileRecordVO> pageInfo = fileRecordService.getFileRecordList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @GetMapping("/list/by-file/{fileId}")
    @ApiOperation("根据文件ID查询操作记录")
    public Result<List<FileRecordVO>> getRecordsByFileId(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long fileId) {
        List<FileRecordVO> recordList = fileRecordService.getRecordsByFileId(fileId);
        return Result.success("查询成功", recordList);
    }

    @GetMapping("/list/by-user/{userId}")
    @ApiOperation("根据用户ID查询操作记录")
    public Result<List<FileRecordVO>> getRecordsByUserId(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<FileRecordVO> recordList = fileRecordService.getRecordsByUserId(userId);
        return Result.success("查询成功", recordList);
    }

    @GetMapping("/count/user/{userId}")
    @ApiOperation("统计用户操作次数")
    public Result<Long> countUserActions(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @ApiParam(value = "操作类型")
            @RequestParam(required = false) String actionType) {
        Long count = fileRecordService.countUserActions(userId, actionType);
        return Result.success("查询成功", count);
    }

    @GetMapping("/count/file/{fileId}")
    @ApiOperation("统计文件操作次数")
    public Result<Long> countFileActions(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long fileId,
            @ApiParam(value = "操作类型")
            @RequestParam(required = false) String actionType) {
        Long count = fileRecordService.countFileActions(fileId, actionType);
        return Result.success("查询成功", count);
    }

    @PostMapping("/record/upload")
    @ApiOperation("记录文件上传操作")
    public Result<Void> recordUploadAction(
            @ApiParam(value = "文件ID", required = true) @RequestParam Long fileId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        fileRecordService.recordUploadAction(fileId, userId);
        return Result.success("记录上传操作成功");
    }

    @PostMapping("/record/download")
    @ApiOperation("记录文件下载操作")
    public Result<Void> recordDownloadAction(
            @ApiParam(value = "文件ID", required = true) @RequestParam Long fileId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        fileRecordService.recordDownloadAction(fileId, userId);
        return Result.success("记录下载操作成功");
    }

    @PostMapping("/record/view")
    @ApiOperation("记录文件查看操作")
    public Result<Void> recordViewAction(
            @ApiParam(value = "文件ID", required = true) @RequestParam Long fileId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        fileRecordService.recordViewAction(fileId, userId);
        return Result.success("记录查看操作成功");
    }

    @PostMapping("/record/favorite")
    @ApiOperation("记录文件收藏操作")
    public Result<Void> recordFavoriteAction(
            @ApiParam(value = "文件ID", required = true) @RequestParam Long fileId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        fileRecordService.recordFavoriteAction(fileId, userId);
        return Result.success("记录收藏操作成功");
    }

    @PostMapping("/record/unfavorite")
    @ApiOperation("记录文件取消收藏操作")
    public Result<Void> recordUnfavoriteAction(
            @ApiParam(value = "文件ID", required = true) @RequestParam Long fileId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        fileRecordService.recordUnfavoriteAction(fileId, userId);
        return Result.success("记录取消收藏操作成功");
    }

    @PostMapping("/record/delete")
    @ApiOperation("记录文件删除操作")
    public Result<Void> recordDeleteAction(
            @ApiParam(value = "文件ID", required = true) @RequestParam Long fileId,
            @ApiParam(value = "用户ID", required = true) @RequestParam Long userId) {
        fileRecordService.recordDeleteAction(fileId, userId);
        return Result.success("记录删除操作成功");
    }
}