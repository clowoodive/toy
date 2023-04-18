package clowoodive.toy.investing.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ProductEntity {

    private int product_id;
    private String title;
    private long total_investing_amount;
    private long accumulated_investing_amount;
    private int investing_user_count;
    private LocalDateTime open_at;
    private LocalDateTime close_at;

    public ProductEntity(ProductDto productDto) {
        this.product_id = productDto.getProductId();
        this.title = productDto.getTitle();
        this.total_investing_amount = productDto.getTotalInvestingAmount();
        this.accumulated_investing_amount = productDto.getAccumInvestingAmount();
        this.investing_user_count = productDto.getInvestingUserCount();
        this.open_at = productDto.getOpenAt();
        this.close_at = productDto.getCloseAt();
    }
}
