package com.kakaopay.coding.investingapi;

import com.kakaopay.coding.investingapi.entity.ProductInvestingEntity;
import com.kakaopay.coding.investingapi.entity.ProductMetaEntity;
import com.kakaopay.coding.investingapi.entity.UserProductEntity;
import com.kakaopay.coding.investingapi.error.InvestingException;
import com.kakaopay.coding.investingapi.error.ResultCode;
import com.kakaopay.coding.investingapi.mapper.InvestingDBMapper;
import com.kakaopay.coding.investingapi.service.InvestingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class InvestingService_InvestingProductTests {

    @InjectMocks
    InvestingService investingService;

    @Mock
    InvestingDBMapper investingDBMapper;


    @Test
    @DisplayName("TestInvestProduct_NoProductData_ShouldInvestingException")
    void testInvestProductNoProductMeta() {
        // given
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(null);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.BadProductId);
    }

    @Test
    @DisplayName("TestInvestProduct_NotOpenedProduct_ShouldInvestingException")
    void testInvestProductNotOpenedProduct() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.plusDays(1);
        productMetaEntity.finished_at = now.plusDays(5);

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.BadPeriod);
    }

    @Test
    @DisplayName("TestInvestProduct_ExpiredProduct_ShouldInvestingException")
    void testInvestProductExpiredProduct() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.minusDays(1);

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.BadPeriod);
    }

    @Test
    @DisplayName("TestInvestProduct_AlreadyInvesting_ShouldInvestingException")
    void testInvestProductAlreadyInvesting() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.plusDays(5);

        UserProductEntity userProductEntity = new UserProductEntity();
        userProductEntity.user_id =  userId;
        userProductEntity.product_id = productId;
        userProductEntity.investing_amount = investingAmount;
        userProductEntity.investing_at = now.minusDays(1);

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);
        Mockito.when(investingDBMapper.selectUserProduct(anyLong(), anyInt())).thenReturn(userProductEntity);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.DuplicatedInvesting);
    }

    @Test
    @DisplayName("TestInvestProduct_NoProductInvesting_ShouldInvestingException")
    void testInvestProductNoProductInvesting() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.plusDays(5);

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);
        Mockito.when(investingDBMapper.selectUserProduct(anyLong(), anyInt())).thenReturn(null);
        Mockito.when(investingDBMapper.selectProductInvesting(anyInt())).thenReturn(null);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.BadInvestingData);
    }

    @Test
    @DisplayName("TestInvestProduct_CompleteTotalAmount_ShouldInvestingException")
    void testInvestProductCompleteTotalAmount() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.plusDays(5);

        ProductInvestingEntity productInvestingEntity = new ProductInvestingEntity();
        productInvestingEntity.product_id = productId;
        productInvestingEntity.accumulated_investing_amount = 5000;
        productInvestingEntity.investing_user_count = 3;

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);
        Mockito.when(investingDBMapper.selectUserProduct(anyLong(), anyInt())).thenReturn(null);
        Mockito.when(investingDBMapper.selectProductInvesting(anyInt())).thenReturn(productInvestingEntity);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.ClosedProduct);
    }

    @Test
    @DisplayName("TestInvestProduct_ExceededUpdate_ShouldInvestingException")
    void testInvestProductExceededUpdate() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.plusDays(5);

        ProductInvestingEntity productInvestingEntity = new ProductInvestingEntity();
        productInvestingEntity.product_id = productId;
        productInvestingEntity.accumulated_investing_amount = 4600;
        productInvestingEntity.investing_user_count = 3;

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);
        Mockito.when(investingDBMapper.selectUserProduct(anyLong(), anyInt())).thenReturn(null);
        Mockito.when(investingDBMapper.selectProductInvesting(anyInt())).thenReturn(productInvestingEntity);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.ExceededAmount);
    }

    @Test
    @DisplayName("TestInvestProduct_FailInsertUserProduct_ShouldInvestingException")
    void testInvestProductFailInsertUserProduct() {
        // given
        var now = LocalDateTime.now();
        long userId = 1234;
        int productId = 1;
        long investingAmount = 500;

        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = "test product";
        productMetaEntity.total_investing_amount = 5000;
        productMetaEntity.started_at = now.minusDays(5);
        productMetaEntity.finished_at = now.plusDays(5);

        ProductInvestingEntity productInvestingEntity = new ProductInvestingEntity();
        productInvestingEntity.product_id = productId;
        productInvestingEntity.accumulated_investing_amount = 4500;
        productInvestingEntity.investing_user_count = 3;

        Mockito.when(investingDBMapper.selectProductMeta(anyInt())).thenReturn(productMetaEntity);
        Mockito.when(investingDBMapper.selectUserProduct(anyLong(), anyInt())).thenReturn(null);
        Mockito.when(investingDBMapper.selectProductInvesting(anyInt())).thenReturn(productInvestingEntity);
        Mockito.when(investingDBMapper.updateProductInvesting(anyInt(), anyLong(), anyLong())).thenReturn(1);
        Mockito.when(investingDBMapper.insertUserProduct(any())).thenReturn(0);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.investProduct(userId, productId, investingAmount));
        Assertions.assertTrue(ex.resultCode == ResultCode.InternalServerError);
    }
}
