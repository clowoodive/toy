package clowoodive.toy.investing;

import clowoodive.toy.investing.controller.InvestingController;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.service.InvestingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestingController.class)
class Unit_Controller_InvestingProductTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestingService investingService;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_ShouldStatusOk")
    void testInvestingProduct() throws Exception {
        // given
        long userId = 1234;
        int productId = 1;
        int investingAmount = 500;

        Mockito.when(investingService.investProduct(anyLong(), anyInt(), anyLong())).thenReturn(productId);

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithNoUserId_ShouldStatusForbidden")
    void testInvestingProductWithNoUserId() throws Exception {
        // given
        int productId = 1;
        int investingAmount = 500;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.NotFoundUserId.getCode()));
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithEmptyUserId_ShouldStatusForbidden")
    void testInvestingProductWithEmptyUserId() throws Exception {
        // given
        String userId = "";
        int productId = 1;
        int investingAmount = 500;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.NotFoundUserId.getCode()));
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithInvalidUserId_ShouldStatusForbidden")
    void testInvestingProductWithInvalidUserId() throws Exception {
        // given
        String userId = "abc";
        int productId = 1;
        int investingAmount = 500;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.InternalServerError.getCode()));
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithZeroUserId_ShouldStatusForbidden")
    void testInvestingProductWithZeroUserId() throws Exception {
        // given
        long userId = 0;
        int productId = 1;
        int investingAmount = 500;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadUserId.getCode()));
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithInvalidProductId_ShouldStatusBadRequest")
    void testInvestingProductWithInvalidProductId() throws Exception {
        // given
        long userId = 1234;
        String productId = "abc";
        int investingAmount = 500;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithZeroProductId_ShouldStatusForbidden")
    void testInvestingProductWithZeroProductId() throws Exception {
        // given
        long userId = 1234;
        int productId = 0;
        int investingAmount = 500;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadProductId.getCode()));
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithInvalidAmount_ShouldStatusBadRequest")
    void testInvestingProductWithInvalidAmount() throws Exception {
        // given
        long userId = 1234;
        int productId = 1;
        String investingAmount = "ten";

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test_Post_InvestingProduct_WithZeroAmount_ShouldStatusForbidden")
    void testInvestingProductWithZeroAmount() throws Exception {
        // given
        long userId = 1234;
        int productId = 1;
        int investingAmount = 0;

        // when
        var resultActions = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadAmount.getCode()));
    }
}
