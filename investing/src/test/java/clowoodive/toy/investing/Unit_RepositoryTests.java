package clowoodive.toy.investing;

import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.service.DataService;
import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.entity.ProductMetaEntity;
import clowoodive.toy.investing.mapper.InvestingDBMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class Unit_RepositoryTests {
    @Autowired
    InvestingDBMapper investingDBMapper;

    @Autowired
    DataService dataService;

    @BeforeEach()
    void initDB() {
        dataService.truncateDBAll();
    }

    @Test
    @DisplayName("Test_ProductMetaEntity_InsertSelect")
    void testProductMetaEntityInsertSelect() {
        // given
        var now = LocalDateTime.now().withNano(0);
        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = 1;
        productMetaEntity.title = "테스트 포트폴리오";
        productMetaEntity.total_investing_amount = 1000000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.plusDays(5);

        // when
        int isSuccess = investingDBMapper.insertOrUpdateProductMeta(productMetaEntity);
        var resultProductMetaEntity = investingDBMapper.selectProductMeta(productMetaEntity.product_id);

        // then
        Assertions.assertTrue(isSuccess == 1);
        Assertions.assertTrue(productMetaEntity.product_id == resultProductMetaEntity.product_id);
        Assertions.assertTrue(productMetaEntity.title.equals(resultProductMetaEntity.title));
        Assertions.assertTrue(productMetaEntity.total_investing_amount == resultProductMetaEntity.total_investing_amount);
        Assertions.assertTrue(productMetaEntity.started_at.isEqual(resultProductMetaEntity.started_at));
        Assertions.assertTrue(productMetaEntity.finished_at.isEqual(resultProductMetaEntity.finished_at));
    }

    @Test
    @DisplayName("Test_ProductInvestingEntity_InsertSelect")
    void testProductInvestingEntityInsertSelect() {
        // given
        ProductInvestingEntity productInvestingEntity = new ProductInvestingEntity();
        productInvestingEntity.product_id = 1;
        productInvestingEntity.accumulated_investing_amount = 200000;
        productInvestingEntity.investing_user_count = 5;

        // when
        int isSuccess = investingDBMapper.insertOrUpdateProductInvesting(productInvestingEntity);
        var resultProductInvestingEntity = investingDBMapper.selectProductInvesting(productInvestingEntity.product_id);

        // then
        Assertions.assertTrue(isSuccess == 1);
        Assertions.assertTrue(productInvestingEntity.product_id == resultProductInvestingEntity.product_id);
        Assertions.assertTrue(productInvestingEntity.accumulated_investing_amount == resultProductInvestingEntity.accumulated_investing_amount);
        Assertions.assertTrue(productInvestingEntity.investing_user_count == resultProductInvestingEntity.investing_user_count);
    }

    @Test
    @DisplayName("Test_UserProductEntity_InsertSelect")
    void testUserProductEntityInsertSelect() {
        // given
        var now = LocalDateTime.now().withNano(0);
        UserProductEntity userProductEntity = new UserProductEntity();
        userProductEntity.user_id = 1234;
        userProductEntity.product_id = 1;
        userProductEntity.investing_amount = 200000;
        userProductEntity.investing_at = now.minusDays(5);

        // when
        int isSuccess = investingDBMapper.insertUserProduct(userProductEntity);
        var resultUserProductEntity = investingDBMapper.selectUserProduct(userProductEntity.user_id, userProductEntity.product_id);

        // then
        Assertions.assertTrue(isSuccess == 1);
        Assertions.assertTrue(userProductEntity.user_id == resultUserProductEntity.user_id);
        Assertions.assertTrue(userProductEntity.product_id == resultUserProductEntity.product_id);
        Assertions.assertTrue(userProductEntity.investing_amount == resultUserProductEntity.investing_amount);
        Assertions.assertTrue(userProductEntity.investing_at.isEqual(userProductEntity.investing_at));
    }
}
