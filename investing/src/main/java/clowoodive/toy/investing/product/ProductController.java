package clowoodive.toy.investing.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/products")
public class ProductController {

    public static final String VIEW_PRODUCT_CREATE_OR_UPDATE_FORM = "products/createOrUpdateProductForm";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("productId");
    }

    @ModelAttribute("productDto")
    public ProductDto findProduct(@PathVariable(name = "productId", required = false) Integer productId) {
        return productId == null ? new ProductDto() : this.productService.getProductById(productId);
    }

    @GetMapping("")
    public String getProducts(Model model) {
        List<ProductDto> productDtos = productService.getProducts();

        model.addAttribute("productDtos", productDtos);

        return "products/productList";
    }

    @GetMapping("/{productId}")
    public String getProductDetail(ProductDto productDto, Model model) {

        return "products/productDetail";
    }

    @GetMapping("/create")
    public String showCreateProductForm(ProductDto productDto, Model model) {

        return VIEW_PRODUCT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/create")
    public String createProductForm(@Valid ProductDto productDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return VIEW_PRODUCT_CREATE_OR_UPDATE_FORM;
        }

//        int nextProductId = productService.getNextProductId();
//        productDto.setProductId(nextProductId);

        int inserted = productService.saveProduct(productDto);

        return "redirect:/products";
    }

    @GetMapping("/{productId}/edit")
    public String showUpdateProductForm(ProductDto productDto, Model model) {

        return VIEW_PRODUCT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/{productId}/edit")
    public String updateProductForm(@Valid ProductDto productDto, BindingResult result, Model model) {

        // InitBinder에서 ProductId를 제외하고 바인딩,  @ModelAttribute("productDto")에서 ProductId를 가져와서 대입해줌
        if (result.hasErrors()) {
            return VIEW_PRODUCT_CREATE_OR_UPDATE_FORM;
        }

        productService.saveProduct(productDto);

        return "redirect:/products";
    }

    @PostMapping("/{productId}/delete")
    public String deleteProduct(ProductDto productDto, Model model) {

        productService.deleteProduct(productDto);

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
