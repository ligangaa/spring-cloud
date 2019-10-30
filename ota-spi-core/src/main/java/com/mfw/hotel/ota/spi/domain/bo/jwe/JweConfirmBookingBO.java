package com.mfw.hotel.ota.spi.domain.bo.jwe;

import lombok.Data;

@Data
public class JweConfirmBookingBO {

    /**
     * 状态  1成功，0失败
     */
    private Integer status;
    /**
     * 失败原因
     */
    private  String msg;
    /**
     * 订单编号
     */
    private  String b_code;
    /**
     * 订单编号
     */
    private  String b_no;
    /**
     * 订单价格 日元
     */
    private  String act;


}
