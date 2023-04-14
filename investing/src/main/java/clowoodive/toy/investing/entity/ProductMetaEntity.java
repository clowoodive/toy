package clowoodive.toy.investing.entity;

import java.time.LocalDateTime;

public class ProductMetaEntity {
    public int product_id;      // PK
    public String title;
    public long total_investing_amount;
    public long accumulated_investing_amount;
    public int investing_user_count;
    public LocalDateTime started_at;
    public LocalDateTime finished_at;
}
