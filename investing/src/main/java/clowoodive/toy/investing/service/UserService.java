package clowoodive.toy.investing.service;

import clowoodive.toy.investing.dto.UserProductDto;
import clowoodive.toy.investing.product.ProductEntity;
import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.mapper.ProductDBMapper;
import clowoodive.toy.investing.mapper.UserDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final ProductDBMapper investingDBMapper;
    private final UserDBMapper userDBMapper;

    @Autowired
    public UserService(ProductDBMapper investingDBMapper, UserDBMapper userDBMapper) {
        this.investingDBMapper = investingDBMapper;
        this.userDBMapper = userDBMapper;
    }

    public List<UserProductDto> getUserProducts(long userId) {
        List<UserProductEntity> userProductEntityList = userDBMapper.selectUserProductListAll(userId);
        if (userProductEntityList.size() == 0)
            return new ArrayList<>();

        var ownProductIdList = userProductEntityList.stream().map(m -> m.product_id).collect(Collectors.toList());

        List<ProductEntity> productMetaEntityList = investingDBMapper.selectProductMetaListByIdList(ownProductIdList);
        if (ownProductIdList.size() != productMetaEntityList.size())
            throw new InvestingException(ResultCode.BadServerData, "not found metadata");

        Map<Integer, ProductEntity> productMetaEntityMap = productMetaEntityList.stream().collect(Collectors.toMap(ProductEntity::getProduct_id, v -> v));

        List<UserProductDto> userProductDtoList = new ArrayList<>();
        for (var userProductEntity : userProductEntityList) {
            ProductEntity productMetaEntity = productMetaEntityMap.get(userProductEntity.product_id);
            UserProductDto userProductDto = new UserProductDto(userProductEntity, productMetaEntity);

            userProductDtoList.add(userProductDto);
        }

        return userProductDtoList;
    }
}
