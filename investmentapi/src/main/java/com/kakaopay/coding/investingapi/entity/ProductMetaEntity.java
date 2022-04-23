package com.kakaopay.coding.investingapi.entity;

import java.time.LocalDateTime;

public class ProductMetaEntity {
    public int product_id;      // PK
    public String title;
    public long total_investing_amount;
    public LocalDateTime started_at;
    public LocalDateTime finished_at;
}
