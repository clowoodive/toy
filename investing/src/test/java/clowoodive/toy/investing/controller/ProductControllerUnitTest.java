package clowoodive.toy.investing.controller;

import clowoodive.toy.investing.BaseProductUnitTest;
import clowoodive.toy.investing.product.ProductController;
import clowoodive.toy.investing.product.ProductDto;
import clowoodive.toy.investing.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerUnitTest extends BaseProductUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageSource messageSource;

    @MockBean
    ProductService productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("투자 상품 정보")
    void testGetProducts() throws Exception {
        // given
        ProductDto productDto1 = new ProductDto();
        productDto1.setProductId(123);
        productDto1.setTitle("first product");
        ProductDto productDto2 = new ProductDto();
        productDto2.setProductId(456);
        productDto2.setTitle("second product");

        given(productService.getProducts()).willReturn(Arrays.asList(this.productDto1, this.productDto2));

        // when
        var resultActions = mockMvc.perform(
                get("/products")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("products/productList"))
                .andExpect(model().attribute("productDtos", hasSize(2)))
                .andExpect(model().attribute("productDtos", hasItem(
                        allOf(
                                hasProperty("productId", is(this.productDto1.getProductId())),
                                hasProperty("title", is(this.productDto1.getTitle()))
                        )
                )))
                .andExpect(model().attribute("productDtos", hasItem(
                        allOf(
                                hasProperty("productId", is(this.productDto2.getProductId())),
                                hasProperty("title", is(this.productDto2.getTitle()))
                        )
                )));
        then(productService).should(times(1)).getProducts();
    }

    @Test
    @DisplayName("투자 상품 정보_데이터 없음")
    void testGetProducts_empty() throws Exception {
        // given
        String noResultMsg = messageSource.getMessage("common.label.noResult", null, null);
        given(productService.getProducts()).willReturn(new ArrayList<>()); // null 리턴해도 동일함

        // when
        var resultActions = mockMvc.perform(
                get("/products")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("productDtos", hasSize(0)))
                .andExpect(content().string(containsString(noResultMsg)));
        then(productService).should(times(1)).getProducts();
    }

    @Test
    @DisplayName("투자 상품 상세 정보")
    void testGetProductDetail() throws Exception {
        // given
        given(productService.getProductById(anyInt())).willReturn(this.productDto1);

        // when
        var resultActions = mockMvc.perform(
                get("/products/" + this.productDto1.getProductId())
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("products/productDetail"))
                .andExpect(model().attribute("productDto", notNullValue()))
                .andExpect(model().attribute("productDto", allOf(
                                hasProperty("productId", is(this.productDto1.getProductId())),
                                hasProperty("title", is(this.productDto1.getTitle()))
                        )
                ));
        then(productService).should(times(1)).getProductById(anyInt());
    }

    @Test
    @DisplayName("투자 상품 등록 폼 요청")
    void testShowCreateProductForm() throws Exception {
        // given

        // when
        var resultActions = mockMvc.perform(
                get("/products/create")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.VIEW_PRODUCT_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attribute("productDto", notNullValue()));
    }

    @Test
    @DisplayName("투자 상품 등록")
    void testCreateProductForm() throws Exception {
        // given
        this.productDto1.setProductId(0); // 무시됨
        MultiValueMap<String, String> mvMap = getFieldMapByReflection(this.productDto1);

        // when
        var resultActions = mockMvc.perform(
                post("/products/create")
                        .queryParams(mvMap)
        );

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
        then(productService).should(times(1)).saveProduct(any());
    }

    private MultiValueMap<String, String> getFieldMapByReflection(Object obj) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        MultiValueMap<String, String> mvMap = new LinkedMultiValueMap<>();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            mvMap.add(field.getName(), field.get(this.productDto1) == null ? null : field.get(this.productDto1).toString());
        }

        return mvMap;
    }

    @Test
    @DisplayName("투자 상품 등록_잘못된 입력")
    void testCreateProductForm_invalid() throws Exception {
        // given
        this.productDto1.setProductId(0); // 무시됨
        this.productDto1.setTitle("0123456789012345678901234567890123456789012345"); // 46자
        this.productDto1.setTotalInvestingAmount(-1);
        this.productDto1.setAccumInvestingAmount(-2);
        this.productDto1.setInvestingUserCount(-3);
        this.productDto1.setOpenAt(null);
        this.productDto1.setCloseAt(null);

        MultiValueMap<String, String> mvMap = getFieldMapByReflection(this.productDto1);

        // when
        var resultActions = mockMvc.perform(
                post("/products/create")
                        .queryParams(mvMap)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.VIEW_PRODUCT_CREATE_OR_UPDATE_FORM))
                .andExpect(model().errorCount(6))
                .andExpectAll(
                        model().attributeHasFieldErrorCode("productDto", "title", "Size"),
                        model().attributeHasFieldErrorCode("productDto", "totalInvestingAmount", "PositiveOrZero"),
                        model().attributeHasFieldErrorCode("productDto", "accumInvestingAmount", "PositiveOrZero"),
                        model().attributeHasFieldErrorCode("productDto", "investingUserCount", "PositiveOrZero"),
                        model().attributeHasFieldErrorCode("productDto", "openAt", "NotNull"),
                        model().attributeHasFieldErrorCode("productDto", "closeAt", "NotNull")
                );
    }

    @Test
    @DisplayName("투자 상품 수정 폼 요청")
    void testShowUpdateProductForm() throws Exception {
        // given
        given(productService.getProductById(productDto1.getProductId())).willReturn(productDto1);

        // when
        var resultActions = mockMvc.perform(
                get("/products/" + productDto1.getProductId() + "/edit")
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.VIEW_PRODUCT_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attribute("productDto", notNullValue()))
                .andExpect(model().attribute("productDto", allOf(
                                hasProperty("productId", is(productDto1.getProductId())),
                                hasProperty("title", is(productDto1.getTitle())),
                                hasProperty("totalInvestingAmount", is(productDto1.getTotalInvestingAmount())),
                                hasProperty("accumInvestingAmount", is(productDto1.getAccumInvestingAmount())),
                                hasProperty("investingUserCount", is(productDto1.getInvestingUserCount())),
                                hasProperty("openAt", is(productDto1.getOpenAt())),
                                hasProperty("closeAt", is(productDto1.getCloseAt()))
                        )
                ));
    }

    @Test
    @DisplayName("투자 상품 수정")
    void testUpdateProductForm() throws Exception {
        // given
        // this.productDto1.setProductId(0); // 무시됨
        this.productDto1.setTitle("update");
        this.productDto1.setTotalInvestingAmount(1);
        this.productDto1.setAccumInvestingAmount(2);
        this.productDto1.setInvestingUserCount(3);
        this.productDto1.setOpenAt(this.productDto1.getOpenAt().minusDays(1));
        this.productDto1.setCloseAt(this.productDto1.getCloseAt().plusDays(1));

        MultiValueMap<String, String> mvMap = getFieldMapByReflection(this.productDto1);

        given(productService.getProductById(productDto1.getProductId())).willReturn(productDto1);

        // when
        var resultActions = mockMvc.perform(
                post("/products/" + productDto1.getProductId() + "/edit")
                        .queryParams(mvMap)
        );

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
        then(productService).should(times(1)).getProductById(anyInt());
        then(productService).should(times(1)).saveProduct(any());
    }

    @Test
    @DisplayName("투자 상품 수정_잘못된 입력")
    void testUpdateProductForm_invalid() throws Exception {
        // given
        given(productService.getProductById(productDto1.getProductId())).willReturn(productDto1);

        // this.productDto1.setProductId(0); // 무시됨
        this.productDto1.setTitle("0123456789012345678901234567890123456789012345"); // 46자
        this.productDto1.setTotalInvestingAmount(-1);
        this.productDto1.setAccumInvestingAmount(-2);
        this.productDto1.setInvestingUserCount(-3);
        this.productDto1.setOpenAt(null);
        this.productDto1.setCloseAt(null);

        MultiValueMap<String, String> mvMap = getFieldMapByReflection(this.productDto1);

        // when
        var resultActions = mockMvc.perform(
                post("/products/" + productDto1.getProductId() + "/edit")
                        .queryParams(mvMap)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.VIEW_PRODUCT_CREATE_OR_UPDATE_FORM))
                .andExpect(model().errorCount(6))
                .andExpectAll(
                        model().attributeHasFieldErrorCode("productDto", "title", "Size"),
                        model().attributeHasFieldErrorCode("productDto", "totalInvestingAmount", "PositiveOrZero"),
                        model().attributeHasFieldErrorCode("productDto", "accumInvestingAmount", "PositiveOrZero"),
                        model().attributeHasFieldErrorCode("productDto", "investingUserCount", "PositiveOrZero"),
                        model().attributeHasFieldErrorCode("productDto", "openAt", "NotNull"),
                        model().attributeHasFieldErrorCode("productDto", "closeAt", "NotNull")
                );
    }

    @Test
    @DisplayName("투자 상품 삭제")
    void testDeleteProduct() throws Exception {
        // given
        given(productService.deleteProduct(any())).willReturn(1);

        // when
        var resultActions = mockMvc.perform(
                post("/products/" + productDto1.getProductId() + "/delete")
        );

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
        then(productService).should(times(1)).getProductById(anyInt());
        then(productService).should(times(1)).deleteProduct(any());
    }
}