package com.soukuan.properties;

import lombok.Data;

import java.io.Serializable;

@Data
public class GlobalParameter implements Serializable {

        /**
         * 参数名称
         */
        private String name;

        /**
         * 参数描述
         */
        private String description;

        /**
         * 参数类型
         */
        private String modelRef;

        /**
         * 是否是必传字段
         */
        private boolean require;

        /**
         * 请求参数类型
         */
        private String parameterType;

        /**
         * 默认值
         */
        private String defaultValue;

}