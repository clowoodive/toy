package clowoodive.toy.investing.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class InvestingDBConfig {

//    @Bean
//    @ConfigurationProperties(prefix = "datasource.investing.hikari")
//    public HikariConfig hikariConfig() {
//        return new HikariConfig();
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
//        return new LazyConnectionDataSourceProxy(dataSource);
//    }
//
//    @Bean
//    public SqlSessionFactory sessionFactory() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource());
//        return factoryBean.getObject();
//    }
//
//    @Bean
//    public MapperFactoryBean<InvestingDBMapper> investingDBMapper() throws Exception {
//        MapperFactoryBean<InvestingDBMapper> factoryBean = new MapperFactoryBean<>(InvestingDBMapper.class);
//
//        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sessionFactory());
//        factoryBean.setSqlSessionTemplate(sqlSessionTemplate);
//
//        return factoryBean;
//    }

}
