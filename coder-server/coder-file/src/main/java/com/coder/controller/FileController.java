package com.coder.controller;

import com.coder.dto.FileCreateDTO;
import com.coder.dto.FileQueryDTO;
import com.coder.dto.FileUpdateDTO;
import com.coder.result.Result;
import com.coder.service.FileService;
import com.coder.vo.FileVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.coder.dto.FileUploadDTO;
import com.coder.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 文件控制器
 *
 * @author Sunset
 * @date 2025-8-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/coder/file")
@Api(tags = "文件管理")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<FileUploadVO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @Valid FileUploadDTO uploadDTO) {
        FileUploadVO result = fileService.uploadFile(file, uploadDTO);
        return Result.success("文件上传成功", result);
    }

    @PostMapping("/create")
    @ApiOperation("创建文件")
    public Result<Long> createFile(@Valid @RequestBody FileCreateDTO createDTO) {
        Long fileId = fileService.createFile(createDTO);
        return Result.success("文件创建成功", fileId);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除文件")
    public Result<Void> deleteFile(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long id) {
        fileService.deleteFile(id);
        return Result.success("文件删除成功");
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除文件")
    public Result<Void> deleteBatchFiles(@RequestBody List<Long> ids) {
        fileService.deleteBatchFiles(ids);
        return Result.success("批量删除文件成功");
    }

    @PutMapping("/update")
    @ApiOperation("更新文件")
    public Result<Void> updateFile(@Valid @RequestBody FileUpdateDTO updateDTO) {
        fileService.updateFile(updateDTO);
        return Result.success("文件更新成功");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID查询文件")
    public Result<FileVO> getFileById(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long id) {
        FileVO fileVO = fileService.getFileById(id);
        return Result.success("查询成功", fileVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询文件列表")
    public Result<PageInfo<FileVO>> getFileList(FileQueryDTO queryDTO) {
        PageInfo<FileVO> pageInfo = fileService.getFileList(queryDTO);
        return Result.success("查询成功", pageInfo);
    }

    @GetMapping("/get-by-md5")
    @ApiOperation("根据MD5查询文件（秒传功能）")
    public Result<FileVO> getFileByMd5(@RequestParam String fileMd5) {
        FileVO fileVO = fileService.getFileByMd5(fileMd5);
        return Result.success("查询成功", fileVO);
    }

    @PutMapping("/download/{id}")
    @ApiOperation("增加文件下载次数")
    public Result<Void> increaseDownloadCount(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long id) {
        fileService.increaseDownloadCount(id);
        return Result.success("操作成功");
    }

    @PutMapping("/view/{id}")
    @ApiOperation("增加文件查看次数")
    public Result<Void> increaseViewCount(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long id) {
        fileService.increaseViewCount(id);
        return Result.success("操作成功");
    }

    @PutMapping("/favorite/{id}")
    @ApiOperation("增加文件收藏次数")
    public Result<Void> increaseFavoriteCount(
            @ApiParam(value = "文件ID", required = true)
            @PathVariable @NotNull(message = "文件ID不能为空") Long id) {
        fileService.increaseFavoriteCount(id);
        return Result.success("操作成功");
    }
}