package com.balls.enums;

public enum StatusCode {
    /**
     * http状态码枚举所有状态码注解
     */
    SUCCESS(200, "操作成功"),
    FAILURE(400, "操作失败"),
    UNAUTHORIZED(401, "没有被授权或者授权已经失效"),
    FORBIDDEN(403, "用户已经被注册"),
    NOT_FOUND(404, "没有查询到信息"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CODE_EXPIRED(410, "验证码过期"),
    REMOTE_LOGIN(411, "异地登录"),
    DATA_FORMAT_ERROR(415, "传输的数据格式错误"),
    TOO_MANY_REQUESTS(429, "请求过多"),
    SERVER_ERROR(500, "服务器内部错误");

    private int code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
