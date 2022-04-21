package com.kakaopay.coding.investapiserver.mapper;

import com.kakaopay.coding.investapiserver.entity.ProductInvestingEntity;
import com.kakaopay.coding.investapiserver.entity.ProductMetaEntity;
import com.kakaopay.coding.investapiserver.entity.UserProductEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface InvestingDBMapper {

    @Select("SELECT * FROM product_meta WHERE started_at <= #{now} AND finished_at >= #{now}")
    List<ProductMetaEntity> selectProductMetaListValid(@Param("now") LocalDateTime now);

    @Select("SELECT * FROM product_meta WHERE product_id = #{product_id}")
    ProductMetaEntity selectProductMeta(@Param("product_id") int productId);

    @Select("<script>"
            + "SELECT * FROM product_investing WHERE product_id IN "
            + "<foreach collection='product_id_list' item='id' index='index' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "</script>")
    List<ProductInvestingEntity> selectProductInvestingListByIdList(@Param("product_id_list") List<Integer> productIdList);

    @Select("SELECT * FROM product_investing WHERE product_id == #{product_id}")
    ProductInvestingEntity selectProductInvesting(@Param("product_id") int productId);

    @Select("SELECT * FROM user_product WHERE user_id = #{user_id} AND product_id = #{product_id}")
    UserProductEntity selectUserProduct(@Param("user_id") long userId, @Param("product_id") int productId);
}
