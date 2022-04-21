package com.kakaopay.coding.investapiserver.dto;

import java.time.LocalDateTime;

public class ProductDto {
    public int product_id;
    public String title;
    public long total_investing_amount;
    public long accumulated_investing_amount;
    public int investing_user_count;
    public boolean is_investing_closed;
    public LocalDateTime started_at;
    public LocalDateTime finished_at;
}
