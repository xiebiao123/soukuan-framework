package com.soukuan.util.web;


import com.soukuan.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Title
 * Author jirenhe@wanshifu.com
 * Time 2017/5/23.
 * Version v1.0
 */
public class RequestUtil {


    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
//        System.out.println("X-Real-IP:"+remoteAddr);
        if (isRemoteAddressNotFound(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
//            System.out.println("X-Forwarded-For:"+remoteAddr);
        }
        if (isRemoteAddressNotFound(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
//            System.out.println("Proxy-Client-IP:"+remoteAddr);
        }
        if (isRemoteAddressNotFound(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
//            System.out.println("WL-Proxy-Client-IP:"+remoteAddr);
        }
        if (isRemoteAddressNotFound(remoteAddr)) {
            remoteAddr = request.getHeader("HTTP_CLIENT_IP");
//            System.out.println("HTTP_CLIENT_IP:"+remoteAddr);
        }
        if (isRemoteAddressNotFound(remoteAddr)) {
            remoteAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
//            System.out.println("HTTP_X_FORWARDED_FOR:"+remoteAddr);
        }
        if (isRemoteAddressNotFound(remoteAddr)) {
            String host = request.getHeader("Host");
            if ("0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) || StringUtils.contains(host, "localhost") || StringUtils.contains(host,
                    "127.0.0.1")) {
                //获取本地IP
                remoteAddr = getLocalAddress();
            }
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    private static boolean isRemoteAddressNotFound(String remoteAddr) {
        return StringUtils.isEmpty(remoteAddr) || "unknown".equalsIgnoreCase(remoteAddr);
    }

    public static String getLocalAddress() {
        String la = "127.0.0.1";
        try {
            InetAddress localAddr = InetAddress.getLocalHost();
            if (!localAddr.isLoopbackAddress() && !localAddr.isLinkLocalAddress() && localAddr.isSiteLocalAddress()) {
                la = localAddr.getHostAddress();
                return la;
            }
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            NETWORK_LOOP:
            for (; n.hasMoreElements(); ) {
                NetworkInterface e = n.nextElement();

                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements(); ) {
                    InetAddress addr = a.nextElement();
                    if (!addr.isLoopbackAddress() && !addr.isLinkLocalAddress() && addr.isSiteLocalAddress()) {
                        la = addr.getHostAddress();
                        break NETWORK_LOOP;
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return la;
    }

}
