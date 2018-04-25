package com.soukuan.propertie;

import lombok.Data;

@Data
public class Servlet {

    private boolean enable = true;

    private String urlPatterns = "/druid/*";

    private String allow = "";

    private String deny = "";

    private String loginUsername = "admin";

    private String loginPassword = "123456";

    private boolean resetEnable = false;

    private boolean sessionStatEnable = false;

    private boolean isNeedLogin = false;

}
