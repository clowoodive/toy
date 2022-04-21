package com.kakaopay.coding.investapiserver.entity;

import java.time.LocalDateTime;

public class ProductMetaEntity {
    public int product_id;
    public String title;
    public long total_investing_amount;
    public LocalDateTime started_at;
    public LocalDateTime finished_at;
}
