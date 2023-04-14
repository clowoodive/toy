package clowoodive.toy.investing.entity;

import clowoodive.toy.investing.dto.ProductDto;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class ProductMetaEntity {
    public int product_id;      // PK
    public String title;
    public long total_investing_amount;
    public long accumulated_investing_amount;
    public int investing_user_count;
    public LocalDateTime started_at;
    public LocalDateTime finished_at;

    public ProductMetaEntity(ProductDto productDto) {
        this.product_id = productDto.getProductId();
        this.title = productDto.getTitle();
        this.total_investing_amount = productDto.getTotalInvestingAmount();
        this.accumulated_investing_amount = productDto.getAccumInvestingAmount();
        this.investing_user_count = productDto.getInvestingUserCount();
        this.started_at = productDto.getOpenAt();
        this.finished_at = productDto.getCloseAt();
    }
}
