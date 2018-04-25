package com.soukuan.web.servlet;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DruidStatViewServlet extends StatViewServlet {

    public static final String PARAM_IS_NEED_LOGIN = "isNeedLogin";

    private boolean isNeedLogin = true;

    @Override
    public void init() throws ServletException {
        isNeedLogin = Boolean.valueOf(getInitParameter(PARAM_IS_NEED_LOGIN));
        super.init();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(isNeedLogin){
            super.service(request, response);
        }else{
            this.serviceNoNeedLogin(request, response);
        }
    }

    private void serviceNoNeedLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/druid/index.html");
            } else {
                response.sendRedirect("druid/index.html");
            }
            return;
        }

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        if (path.contains(".json")) {
            String fullUrl = path;
            if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }
            response.getWriter().print(process(fullUrl));
            return;
        }

        // find file in resources path
        returnResourceFile(path, uri, response);
    }
}