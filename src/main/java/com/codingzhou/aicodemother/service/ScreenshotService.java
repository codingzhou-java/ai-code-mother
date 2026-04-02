package com.codingzhou.aicodemother.service;

public interface ScreenshotService {

    String generateAndUploadScreenshot(String webUrl);

    String uploadScreenshotToCos(String localScreenshotPath);

    String generateScreenshotKey(String fileName);

    void cleanupLocalFile(String localFilePath);



}
