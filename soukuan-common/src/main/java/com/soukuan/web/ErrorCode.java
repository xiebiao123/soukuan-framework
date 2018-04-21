package com.soukuan.web;

/**
 * Title 系统通用错误码
 * DateTime 17-4-11 上午9:51
 * Version V1.0.0
 */
public enum ErrorCode {

    /**
     * 服务器异常
     */
    DEFAULT_ERR_CODE("-99999"),

    /**
     * 更新记录失败
     */
    UPDATE_RECORD_FAILED("-99998"),

    /**
     * 非法请求
     */
    ILLEGAL_REQUEST("-99998"),

    /**
     * 请求参数不合法
     **/
    ILLEGAL_PARAMETER("-88888"),

    /**
     * 非法状态
     **/
    ILLEGAL_STATUS("-99997"),

    /**
     * 查询记录不存在
     **/
    RECORD_NOT_EXISTS("404"),

    /**
     * 拒绝访问
     **/
    FORBIDDEN("403"),

    UNKNOWN("-1989"),

    SUCCESS("200");

    public static final String ERROR_PREFIX = "error.msg.";

    public final String code;

    ErrorCode(String v) {
        code = v;
    }

}
