package clowoodive.toy.investing.service;

import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.dto.ProductDto;
import clowoodive.toy.investing.dto.UserProductDto;
import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.entity.ProductMetaEntity;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.mapper.InvestingDBMapper;
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
public class InvestingService {
    private final InvestingDBMapper investingDBMapper;

    @Autowired
    public InvestingService(InvestingDBMapper investingDBMapper) {
        this.investingDBMapper = investingDBMapper;
    }

    public List<ProductDto> getProductList() {
        val now = LocalDateTime.now();

        List<ProductMetaEntity> validProductMetaEntityList = investingDBMapper.selectProductMetaListValid(now);
        if (validProductMetaEntityList.size() == 0)
            return new ArrayList<ProductDto>();

        Map<Integer, ProductInvestingEntity> productInvestingMap = null;
        var validProductIdList = validProductMetaEntityList.stream().map(m -> m.product_id).collect(Collectors.toList());

        List<ProductInvestingEntity> productInvestingEntityList = investingDBMapper.selectProductInvestingListByIdList(validProductIdList);
        if (validProductIdList.size() != productInvestingEntityList.size())
            throw new InvestingException(ResultCode.BadServerData, "not found product investing data");

        productInvestingMap = productInvestingEntityList.stream().collect(Collectors.toMap(k -> k.product_id, v -> v));

        List<ProductDto> productDtoList = new ArrayList<>();
        for (var productMetaEntity : validProductMetaEntityList) {
            ProductInvestingEntity productInvestingEntity = productInvestingMap.get(productMetaEntity.product_id);

            boolean isInvestingClosed = productMetaEntity.total_investing_amount <= productInvestingEntity.accumulated_investing_amount;
            ProductDto productDto = new ProductDto(productMetaEntity, productInvestingEntity, isInvestingClosed);
            productDtoList.add(productDto);
        }

        return productDtoList;
    }

    @Transactional
    public int investProduct(long userId, int productId, long investingAmount) {
        var now = LocalDateTime.now();

        ProductMetaEntity productMetaEntity = investingDBMapper.selectProductMeta(productId);
        if (productMetaEntity == null)
            throw new InvestingException(ResultCode.BadProductId, "invalid product_id");

        if (productMetaEntity.started_at.isAfter(now) || productMetaEntity.finished_at.isBefore(now))
            throw new InvestingException(ResultCode.BadPeriod, "invalid product period");

        UserProductEntity userProductEntity = investingDBMapper.selectUserProduct(userId, productId);
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

        int isInserted = investingDBMapper.insertUserProduct(userProductEntity);
        if (isInserted <= 0)
            throw new InvestingException(ResultCode.InternalServerError, "error create user data");

        return productId;
    }

    public List<UserProductDto> getUserProductList(long userId) {
        List<UserProductEntity> userProductEntityList = investingDBMapper.selectUserProductListAll(userId);
        if (userProductEntityList.size() == 0)
            return new ArrayList<UserProductDto>();

        var ownProductIdList = userProductEntityList.stream().map(m -> m.product_id).collect(Collectors.toList());

        List<ProductMetaEntity> productMetaEntityList = investingDBMapper.selectProductMetaListByIdList(ownProductIdList);
        if (ownProductIdList.size() != productMetaEntityList.size())
            throw new InvestingException(ResultCode.BadServerData, "not found metadata");

        Map<Integer, ProductMetaEntity> productMetaEntityMap = productMetaEntityList.stream().collect(Collectors.toMap(k -> k.product_id, v -> v));

        List<UserProductDto> userProductDtoList = new ArrayList<>();
        for (var userProductEntity : userProductEntityList) {
            ProductMetaEntity productMetaEntity = productMetaEntityMap.get(userProductEntity.product_id);
            UserProductDto userProductDto = new UserProductDto(userProductEntity, productMetaEntity);

            userProductDtoList.add(userProductDto);
        }

        return userProductDtoList;
    }
}
