package clowoodive.toy.investing.mapper;

import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.product.ProductEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface ProductDBMapper {
    // ProductMetaEntity

    @Insert("INSERT INTO product_entity (product_id, title, total_investing_amount, accumulated_investing_amount, investing_user_count, open_at, close_at) " +
            "VALUES (#{product_id}, #{title}, #{total_investing_amount}, #{accumulated_investing_amount}, #{investing_user_count}, #{open_at}, #{close_at})" +
            "ON DUPLICATE KEY UPDATE title = #{title}, total_investing_amount = #{total_investing_amount}, accumulated_investing_amount = " +
            "#{accumulated_investing_amount}, investing_user_count = #{investing_user_count}, open_at = #{open_at}, close_at = #{close_at}")
    int insertOrUpdateProduct(ProductEntity productMetaEntity);

    @Select("SELECT * FROM product_entity")
    List<ProductEntity> selectProductAll();

//    @Select("SELECT * FROM product_entity WHERE open_at <= #{now} AND close_at >= #{now}")
//    List<ProductEntity> selectProductMetaListValid(@Param("now") LocalDateTime now);

    @Select("SELECT * FROM product_entity WHERE product_id = #{product_id}")
    ProductEntity selectProductById(@Param("product_id") int productId);

    @Select("<script>"
            + "SELECT * FROM product_entity WHERE product_id IN "
            + "<foreach collection='product_id_list' item='id' index='index' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "</script>")
    List<ProductEntity> selectProductMetaListByIdList(@Param("product_id_list") List<Integer> productIdList);


    // ProductInvestingEntity

//    @Insert("INSERT INTO product_entity (product_id, accumulated_investing_amount, investing_user_count) " +
//            "VALUES (#{product_id}, #{accumulated_investing_amount}, #{investing_user_count})" +
//            "ON DUPLICATE KEY UPDATE accumulated_investing_amount = #{accumulated_investing_amount}, investing_user_count = #{investing_user_count}")
//    int insertOrUpdateProductInvesting(ProductInvestingEntity productInvestingEntity);

//    @Select("<script>"
//            + "SELECT * FROM product WHERE product_id IN "
//            + "<foreach collection='product_id_list' item='id' open='(' separator=',' close=')'>"
//            + "#{id}"
//            + "</foreach>"
//            + "</script>")
//    List<ProductInvestingEntity> selectProductInvestingListByIdList(@Param("product_id_list") List<Integer> productIdList);

    @Select("SELECT * FROM product_entity WHERE product_id = #{product_id}")
    ProductInvestingEntity selectProductInvesting(@Param("product_id") int productId);

    @Select("SELECT MAX(product_id) + 1 FROM product_entity")
    int selectNextProductId();

    @Update("UPDATE product_entity " +
            "SET accumulated_investing_amount = accumulated_investing_amount + #{add_investing_amount}, investing_user_count = investing_user_count + 1 " +
            "WHERE product_id = #{product_id} AND (accumulated_investing_amount + #{add_investing_amount}) <= #{total_investing_amount}")
    int updateProductInvesting(@Param("product_id") int productId, @Param("add_investing_amount") long addInvestingAmount,
                               @Param("total_investing_amount") long totalInvestingAmount);

    @Delete("DELETE FROM product_entity WHERE product_id = #{product_id}")
    int deleteProductById(@Param("product_id") int productId);


//    // UserProductEntity
//
//    @Insert("INSERT INTO user_investing (user_id, product_id, investing_amount, investing_at) " +
//            "VALUES (#{user_id}, #{product_id}, #{investing_amount}, #{investing_at})")
//    int insertUserProduct(UserProductEntity userProductEntity);
//
//    @Select("SELECT * FROM user_investing WHERE user_id = #{user_id} AND product_id = #{product_id}")
//    UserProductEntity selectUserProduct(@Param("user_id") long userId, @Param("product_id") int productId);
//
//    @Select("SELECT * FROM user_investing WHERE user_id = #{user_id}")
//    List<UserProductEntity> selectUserProductListAll(@Param("user_id") long userId);


    // MetaData
//    @Delete("TRUNCATE product_meta")
//    void truncateProductMeta();
//
//    @Delete("TRUNCATE product_entity")
//    void truncateProductInvesting();

//    @Delete("TRUNCATE user_investing")
//    void truncateUserProduct();

}
