package clowoodive.toy.investing.product;

import clowoodive.toy.investing.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("productDto")
    public ProductDto findProduct(@PathVariable(name = "productId", required = false) Integer productId) {
        return productId == null ? new ProductDto() : this.productService.getProductsById(productId);
    }

    @GetMapping("")
    public String getProducts(Model model) {
        List<ProductDto> productDtos = productService.getProducts();

        model.addAttribute("productList", productDtos);

        return "product/productList";
    }

    @GetMapping("/{productId}")
    public String getProductDetail(ProductDto productDto, Model model) {
//        ProductDto productDto = productService.getProductsById(productId);

        model.addAttribute("product", productDto);

        return "product/productDetail";
    }

    @GetMapping("/{productId}/edit")
    public String showProductEditForm(ProductDto productDto, Model model) {
//        ProductDto productDto = productService.getProductsById(productId);

        model.addAttribute("product", productDto);

        return "product/productEditForm";
    }

    @PostMapping("/{productId}/edit")
    public String saveProductEditForm(@Valid ProductDto productDto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.put("product", productDto);
            return "product/productEditForm";
        }

        productService.updateProduct(productDto);

        return "redirect:/products";
    }

//    @PostMapping("/user/products/{product_id}/investing-amount/{investing_amount}")
//    public void investProduct(@RequestHeader Map<String, String> headers, @PathVariable("product_id") int productId,
//                              @PathVariable("investing_amount") long investingAmount) {
//        String userIdStr = headers.get(HEADER_USER_ID_KEY);
//        if (userIdStr == null || userIdStr.isEmpty())
//            throw new InvestingException(ResultCode.BadReqParamUserId, "userId is null or empty, userIdStr : " + userIdStr);
//
//        long userId = Long.parseLong(userIdStr);
//        if (userId <= 0)
//            throw new InvestingException(ResultCode.InvalidUserId, "invalid userId");
//
//        if (productId <= 0)
//            throw new InvestingException(ResultCode.InvalidProductId, "invalid productId");
//
//        if (investingAmount <= 0)
//            throw new InvestingException(ResultCode.InvalidAmount, "invalid investing amount");
//
//        investingService.investProduct(userId, productId, investingAmount);
//    }
}
