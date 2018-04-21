package com.soukuan.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Title
 * Time 2017/11/14.
 * Version v1.0
 */
@ApiModel(value = "ResponseEntity", description = "统一返回格式")
@Data
public class ResponseEntity {

    @ApiModelProperty(value = "返回状态码", example = "200")
    private final String retCode;

    @ApiModelProperty(value = "返回消息，仅当retCode不为200时不为空")
    private final String retMesg;

    @ApiModelProperty(value = "返回的具体业务对象")
    private final Object retData;


    public ResponseEntity(String retCode, String retMesg, Object retData) {
        this.retCode = retCode;
        this.retMesg = retMesg;
        this.retData = retData;
    }

    public String getRetCode() {
        return retCode;
    }

    public Object getRetData() {
        return retData;
    }

    public String getRetMesg() {
        return retMesg;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>(3);
        map.put("retCode", retCode);
        map.put("retMesg", retMesg);
        map.put("retData", retData);
        return map;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "retCode='" + retCode + '\'' +
                ", retMesg='" + retMesg + '\'' +
                ", retData=" + retData +
                '}';
    }
}
