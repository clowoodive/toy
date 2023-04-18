package clowoodive.toy.investing.product;

import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.entity.ProductMetaEntity;
import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.mapper.InvestingDBMapper;
import clowoodive.toy.investing.mapper.UserDBMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final InvestingDBMapper investingDBMapper;
    private final UserDBMapper userDBMapper;

    @Autowired
    public ProductService(InvestingDBMapper investingDBMapper, UserDBMapper userDBMapper) {
        this.investingDBMapper = investingDBMapper;
        this.userDBMapper = userDBMapper;
    }

    public List<ProductDto> getProducts() {
        val now = LocalDateTime.now();

        List<ProductMetaEntity> validProductMetaEntityList = investingDBMapper.selectProductAll();
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

            boolean isInvestingClosed = productMetaEntity.total_investing_amount <= productMetaEntity.accumulated_investing_amount;
            ProductDto productDto = new ProductDto(productMetaEntity, isInvestingClosed);
            productDtoList.add(productDto);
        }

        return productDtoList;
    }

    public ProductDto getProductsById(int productId) {
        ProductMetaEntity productMetaEntity = investingDBMapper.selectProductMeta(productId);
        if (productMetaEntity == null)
            return null;

        boolean isInvestingClosed = productMetaEntity.total_investing_amount <= productMetaEntity.accumulated_investing_amount;
        return new ProductDto(productMetaEntity, isInvestingClosed);
    }

    public int getNextProductId() {
        return investingDBMapper.selectNextProductId();
    }

    @Transactional
    public void updateProduct(ProductDto productDto){

        ProductMetaEntity productEntity = new ProductMetaEntity(productDto);

        this.investingDBMapper.insertOrUpdateProductMeta(productEntity);
    }

    @Transactional
    public void deleteProduct(ProductDto productDto){

        int result = this.investingDBMapper.deleteProductById(productDto.productId);
    }

    @Transactional
    public int investProduct(long userId, int productId, long investingAmount) {
        var now = LocalDateTime.now();

        ProductMetaEntity productMetaEntity = investingDBMapper.selectProductMeta(productId);
        if (productMetaEntity == null)
            throw new InvestingException(ResultCode.InvalidProductId, "invalid product_id");

        if (productMetaEntity.started_at.isAfter(now) || productMetaEntity.finished_at.isBefore(now))
            throw new InvestingException(ResultCode.BadPeriod, "invalid product period");

        UserProductEntity userProductEntity = userDBMapper.selectUserProduct(userId, productId);
        if (userProductEntity != null)
            throw new InvestingException(ResultCode.DuplicatedInvesting, "already investing");

        ProductInvestingEntity productInvestingEntity = investingDBMapper.selectProductInvesting(productId);
        if (productInvestingEntity == null)
            throw new InvestingException(ResultCode.BadInvestingData);

        if (productInvestingEntity.accumulated_investing_amount >= productMetaEntity.total_investing_amount)
            throw new InvestingException(ResultCode.SoldOut, "already soldout");

        int isUpdated = investingDBMapper.updateProductInvesting(productId, investingAmount, productMetaEntity.total_investing_amount);
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
