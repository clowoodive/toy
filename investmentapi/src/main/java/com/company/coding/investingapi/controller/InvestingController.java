package com.company.coding.investingapi.controller;

import com.company.coding.investingapi.dto.ProductDto;
import com.company.coding.investingapi.dto.UserProductDto;
import com.company.coding.investingapi.service.InvestingService;
import com.company.coding.investingapi.error.InvestingException;
import com.company.coding.investingapi.error.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/investing", produces = MediaType.APPLICATION_JSON_VALUE)
public class InvestingController {
    public final static String HEADER_USER_ID_KEY = "x-user-id";

    private final InvestingService investingService;

    @Autowired
    public InvestingController(InvestingService investingService) {
        this.investingService = investingService;
    }

    @GetMapping("/products")
    @ResponseBody
    public List<ProductDto> getProducts() {
        return investingService.getProductList();
    }

    @PostMapping("/user/products/{product_id}/investing-amount/{investing_amount}")
    public void investingProduct(@RequestHeader Map<String, String> headers, @PathVariable("product_id") int productId,
                              @PathVariable("investing_amount") long investingAmount) {
        String userIdStr = headers.get(HEADER_USER_ID_KEY);
        if (userIdStr == null || userIdStr.isEmpty())
            throw new InvestingException(ResultCode.NotFoundUserId, "no user_id");

        long userId = Long.parseLong(userIdStr);
        if (userId <= 0)
            throw new InvestingException(ResultCode.BadUserId, "invalid user_id");

        if (productId <= 0)
            throw new InvestingException(ResultCode.BadProductId, "invalid product_id");

        if (investingAmount <= 0)
            throw new InvestingException(ResultCode.BadAmount, "invalid amount");

        investingService.investProduct(userId, productId, investingAmount);
    }

    @GetMapping("/user/products")
    public List<UserProductDto> getUserProducts(@RequestHeader Map<String, String> headers) {
        String userIdStr = headers.get(HEADER_USER_ID_KEY);
        if (userIdStr == null || userIdStr.isEmpty())
            throw new InvestingException(ResultCode.NotFoundUserId, "no user_id");

        long userId = Long.parseLong(userIdStr);
        if (userId <= 0)
            throw new InvestingException(ResultCode.BadUserId, "invalid user_id");

        return investingService.getUserProductList(userId);
    }
}
