package com.mfw.hotel.ota.spi.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.mfw.hotel.ota.proxy.sdk.IOtaBaseService;
import com.mfw.hotel.ota.proxy.sdk.constants.OtaConstant;
import com.mfw.hotel.ota.proxy.sdk.domain.*;
import com.mfw.hotel.ota.proxy.sdk.domain.Order;
import com.mfw.hotel.ota.proxy.sdk.annotation.Ota;
import com.mfw.hotel.ota.proxy.sdk.domain.Order;
import com.mfw.hotel.ota.proxy.sdk.domain.OrderInfo;
import com.mfw.hotel.ota.proxy.sdk.domain.OtaProxyResponse;
import com.mfw.hotel.ota.proxy.sdk.domain.RoomContact;
import com.mfw.hotel.ota.proxy.sdk.domain.dto.*;
import com.mfw.hotel.ota.proxy.sdk.domain.param.*;
import com.mfw.hotel.ota.proxy.sdk.enums.*;
import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import com.mfw.hotel.ota.proxy.sdk.util.OtaProxyResponseUtil;
import com.mfw.hotel.ota.proxy.sdk.enums.OtaProxyResponseEnum;
import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import com.mfw.hotel.ota.proxy.sdk.util.OtaProxyResponseUtil;
import com.mfw.hotel.ota.spi.constants.JweConnectionPoolParam;
import com.mfw.hotel.ota.spi.constants.JweConstants;
import com.mfw.hotel.ota.spi.domain.bo.JweHotelDetailBO;
import com.mfw.hotel.ota.spi.domain.bo.JweHotelRoomsBO;
import com.mfw.hotel.ota.spi.domain.bo.JweHotelsBO;
import com.mfw.hotel.ota.spi.domain.bo.jwe.JweCancelDetailBO;
import com.mfw.hotel.ota.spi.domain.bo.jwe.JweConfirmBookingBO;
import com.mfw.hotel.ota.spi.domain.bo.jwe.JweOrderDetailBO;
import com.mfw.hotel.ota.spi.enums.jwe.JweCancelStatusEnum;
import com.mfw.hotel.ota.spi.enums.jwe.JweOrderStateEnum;
import com.mfw.hotel.ota.spi.enums.jwe.JweOrderStatusEnum;
import com.mfw.hotel.ota.spi.util.OtaOrderIdUtil;
import com.mfw.hotel.ota.spi.util.PropertiesUtil;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Ota(id = OtaConstant.JWE_ID)
public class JweService implements IOtaBaseService {

    private static final String HOTELS_URL = "url.jwe.getHotels";
    private static final String HOTEL_DETAIL_URL = "url.jwe.getHotelDetail";
    private static final String HOTEL_ROOM = "url.jwe.getHotelRooms";
    private static final String AGENT_ID = "MFW";
    private static final String ROOM_TYPE = "room_type";

