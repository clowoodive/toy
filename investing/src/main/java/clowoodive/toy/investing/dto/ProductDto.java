package clowoodive.toy.investing.dto;

import clowoodive.toy.investing.entity.ProductMetaEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    public int productId;
    public String title;
    public long totalInvestingAmount;
    public long accumInvestingAmount;
    public int investingUserCount;
    public boolean isInvestingAmountFull;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime openAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime closeAt;

    public ProductDto(ProductMetaEntity productMeta, boolean isInvestingAmountFull) {
        this.productId = productMeta.product_id;
        this.title = productMeta.title;
        this.totalInvestingAmount = productMeta.total_investing_amount;
        this.accumInvestingAmount = productMeta.accumulated_investing_amount;
        this.investingUserCount = productMeta.investing_user_count;
        this.isInvestingAmountFull = isInvestingAmountFull;
        this.openAt = productMeta.started_at;
        this.closeAt = productMeta.finished_at;
    }
}
