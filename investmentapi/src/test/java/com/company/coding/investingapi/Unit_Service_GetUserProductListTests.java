package com.company.coding.investingapi;

import com.company.coding.investingapi.entity.UserProductEntity;
import com.company.coding.investingapi.dto.UserProductDto;
import com.company.coding.investingapi.entity.ProductMetaEntity;
import com.company.coding.investingapi.error.InvestingException;
import com.company.coding.investingapi.error.ResultCode;
import com.company.coding.investingapi.mapper.InvestingDBMapper;
import com.company.coding.investingapi.service.InvestingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class Unit_Service_GetUserProductListTests {
    @InjectMocks
    private InvestingService investingService;

    @Mock
    private InvestingDBMapper investingDBMapper;


    @Test
    @DisplayName("Test_GetUserProductList_WithNoUserProduct_ShouldEmpty")
    void testGetUserProductListWithNoUserProduct() {
        // given
        long userId = 1234;
        Mockito.when(investingDBMapper.selectUserProductListAll(anyLong())).thenReturn(List.of());

        // when
        List<UserProductDto> userProductDtoList = investingService.getUserProductList(userId);

        // then
        Assertions.assertTrue(userProductDtoList.isEmpty());
    }

    @Test
    @DisplayName("Test_GetUserProductList_WithNoProductMeta_ShouldInvestingException")
    void testGetUserProductListWithNoProductMeta() {
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
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getUserProductList(userId));

        // then
        Assertions.assertTrue(ex.resultCode == ResultCode.BadServerData);
    }

    @Test
    @DisplayName("Test_GetUserProductList_WithMissMatchUserProductAndMeta_ShouldInvestingException")
    void testGetUserProductListWithMissMatchUserProductAndMeta() {
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
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getUserProductList(userId));

        // then
        Assertions.assertTrue(ex.resultCode == ResultCode.BadServerData);
    }
}
