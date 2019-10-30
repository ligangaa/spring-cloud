package com.mfw.hotel.ota.spi.service;

import com.mfw.hotel.ota.proxy.sdk.domain.dto.HotelDetailDTO;
import com.mfw.hotel.ota.proxy.sdk.domain.dto.HotelIdsDTO;
import com.mfw.hotel.ota.proxy.sdk.domain.dto.HotelRoomsDTO;
import com.mfw.hotel.ota.proxy.sdk.domain.param.HotelParam;
import com.mfw.hotel.ota.proxy.sdk.domain.param.OrderDetailParam;
import com.mfw.hotel.ota.proxy.sdk.domain.param.OtaProxyPagingParam;
import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import com.mfw.hotel.ota.spi.util.PropertiesUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JweTest {
    public static void main(String[] arg){
        OtaProxyPagingParam otaProxyPagingParam=new OtaProxyPagingParam();
        otaProxyPagingParam.setPageNum(1);
        otaProxyPagingParam.setPageSize(2);
        String response = OtaHttpUtil.get("https://www.jwe.jp/admin/api/get_hotel_list.jsp");
        HotelIdsDTO data=new JweService().getHotels(otaProxyPagingParam).getData();

    }
    @Test
    public void getHotelDetail() {
        //构建请求数据
        HotelParam hotelParam = new HotelParam();
        hotelParam.setHotelId("1193");

       HotelDetailDTO s= new JweService().getHotelDetail(hotelParam).getData();
    }
    @Test
    public void getHotelRooms() {
        //构建请求数据
        HotelParam hotelParam = new HotelParam();
        hotelParam.setHotelId("1193");

        HotelRoomsDTO s= new JweService().getHotelRooms(hotelParam).getData();
    }
}
