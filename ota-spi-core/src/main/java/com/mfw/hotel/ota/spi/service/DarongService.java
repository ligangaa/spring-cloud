package com.mfw.hotel.ota.spi.service;

import com.alibaba.fastjson.JSON;
import com.mfw.hotel.ota.proxy.sdk.IOtaBaseService;
import com.mfw.hotel.ota.proxy.sdk.annotation.Ota;
import com.mfw.hotel.ota.spi.constant.BedTypeConstant;
import com.mfw.hotel.ota.spi.constant.CurrencyConstant;
import com.mfw.hotel.ota.spi.constant.DarongAddressTagConstant;
import com.mfw.hotel.ota.proxy.sdk.constants.OtaConstant;
import com.mfw.hotel.ota.proxy.sdk.domain.*;
import com.mfw.hotel.ota.proxy.sdk.domain.dto.*;
import com.mfw.hotel.ota.proxy.sdk.domain.param.*;
import com.mfw.hotel.ota.proxy.sdk.enums.*;
import com.mfw.hotel.ota.proxy.sdk.util.OtaHttpUtil;
import com.mfw.hotel.ota.proxy.sdk.util.OtaProxyResponseUtil;
import com.mfw.hotel.ota.spi.enums.RoomTypeEnum;
import com.mfw.hotel.ota.spi.domain.bo.RatePlanBo;
import com.mfw.hotel.ota.spi.util.PropertiesUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.net.ResponseCache;
import java.util.*;

@Ota(id = OtaConstant.DARONG_ID)
public class DarongService implements IOtaBaseService {

