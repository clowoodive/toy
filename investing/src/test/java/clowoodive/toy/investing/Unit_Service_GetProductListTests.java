package clowoodive.toy.investing;

import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.product.ProductService;
import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.entity.ProductMetaEntity;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.mapper.InvestingDBMapper;
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
    private ProductService investingService;

    @Mock
    private InvestingDBMapper investingDBMapper;


    @Test
    @DisplayName("Test_GetProductList_WithNoProductMeta_ShouldEmpty")
    void testGetProductListWithNoProductMeta() {
        // given
        Mockito.when(investingDBMapper.selectProductMetaListValid(any())).thenReturn(List.of());

        // when
        List<ProductDto> productDtoList = investingService.getProducts();

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
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getProducts());

        // then
        Assertions.assertTrue(ex.getCode() == ResultCode.BadServerData);
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
        InvestingException ex = Assertions.assertThrows(InvestingException.class, () -> investingService.getProducts());

        // then
        Assertions.assertTrue(ex.getCode() == ResultCode.BadServerData);
    }
}
