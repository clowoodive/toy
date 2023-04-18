package clowoodive.toy.investing.product;

import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.mapper.ProductDBMapper;
import clowoodive.toy.investing.mapper.UserDBMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductDBMapper productDBMapper;
    private final UserDBMapper userDBMapper;

    @Autowired
    public ProductService(ProductDBMapper productDBMapper, UserDBMapper userDBMapper) {
        this.productDBMapper = productDBMapper;
        this.userDBMapper = userDBMapper;
    }

    public List<ProductDto> getProducts() {
        val now = LocalDateTime.now();

        List<ProductEntity> validProductMetaEntityList = productDBMapper.selectProductAll();
        if (validProductMetaEntityList.size() == 0)
            return new ArrayList<ProductDto>();

//        Map<Integer, ProductInvestingEntity> productInvestingMap = null;
//        var validProductIdList = validProductMetaEntityList.stream().map(m -> m.product_id).collect(Collectors.toList());

//        List<ProductInvestingEntity> productInvestingEntityList = investingDBMapper.selectProductInvestingListByIdList(validProductIdList);
//        if (validProductIdList.size() != productInvestingEntityList.size())
//            throw new InvestingException(ResultCode.BadServerData, "not found product investing data");
//
//        productInvestingMap = productInvestingEntityList.stream().collect(Collectors.toMap(k -> k.product_id, v -> v));

        List<ProductDto> productDtoList = new ArrayList<>();
        for (var productMetaEntity : validProductMetaEntityList) {
            ProductDto productDto = new ProductDto(productMetaEntity);
            productDtoList.add(productDto);
        }

        return productDtoList;
    }

    public ProductDto getProductById(int productId) {
        ProductEntity productEntity = productDBMapper.selectProductById(productId);
        if (productEntity == null)
            return null;

        return new ProductDto(productEntity);
    }

    public int getNextProductId() {
        return productDBMapper.selectNextProductId();
    }

    @Transactional
    public int saveProduct(ProductDto productDto){

        ProductEntity productEntity = new ProductEntity(productDto);

        return this.productDBMapper.insertOrUpdateProductMeta(productEntity);
    }

    @Transactional
    public void deleteProduct(ProductDto productDto){

        int result = this.productDBMapper.deleteProductById(productDto.getProductId());
    }

    @Transactional
    public int investProduct(long userId, int productId, long investingAmount) {
        var now = LocalDateTime.now();

        ProductEntity productMetaEntity = productDBMapper.selectProductById(productId);
        if (productMetaEntity == null)
            throw new InvestingException(ResultCode.InvalidProductId, "invalid product_id");

        if (productMetaEntity.getOpen_at().isAfter(now) || productMetaEntity.getClose_at().isBefore(now))
            throw new InvestingException(ResultCode.BadPeriod, "invalid product period");

        UserProductEntity userProductEntity = userDBMapper.selectUserProduct(userId, productId);
        if (userProductEntity != null)
            throw new InvestingException(ResultCode.DuplicatedInvesting, "already investing");

        ProductInvestingEntity productInvestingEntity = productDBMapper.selectProductInvesting(productId);
        if (productInvestingEntity == null)
            throw new InvestingException(ResultCode.BadInvestingData);

        if (productInvestingEntity.accumulated_investing_amount >= productMetaEntity.getTotal_investing_amount())
            throw new InvestingException(ResultCode.SoldOut, "already soldout");

        int isUpdated = productDBMapper.updateProductInvesting(productId, investingAmount, productMetaEntity.getTotal_investing_amount());
        if (isUpdated <= 0)
            throw new InvestingException(ResultCode.ExceededAmount, "exceeded investing amount");

        userProductEntity = new UserProductEntity();
        userProductEntity.user_id = userId;
        userProductEntity.product_id = productId;
        userProductEntity.investing_amount = investingAmount;
        userProductEntity.investing_at = LocalDateTime.now();

        int isInserted = userDBMapper.insertUserProduct(userProductEntity);
        if (isInserted <= 0)
            throw new InvestingException(ResultCode.InternalServerError, "error create user data");

        return productId;
    }
}
