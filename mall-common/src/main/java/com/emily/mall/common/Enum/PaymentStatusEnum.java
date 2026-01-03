package com.emily.mall.common.Enum;

import lombok.Getter;

public enum PaymentStatusEnum {
// 支付状态枚举
    WAIT_PAY(0, "待支付"),
    PAYING(1, "支付中"),
    PAY_SUCCESS(2, "支付成功"),
    PAY_FAIL(3, "支付失败");

    @Getter
    private final int code;
    @Getter
    private final String desc;

    PaymentStatusEnum(int code, String desc) {
        this.code=code;
        this.desc=desc;
    }


}
