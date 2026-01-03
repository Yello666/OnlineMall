package com.emily.mall.common.Enum;

import lombok.Getter;

public enum OrderStatusEnum {
    WAIT_PAY(0, "待支付"),
    WAIT_DELIVER(1, "待发货"),
    WAIT_RECEIVE(2, "待收货"),
    ORDER_SUCCESS(3, "订单完成"),
    ORDER_CANCEL(4, "订单取消");

    @Getter
    private final int code;
    @Getter
    private final String desc;

    OrderStatusEnum(int code, String desc) {
        this.code=code;
        this.desc=desc;
    }
}
