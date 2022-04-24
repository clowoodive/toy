package com.kakaopay.coding.investingapi;

import com.kakaopay.coding.investingapi.service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class Integrated_GetProductsTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataService dataService;

    @BeforeEach()
    void initDB() {
        dataService.truncateDBAll();
    }


    @Test
    @DisplayName("Test_Req_GetProducts_WithValid")
    void testReqGetProducts() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 1;
        String title = "개인신용 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(7);
        long accumulatedInvestingAmount = 0;
        int investingUserCount = 0;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // when
        var resultActions = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].is_investing_closed").value(false));
    }

    @Test
    @DisplayName("Test_Req_GetProducts_WithExpired")
    void testReqGetProductsWithExpired() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 2;
        String title = "만료된 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(6);
        LocalDateTime finishedAt = now.minusDays(2);
        long accumulatedInvestingAmount = 0;
        int investingUserCount = 0;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // when
        var resultActions = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Test_Req_GetProducts_WithNotOpened")
    void testReqGetProductsWithNotOpened() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 3;
        String title = "시작안된 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.plusDays(1);
        LocalDateTime finishedAt = now.plusDays(5);
        long accumulatedInvestingAmount = 0;
        int investingUserCount = 0;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // when
        var resultActions = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Test_Req_GetProducts_WithClosed")
    void testReqGetProductsWithClosed() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 4;
        String title = "투자 마감된 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(5);
        long accumulatedInvestingAmount = 1000000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // when
        var resultActions = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].is_investing_closed").value(true));
    }
}
