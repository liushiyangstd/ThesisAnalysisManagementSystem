package com.tj.serve.config;


import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableTransactionManagement
public class MybatisConfig {
    //逻辑删除配置
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }
    //分页配置
    @Bean
    public PaginationInterceptor interceptor(){
        return new PaginationInterceptor();
    }
}

