package com.codingzhou.aicodemother.ai.model;


import dev.langchain4j.model.output.structured.Description;
import lombok.Data;


/**
 * 多文件代码结果类
 */
@Description("生成多个代码文件的结果")
@Data
public class MultiFileCodeResult {


    /**
     * HTML代码内容
     * 存储生成的HTML代码字符串
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * CSS代码内容
     * 存储生成的CSS样式代码字符串
     */
    @Description("CSS代码")
    private String cssCode;

    /**
     * JavaScript代码内容
     * 存储生成的JavaScript脚本代码字符串
     */
    @Description("JavaScript代码")
    private String jsCode;

    /**
     * 代码描述信息
     * 存储对生成代码的说明或解释
     */
    @Description("代码描述")
    private String description;
}
