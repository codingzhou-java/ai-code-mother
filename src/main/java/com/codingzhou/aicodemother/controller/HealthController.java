package com.codingzhou.aicodemother.controller;

import com.codingzhou.aicodemother.common.BaseResponse;
import com.codingzhou.aicodemother.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public BaseResponse<String> health() {
        return ResultUtils.success("OK");
    }

}
