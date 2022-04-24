package com.kakaopay.coding.investingapi;

import com.kakaopay.coding.investingapi.controller.InvestingController;
import com.kakaopay.coding.investingapi.dto.UserProductDto;
import com.kakaopay.coding.investingapi.error.ResultCode;
import com.kakaopay.coding.investingapi.service.InvestingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestingController.class)
class InvestingController_GetUserProductsTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestingService investingService;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Test_Get_UserProducts_ShouldStatusOk")
    void testGetUserProducts() throws Exception {
        // given
        long userId = 1234;
        UserProductDto userProductDto = new UserProductDto();
        userProductDto.product_id = 1;

        Mockito.when(investingService.getUserProductList(anyLong())).thenReturn(List.of(userProductDto));

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value("1"));
    }

    @Test
    @DisplayName("Test_Get_UserProducts_WithNoUserId_ShouldStatusForbidden")
    void testGetUserProductsWithNoUserId() throws Exception {
        // given

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products"));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.NotFoundUserId.toString()));
    }

    @Test
    @DisplayName("Test_Get_UserProducts_WithEmptyUserId_ShouldStatusForbidden")
    void testGetUserProductsWithEmptyUserId() throws Exception {
        // given
        String userId = "";

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.NotFoundUserId.toString()));
    }

    @Test
    @DisplayName("Test_Get_UserProducts_WithInvalidUserId_ShouldStatusForbidden")
    void testGetUserProductsWithInvalidUserId() throws Exception {
        // given
        String userId = "abc";

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.InternalServerError.toString()));
    }

    @Test
    @DisplayName("Test_Get_UserProducts_WithZeroUserId_ShouldStatusForbidden")
    void testGetUserProductsWithZeroUserId() throws Exception {
        // given
        long userId = 0;

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadUserId.toString()));
    }
}
