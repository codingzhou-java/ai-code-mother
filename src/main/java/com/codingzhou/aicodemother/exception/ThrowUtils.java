package com.codingzhou.aicodemother.exception;

public class ThrowUtils {

/**
 * 检查条件是否为真，如果为真则抛出指定的运行时异常
 *
 * @param condition 要检查的条件
 * @param e 要抛出的运行时异常
 */
    public static void throwIf(Boolean condition, RuntimeException e) {
    // 如果条件为真，则抛出指定的异常
        if (condition) {
            throw e;
        }
    }

    public static void throwIf(Boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    public static void throwIf(Boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
