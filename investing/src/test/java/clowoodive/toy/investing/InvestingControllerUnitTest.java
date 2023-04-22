//package clowoodive.toy.investing.controller;
//
//import clowoodive.toy.investing.product.ProductDto;
//import clowoodive.toy.investing.error.ResultCode;
//import clowoodive.toy.investing.product.ProductController;
//import clowoodive.toy.investing.product.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.MessageSource;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProductController.class)
//class InvestingControllerUnitTest {
//
//    @Autowired
//    MessageSource messageSource;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    ProductService productService;
//
//    private long userId;
//
//    private int productId;
//
//    private int investingAmount;
//
//    @BeforeEach
//    void setUp() {
//        this.userId = 12340001;
//        this.productId = 1;
//        this.investingAmount = 500;
//    }
//
//    @Test
//    @DisplayName("투자 상품 정보")
//    void testGetProducts() throws Exception {
//        // given
//        ProductDto productDto1 = new ProductDto();
//        productDto1.setProductId(123);
//        productDto1.setTitle("first product");
//        ProductDto productDto2 = new ProductDto();
//        productDto2.setProductId(456);
//        productDto2.setTitle("second product");
//
//        given(productService.getProducts()).willReturn(Arrays.asList(productDto1, productDto2));
//
//        // when
//        var resultActions = mockMvc.perform(
//                get("/products")
//        );
//
//        // then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(view().name("products/productList"))
//                .andExpect(model().attribute("productList", hasSize(2)))
//                .andExpect(model().attribute("productList", hasItem(
//                        allOf(
//                                hasProperty("productId", is(123)),
//                                hasProperty("title", is("first product"))
//                        )
//                )))
//                .andExpect(model().attribute("productList", hasItem(
//                        allOf(
//                                hasProperty("productId", is(456)),
//                                hasProperty("title", is("second product"))
//                        )
//                )));
//        then(productService).should(times(1)).getProducts();
//    }
//
//    @Test
//    @DisplayName("투자 상품 정보_데이터 없음")
//    void testGetProducts_empty() throws Exception {
//        // given
//        String noResultMsg = messageSource.getMessage("common.label.noResult", null, null);
//        given(productService.getProducts()).willReturn(new ArrayList<>()); // null 리턴해도 동일함
//
//        // when
//        var resultActions = mockMvc.perform(
//                get("/products")
//        );
//
//        // then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("productDtos", hasSize(0)))
//                .andExpect(content().string(containsString(noResultMsg)));
//        then(productService).should(times(1)).getProducts();
//    }
//
//
//    @Test
//    @DisplayName("상품 투자")
//    void testInvestProduct() throws Exception {
//        // given
//        given(productService.investProduct(anyLong(), anyInt(), anyLong())).willReturn(this.productId);
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isOk());
//        then(productService).should(times(1)).investProduct(anyLong(), anyInt(), anyLong());
//    }
//
//    @Test
//    @DisplayName("상품 투자_userId 누락")
//    void testInvestProduct_userId_miss() throws Exception {
//        // given
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
////                        .header(InvestingController.HEADER_USER_ID_KEY, userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.BadReqParamUserId.getCode()));
//    }
//
//    @Test
//    @DisplayName("상품 투자_빈 userId")
//    void testInvestProduct_userId_empty() throws Exception {
//        // given
//        String emptyUserId = "";
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, emptyUserId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.BadReqParamUserId.getCode()));
//    }
//
//    @Test
//    @DisplayName("상품 투자_숫자가 아닌 userId")
//    void testInvestProduct_userId_notNumber() throws Exception {
//        // given
//        String strUserId = "notNumber";
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, strUserId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.InternalServerError.getCode()));
//    }
//
//    @Test
//    @DisplayName("상품 투자_유효하지 않은 userId")
//    void testInvestProduct_userId_invalid() throws Exception {
//        // given
//        long invalidUserId = -1;
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, invalidUserId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidUserId.getCode()));
//    }
//
//    @Test
//    @DisplayName("상품 투자_숫자가 아닌 productId")
//    void testInvestProduct_productId_notNumber() throws Exception {
//        // given
//        String strProductId = "notNumber";
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", strProductId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("상품 투자_유효하지 않은 productId")
//    void testInvestProduct_productId_invalid() throws Exception {
//        // given
//        int invalidProductId = 0;
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", invalidProductId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidProductId.getCode()));
//    }
//
//    @Test
//    @DisplayName("상품 투자_숫자가 아닌 investingAmount")
//    void testInvestProduct_investingAmount_notNumber() throws Exception {
//        // given
//        String strInvestingAmount = "notNumber";
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, strInvestingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("상품 투자_유효하지 않은 investingAmount")
//    void testInvestProduct_investingAmount_invalid() throws Exception {
//        // given
//        int invalidInvestingAmount = 0;
//
//        // when
//        var resultActions = mockMvc.perform(
//                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, invalidInvestingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidAmount.getCode()));
//    }
//}