package com.youyu.conference.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 业务数据库
 */
@Configuration
@EnableTransactionManagement //开启事物管理
@MapperScan(basePackages = {"com.youyu.conference.repository"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConfig {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    public static final String COTENTANT_TRANSACTION_MANAGER = "dataSourceTransactionManager";

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory());
        return template;
    }

    @Bean(COTENTANT_TRANSACTION_MANAGER)
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
