package com.kakaopay.coding.investingapi;

import com.kakaopay.coding.investingapi.dto.UserProductDto;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class InvestingService_GetUserProductListTests {

    @InjectMocks
    InvestingService investingService;

    @Mock
    InvestingDBMapper investingDBMapper;


    @Test
    @DisplayName("TestGetUserProductList_NoUserProduct_ShouldEmpty")
    void testGetUserProductListNoUserProduct() {
        // given
        long userId = 1234;
        Mockito.when(investingDBMapper.selectUserProductListAll(anyLong())).thenReturn(List.of());

        // when
        List<UserProductDto> userProductDtoList = investingService.getUserProductList(userId);

        // then
        Assertions.assertTrue(userProductDtoList.isEmpty());
    }

    @Test
    @DisplayName("TestGetUserProductList_NoProductMeta_ShouldInvestingException")
    void testGetUserProductListNoProductMeta() {
        // given
        long userId = 1234;

        List<UserProductEntity> userProductEntityList = new ArrayList<>();
        UserProductEntity userProductEntity1 = new UserProductEntity();
        userProductEntity1.user_id = userId;
        userProductEntity1.product_id = 1;
        userProductEntityList.add(userProductEntity1);
        UserProductEntity userProductEntity2 = new UserProductEntity();
        userProductEntity2.user_id = userId;
        userProductEntity2.product_id = 2;
        userProductEntityList.add(userProductEntity2);

        Mockito.when(investingDBMapper.selectUserProductListAll(anyLong())).thenReturn(userProductEntityList);
        Mockito.when(investingDBMapper.selectProductMetaListByIdList(any())).thenReturn(List.of());

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getUserProductList(userId));
        Assertions.assertTrue(ex.resultCode == ResultCode.BadServerData);
    }

    @Test
    @DisplayName("TestGetUserProductList_MissMatchUserProductWithMeta_ShouldInvestingException")
    void testGetUserProductListMissMatchUserProductWithMeta() {
        // given
        long userId = 1234;

        List<UserProductEntity> userProductEntityList = new ArrayList<>();
        UserProductEntity userProductEntity1 = new UserProductEntity();
        userProductEntity1.user_id = userId;
        userProductEntity1.product_id = 1;
        userProductEntityList.add(userProductEntity1);
        UserProductEntity userProductEntity2 = new UserProductEntity();
        userProductEntity2.user_id = userId;
        userProductEntity2.product_id = 2;
        userProductEntityList.add(userProductEntity2);

        List<ProductMetaEntity> productMetaEntityList = new ArrayList<>();
        ProductMetaEntity productMeta = new ProductMetaEntity();
        productMeta.product_id = 1;
        productMetaEntityList.add(productMeta);

        Mockito.when(investingDBMapper.selectUserProductListAll(anyLong())).thenReturn(userProductEntityList);
        Mockito.when(investingDBMapper.selectProductMetaListByIdList(any())).thenReturn(productMetaEntityList);

        // when

        // then
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getUserProductList(userId));
        Assertions.assertTrue(ex.resultCode == ResultCode.BadServerData);
    }
}
