package clowoodive.toy.investing;

import clowoodive.toy.investing.service.DataService;
import clowoodive.toy.investing.controller.InvestingController;
import clowoodive.toy.investing.error.ResultCode;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class Integrated_InvestingProductTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataService dataService;

    @BeforeEach()
    void initDB() {
        dataService.truncateDBAll();
    }


    @Test
    @DisplayName("Test_Req_InvestingProduct_WithValid")
    void testReqInvestingProductWithValid() throws Exception {
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

        long userId = 12345;
        long investingAmount = 5000;

        // when
        var resultInvesting = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultUserProduct = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultProducts = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultInvesting.andExpect(status().isOk());

        resultUserProduct.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].investing_amount").value(String.valueOf(investingAmount)));

        resultProducts.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].accumulated_investing_amount").value(String.valueOf(investingAmount)))
                .andExpect(jsonPath("$[0].investing_user_count").value(String.valueOf(investingUserCount + 1)))
                .andExpect(jsonPath("$[0].is_investing_closed").value(false));
    }

    @Test
    @DisplayName("Test_Req_InvestingProduct_WithLast")
    void testReqInvestingProductWithLast() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 1;
        String title = "개인신용 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(7);
        long accumulatedInvestingAmount = 700000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        long userId = 12345;
        long investingAmount = 300000;

        // when
        var resultInvesting = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultUserProduct = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultProducts = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultInvesting.andExpect(status().isOk());

        resultUserProduct.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].investing_amount").value(String.valueOf(investingAmount)));

        resultProducts.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].accumulated_investing_amount").value(String.valueOf(accumulatedInvestingAmount + investingAmount)))
                .andExpect(jsonPath("$[0].investing_user_count").value(String.valueOf(investingUserCount + 1)))
                .andExpect(jsonPath("$[0].is_investing_closed").value(true));
    }

    @Test
    @DisplayName("Test_Req_InvestingProduct_WithExceed")
    void testReqInvestingProductWithExceed() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 1;
        String title = "개인신용 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(7);
        long accumulatedInvestingAmount = 700000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        long userId = 12345;
        long investingAmount = 300001;

        // when
        var resultInvesting = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultUserProduct = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultProducts = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultInvesting.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.ExceededAmount.getCode()));

        resultUserProduct.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

        resultProducts.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].accumulated_investing_amount").value(String.valueOf(accumulatedInvestingAmount)))
                .andExpect(jsonPath("$[0].investing_user_count").value(String.valueOf(investingUserCount)))
                .andExpect(jsonPath("$[0].is_investing_closed").value(false));
    }

    @Test
    @DisplayName("Test_Req_InvestingProduct_WithExpired")
    void testReqInvestingProductWithExpired() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 1;
        String title = "개인신용 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(5);
        LocalDateTime finishedAt = now.minusDays(1);
        long accumulatedInvestingAmount = 700000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        long userId = 12345;
        long investingAmount = 300000;

        // when
        var resultInvesting = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultUserProduct = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultProducts = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultInvesting.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadPeriod.getCode()));

        resultUserProduct.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

        resultProducts.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Test_Req_InvestingProduct_WithNotOpened")
    void testReqInvestingProductWithNotOpened() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 1;
        String title = "개인신용 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.plusDays(2);
        LocalDateTime finishedAt = now.plusDays(6);
        long accumulatedInvestingAmount = 700000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        long userId = 12345;
        long investingAmount = 300000;

        // when
        var resultInvesting = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultUserProduct = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultProducts = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultInvesting.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.BadPeriod.getCode()));

        resultUserProduct.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

        resultProducts.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Test_Req_InvestingProduct_WithClosed")
    void testReqInvestingProductWithClosed() throws Exception {
        // given
        var now = LocalDateTime.now();
        int productId = 1;
        String title = "개인신용 포트폴리오";
        long totalInvestingAmount = 1000000;
        LocalDateTime startedAt = now.minusDays(1);
        LocalDateTime finishedAt = now.plusDays(7);
        long accumulatedInvestingAmount = 1000000;
        int investingUserCount = 5;
        dataService.upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        long userId = 12345;
        long investingAmount = 30000;

        // when
        var resultInvesting = mockMvc.perform(post("/investing/user/products/{product_id}/investing-amount/{investing_amount}", productId, investingAmount)
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultUserProduct = mockMvc.perform(get("/investing/user/products")
                .header(InvestingController.HEADER_USER_ID_KEY, userId)
                .accept(MediaType.APPLICATION_JSON));

        var resultProducts = mockMvc.perform(get("/investing/products")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultInvesting.andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result_code").value(ResultCode.SoldOut.getCode()));

        resultUserProduct.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

        resultProducts.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id").value(String.valueOf(productId)))
                .andExpect(jsonPath("$[0].accumulated_investing_amount").value(String.valueOf(accumulatedInvestingAmount)))
                .andExpect(jsonPath("$[0].investing_user_count").value(String.valueOf(investingUserCount)))
                .andExpect(jsonPath("$[0].is_investing_closed").value(true));
    }
}
