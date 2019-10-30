package com.mfw.hotel.ota.spi.enums;

public enum  RoomTypeEnum {
    SGL("1", "SGL"),
    DBL("2", "DBL"),
    TWN("3", "TWN"),
    TRP("4", "TRP"),
    Fourth("5", "Fourth"),
    JapaneseRoom("6", "Japanese Room");

    private String code;
    private String description;

    RoomTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
