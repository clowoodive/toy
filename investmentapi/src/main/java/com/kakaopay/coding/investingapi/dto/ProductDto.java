package com.kakaopay.coding.investingapi.dto;

import com.kakaopay.coding.investingapi.entity.ProductInvestingEntity;
import com.kakaopay.coding.investingapi.entity.ProductMetaEntity;

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

    public ProductDto(ProductMetaEntity productMeta, ProductInvestingEntity productInvesting) {
        this.product_id = productMeta.product_id;
        this.title = productMeta.title;
        this.total_investing_amount = productMeta.total_investing_amount;
        this.is_investing_closed = false;

        if (productInvesting != null) {
            this.accumulated_investing_amount = productInvesting.accumulated_investing_amount;
            this.investing_user_count = productInvesting.investing_user_count;
            if (productMeta.total_investing_amount <= productInvesting.accumulated_investing_amount)
                this.is_investing_closed = true;
        }

        this.started_at = productMeta.started_at;
        this.finished_at = productMeta.finished_at;
    }
}
