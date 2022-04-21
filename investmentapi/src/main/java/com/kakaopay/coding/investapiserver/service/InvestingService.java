package com.kakaopay.coding.investapiserver.service;

import com.kakaopay.coding.investapiserver.dto.ProductDto;
import com.kakaopay.coding.investapiserver.dto.UserProductDto;
import com.kakaopay.coding.investapiserver.entity.ProductInvestingEntity;
import com.kakaopay.coding.investapiserver.entity.ProductMetaEntity;
import com.kakaopay.coding.investapiserver.entity.UserProductEntity;
import com.kakaopay.coding.investapiserver.mapper.InvestingDBMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<ProductDto> getProductDtoList() {
        val now = LocalDateTime.now();

        List<ProductMetaEntity> validProductMetaEntityList = investingDBMapper.selectProductMetaListValid(now);
        if(validProductMetaEntityList == null || validProductMetaEntityList.size() == 0)
            return List.of();

        var validProductIdList = validProductMetaEntityList.stream().map(m -> m.product_id).collect(Collectors.toList());

        List<ProductInvestingEntity> productInvestingList = investingDBMapper.selectProductInvestingListByIdList(validProductIdList);
        Map<Integer, ProductInvestingEntity> productInvestingMap = null;
        if(productInvestingList != null && productInvestingList.size() > 0)
            productInvestingMap = productInvestingList.stream().collect(Collectors.toMap(k -> k.product_id, v -> v));

        List<ProductDto> productDtoList = new ArrayList<>();
        for(var productMeta : validProductMetaEntityList) {
            ProductInvestingEntity productInvestingEntity = null;
            if(productInvestingMap != null)
                productInvestingEntity = productInvestingMap.get(productMeta.product_id);

                ProductDto productDto = new ProductDto();
                productDto.product_id =  productMeta.product_id;
                productDto.title =  productMeta.title;
                productDto.total_investing_amount =  productMeta.total_investing_amount;
                productDto.is_investing_closed = false;
                if(productInvestingEntity != null) {
                    productDto.accumulated_investing_amount = productInvestingEntity.accumulated_investing_amount;
                    productDto.investing_user_count = productInvestingEntity.investing_user_count;
                    if(productMeta.total_investing_amount <= productInvestingEntity.accumulated_investing_amount)
                        productDto.is_investing_closed = true;
                }
                productDto.started_at = productMeta.started_at;
                productDto.finished_at = productMeta.finished_at;
                productDtoList.add(productDto);
        }

        return productDtoList;
    }

    public void investProduct(int productId, long amount) {
        ProductMetaEntity productMetaEntity = investingDBMapper.selectProductMeta(productId);
        if(productMetaEntity == null){
            // exception
            return;
        }

        UserProductEntity userProductEntity =  investingDBMapper.selectUserProduct(1234, productId);
        if(userProductEntity != null) {
            // exception, already invest
            return;
        }


        ProductInvestingEntity productInvestingEntity =  investingDBMapper.selectProductInvesting(productId);
        if(productInvestingEntity == null) {
            productInvestingEntity = new ProductInvestingEntity();
            productInvestingEntity.product_id =  productId;
            productInvestingEntity.investing_user_count = 0;
            productInvestingEntity.accumulated_investing_amount = 0;
        }






    }
}
