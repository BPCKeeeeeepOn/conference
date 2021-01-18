package com.youyu.conference.common;

public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    DEFAULT_ERROR(110110, "操作失败"),

    /**
     * 系统错误
     */
    SYS_ERROR(402, "系统错误"),
    /**
     * 参数错误
     */
    PARAMS_ERROR(403, "参数错误"),

    /**
     * 无效token
     */
    INVALID_AUTHTOKEN(405, "无效的Token"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PWD_ERROR(406, "用户身份验证错误"),

    /**
     * 验证码发送失败
     */
    SEND_SMS_CODE_FAILED(407, "验证码发送失败"),

    /**
     * 验证码错误
     */
    SMS_CODE_ERROR(408, "验证码错误"),

    /**
     * 没有该用户
     */
    NOT_USER(410, "没有该用户"),

    /**
     * 用户已存在
     */
    USER_EXISZTS(420, "用户已存在"),

    /**
     * 服务器异常
     */
    INTERNAL_SERVER_ERROR(500, "服务器异常"),

    /**
     * 作品已存在
     */
    WORK_EXISZTS(1001, "您已提交过作品，请勿重复提交");


    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
