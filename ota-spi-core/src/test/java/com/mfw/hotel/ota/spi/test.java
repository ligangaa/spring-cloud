package com.mfw.hotel.ota.spi;


import com.alibaba.fastjson.JSON;
import com.mfw.hotel.ota.proxy.sdk.domain.RatePlan;
import com.mfw.hotel.ota.spi.domain.bo.RatePlanBo;

import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
    @Test
    public void test(){
        Map map = new HashMap();
        StringBuilder ls_XML_ALL = new StringBuilder();
        ls_XML_ALL.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n");
        ls_XML_ALL.append("<ConditionMultiHotel>\r\n");
        ls_XML_ALL.append("  <ChkIn>2019-11-21</ChkIn>\r\n");
        ls_XML_ALL.append("  <ChkOut>2019-11-22</ChkOut>\r\n");
        ls_XML_ALL.append("  <HotelID>455</HotelID>\r\n");
        ls_XML_ALL.append("  <AccountUserID>223</AccountUserID>\r\n");
        ls_XML_ALL.append("  <MainRoomTypeID></MainRoomTypeID>\r\n");
        ls_XML_ALL.append("  <GradeID></GradeID>\r\n");
        ls_XML_ALL.append("  <RoomBedID></RoomBedID>\r\n");
        ls_XML_ALL.append("  <RoomViewID></RoomViewID>\r\n");
        ls_XML_ALL.append("  <Persons></Persons>\r\n");
        ls_XML_ALL.append("</ConditionMultiHotel>\r\n");
        String lsString = ls_XML_ALL.toString();
        System.out.println(lsString);
        map.put("Inquery_XML",lsString);
        String postString = OtaHttpUtil.postForm("http://test.58899.net/Transport/Inquery807.aspx",map);
        JSONObject jsonObject = XML.toJSONObject(postString);
        List<RatePlanBo> ratePlanVos = JSON.parseArray(jsonObject.getJSONObject("HotelRoom").get("RoomPrice").toString(), RatePlanBo.class);
        List<RatePlan> list = new ArrayList<>();
        for (RatePlanBo ratePlanVo : ratePlanVos) {
            System.out.println(ratePlanVo.toString());
        }



    }
}
