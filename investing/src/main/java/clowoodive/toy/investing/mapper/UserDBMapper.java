package clowoodive.toy.investing.mapper;

import clowoodive.toy.investing.entity.UserProductEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDBMapper {

    @Insert("INSERT INTO user_investing (user_id, product_id, investing_amount, investing_at) " +
            "VALUES (#{user_id}, #{product_id}, #{investing_amount}, #{investing_at})")
    int insertUserProduct(UserProductEntity userProductEntity);

    @Select("SELECT * FROM user_investing WHERE user_id = #{user_id} AND product_id = #{product_id}")
    UserProductEntity selectUserProduct(@Param("user_id") long userId, @Param("product_id") int productId);

    @Select("SELECT * FROM user_investing WHERE user_id = #{user_id}")
    List<UserProductEntity> selectUserProductListAll(@Param("user_id") long userId);

    @Delete("TRUNCATE user_investing")
    void truncateUserProduct();
}
