package com.kakaopay.coding.investapiserver.entity;

import java.time.LocalDateTime;

public class UserProductEntity {
    public long user_id;
    public int product_id;
    // 상품명, 총 모집금액
    public long investing_amount;
    public LocalDateTime created_at;
}
