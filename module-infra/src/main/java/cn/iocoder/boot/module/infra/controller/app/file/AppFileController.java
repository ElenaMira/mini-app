package cn.iocoder.boot.module.infra.controller.app.file;

import cn.hutool.core.io.IoUtil;
import cn.iocoder.boot.common.pojo.CommonResult;
import cn.iocoder.boot.module.infra.controller.app.file.vo.AppFileUploadReqVO;
import cn.iocoder.boot.module.infra.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.boot.common.pojo.CommonResult.success;

/**
 * @author xiaosheng
 */
@Tag(name = "用户 App - 文件存储")
@RestController
@RequestMapping("/infra/file")
@Validated
@Slf4j
public class AppFileController {

    @Resource
    private FileService fileService;


    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @Parameter(name = "file",description = "文件附件",required = true,
    schema = @Schema(type = "string", format = "binary"))
    @PermitAll
    public CommonResult<String> uploadFile(AppFileUploadReqVO uploadReqVO) throws Exception {
        MultipartFile file = uploadReqVO.getFile();
        byte[] content = IoUtil.readBytes(file.getInputStream());
        return success(fileService.createFile(content, file.getOriginalFilename(),
                uploadReqVO.getDirectory(), file.getContentType()));
    }
}
