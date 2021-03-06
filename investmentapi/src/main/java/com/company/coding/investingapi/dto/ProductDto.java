package com.company.coding.investingapi.dto;

import com.company.coding.investingapi.entity.ProductInvestingEntity;
import com.company.coding.investingapi.entity.ProductMetaEntity;

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

    public ProductDto() {
    }

    public ProductDto(ProductMetaEntity productMeta, ProductInvestingEntity productInvesting, boolean isInvestingClosed) {
        this.product_id = productMeta.product_id;
        this.title = productMeta.title;
        this.total_investing_amount = productMeta.total_investing_amount;
        this.is_investing_closed = false;

        if (productInvesting != null) {
            this.accumulated_investing_amount = productInvesting.accumulated_investing_amount;
            this.investing_user_count = productInvesting.investing_user_count;
            this.is_investing_closed = isInvestingClosed;
        }

        this.started_at = productMeta.started_at;
        this.finished_at = productMeta.finished_at;
    }
}
