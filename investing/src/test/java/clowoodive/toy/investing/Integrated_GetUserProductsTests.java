package clowoodive.toy.investing;

import clowoodive.toy.investing.controller.InvestingController;
import clowoodive.toy.investing.service.DataService;
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
class Integrated_GetUserProductsTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataService dataService;

    @BeforeEach()
    void initDB() {
        dataService.truncateDBAll();
    }


    @Test
    @DisplayName("Test_Req_UserProduct_WithOne")
    void testReqGetUserProductWithOne() throws Exception {
        // given
        var now = LocalDateTime.now();
        long userId = 112233;
        int productId = 1;
        long investingAmount = 500000;
        LocalDateTime investingAt = now;
        dataService.insertUserProduct(userId, productId, investingAmount, investingAt);

        String title = "투자중인 포트폴리오";
        long totalInvestingAmount = 5000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(5);
        long accumulatedInvestingAmount = 1000000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].investing_amount").value(String.valueOf(investingAmount)));
    }

    @Test
    @DisplayName("Test_Req_UserProduct_WithMany")
    void testReqGetUserProductWithMany() throws Exception {
        // given
        var now = LocalDateTime.now();
        long userId = 112233;
        int productId = 1;
        long investingAmount = 500000;
        LocalDateTime investingAt = now;
        dataService.insertUserProduct(userId, productId, investingAmount, investingAt);

        String title = "투자중인 포트폴리오";
        long totalInvestingAmount = 5000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(5);
        long accumulatedInvestingAmount = 1000000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        int productId2 = 2;
        dataService.insertUserProduct(userId, productId2, investingAmount, investingAt);
        dataService.upsertProductData(productId2, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[1].product_id").value(String.valueOf(productId2)));
    }

    @Test
    @DisplayName("Test_Req_UserProduct_WithEmpty")
    void testReqGetUserProductWithEmpty() throws Exception {
        // given
        long userId = 11223344;

        // when
        var resultActions = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }
}
