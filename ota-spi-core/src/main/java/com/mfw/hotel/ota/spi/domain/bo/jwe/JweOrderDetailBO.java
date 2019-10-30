package com.mfw.hotel.ota.spi.domain.bo.jwe;

import com.mfw.hotel.ota.proxy.sdk.domain.Order;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class JweOrderDetailBO
{
    private Integer status;
    private String msg;
    private List<Map<String,String>> data;
}
