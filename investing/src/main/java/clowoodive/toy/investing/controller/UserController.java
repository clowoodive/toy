package clowoodive.toy.investing.controller;

import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.dto.UserProductDto;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.service.InvestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    public final static String HEADER_USER_ID_KEY = "x-user-id";

    private final InvestingService investingService;

    @Autowired
    public UserController(InvestingService investingService) {
        this.investingService = investingService;
    }


    @GetMapping("/products")
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
