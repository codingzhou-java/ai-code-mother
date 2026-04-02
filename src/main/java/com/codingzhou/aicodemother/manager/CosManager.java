package com.codingzhou.aicodemother.manager;

import com.codingzhou.aicodemother.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * COS对象存储管理器
 */
@Component
@Slf4j
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;


    /**
      * 该方法接收文件名和文件对象，将文件上传到COS，并生成可访问的URL
      * @param key 文件在COS中的存储键（路径）
      * @param file 要上传的文件对象
      * @return 返回文件在COS中的访问URL，上传失败返回null
     */
    public String uploadFile(String key, File file) {
        //上传文件
        PutObjectResult result = putObject(key, file);
        if(result != null) {
            String url = String.format("%s/%s", cosClientConfig.getHost(), key);
            log.info("文件上传COS成功：{} -> {}", file.getName(), url);
            return url;
        }else{
            log.error("文件上传COS失败,返回结果为空");
            return null;
        }
    }

    /**
     * 上传对象
     * @Param key 对象key
     * @Param file 文件
     * @return 上传结果
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }


}
