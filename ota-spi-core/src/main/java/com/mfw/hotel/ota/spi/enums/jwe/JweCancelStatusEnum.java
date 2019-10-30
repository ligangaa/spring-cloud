package com.mfw.hotel.ota.spi.enums.jwe;

public enum JweCancelStatusEnum {
    FAIL(0, "失败"),
    SUCCESS(1, "成功");

    final private Integer code;
    final private String description;

    JweCancelStatusEnum(Integer code, String description) {
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
