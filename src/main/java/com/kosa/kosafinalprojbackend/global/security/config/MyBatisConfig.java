package com.kosa.kosafinalprojbackend.global.security.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.kosa.kosafinalprojbackend.mybatis.mappers")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(javax.sql.DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // TypeHandler 등록
        sessionFactory.getObject().getConfiguration().getTypeHandlerRegistry().register(
                com.kosa.kosafinalprojbackend.domains.member.oAuth.domain.ProviderTypeHandler.class);

        return sessionFactory.getObject();
    }
}