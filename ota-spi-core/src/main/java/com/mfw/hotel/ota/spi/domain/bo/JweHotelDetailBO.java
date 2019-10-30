package com.mfw.hotel.ota.spi.domain.bo;

import java.math.BigDecimal;

public class JweHotelDetailBO extends JweHotelsBO{
    private String h_name;
    private String tejimabi;
    private BigDecimal lat;
    private BigDecimal lng;
    private String address;
    private String tal;
    private String cancel_day;

    public String getH_name() {
        return h_name;
    }

    public void setH_name(String h_name) {
        this.h_name = h_name;
    }

    public String getTejimabi() {
        return tejimabi;
    }

    public void setTejimabi(String tejimabi) {
        this.tejimabi = tejimabi;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTal() {
        return tal;
    }

    public void setTal(String tal) {
        this.tal = tal;
    }

    public String getCancel_day() {
        return cancel_day;
    }

    public void setCancel_day(String cancel_day) {
        this.cancel_day = cancel_day;
    }
}
