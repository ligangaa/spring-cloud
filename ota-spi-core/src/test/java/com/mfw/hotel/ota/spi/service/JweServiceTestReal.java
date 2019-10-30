package com.mfw.hotel.ota.spi.service;

import com.mfw.hotel.ota.proxy.sdk.domain.OrderInfo;
import com.mfw.hotel.ota.proxy.sdk.domain.param.BookingParam;
import com.mfw.hotel.ota.proxy.sdk.domain.param.OrderDetailParam;
import com.mfw.hotel.ota.proxy.sdk.enums.CancelStatusEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JweServiceTestReal {
    @Test
    public void confirmCancelTestReal() {

        //构建请求数据
        JweService jweService = new JweService();
        BookingParam bookingParam = new BookingParam();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOtaOrderId("123,456");
        bookingParam.setOrderInfo(orderInfo);
        //假数据 正常返回应该是失败
        assertEquals(new JweService().confirmCancel(bookingParam).getData().getStatus(), CancelStatusEnum.EXCEPTION.getCode());
    }

    @Test
    public void getOrderDetailTest() {
        //构建请求数据
        JweService jweService = new JweService();
        OrderDetailParam orderDetailParam = new OrderDetailParam();
        //OrderId是可空参数，不设置应该测试通过
        //orderDetailParam.setOtaOrderId("123,456");

        assertEquals("成功", new JweService().getOrderDetail(orderDetailParam).getMessage());
    }
}
