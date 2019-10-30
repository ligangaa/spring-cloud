package com.mfw.hotel.ota.spi.domain.bo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class RatePlanBo {
    private String hotelID;
    private String hotelName;
    private String planID;
    private String planName;
    private String date;
    private BigDecimal price;
    private String mainRoomType;
    private String roomGrade;
    private String roomTypeByBed;
    private String roomView;
    private String breakfast;
    private String guarantee;
    private Integer activeDays;
    private Integer alloment;
    private Integer minOrderDays;
    private String remark;
    private Date cancelDeadLine;
    private Integer persons;
}
