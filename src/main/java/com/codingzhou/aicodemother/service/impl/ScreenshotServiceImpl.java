package com.codingzhou.aicodemother.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.codingzhou.aicodemother.exception.ErrorCode;
import com.codingzhou.aicodemother.exception.ThrowUtils;
import com.codingzhou.aicodemother.manager.CosManager;
import com.codingzhou.aicodemother.service.ScreenshotService;
import com.codingzhou.aicodemother.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {
    @Resource
    private CosManager cosManager;


    /**
     * 生成网页截图并上传到COS对象存储
     * @param webUrl 网页URL地址
     * @return 返回上传到COS后的图片URL地址
     */
    @Override
    public String generateAndUploadScreenshot(String webUrl) {
    // 检查网页URL是否为空，为空则抛出参数错误异常
        ThrowUtils.throwIf(webUrl == null, ErrorCode.PARAMS_ERROR,"网页URL不能为空");
        log.info("开始生成网页截图，URL: {}", webUrl);
    // 调用WebScreenshotUtils工具类保存网页截图
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);
    // 检查本地截图路径是否为空，为空则抛出操作错误异常
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath), ErrorCode.OPERATION_ERROR,"本地生成截图失败");
        try{
        // 调用uploadScreenshotToCos方法将截图上传到COS
            String cosUrl = uploadScreenshotToCos(localScreenshotPath);
        // 检查COS URL是否为空，为空则抛出操作错误异常
            ThrowUtils.throwIf(StrUtil.isBlank(cosUrl), ErrorCode.OPERATION_ERROR,"上传截图到COS失败");
            log.info("截图生成并上传成功，{} -> {}", webUrl, cosUrl);
            return cosUrl;
        }finally {
        // 无论上传成功或失败，都会执行finally块清理本地截图文件
            cleanupLocalFile(localScreenshotPath);
        }
    }


    /**
     * 上传截图到腾讯云对象存储(COS)
     * @param localScreenshotPath 本地截图文件的完整路径
     * @return 返回上传后的文件访问路径，如果上传失败则返回null
     */
    @Override
    public String uploadScreenshotToCos(String localScreenshotPath) {
        // 检查输入参数是否为空
        if(StrUtil.isBlank(localScreenshotPath)) {
            return null;
        }
        // 根据路径创建文件对象
        File screenshotFile  = new File(localScreenshotPath);
        // 检查文件是否存在
        if(!screenshotFile.exists()) {
            log.error("截图文件不存在：{}", localScreenshotPath);
            return null;
        }
        // 生成唯一的文件名，使用UUID并截取前8位，加上"_compressed.jpg"后缀
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        // 生成COS存储的键值
        String cosKey = generateScreenshotKey(fileName);
        // 调用COS管理器上传文件并返回结果
        return cosManager.uploadFile(cosKey, screenshotFile);
    }


    /**
     * 生成截图文件存储的键值（路径）
     * @param fileName 文件名
     * @return 返回包含日期路径的完整文件键值
     */
    @Override
    public String generateScreenshotKey(String fileName) {
        // 获取当前日期并格式化为"年/月/日"的路径格式
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 返回格式化后的完整路径，格式为：/screenshots/年/月/日/文件名
        return String.format("/screenshots/%s/%s", datePath, fileName);

    }

    /**
     * 清理本地文件的方法
     * @param localFilePath 本地文件路径，需要被清理的文件的完整路径
     */
    @Override
    public void cleanupLocalFile(String localFilePath) {
        // 根据文件路径创建File对象
        File localFile = new File(localFilePath);
        // 检查文件是否存在
        if(localFile.exists()) {
            // 获取文件所在目录的父目录
            File parentDir = localFile.getParentFile();
            // 使用FileUtil工具类删除文件
            FileUtil.del(localFile);
            // 记录清理成功的日志信息
            log.info("清理本地文件成功：{}", localFilePath);
        }
    }
}
