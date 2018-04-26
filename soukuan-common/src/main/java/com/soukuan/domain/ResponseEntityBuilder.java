package com.soukuan.domain;

import com.github.pagehelper.PageInfo;
import com.soukuan.enums.ErrorCodeEnum;
import com.soukuan.web.page.PageUtils;
import com.soukuan.web.page.SimplePageInfo;

import java.util.List;

/**
 * Title 新版返回报文工具类
 * Time 2017/11/14.
 * Version v1.0
 */
public class ResponseEntityBuilder {

    private static final String SUCCESS_CODE = "200";

    /**
     * 默认业务异常返回消息
     */
    public static final String FAILED_BUS_MESG = "业务异常";

    /**
     * 返回报文
     *
     * @param code    代码
     * @param message 消息
     * @param data    数据，如果为空，则没有
     * @return
     */
    public static ResponseEntity response(String code, String message, Object data) {
        return new ResponseEntity(code, message, data);
    }

    /**
     * 返回成功报文
     *
     * @return
     */
    public static ResponseEntity success() {
        return response(SUCCESS_CODE, "", null);
    }

    /**
     * 返回成功报文（带数据）
     *
     * @param data 返回的数据
     * @return
     */
    public static ResponseEntity success(Object data) {
        return response(SUCCESS_CODE, "", data);
    }


    /**
     * 返回分页成功报文（带数据）
     *
     * @param data 返回的数据
     * @return
     */
    public static ResponseEntity successPage(List<?> data) {
        return successPage(data, true);
    }

    /**
     * 返回分页成功报文（带数据）
     *
     * @param data 返回的数据
     * @return
     */
    private static ResponseEntity successPage(List<?> data, boolean isSimple) {
        if (isSimple) {
            SimplePageInfo<?> simplePageInfo = PageUtils.getSimplePageInfo(data);
            return response(SUCCESS_CODE, "", simplePageInfo);
        }
        PageInfo<?> pageInfo = new PageInfo<>(data);
        return response(SUCCESS_CODE, "", pageInfo);
    }


    /**
     * 返回业务异常失败报文
     *
     * @return
     */
    public static ResponseEntity failed() {
        return response(ErrorCodeEnum.DEFAULT_ERR_CODE.code, FAILED_BUS_MESG, null);
    }

    /**
     * 返回参数不合法异常失败报文
     *
     * @param message
     * @return
     */
    public static ResponseEntity failed(String message) {
        return response(ErrorCodeEnum.DEFAULT_ERR_CODE.code, message, null);
    }

    /**
     * 返回失败报文
     *
     * @param code
     * @param message
     * @return
     */
    public static ResponseEntity failed(String code, String message) {
        return response(code, message, null);
    }
}
