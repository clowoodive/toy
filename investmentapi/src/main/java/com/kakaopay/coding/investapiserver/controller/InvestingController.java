package com.kakaopay.coding.investapiserver.controller;

import com.kakaopay.coding.investapiserver.dto.ProductDto;
import com.kakaopay.coding.investapiserver.service.InvestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/investing", produces = MediaType.APPLICATION_JSON_VALUE)
public class InvestingController {
    private final InvestingService investingService;

    @Autowired
    public InvestingController(InvestingService investingService) {
        this.investingService = investingService;
    }

    @GetMapping("/products")
    @ResponseBody
    public List<ProductDto> getProducts() {
        var productDtoList = investingService.getProductDtoList();

        return productDtoList;
    }

    @PostMapping("/user/product/{product_id}/amount/{amount}")
    public String addUserProduct(@PathVariable("product_id") int productId, @PathVariable("amount") long amount) {
        investingService.investProduct(productId, amount);

        return "";
    }

    @GetMapping("/user/products")
    public String getUserProducts() {

        return "";
    }

}
