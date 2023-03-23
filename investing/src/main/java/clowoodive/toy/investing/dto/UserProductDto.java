package clowoodive.toy.investing.dto;

import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.entity.ProductMetaEntity;

import java.time.LocalDateTime;

public class UserProductDto {
    public int product_id;
    public String title;
    public long total_investing_amount;
    public long investing_amount;
    public LocalDateTime investing_at;

    public UserProductDto(){}

    public UserProductDto(UserProductEntity userProductEntity, ProductMetaEntity productMeta) {
        this.product_id = userProductEntity.product_id;

        if (productMeta != null) {
            this.title = productMeta.title;
            this.total_investing_amount = productMeta.total_investing_amount;
        }

        this.investing_amount = userProductEntity.investing_amount;
        this.investing_at = userProductEntity.investing_at;
    }
}
