package clowoodive.toy.investing.controller;

import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.service.InvestingService;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
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
    public List<ProductDto> getProducts() {
        return investingService.getProducts();
    }

    @PostMapping("/user/products/{product_id}/investing-amount/{investing_amount}")
    public void investProduct(@RequestHeader Map<String, String> headers, @PathVariable("product_id") int productId,
                              @PathVariable("investing_amount") long investingAmount) {
        String userIdStr = headers.get(HEADER_USER_ID_KEY);
        if (userIdStr == null || userIdStr.isEmpty())
            throw new InvestingException(ResultCode.BadReqParamUserId, "userId is null or empty, userIdStr : " + userIdStr);

        long userId = Long.parseLong(userIdStr);
        if (userId <= 0)
            throw new InvestingException(ResultCode.InvalidUserId, "invalid userId");

        if (productId <= 0)
            throw new InvestingException(ResultCode.InvalidProductId, "invalid productId");

        if (investingAmount <= 0)
            throw new InvestingException(ResultCode.InvalidAmount, "invalid investing amount");

        investingService.investProduct(userId, productId, investingAmount);
    }
}
