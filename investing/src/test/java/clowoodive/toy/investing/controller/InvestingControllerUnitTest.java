package clowoodive.toy.investing.controller;

import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.service.InvestingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestingController.class)
class InvestingControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InvestingService investingService;

    private long userId;

    private int productId;

    private int investingAmount;

    @BeforeEach
    void setUp() {
        this.userId = 12340001;
        this.productId = 1;
        this.investingAmount = 500;
    }

    @Test
    @DisplayName("상품 투자 정보")
    void testGetProducts() throws Exception {
        // given
        ProductDto productDto1 = new ProductDto();
        productDto1.setProduct_id(123);
        ProductDto productDto2 = new ProductDto();
        productDto2.setProduct_id(456);

        given(investingService.getProducts()).willReturn(Arrays.asList(productDto1, productDto2));

        // when
        var resultActions = mockMvc.perform(
                get("/investing/products")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value("123"))
                .andExpect(jsonPath("$[1].product_id").value("456"));
        then(investingService).should(times(1)).getProducts();
    }

    @Test
    @DisplayName("상품 투자")
    void testInvestProduct() throws Exception {
        // given
        given(investingService.investProduct(anyLong(), anyInt(), anyLong())).willReturn(this.productId);

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
        then(investingService).should(times(1)).investProduct(anyLong(), anyInt(), anyLong());
    }

    @Test
    @DisplayName("상품 투자_userId 누락")
    void testInvestProduct_userId_miss() throws Exception {
        // given

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
//                        .header(InvestingController.HEADER_USER_ID_KEY, userId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadReqParamUserId.getCode()));
    }

    @Test
    @DisplayName("상품 투자_빈 userId")
    void testInvestProduct_userId_empty() throws Exception {
        // given
        String emptyUserId = "";

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, emptyUserId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadReqParamUserId.getCode()));
    }

    @Test
    @DisplayName("상품 투자_숫자가 아닌 userId")
    void testInvestProduct_userId_notNumber() throws Exception {
        // given
        String strUserId = "notNumber";

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, strUserId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.InternalServerError.getCode()));
    }

    @Test
    @DisplayName("상품 투자_유효하지 않은 userId")
    void testInvestProduct_userId_invalid() throws Exception {
        // given
        long invalidUserId = -1;

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, this.investingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, invalidUserId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidUserId.getCode()));
    }

    @Test
    @DisplayName("상품 투자_숫자가 아닌 productId")
    void testInvestProduct_productId_notNumber() throws Exception {
        // given
        String strProductId = "notNumber";

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", strProductId, this.investingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 투자_유효하지 않은 productId")
    void testInvestProduct_productId_invalid() throws Exception {
        // given
        int invalidProductId = 0;

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", invalidProductId, this.investingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidProductId.getCode()));
    }

    @Test
    @DisplayName("상품 투자_숫자가 아닌 investingAmount")
    void testInvestProduct_investingAmount_notNumber() throws Exception {
        // given
        String strInvestingAmount = "notNumber";

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, strInvestingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 투자_유효하지 않은 investingAmount")
    void testInvestProduct_investingAmount_invalid() throws Exception {
        // given
        int invalidInvestingAmount = 0;

        // when
        var resultActions = mockMvc.perform(
                post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", this.productId, invalidInvestingAmount)
                        .header(InvestingController.HEADER_USER_ID_KEY, this.userId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidAmount.getCode()));
    }
}