package com.mfw.hotel.ota.spi.service;

import com.mfw.hotel.ota.proxy.sdk.domain.OrderInfo;
import com.mfw.hotel.ota.proxy.sdk.domain.OtaProxyResponse;
import com.mfw.hotel.ota.proxy.sdk.domain.dto.CancelDTO;
import com.mfw.hotel.ota.proxy.sdk.domain.dto.OrderDetailDTO;
import com.mfw.hotel.ota.proxy.sdk.domain.param.BookingParam;
import com.mfw.hotel.ota.proxy.sdk.domain.param.OrderDetailParam;
import com.mfw.hotel.ota.proxy.sdk.enums.CancelStatusEnum;
import com.mfw.hotel.ota.proxy.sdk.enums.ConfirmTypeEnum;
import com.mfw.hotel.ota.proxy.sdk.enums.OrderStatusEnum;
import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import com.mfw.hotel.ota.spi.constants.JweConnectionPoolParam;
import com.mfw.hotel.ota.spi.util.PropertiesUtil;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OtaHttpUtil.class})
@PowerMockIgnore("javax.net.ssl.*")
public class JweServiceTest {

    @Test
    public void preBooking() {
        PowerMockito.mockStatic(OtaHttpUtil.class);
        PowerMockito.when(OtaHttpUtil.get("http://localhost:8080/dst")).thenReturn("test result");
        assertEquals(new JweService().preBooking(null).getData(), "test result");
    }
    @Test
    public void confirmCancelTest() {
        //构建PowerMockito
        PowerMockito.mockStatic(OtaHttpUtil.class);
        //构建请求参数
        HashMap<String, String> map = new HashMap<>();
        map.put("b_no", "456");
        //设置连接池
        OkHttpClient okHttpClient = OtaHttpUtil.newClient(
                JweConnectionPoolParam.CONFIRMCACNCEL_MAX_IDLE_CONNECTIONS,
                JweConnectionPoolParam.CONFIRMCACNCEL_KEEPALIVE_DURATION,
                JweConnectionPoolParam.CONFIRMCACNCEL_READ_TIMEOUT,
                JweConnectionPoolParam.CONFIRMCACNCEL_CONNECT_TIMEOUT
        );
        PowerMockito.when(OtaHttpUtil.get(PropertiesUtil.get("url.jwe.confirmCancel"), map, okHttpClient))
                .thenReturn("{'status':1}");
        //构建请求数据
        JweService jweService = new JweService();
        BookingParam bookingParam = new BookingParam();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOtaOrderId("123,456");
        bookingParam.setOrderInfo(orderInfo);

        assertEquals(new JweService().confirmCancel(bookingParam).getData().getStatus(), CancelStatusEnum.SUCESS.getCode());
    }


    @Test
    public void getOrderDetailTest() {
        //构建PowerMockito
        PowerMockito.mockStatic(OtaHttpUtil.class);
        HashMap<String, String> queryMap = new HashMap<>();
        //ota id 为123
        queryMap.put("b_code", "123");
        //mfw id 为456
        queryMap.put("ctrip_no", "456");
        //马蜂窝的代理商id
        queryMap.put("agent_id", "MFW");
        PowerMockito.when(OtaHttpUtil.get(PropertiesUtil.get("url.jwe.getOrderDetail"), queryMap))
                .thenReturn("{'status':1,'data':[{'b_state':2,'b_code':'001'},{'b_state':0,'b_code':'002'}]}");
        //构建请求数据
        JweService jweService = new JweService();
        OrderDetailParam orderDetailParam = new OrderDetailParam();
        //mfw id 为456
        orderDetailParam.setOrderId("456");
        //ota id 为123
        orderDetailParam.setOtaOrderId("123,456");
        OtaProxyResponse<OrderDetailDTO> orderDetail = new JweService().getOrderDetail(orderDetailParam);
        //验证父订单状态，任意子订单状态不为2时父订单成功
        assertEquals(orderDetail.getData().getStatus(), OrderStatusEnum.SUCCESS.getCode());
        //验证子订单是否正确封装
        assertEquals(orderDetail.getData().getOrder().get(0).getOtaOrderId(), "001");
        assertEquals(orderDetail.getData().getOrder().get(1).getStatus(), "0");
    }
}