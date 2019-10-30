package com.mfw.hotel.ota.spi.constants;

public class JweConnectionPoolParam {
    //确认取消接口独享的连接池 参数暂定 未最终确认
    public final static int CONFIRMCACNCEL_MAX_IDLE_CONNECTIONS = 50;
    public final static int CONFIRMCACNCEL_KEEPALIVE_DURATION = 5;
    public final static int CONFIRMCACNCEL_READ_TIMEOUT = 60;
    public final static int CONFIRMCACNCEL_CONNECT_TIMEOUT = 20;
    //下单接口最大连接数
    public final static int CONFIRMCACNCEL_MAX_IDLE_CONNECTIONS_confirmBooking = 200;
}
