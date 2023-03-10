package org.zj2.lite.service.constant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * ServiceConstants
 *
 * @author peijie.ye
 * @date 2022/11/27 20:34
 */
@Component
public class ServiceConstants {
    public static final String APP_CODE = "appCode";
    public static final String ORG_CODE = "orgCode";
    public static final String ORG_GROUP_CODE = "orgGroupCode";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_USER = "createUser";
    public static final String CREATE_USER_NAME = "createUserName";
    public static final String UPDATE_TIME = "updateTime";
    public static final String UPDATE_USER = "updateUser";
    public static final String UPDATE_USER_NAME = "updateUserName";
    //
    public static final String JWT_TOKEN_ID = "tid";
    public static final String JWT_USER_ID = "uid";
    public static final String JWT_USERNAME = "uname";
    public static final String JWT_APP_CODE = "app";
    public static final String JWT_ORG_CODE = "org";
    public static final String JWT_CLIENT_CODE = "ccd";
    //
    public static final String AUTHORIZATION = "Authorization";
    public static final String DATA_AUTHORITY = "Data-Range";
    //
    public static final String REQUEST_ROOT_URI = "Root-Uri";
    public static final String REQUEST_ATTR_IP = "X-Forwarded-For";
    public static final String REQUEST_DEVICE = "User-Agent";
    //
    public static final String SERVER_ID;
    public static final String SERVER_MACHINE;
    //
    private static String serviceName;


    public ServiceConstants(@Value("${spring.application.name:}") String serviceName) {
        ServiceConstants.serviceName = serviceName;//NOSONAR
    }

    public static String serviceName() {
        return serviceName;
    }

    static {
        //
        String machineCode;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(256);
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                List<InterfaceAddress> addresses = networkInterface.getInterfaceAddresses();
                for(InterfaceAddress addr : addresses) {
                    InetAddress ip = addr.getAddress();
                    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                    out.write(network.getHardwareAddress());
                }
            }
            machineCode = DigestUtils.md5DigestAsHex(out.toByteArray());
        } catch(Throwable e) {//NOSONAR
            machineCode = Long.toString(System.nanoTime(), 36);
        }
        SERVER_MACHINE = StringUtils.upperCase(machineCode);
        //
        long time = System.currentTimeMillis();
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder(64);
        sb.append(StringUtils.upperCase(Long.toString(time, 36)));
        sb.append('.' );
        sb.append(SERVER_MACHINE);
        SERVER_ID = sb.toString();
    }

}
