package com.mfw.hotel.ota.spi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final String PATH = "/ota-spi-application.properties";
    private static Properties properties = new Properties();

    private PropertiesUtil() {

    }

    static {
        InputStream inputStream = new PropertiesUtil().getClass().getResourceAsStream(PATH);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

//    /**
//     * 使用示例
//     */
//    public static void main(String[] args) {
//        System.out.println(PropertiesUtil.get("host"));
//    }
}
