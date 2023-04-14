package clowoodive.toy.investing.controller;

import clowoodive.toy.investing.dto.UserProductDto;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    public final static String HEADER_USER_ID_KEY = "x-user-id";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/products")
    public List<UserProductDto> getProducts(@RequestHeader Map<String, String> headers) {
        String userIdStr = headers.get(HEADER_USER_ID_KEY);
        if (userIdStr == null || userIdStr.isEmpty())
            throw new InvestingException(ResultCode.BadReqParamUserId, "userId is null or empty, userIdStr : " + userIdStr);

        long userId = Long.parseLong(userIdStr);
        if (userId <= 0)
            throw new InvestingException(ResultCode.InvalidUserId, "invalid user_id");

        return userService.getUserProducts(userId);
    }
}
