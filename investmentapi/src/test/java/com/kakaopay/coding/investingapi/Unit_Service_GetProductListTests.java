package com.kakaopay.coding.investingapi;

import com.kakaopay.coding.investingapi.dto.ProductDto;
import com.kakaopay.coding.investingapi.entity.ProductInvestingEntity;
import com.kakaopay.coding.investingapi.entity.ProductMetaEntity;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
public class Unit_Service_GetProductListTests {
    @InjectMocks
    private InvestingService investingService;

    @Mock
    private InvestingDBMapper investingDBMapper;


    @Test
    @DisplayName("Test_GetProductList_WithNoProductMeta_ShouldEmpty")
    void testGetProductListWithNoProductMeta() {
        // given
        Mockito.when(investingDBMapper.selectProductMetaListValid(any())).thenReturn(List.of());

        // when
        List<ProductDto> productDtoList = investingService.getProductList();

        // then
        Assertions.assertTrue(productDtoList.isEmpty());
    }

    @Test
    @DisplayName("Test_GetProductList_WithNoProductInvesting_ShouldInvestingException")
    void testGetProductListWithNoProductInvesting() {
        // given
        List<ProductMetaEntity> productMetaEntityList = new ArrayList<>();
        ProductMetaEntity productMeta1 = new ProductMetaEntity();
        productMeta1.product_id = 1;
        productMetaEntityList.add(productMeta1);

        ProductMetaEntity productMeta2 = new ProductMetaEntity();
        productMeta2.product_id = 2;
        productMetaEntityList.add(productMeta2);

        Mockito.when(investingDBMapper.selectProductMetaListValid(any())).thenReturn(productMetaEntityList);
        Mockito.when(investingDBMapper.selectProductInvestingListByIdList(anyList())).thenReturn(List.of());

        // when
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getProductList());

        // then
        Assertions.assertTrue(ex.resultCode == ResultCode.BadServerData);
    }

    @Test
    @DisplayName("Test_GetProductList_WithMissMatchMetaAndInvesting_ShouldInvestingException")
    void testGetProductListWithMissMatchData() {
        // given
        List<ProductMetaEntity> productMetaEntityList = new ArrayList<>();
        ProductMetaEntity productMeta1 = new ProductMetaEntity();
        productMeta1.product_id = 1;
        productMetaEntityList.add(productMeta1);

        ProductMetaEntity productMeta2 = new ProductMetaEntity();
        productMeta2.product_id = 2;
        productMetaEntityList.add(productMeta2);

        List<ProductInvestingEntity> productInvestingEntityList = new ArrayList<>();
        ProductInvestingEntity productInvesting = new ProductInvestingEntity();
        productInvesting.product_id = 1;
        productInvestingEntityList.add(productInvesting);

        Mockito.when(investingDBMapper.selectProductMetaListValid(any())).thenReturn(productMetaEntityList);
        Mockito.when(investingDBMapper.selectProductInvestingListByIdList(anyList())).thenReturn(productInvestingEntityList);

        // when
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getProductList());

        // then
        Assertions.assertTrue(ex.resultCode == ResultCode.BadServerData);
    }
}
