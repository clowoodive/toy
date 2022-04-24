package com.kakaopay.coding.investingapi;

import com.kakaopay.coding.investingapi.controller.InvestingController;
import com.kakaopay.coding.investingapi.dto.ProductDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestingController.class)
class InvestingController_GetProductsTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestingService investingService;


    @Test
    @DisplayName("Test_Get_Products_ShouldStatusOk")
    void testGetProductList() throws Exception {
        // given
        ProductDto productDto = new ProductDto();
        productDto.product_id = 1;

        Mockito.when(investingService.getProductList()).thenReturn(List.of(productDto));

        // when
        var resultActions = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value("1"));
    }
}
