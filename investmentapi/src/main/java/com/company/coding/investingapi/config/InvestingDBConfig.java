package com.company.coding.investingapi.config;

import com.company.coding.investingapi.mapper.InvestingDBMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class InvestingDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.investing.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory sessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean
    public MapperFactoryBean<InvestingDBMapper> investingDBMapper() throws Exception {
        MapperFactoryBean<InvestingDBMapper> factoryBean = new MapperFactoryBean<>(InvestingDBMapper.class);

        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sessionFactory());
        factoryBean.setSqlSessionTemplate(sqlSessionTemplate);

        return factoryBean;
    }

}
