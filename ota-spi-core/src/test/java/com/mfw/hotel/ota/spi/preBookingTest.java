package com.mfw.hotel.ota.spi;


import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class preBookingTest {

    @Test
    public void test01() {

        StringBuilder ls_XML_ALL = new StringBuilder();

        ls_XML_ALL.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
        ls_XML_ALL.append("<NewBooking_PreBooking>\r\n");
        ls_XML_ALL.append("<AccountUserID>223</AccountUserID>\r\n");
        ls_XML_ALL.append("<BookingNo>K1-000091</BookingNo>\r\n");
        ls_XML_ALL.append("<HotelID>455</HotelID>\r\n");
        ls_XML_ALL.append("<PlanID>2530</PlanID>\r\n");
        ls_XML_ALL.append("<ChkIn>2019-11-20</ChkIn>\r\n");
        ls_XML_ALL.append("<ChkOut>2019-11-21</ChkOut>\r\n");
        ls_XML_ALL.append("<QTY>1</QTY>\r\n");
        ls_XML_ALL.append("<breakfast>NO MEAL</breakfast>\r\n");
        ls_XML_ALL.append("</NewBooking_PreBooking>\r\n");
        String ls_String = ls_XML_ALL.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("NewBooking_XML", ls_String);
        String result = OtaHttpUtil.postForm("http://test.58899.net/Transport/PreBooking_100.aspx", map);
        System.out.println(result);
    }
}
