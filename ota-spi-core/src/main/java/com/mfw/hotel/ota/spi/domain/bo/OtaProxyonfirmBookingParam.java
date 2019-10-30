package com.mfw.hotel.ota.spi.domain.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OtaProxyonfirmBookingParam {
    /**
     * 外部订单编号
     */
    @NotNull
    private String ctrip_no;

    /**
     * 代理id
     */
    @NotNull
    private String agent_id;

    /**
     * agent_op	代理op	string
     */
    @NotNull
    private String agent_op;

    /**
     *固定1
     */
    @NotNull
    private String agreement;

    /**
     * 固定1
     */
    @NotNull
    private String shortterm;

    /**
     * 固定1
     */
    @NotNull
    private String purchase;

    /**
     * 固定1
     */
    @NotNull
    private String freesales;

    /**
     * 固定1
     */
    @NotNull
    private String keeproom;

    /**
     * 固定1
     */
    @NotNull
    private String temairazu;

    /**
     * 酒店编号
     */
    @NotNull
    private String h_code;

    /**
     * 房型
     */
    @NotNull
    private String room_type;

    /**
     * 房间数量
     */
    @NotNull
    private int cnt;

    /**
     * year
     */
    @NotNull
    private int year;

    /**
     * month
     */
    @NotNull
    private int month;

    /**
     * month
     */
    @NotNull
    private int day;

    /**
     * 天数
     */
    @NotNull
    private int night;

    /**
     * 人数
     */

    //private String pax[n];

    /**
     * 姓名
     */

    //private String people[n];

}