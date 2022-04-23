package com.kakaopay.coding.investingapi.service;

import com.kakaopay.coding.investingapi.entity.ProductInvestingEntity;
import com.kakaopay.coding.investingapi.entity.ProductMetaEntity;
import com.kakaopay.coding.investingapi.mapper.InvestingDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class MetaDataService {
    private final InvestingDBMapper investingDBMapper;

    private @Value("${is_meta_data_init_startup}")
    boolean isMetaDataInitStartup;

    @Autowired
    public MetaDataService(InvestingDBMapper investingDBMapper) {
        this.investingDBMapper = investingDBMapper;
    }

    @PostConstruct
    private void initMetaDataStartup() {
        if (this.isMetaDataInitStartup == false)
            return;

        initMetaData();
    }

    public void initMetaData() {
        // 유효한 데이터
        ProductMetaEntity productMetaEntity1 = new ProductMetaEntity();
        productMetaEntity1.product_id = 1;
        productMetaEntity1.title = "개인신용 포트폴리오";
        productMetaEntity1.total_investing_amount = 1000000;
        productMetaEntity1.started_at = LocalDateTime.now().minusDays(1);
        productMetaEntity1.finished_at = LocalDateTime.now().plusDays(1);

        ProductInvestingEntity productInvestingEntity1 = new ProductInvestingEntity();
        productInvestingEntity1.product_id = productMetaEntity1.product_id;
        productInvestingEntity1.accumulated_investing_amount = 0;
        productInvestingEntity1.investing_user_count = 0;

        investingDBMapper.insertOrUpdateProductMeta(productMetaEntity1);
        investingDBMapper.insertOrUpdateProductInvesting(productInvestingEntity1);

        // 유효한 데이터
        ProductMetaEntity productMetaEntity2 = new ProductMetaEntity();
        productMetaEntity2.product_id = 2;
        productMetaEntity2.title = "부동산 포트폴리오";
        productMetaEntity2.total_investing_amount = 5000000;
        productMetaEntity2.started_at = LocalDateTime.now().minusDays(2);
        productMetaEntity2.finished_at = LocalDateTime.now().plusDays(2);

        ProductInvestingEntity productInvestingEntity2 = new ProductInvestingEntity();
        productInvestingEntity2.product_id = productMetaEntity2.product_id;
        productInvestingEntity2.accumulated_investing_amount = 0;
        productInvestingEntity2.investing_user_count = 0;

        investingDBMapper.insertOrUpdateProductMeta(productMetaEntity2);
        investingDBMapper.insertOrUpdateProductInvesting(productInvestingEntity2);

        // 기간 만료된 데이터
        ProductMetaEntity productMetaEntity3 = new ProductMetaEntity();
        productMetaEntity3.product_id = 3;
        productMetaEntity3.title = "만료된 포트폴리오";
        productMetaEntity3.total_investing_amount = 3000000;
        productMetaEntity3.started_at = LocalDateTime.now().minusDays(3);
        productMetaEntity3.finished_at = LocalDateTime.now().minusDays(1);

        ProductInvestingEntity productInvestingEntity3 = new ProductInvestingEntity();
        productInvestingEntity3.product_id = productMetaEntity3.product_id;
        productInvestingEntity3.accumulated_investing_amount = 0;
        productInvestingEntity3.investing_user_count = 0;

        investingDBMapper.insertOrUpdateProductMeta(productMetaEntity3);
        investingDBMapper.insertOrUpdateProductInvesting(productInvestingEntity3);

        // 오픈 안된 데이터
        ProductMetaEntity productMetaEntity4 = new ProductMetaEntity();
        productMetaEntity4.product_id = 4;
        productMetaEntity4.title = "시작안된 포트폴리오";
        productMetaEntity4.total_investing_amount = 4000000;
        productMetaEntity4.started_at = LocalDateTime.now().plusDays(1);
        productMetaEntity4.finished_at = LocalDateTime.now().plusDays(4);

        ProductInvestingEntity productInvestingEntity4 = new ProductInvestingEntity();
        productInvestingEntity4.product_id = productMetaEntity4.product_id;
        productInvestingEntity4.accumulated_investing_amount = 0;
        productInvestingEntity4.investing_user_count = 0;

        investingDBMapper.insertOrUpdateProductMeta(productMetaEntity4);
        investingDBMapper.insertOrUpdateProductInvesting(productInvestingEntity4);
    }
}
