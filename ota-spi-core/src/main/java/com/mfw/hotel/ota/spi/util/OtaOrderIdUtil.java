package com.mfw.hotel.ota.spi.util;

public class OtaOrderIdUtil {
    //将ota返回的两个订单号 拼接成一个ota订单id
    public static String getOtaOrderId(String b_code, String b_no) {
        return b_code + "," + b_no;
    }

    //取出ota订单id中的b_code
    public static String getBcode(String otaOrderId) {
        return otaOrderId.split(",")[0];
    }

    //取出ota订单id中的b_no
    public static String getBno(String otaOrderId) {
        return otaOrderId.split(",")[1];
    }
}
