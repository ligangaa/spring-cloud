package com.mfw.hotel.ota.spi.domain.bo;

import java.util.List;
import java.util.Map;

public class JweHotelRoomsBO {
    private Integer status;
    private String msg;
    private String date;
    private List<Map<String,String>> room_data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Map<String, String>> getRoom_data() {
        return room_data;
    }

    public void setRoom_data(List<Map<String, String>> room_data) {
        this.room_data = room_data;
    }
}
