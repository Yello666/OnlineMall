package com.emily.mall.common.result;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一返回结果类
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 成功标识
     */
    private Boolean success;

    public Result() {
    }

    public Result(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> ok() {
        return new Result<>(200, "操作成功", null, true);
    }

    /**
     * 成功返回带数据
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data, true);
    }

    /**
     * 成功返回带消息和数据
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data, true);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> fail() {
        return new Result<>(500, "操作失败", null, false);
    }

    /**
     * 失败返回带消息
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null, false);
    }

    /**
     * 失败返回带状态码和消息
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null, false);
    }
}