    @Override
    public OtaProxyResponse<HotelIdsDTO> getHotels(OtaProxyPagingParam otaProxyPagingParam) {
        //请求参数支持分页
        Integer pageNum = otaProxyPagingParam.getPageNum();//当前页
        Integer pageSize = otaProxyPagingParam.getPageSize();//每页多少条
        String response = OtaHttpUtil.get(PropertiesUtil.get(JweService.HOTELS_URL));
        JweHotelsBO jweHotelsBO = JSONObject.parseObject(response, JweHotelsBO.class);
        //返回成功
        if (jweHotelsBO.getStatus() == 1) {
            List<Map<String, String>> list = jweHotelsBO.getData();
            HotelIdsDTO hotelIdsDTO = HotelIdsDTO.builder().build();
            List<String> codeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(codeList)) {
                for (Map m : list) {
                    codeList.add((String) m.get(JweConstants.H_CODE));//h_code
                }
            }
            hotelIdsDTO.setIdList(codeList.subList((pageNum - 1) * pageSize, (pageNum * pageSize - 1)<codeList.size()?pageNum * pageSize - 1:codeList.size()-1));
            hotelIdsDTO.setTotal(codeList.size());
            return OtaProxyResponse.<HotelIdsDTO>builder().data(hotelIdsDTO).
                    message(jweHotelsBO.getMsg()).code(jweHotelsBO.getStatus()).build();
        } else {
            return OtaProxyResponse.<HotelIdsDTO>builder().message(jweHotelsBO.getMsg()).code(jweHotelsBO.getStatus()).build();
        }

    }

    @Override
    public OtaProxyResponse<HotelDetailDTO> getHotelDetail(HotelParam hotelParam) {
        Map parameterMap = new HashMap();
        parameterMap.put(JweConstants.H_CODE, hotelParam.getHotelId());
        parameterMap.put(JweConstants.AGENT_ID, JweService.AGENT_ID);
        String response = OtaHttpUtil.get(PropertiesUtil.get(JweService.HOTEL_DETAIL_URL), parameterMap);
        JweHotelDetailBO jweHotelDetailBO = JSONObject.parseObject(response, JweHotelDetailBO.class);

        if (jweHotelDetailBO.getStatus() == 1) {
            //详情vo
            HotelDetailDTO hotelDetailDTO = HotelDetailDTO.builder().build();
            HotelBaseInfo hotelBaseInfo = new HotelBaseInfo();
            hotelBaseInfo.setHotelId(jweHotelDetailBO.getH_code());//酒店id
            hotelBaseInfo.setName(jweHotelDetailBO.getH_name());
            hotelBaseInfo.setAddress(jweHotelDetailBO.getAddress());
            hotelBaseInfo.setLatitude(jweHotelDetailBO.getLat());
            hotelBaseInfo.setLongitude(jweHotelDetailBO.getLng());
            hotelBaseInfo.setPhone(jweHotelDetailBO.getTal());
            hotelDetailDTO.setBaseInfo(hotelBaseInfo);
            return OtaProxyResponse.<HotelDetailDTO>builder().data(hotelDetailDTO).code(jweHotelDetailBO.getStatus()).message(jweHotelDetailBO.getMsg()).build();
        } else {
            //失败返回
            return OtaProxyResponse.<HotelDetailDTO>builder().code(jweHotelDetailBO.getStatus()).message(jweHotelDetailBO.getMsg()).build();

        }
    }

    @Override
    public OtaProxyResponse<HotelRoomsDTO> getHotelRooms(HotelParam hotelParam) {
        //请求ota参数
        Map parameterMap = new HashMap();
        parameterMap.put(JweConstants.H_CODE, hotelParam.getHotelId());
        SimpleDateFormat sdf = new SimpleDateFormat(JweConstants.DATE_FORMAT);
        Date date = new Date();
        String dt = sdf.format(date);
        parameterMap.put(JweConstants.DATE, dt);
        String response = OtaHttpUtil.get(PropertiesUtil.get(JweService.HOTEL_ROOM), parameterMap);
        //ota返回结果
        JweHotelRoomsBO jweHotelRoomsBO = JSONObject.parseObject(response, JweHotelRoomsBO.class);
        if (jweHotelRoomsBO.getStatus() == 1) {
            HotelRoomsDTO hotelRoomsDTO = HotelRoomsDTO.builder().build();
            hotelRoomsDTO.setHotelId(hotelParam.getHotelId());
            List<Map<String, String>> list = jweHotelRoomsBO.getRoom_data();
            List<Room> rooms = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(list)) {
                for (Map<String, String> m : list) {
                    Room room = new Room();
                    String roomType = m.get(JweService.ROOM_TYPE);
                    room.setRoomId(roomType);
                    room.setName(getChineseName(getName(roomType)));//房型中文名
                    room.setDescription(getRoomDesc(getName(roomType)));//房型描述
                    room.setMaxPerson(0);
                    List<Meals> mList = new ArrayList<>();
                    if (roomType.contains("R/B")) {
                        Meals meals = new Meals();
                        meals.setType(MealTypeEnum.BREAKFAST.getCode());
                        mList.add(meals);
                    }
                    if (roomType.contains("R/D")) {
                        Meals meals = new Meals();
                        meals.setType(MealTypeEnum.DINNER.getCode());//
                        mList.add(meals);
                    }
                    if (roomType.contains("R/BD")) {
                        Meals meals = new Meals();
                        meals.setType(MealTypeEnum.OTHER.getCode());
                        mList.add(meals);
                    }
                    room.setMeals(mList);
                    rooms.add(room);
                }
            }
            hotelRoomsDTO.setRooms(rooms);
            return OtaProxyResponse.<HotelRoomsDTO>builder().data(hotelRoomsDTO).
                    code(jweHotelRoomsBO.getStatus()).message(jweHotelRoomsBO.getMsg()).build();
        } else {
            return OtaProxyResponse.<HotelRoomsDTO>builder().code(jweHotelRoomsBO.getStatus()).message(jweHotelRoomsBO.getMsg()).build();
        }

    }

    // 获取房型描述
    private String getRoomDesc(String name) {
        String desc = null;
        if (name == null) {
            return null;
        } else if ("SGL USE".equals(name) || "SGL".equals(name)) {
            desc = "限一人使用";
        } else if ("SDBL".equals(name) || "SINGLG 2PAX".equals(name)) {
            desc = "单人房的面积，床稍微大点，通常跟单人间一样，但可以两人使用";
        } else if ("DBL".equals(name)) {
            desc = "一张大床，通常可2人使用";
        } else if ("TWN".equals(name)) {
            desc = "2张单人床，通常可2人使用";
        } else if ("TRP".equals(name) || "TWN+EXTRA".equals(name)) {
            desc = "根据酒店不同，床型不同，通常为2张单人床，可3人使用";
        } else if (name.contains("PAX")) {
            desc = "可入住的成年人最多" + name.charAt(0) + "位";
        } else if ("CAPSULE".equals(name)) {
            desc = "只有一个床位，其他设施一般共用";
        } else if ("RUN OF HOUSE".equals(name)) {
            desc = "入住时不可制定房型及床型";
        } else if ("PRIVATE ROOM".equals(name)) {
            desc = "一般民宿，胶囊酒店中有独立卫浴的房间";
        } else if ("MIXED".equals(name)) {
            desc = "一般为民宿/胶囊酒店";
        } else {
            desc = null;
        }
        return desc;
    }

    //解析中文名
    private String getName(String roomType) {
        Pattern pattern = Pattern.compile("SGL USE|SGL|\\dPAX|SDBL|SINGLG 2PAX|DBL|TWN|TRP|TWN+EXTRA|QUAD|CAPSULE|FAMILY|JAPANESE(STYLE)|WESTERN(STYLE)|JAPANESE-WESTERN(STYLE)|RUN OF HOUSE|PRIVATE ROOM|LOFT|SUITE|MIXED");
        Matcher matcher = pattern.matcher(roomType);
        String name = null;
        while (matcher.find()) {
            name = matcher.group();
        }
        return name;
    }

    //解析room_type拿到房型中文name
    private String getChineseName(String name) {
        String chineseName = null;
        String[] rooms = {"SGL USE", "仅限一人使用", "SGL", "单人房", "SDBL", "小间大床房OR小型双人房", "SINGLG 2PAX", "单人间两人使用",
                "DBL", "大床房", "TWN", "双床房", "TRP", "三人间", "TWN+EXTRA", "三人间（双床加床）", "QUAD", "四人间",
                "CAPSULE", "胶囊房", "FAMILY", "家庭房", "JAPANESE(STYLE)", " 和室（和式）/日式房", "WESTERN(STYLE)", "洋室（洋式）/西式房",
                "JAPANESE-WESTERN(STYLE)", "和洋室（和洋式）", "RUN OF HOUSE", "不可指定房型", "PRIVATE ROOM", "私人房",
                "LOFT", "带阁楼房间", "SUITE", "套房", "MIXED", "男女混住"};
        if (name.contains("PAX")) {
            chineseName = name.charAt(0) + "人间";
            return chineseName;
        }
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].equals(name)) {
                chineseName = rooms[i + 1];
                return chineseName;
            }

        }

        return chineseName;
    }


    @Override
    public OtaProxyResponse<RatePlanDTO> getRatePlans(RatePlanParam ratePlanParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<RatePlanDTO> getAvailRooms(RatePlanParam ratePlanParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<String> preBooking(BookingParam bookingParam) {
        String result = OtaHttpUtil.get("http://localhost:8080/dst");
        return OtaProxyResponseUtil.success(OtaProxyResponseEnum.SUCCESS, result);
    }

    /**
     * 下单
     *
     * @param bookingParam
     * @return
     */
    @Override
    public OtaProxyResponse<BookingDTO> confirmBooking(BookingParam bookingParam) {
        Map confirmBookingMap = new HashMap<>();
        OrderInfo orderInfo = bookingParam.getOrderInfo();
        //外部订单编号ctrip_no
        String orderId = orderInfo.getOrderId();
        confirmBookingMap.put("ctrip_no", orderId);
        //代理id agent_id
        confirmBookingMap.put("agent_id", JweConstants.AGENT_ID);
        //代理op agent_op
        confirmBookingMap.put("agent_op", JweConstants.AGENT_OP);

        confirmBookingMap.put("agreement", JweConstants.NOE);
        confirmBookingMap.put("shortterm", JweConstants.NOE);
        confirmBookingMap.put("purchase", JweConstants.NOE);
        confirmBookingMap.put("freesales", JweConstants.NOE);
        confirmBookingMap.put("keeproom", JweConstants.NOE);
        confirmBookingMap.put("temairazu", JweConstants.NOE);
        //酒店编号 h_code
        confirmBookingMap.put("h_code", orderInfo.getOtaHotelId());
        //房型 room_type
        confirmBookingMap.put("room_type", orderInfo.getRoomTypeId());
        //房间数量cnt
        confirmBookingMap.put("cnt", orderInfo.getRoomNum());

        Date checkIn = orderInfo.getCheckIn();
        Date checkOut = orderInfo.getCheckOut();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr1 = format1.format(checkIn);
        String dateStr2 = format1.format(checkOut);
        String[] dateStr1Str = dateStr1.split("-");
        //入住 year
        confirmBookingMap.put("year", Integer.parseInt(dateStr1Str[0]));
        //入住 month
        confirmBookingMap.put("month", Integer.parseInt(dateStr1Str[1]));
        //入住 day
        confirmBookingMap.put("day", Integer.parseInt(dateStr1Str[2]));
        //天数 night
        int night = (int) ((checkOut.getTime() - checkIn.getTime()) / (24 * 60 * 60 * 1000));
        confirmBookingMap.put("night", night);
        //人数pax[n]
        List<String> roomInfo = orderInfo.getRoomInfo();
        //姓名people[n]
        List<RoomContact> roomContacts = orderInfo.getRoomContacts();
        int peonNum = 0;
        String cusName = "";
        String people = "";
        for (int i = 0; i < roomInfo.size(); i++) {//["2,3,5", "3,6,7"]
            String[] strArr = roomInfo.get(i).split(",");//2,3,5
            peonNum += Integer.parseInt(strArr[0]);
            if (strArr.length > 1) {//有儿童
                for (int j = 1; j < strArr.length; j++) {
                    int parseInt = Integer.parseInt(strArr[j]);
                    if (parseInt > 6) {//儿童年龄大于6岁
                        peonNum++;
                    }
                    confirmBookingMap.put("pax" + i + 1, peonNum);
                    for (int k = 0; k < roomContacts.size(); k++) {//房间联系人的姓名
                        if (k == i) {
                            cusName += roomContacts.get(k).getLn() + roomContacts.get(k).getFn();
                        }
                    }
                    people += "people" + i + 1;
                    confirmBookingMap.put(people, cusName);
                }
            } else {//没有儿童
                confirmBookingMap.put("pax" + 1, peonNum);
                for (int k = 0; k < roomContacts.size(); k++) {//房间联系人的姓名
                    if (k == i) {
                        cusName += roomContacts.get(k).getLn() + roomContacts.get(k).getFn();
                    }
                }
                people += "people" + i + 1;
                confirmBookingMap.put(people, cusName);
            }
        }
        String result = "";
        try {
            //给下单接口设置了独立的连接池
            OkHttpClient okHttpClient = OtaHttpUtil.newClient(
                    JweConnectionPoolParam.CONFIRMCACNCEL_MAX_IDLE_CONNECTIONS_confirmBooking,
                    JweConnectionPoolParam.CONFIRMCACNCEL_KEEPALIVE_DURATION,
                    JweConnectionPoolParam.CONFIRMCACNCEL_READ_TIMEOUT,
                    JweConnectionPoolParam.CONFIRMCACNCEL_CONNECT_TIMEOUT
            );
            result = OtaHttpUtil.get(PropertiesUtil.get("url.jwe.confirmBooking"), confirmBookingMap, okHttpClient);
        } catch (Exception e) {
            return OtaProxyResponse.<BookingDTO>builder().code(
                    OtaProxyResponseEnum.UNAVAILABLE_FOR_THIRD_PARTY.getCode()).build();
        }

        JweConfirmBookingBO confirmBookingOta = JSONObject.parseObject(result, JweConfirmBookingBO.class);
        if (confirmBookingOta == null || confirmBookingOta.getStatus() == null || confirmBookingOta.getB_code() == null || confirmBookingOta.getB_no() == null) {
            return OtaProxyResponse.<BookingDTO>builder().code(
                    OtaProxyResponseEnum.ILLEGAL_RESPONSE_STRUCTURE.getCode()
            ).build();
        }

        if (confirmBookingOta.getStatus() == JweOrderStatusEnum.FAIL.getCode()) {
            //第三方服务异常时响应
            return OtaProxyResponse.<BookingDTO>builder().code(
                    OtaProxyResponseEnum.UNAVAILABLE_FOR_THIRD_PARTY.getCode()
            ).build();
        }
        String bno = confirmBookingOta.getB_no();
        String bcode = confirmBookingOta.getB_code();
        String order_bno_bcode = bno + "," + bcode;
        BookingDTO bookingDTO = BookingDTO.builder().status(confirmBookingOta.getStatus() == 1 ?
                OrderStatusEnum.SUCCESS.getCode()
                : OrderStatusEnum.FAIL.getCode())
                .otaOrderId(order_bno_bcode)
                .refundReason("")
                .isRefund("").build();
        //正常响应
        return OtaProxyResponse.<BookingDTO>builder()
                .data(bookingDTO)
                .code(OtaProxyResponseEnum.SUCCESS.getCode())
                .build();
    }

    @Override
    public OtaProxyResponse<OrderDetailDTO> getOrderDetail(OrderDetailParam orderDetailParam) {
        //构造请求参数,电话和邮箱未使用
        HashMap<String, String> queryMap = new HashMap<>();
        //ota订单id可以为空，为空时跳过空指针异常
        try {
            queryMap.put(JweConstants.B_CODE, OtaOrderIdUtil.getBcode(orderDetailParam.getOtaOrderId()));
        } catch (Exception e) {

        }
        //非空
        queryMap.put(JweConstants.CTRIP_NO, orderDetailParam.getOrderId());
        //马蜂窝的代理商id，非空，常量
        queryMap.put(JweConstants.AGENT_ID, JweConstants.MFW_AGENT_ID);

        String json = OtaHttpUtil.get(PropertiesUtil.get("url.jwe.getOrderDetail"), queryMap);

        //处理响应结果
        JweOrderDetailBO orderDetailOta = JSONObject.parseObject(json, JweOrderDetailBO.class);

        //jwe接口返回的json解析失败
        if (orderDetailOta == null) {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.ILLEGAL_RESPONSE_STRUCTURE);
        }

        //jwe接口返回请求失败状态码
        if (orderDetailOta.getStatus().equals(JweOrderStatusEnum.FAIL.getCode())) {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.UNAVAILABLE_FOR_THIRD_PARTY);
        }

        //封装子订单数据
        ArrayList<Order> orders = new ArrayList<>();
        List<Map<String, String>> datas = orderDetailOta.getData();
        boolean flag = false;
        for (Map data : datas) {
            Order order = new Order();
            //设置订单id
            order.setOtaOrderId((String) data.get(JweConstants.B_CODE));
            order.setStatus((String) data.get(JweConstants.B_STATE));
            //2为取消订单,不等于表示订单成功，有一个自订单成功即整个订单成功
            if (!JweOrderStateEnum.CANCEL.getCode().equals(data.get(JweConstants.B_STATE))) {
                flag = true;
            }

            orders.add(order);
        }
        //封装父订单状态码，存在未被取消的子订单时，父订单状态为成功
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .order(orders)
                .status(flag ?
                        OrderStatusEnum.SUCCESS.getCode()
                        : OrderStatusEnum.FAIL.getCode())
                .build();
        //正常响应
        return OtaProxyResponseUtil.success(orderDetailDTO);
    }

    //jwe接口不需要预取消
    @Override
    public OtaProxyResponse<String> preCancel(BookingParam bookingParam) {
        return null;
    }

    //确认取消订单的连接池
    private static OkHttpClient confirmCancelClient;

    static {
        confirmCancelClient = OtaHttpUtil.newClient(
                JweConnectionPoolParam.CONFIRMCACNCEL_MAX_IDLE_CONNECTIONS,
                JweConnectionPoolParam.CONFIRMCACNCEL_KEEPALIVE_DURATION,
                JweConnectionPoolParam.CONFIRMCACNCEL_READ_TIMEOUT,
                JweConnectionPoolParam.CONFIRMCACNCEL_CONNECT_TIMEOUT
        );
    }

    @Override
    public OtaProxyResponse<CancelDTO> confirmCancel(BookingParam bookingParam) {
        //构造请求参数
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(JweConstants.B_NO, OtaOrderIdUtil.getBno(bookingParam.getOrderInfo().getOtaOrderId()));

        //给确认取消接口设置了独立的连接池

        String json = OtaHttpUtil.get(PropertiesUtil.get("url.jwe.confirmCancel"), queryMap, confirmCancelClient);

        //处理响应结果
        JweCancelDetailBO cancelDetailBO = JSONObject.parseObject(json, JweCancelDetailBO.class);
        //jwe返回的json解析失败
        if (cancelDetailBO == null || cancelDetailBO.getStatus() == null) {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.ILLEGAL_RESPONSE_STRUCTURE);
        }
        //判断返回的状态码
        if (JweCancelStatusEnum.SUCCESS.getCode().equals(cancelDetailBO.getStatus())) {
            return OtaProxyResponseUtil.success(CancelDTO.builder().status(CancelStatusEnum.SUCESS.getCode()).build());
        } else if (JweCancelStatusEnum.FAIL.getCode().equals(cancelDetailBO.getStatus())) {
            return OtaProxyResponseUtil.success(CancelDTO.builder().status(CancelStatusEnum.EXCEPTION.getCode()).build());
        } else {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.ILLEGAL_RESPONSE_STRUCTURE);
        }
    }
}
