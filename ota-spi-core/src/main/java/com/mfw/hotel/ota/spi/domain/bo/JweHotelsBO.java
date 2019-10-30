package com.mfw.hotel.ota.spi.domain.bo;

import lombok.Builder;

import java.util.List;
import java.util.Map;

public class JweHotelsBO {


    private Integer status;
    private String msg;
    private String h_no;
    private String h_code;
    private List<Map<String,String>> data;
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMesg(String msg) {
        this.msg = msg;
    }
    public String getH_no() {
        return h_no;
    }

    public void setH_no(String h_no) {
        this.h_no = h_no;
    }

    public String getH_code() {
        return h_code;
    }

    public void setH_code(String h_code) {
        this.h_code = h_code;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String,String>> data) {
        this.data = data;
    }
}
