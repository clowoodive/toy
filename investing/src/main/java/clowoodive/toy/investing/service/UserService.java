package clowoodive.toy.investing.service;

import clowoodive.toy.investing.dto.UserProductDto;
import clowoodive.toy.investing.entity.ProductMetaEntity;
import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.error.InvestingException;
import clowoodive.toy.investing.error.ResultCode;
import clowoodive.toy.investing.mapper.InvestingDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InvestingDBMapper investingDBMapper;

    @Autowired
    public UserService(InvestingDBMapper investingDBMapper) {
        this.investingDBMapper = investingDBMapper;
    }

    public List<UserProductDto> getUserProducts(long userId) {
        List<UserProductEntity> userProductEntityList = investingDBMapper.selectUserProductListAll(userId);
        if (userProductEntityList.size() == 0)
            return new ArrayList<>();

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
