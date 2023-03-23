package clowoodive.toy.investing.entity;

import java.time.LocalDateTime;

public class UserProductEntity {
    public long user_id;        // PK
    public int product_id;      // PK
    public long investing_amount;
    public LocalDateTime investing_at;
}
