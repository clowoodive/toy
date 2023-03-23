package clowoodive.toy.investing.service;

import clowoodive.toy.investing.entity.ProductInvestingEntity;
import clowoodive.toy.investing.entity.ProductMetaEntity;
import clowoodive.toy.investing.entity.UserProductEntity;
import clowoodive.toy.investing.mapper.InvestingDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class DataService {
    private final InvestingDBMapper investingDBMapper;

    @Value("${is_meta_data_init_startup}")
    private boolean isMetaDataInitStartup;

    @Autowired
    public DataService(InvestingDBMapper investingDBMapper) {
        this.investingDBMapper = investingDBMapper;
    }

    @PostConstruct
    private void initMetaDataStartup() {
        if (this.isMetaDataInitStartup == false)
            return;

        initProductMetaData();
    }

    public void initProductMetaData() {
        var now = LocalDateTime.now();

        int productId = 0;
        String title = null;
        long totalInvestingAmount = 0;
        LocalDateTime startedAt = null;
        LocalDateTime finishedAt = null;
        long accumulatedInvestingAmount = 0;
        int investingUserCount = 0;

        truncateDBAll();

        // 유효한 데이터
        productId = 1;
        title = "개인신용 포트폴리오";
        totalInvestingAmount = 1000000;
        startedAt = now.minusDays(1);
        finishedAt = now.plusDays(7);
        accumulatedInvestingAmount = 0;
        investingUserCount = 0;
        upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // 유효한 데이터
        productId = 2;
        title = "부동산 포트폴리오";
        totalInvestingAmount = 5000000;
        startedAt = now.minusDays(2);
        finishedAt = now.plusDays(8);
        accumulatedInvestingAmount = 1;
        investingUserCount = 100000;
        upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // 기간 만료된 데이터
        productId = 3;
        title = "만료된 포트폴리오";
        totalInvestingAmount = 3000000;
        startedAt = now.minusDays(3);
        finishedAt = now.minusDays(1);
        accumulatedInvestingAmount = 3;
        investingUserCount = 600000;
        upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // 오픈 안된 데이터
        productId = 4;
        title = "시작안된 포트폴리오";
        totalInvestingAmount = 4000000;
        startedAt = now.plusDays(4);
        finishedAt = now.plusDays(8);
        accumulatedInvestingAmount = 0;
        investingUserCount = 0;
        upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);

        // 투자금 달성 데이터
        productId = 5;
        title = "마감된 포트폴리오";
        totalInvestingAmount = 5000000;
        startedAt = now.minusDays(3);
        finishedAt = now.plusDays(5);
        accumulatedInvestingAmount = 15;
        investingUserCount = 5000000;
        upsertProductData(productId, title, totalInvestingAmount, startedAt, finishedAt, accumulatedInvestingAmount, investingUserCount);
    }

    public void upsertProductData(int productId, String title, long totalInvestingAmount, LocalDateTime startedAt, LocalDateTime finishedAt,
                                  long accumulatedInvestingAmount, int investing_user_count) {
        ProductMetaEntity productMetaEntity = new ProductMetaEntity();
        productMetaEntity.product_id = productId;
        productMetaEntity.title = title;
        productMetaEntity.total_investing_amount = totalInvestingAmount;
        productMetaEntity.started_at = startedAt;
        productMetaEntity.finished_at = finishedAt;

        ProductInvestingEntity productInvestingEntity = new ProductInvestingEntity();
        productInvestingEntity.product_id = productId;
        productInvestingEntity.accumulated_investing_amount = accumulatedInvestingAmount;
        productInvestingEntity.investing_user_count = investing_user_count;

        investingDBMapper.insertOrUpdateProductMeta(productMetaEntity);
        investingDBMapper.insertOrUpdateProductInvesting(productInvestingEntity);
    }

    public void insertUserProduct(long userId, int productId, long investingAmount, LocalDateTime investingAt) {
        UserProductEntity userProductEntity = new UserProductEntity();
        userProductEntity.user_id = userId;
        userProductEntity.product_id = productId;
        userProductEntity.investing_amount = investingAmount;
        userProductEntity.investing_at = investingAt;

        investingDBMapper.insertUserProduct(userProductEntity);
    }

    public void truncateDBAll() {
        investingDBMapper.truncateProductMeta();
        investingDBMapper.truncateProductInvesting();
        investingDBMapper.truncateUserProduct();
    }
}
