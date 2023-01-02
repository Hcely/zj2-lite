package org.zj2.lite.service.util;

import org.zj2.lite.codec.CodecUtil;
import org.zj2.lite.sign.Md5Sign;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 *  ServerUtil
 *
 * @author peijie.ye
 * @date 2022/12/12 16:14
 */
public class ServerInfoUtil {
    private static final String SERVER_ID;
    private static final String MACHINE_CODE;

    static {
        //
        String machineCode;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(256);
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                List<InterfaceAddress> addresses = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress addr : addresses) {
                    InetAddress ip = addr.getAddress();
                    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                    out.write(network.getHardwareAddress());
                }
            }
            machineCode = Md5Sign.INSTANCE.sign(out.toByteArray());
        } catch (Throwable e) {//NOSONAR
            machineCode = CodecUtil.encodeHex(System.nanoTime());
        }
        MACHINE_CODE = machineCode;
        //
        long time = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder(64);
        CodecUtil.encodeHex(sb, time);
        sb.append('.');
        sb.append(MACHINE_CODE);
        SERVER_ID = sb.toString();
    }

    public static String getServerId() {
        return SERVER_ID;
    }

    public static String getMachineCode() {
        return MACHINE_CODE;
    }
}
