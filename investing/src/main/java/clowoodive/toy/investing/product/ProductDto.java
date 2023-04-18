package clowoodive.toy.investing.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

    private int productId;

    @Size(min = 1, max = 45)
    @NotEmpty
    private String title;

    @PositiveOrZero
    private long totalInvestingAmount;

    @PositiveOrZero
    private long accumInvestingAmount;

    @PositiveOrZero
    private int investingUserCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime openAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime closeAt;

    public ProductDto(ProductEntity productEntity) {
        this.productId = productEntity.getProduct_id();
        this.title = productEntity.getTitle();
        this.totalInvestingAmount = productEntity.getTotal_investing_amount();
        this.accumInvestingAmount = productEntity.getAccumulated_investing_amount();
        this.investingUserCount = productEntity.getInvesting_user_count();
        this.openAt = productEntity.getOpen_at();
        this.closeAt = productEntity.getClose_at();
    }
}