    @Autowired
    private DarongService darongService;
    @Override
    public OtaProxyResponse<HotelIdsDTO> getHotels(OtaProxyPagingParam otaProxyPagingParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<HotelDetailDTO> getHotelDetail(HotelParam hotelParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<HotelRoomsDTO> getHotelRooms(HotelParam hotelParam) {
        return null;
    }
    /**
     * 价格计划列表
     *
     */
    @Override
    public OtaProxyResponse<RatePlanDTO> getRatePlans(RatePlanParam ratePlanParam) {
        String postString = null;
        List<RatePlanBo> ratePlanBos = new ArrayList<>();
        try {
            Map map = new HashMap();
            StringBuilder ls_XML_ALL = new StringBuilder();
            ls_XML_ALL.append(DarongAddressTagConstant.HEADER_TAG);
            ls_XML_ALL.append(DarongAddressTagConstant.CONDITIONMULTIHOTEL_TAG_PRE);
            ls_XML_ALL.append(DarongAddressTagConstant.CHKIN_TAG_PRE+ratePlanParam.getCheckIn()+DarongAddressTagConstant.CHKIN_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.CHKOUT_TAG_PRE+ratePlanParam.getCheckOut()+DarongAddressTagConstant.CHKOUT_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.HOTELID_TAG_PRE+ratePlanParam.getOtaHotelId()+DarongAddressTagConstant.HOTELID_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.ACCOUNTUSERID_TAG_PRE+"223"+DarongAddressTagConstant.ACCOUNTUSERID_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.MAINROOMTYPEID_TAG_PRE+DarongAddressTagConstant.MAINROOMTYPEID_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.GRADEID_TAG_PRE+DarongAddressTagConstant.GRADEID_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.ROOMBEDID_TAG_PRE+DarongAddressTagConstant.ROOMBEDID_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.ROOMVIEWID_TAG_PRE+DarongAddressTagConstant.ROOMVIEWID_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.PERSONS_TAG_PRE+ratePlanParam.getRooms()+DarongAddressTagConstant.PERSONS_TAG_SUF);
            ls_XML_ALL.append(DarongAddressTagConstant.CONDITIONMULTIHOTEL_TAG_SUF);
            map.put("Inquery_XML",ls_XML_ALL.toString());
            postString = OtaHttpUtil.postForm(PropertiesUtil.get("Darong.Address807"),map);
            JSONObject jsonObject = XML.toJSONObject(postString);
            ratePlanBos = JSON.parseArray(jsonObject.getJSONObject("HotelRoom").get("RoomPrice").toString(), RatePlanBo.class);
        } catch (Exception e) {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.ILLEGAL_PARAM);
        }

        try {
            //HotelInfo节点：酒店信息
            HotelInfo hotelInfo = new HotelInfo();
            hotelInfo.setCheckInNotice("");
            //RatePlan节点：价格计划列表
            List<RatePlan> ratePlans = new ArrayList<>();
            for (RatePlanBo ratePlanBo : ratePlanBos) {
                RatePlan ratePlan = new RatePlan();
                //room type id房型ID
                String mainRoomType = ratePlanBo.getMainRoomType();
                if (RoomTypeEnum.SGL.getDescription().equals(mainRoomType)){
                    ratePlan.setRoomTypeId(RoomTypeEnum.SGL.getCode());
                }else if (RoomTypeEnum.DBL.getDescription().equals(mainRoomType)){
                    ratePlan.setRoomTypeId(RoomTypeEnum.DBL.getCode());
                }else if (RoomTypeEnum.TWN.getDescription().equals(mainRoomType)){
                    ratePlan.setRoomTypeId(RoomTypeEnum.TWN.getCode());
                }else if (RoomTypeEnum.TRP.getDescription().equals(mainRoomType)){
                    ratePlan.setRoomTypeId(RoomTypeEnum.TRP.getCode());
                }else if (RoomTypeEnum.Fourth.getDescription().equals(mainRoomType)){
                    ratePlan.setRoomTypeId(RoomTypeEnum.Fourth.getCode());
                }else if (RoomTypeEnum.JapaneseRoom.getDescription().equals(mainRoomType)){
                    ratePlan.setRoomTypeId(RoomTypeEnum.JapaneseRoom.getCode());
                }

                ratePlan.setRoomId(ratePlanBo.getPlanID());
                //CancelValue节点   取消政策结构化
                List<CancelValue> cancelValues = new ArrayList<>();
                CancelValue cancelValueBefore = new CancelValue();
                cancelValueBefore.setDate(ratePlanBo.getCancelDeadLine());
                cancelValueBefore.setDateType(DateTypeEnum.BEFORE.getCode());
                cancelValueBefore.setDeduct(new BigDecimal(0));
                cancelValueBefore.setDeductType(DeductTypeEnum.PERCENT.getCode());
                cancelValueBefore.setCurrency(CurrencyConstant.YEN);
                CancelValue cancelValueAfter = new CancelValue();
                cancelValueAfter.setDate(ratePlanBo.getCancelDeadLine());
                cancelValueAfter.setDateType(DateTypeEnum.AFTER.getCode());
                cancelValueAfter.setDeduct(new BigDecimal(1));
                cancelValueAfter.setDeductType(DeductTypeEnum.PERCENT.getCode());
                cancelValueAfter.setCurrency(CurrencyConstant.YEN);
                cancelValues.add(cancelValueBefore);
                cancelValues.add(cancelValueAfter);
                ratePlan.setCancelValues(cancelValues);

                // PerNightRate节点 每晚价格
                PerNightRate perNightRate = new PerNightRate();
                perNightRate.setD(new Date(0));
                perNightRate.setOp(ratePlanBo.getPrice());
                perNightRate.setP(ratePlanBo.getPrice());
                perNightRate.setY(DiscountEnum.NO.getCode());
                ratePlan.setTotalRate(ratePlanBo.getPrice());
                ratePlan.setPayType(PayTypeEnum.PREPAY.getCode());

                //BedType节点: 床型 和免费最大儿童数处理
                ArrayList<BedType> bedTypes = new ArrayList<>();
                String bedTypeName = ratePlanBo.getRoomTypeByBed();
                BedType bedType = new BedType();
                switch (bedTypeName){
                    case BedTypeConstant.SGL: bedType.setId("1");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.DBL_FOR_SGL_USE: bedType.setId("2");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.ONE_PAX_RUN_OF_HOUSE: bedType.setId("3");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.TWN_FOR_SGL_USE: bedType.setId("4");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.DBL: bedType.setId("5");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.SEMIDBL: bedType.setId("6");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.TWN: bedType.setId("7");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.SGL_EXTRABED: bedType.setId("8");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.TWO_PAX_RUN_OF_HOUSE: bedType.setId("9");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.TWN_EXTRABED: bedType.setId("10");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.DBL_EXTRABED: bedType.setId("11");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.THREE_PAX_RUN_OF_HOUSE: bedType.setId("12");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.TWN_2EXTRABEDS: bedType.setId("13");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.FOURTH: bedType.setId("14");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.JAPANESE_STYLE: bedType.setId("15");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.JAPANESE_AND_WESTERN_STYLE: bedType.setId("16");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.TRP: bedType.setId("17");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.SEMIDBL_FOR_SGL_USE: bedType.setId("18");ratePlan.setMaxChildrenFree(1);break;
                    case BedTypeConstant.SEMIDBL_EXTRA_BED: bedType.setId("19");ratePlan.setMaxChildrenFree(1);break;
                    default:bedType.setId("0");ratePlan.setMaxChildrenFree(1);;
                }
                bedType.setName(ratePlanBo.getRoomTypeByBed());
                bedTypes.add(bedType);
                ratePlan.setBedTypes(bedTypes);
                //remain num剩余库存
                if (ratePlanBo.getAlloment()==null || "".equals(ratePlanBo.getAlloment())){
                    ratePlan.setRemainNum(2);
                }else {
                    ratePlan.setRemainNum(ratePlanBo.getAlloment());
                }

                ratePlan.setPerRoomPNum(ratePlanBo.getPersons());
                //Meal节点：结构化餐食信息
                Meals meals = new Meals();
                meals.setType(MealTypeEnum.BREAKFAST.getCode());
                meals.setNum(-1);
                List<MealDetail> mealDetails = new ArrayList<>();
                MealDetail mealDetail = new MealDetail();
                mealDetail.setName(ratePlanBo.getBreakfast());
                mealDetails.add(mealDetail);
                meals.setDetail(mealDetails);

                ratePlan.setBreakfast(ratePlanBo.getBreakfast());
                ratePlan.setDescription(ratePlanBo.getPlanName());
                List<ExcludeSurcharge> excludeSurcharges = new ArrayList<>();

                ratePlan.setMaxChildrenFreeAge(6);
                ratePlan.setBeforeDays(ratePlanBo.getActiveDays());
                ratePlan.setMinDays(ratePlanBo.getMinOrderDays());
                ratePlan.setConfirmType(ConfirmTypeEnum.IMMEDIATE.getCode());
                ratePlan.setCurrency(CurrencyConstant.YEN);
                ratePlan.setInvoiceMode(InvoiceModeEnum.MFW_INVOICE.getCode());
                ratePlan.setPromotion(new ArrayList<Promotion>());
                ratePlan.setSpecialRequire(Arrays.asList(SpecialRequireEnum.QUIET_ROOM.getCode(),SpecialRequireEnum.TOP_FLOOR.getCode(),SpecialRequireEnum.NEIGHBOUR_ROOM.getCode()));
                ratePlan.setAllowManSpecial(AllowManSpecialEnum.YES.getCode());
                ratePlans.add(ratePlan);
            }
            RatePlanDTO ratePlanDTO = RatePlanDTO.builder().build();
            ratePlanDTO.setHotelInfo(hotelInfo);
            ratePlanDTO.setRatePlans(ratePlans);
            return OtaProxyResponseUtil.success(OtaProxyResponseEnum.SUCCESS,ratePlanDTO);
        } catch (Exception e) {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.ILLEGAL_RESPONSE_STRUCTURE);
        }
    }

