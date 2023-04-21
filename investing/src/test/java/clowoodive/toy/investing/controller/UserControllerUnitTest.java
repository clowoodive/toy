//package clowoodive.toy.investing.controller;
//
//import clowoodive.toy.investing.dto.UserProductDto;
//import clowoodive.toy.investing.error.ResultCode;
//import clowoodive.toy.investing.service.UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.times;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//class UserControllerUnitTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    UserService userService;
//
//    private String url = "/user/products";
//
//    @Test
//    @DisplayName("유저 상품 투자 정보")
//    void testGetProducts() throws Exception {
//        // given
//        long userId = 1234;
//        UserProductDto userProductDto = new UserProductDto();
//        userProductDto.product_id = 5;
//
//        given(userService.getUserProducts(anyLong())).willReturn(List.of(userProductDto));
//
//        // when
//        var resultActions = mockMvc.perform(
//                get(url)
//                        .header(InvestingController.HEADER_USER_ID_KEY, userId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].product_id").value(userProductDto.product_id));
//        then(userService).should(times(1)).getUserProducts(anyLong());
//    }
//
//    @Test
//    @DisplayName("유저 상품 투자 정보_userId 누락")
//    void testGetProducts_userId_miss() throws Exception {
//        // given
//
//        // when
//        var resultActions = mockMvc.perform(
//                get(url)
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
//    @DisplayName("유저 상품 투자 정보_빈 userId")
//    void testGetProducts_userId_empty() throws Exception {
//        // given
//        String emptyUserId = "";
//
//        // when
//        var resultActions = mockMvc.perform(
//                get(url)
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
//    @DisplayName("유저 상품 투자 정보_숫자가 아닌 userId")
//    void testGetProducts_userId_notNumber() throws Exception {
//        // given
//        String strUserId = "notNumber";
//
//        // when
//        var resultActions = mockMvc.perform(
//                get(url)
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
//    @DisplayName("유저 상품 투자 정보_유효하지 않은 userId")
//    void testGetProducts_userId_invalid() throws Exception {
//        // given
//        long invalidUserId = -1;
//
//        // when
//        var resultActions = mockMvc.perform(
//                get(url)
//                        .header(InvestingController.HEADER_USER_ID_KEY, invalidUserId)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        resultActions.andExpect(status().isForbidden())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.result_code").value(ResultCode.InvalidUserId.getCode()));
//    }
//}