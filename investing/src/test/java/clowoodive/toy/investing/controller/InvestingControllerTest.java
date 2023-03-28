package clowoodive.toy.investing.controller;

import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.service.InvestingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvestingController.class)
class InvestingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InvestingService investingService;

    @Test
    void getProducts() throws Exception {
        // given
        ProductDto productDto1 = new ProductDto();
        productDto1.setProduct_id(123);
        ProductDto productDto2 = new ProductDto();
        productDto2.setProduct_id(456);

        given(investingService.getProductList()).willReturn(Arrays.asList(productDto1, productDto2));

        // when
        ResultActions resultActions = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value("123"))
                .andExpect(jsonPath("$[1].product_id").value("456"));

        then(investingService).should(times(1)).getProductList();
    }

    @Test
    void investingProduct() {
    }

    @Test
    void getUserProducts() {
    }
}