package com.soukuan.properties;

import lombok.Data;

@Data
public class Filter {

    private boolean enable = true;

    private String filterName = "druidWebStatFilter";

    private String urlPatterns = "/*";

    private String exclusions = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/servlets/*";

}
