package clowoodive.toy.investing.dto;

import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.product.ProductEntity;

import java.time.LocalDateTime;

public class UserProductDto {
    public int product_id;
    public String title;
    public long total_investing_amount;
    public long investing_amount;
    public LocalDateTime investing_at;

    public UserProductDto(){}

    public UserProductDto(UserProductEntity userProductEntity, ProductEntity productEntity) {
        this.product_id = userProductEntity.product_id;

        if (productEntity != null) {
            this.title = productEntity.getTitle();
            this.total_investing_amount = productEntity.getTotal_investing_amount();
        }

        this.investing_amount = userProductEntity.investing_amount;
        this.investing_at = userProductEntity.investing_at;
    }
}