    /**
     * 价格计划列表
     * @param
     * @return
     */
    @Override
    public OtaProxyResponse<RatePlanDTO> getAvailRooms(RatePlanParam ratePlanParam) {
        try {
            OtaProxyResponse<RatePlanDTO> ratePlans = darongService.getRatePlans(ratePlanParam);
            HotelInfo hotelInfo = ratePlans.getData().getHotelInfo();
            List<RatePlan> queryRatePlans = ratePlans.getData().getRatePlans();
            List<RatePlan> availRooms = new ArrayList<>();
            for (RatePlan ratePlan : queryRatePlans) {
                if (ratePlanParam.getRoomTypeId() == ratePlan.getRoomTypeId() && ratePlanParam.getRoomId() == ratePlan.getRoomId()){
                    availRooms.add(ratePlan);
                }
            }
            RatePlanDTO ratePlanDTO = RatePlanDTO.builder().build();
            ratePlanDTO.setHotelInfo(hotelInfo);
            ratePlanDTO.setRatePlans(availRooms);
            return OtaProxyResponseUtil.success(OtaProxyResponseEnum.SUCCESS,ratePlanDTO);
        } catch (Exception e) {
            return OtaProxyResponseUtil.error(OtaProxyResponseEnum.ILLEGAL_PARAM);
        }
    }

    @Override
    public OtaProxyResponse<String> preBooking(BookingParam bookingParam) {
        StringBuilder ls_XML_ALL = new StringBuilder() ;

        ls_XML_ALL.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n")
        .append("<NewBooking_PreBooking>\r\n")
        .append("<AccountUserID>"+ bookingParam.getOrderInfo().getOtaHotelId()+"</AccountUserID>\r\n")
        .append("<BookingNo>"+ bookingParam.getOrderInfo().getOrderId()+"</BookingNo>\r\n")
        .append("<HotelID>"+ bookingParam.getOrderInfo().getOtaHotelId()+"</HotelID>\r\n")
        .append("<PlanID>"+ bookingParam.getOrderInfo().getRoomId()+"</PlanID>\r\n")
        .append("<ChkIn>"+ bookingParam.getOrderInfo().getCheckIn()+"</ChkIn>\r\n")
        .append("<ChkOut>"+ bookingParam.getOrderInfo().getCheckOut()+"</ChkOut>\r\n")
        .append("<QTY>"+ bookingParam.getOrderInfo().getRoomNum()+"</QTY>\r\n")
        .append("<breakfast>"+ bookingParam.getOrderInfo().getBreakfast()+"</breakfast>\r\n")
        .append("</NewBooking_PreBooking>\r\n");
        String ls_String = ls_XML_ALL.toString();

        Map<String,String> map = new HashMap<String,String>();
        map.put("NewBooking_XML",ls_String);
        String result = OtaHttpUtil.postForm("http://test.58899.net/Transport/PreBooking_100.aspx", map);
        //将加入购物车的返回结果放入bookingParam中
        bookingParam.setPreBookingResult(result);
        //成功返回数据
        return OtaProxyResponseUtil.success(result);
    }

    @Override
    public OtaProxyResponse<BookingDTO> confirmBooking(BookingParam bookingParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<OrderDetailDTO> getOrderDetail(OrderDetailParam orderDetailParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<String> preCancel(BookingParam bookingParam) {
        return null;
    }

    @Override
    public OtaProxyResponse<CancelDTO> confirmCancel(BookingParam bookingParam) {
        return null;
    }
}
