package com.codingzhou.aicodemother.ai.tools;

import com.codingzhou.aicodemother.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件写入工具
 * 支持 AI 通过工具调用的方式写入文件
 */
@Slf4j
public class FileWriteTool {

    /**
     * 写入文件到指定路径
     * @param relativeFilePath 文件的相对路径
     * @param content 要写入文件的内容
     * @param appId 应用ID，用于创建项目目录
     * @return 返回操作结果信息
     */
    @Tool("写入文件到指定路径")
    public String writeFile(
            @P("文件的相对路径")
            String relativeFilePath,
            @P("要写入文件的内容")
            String content,
            @ToolMemoryId Long appId
    ) {
        try {
            // 创建文件路径对象
            Path path = Paths.get(relativeFilePath);
            // 判断是否为相对路径
            if (!path.isAbsolute()) {
                // 相对路径处理，创建基于 appId 的项目目录
                String projectDirName = "vue_project_" + appId;
                // 构建项目根目录路径
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                // 解析为绝对路径
                path = projectRoot.resolve(relativeFilePath);
            }
            // 创建父目录（如果不存在）
            Path parentDir = path.getParent();
            if (parentDir != null) {
                // 使用 Files.createDirectories 创建所有不存在的父目录
                Files.createDirectories(parentDir);
            }
            // 写入文件内容
            // 使用 StandardOpenOption.CREATE 创建文件（如果不存在）
            // 使用 StandardOpenOption.TRUNCATE_EXISTING 截断已存在的文件
            Files.write(path, content.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            // 记录成功日志
            log.info("成功写入文件: {}", path.toAbsolutePath());
            // 注意要返回相对路径，不能让 AI 把文件绝对路径返回给用户
            return "文件写入成功: " + relativeFilePath;
        } catch (IOException e) {
            // 构建错误信息
            String errorMessage = "文件写入失败: " + relativeFilePath + ", 错误: " + e.getMessage();
            // 记录错误日志
            log.error(errorMessage, e);
            // 返回错误信息
            return errorMessage;
        }
    }
}
