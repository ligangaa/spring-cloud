package com.mfw.hotel.ota.spi.enums.jwe;

public enum JweOrderStateEnum {
    NEW(0, "新增"),
    UPDATE(1, "变更"),
    CANCEL(2, "取消");

    final private Integer code;
    final private String description;

    JweOrderStateEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
