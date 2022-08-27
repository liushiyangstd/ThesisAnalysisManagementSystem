package com.tj.serve.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");

        corsConfiguration.addAllowedOrigin("http://localhost:8081");
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:8081");
//
//        corsConfiguration.addAllowedOrigin("http://localhost:8082");
//        corsConfiguration.addAllowedOrigin("http://127.0.0.1:8082");
//
//        // 增加允许8083端口跨域
//        corsConfiguration.addAllowedOrigin("http://localhost:8083");
//        corsConfiguration.addAllowedOrigin("http://127.0.0.1:8083");
//
        corsConfiguration.addAllowedOrigin("http://120.27.202.66:80");
        corsConfiguration.addAllowedOrigin("http://120.27.202.66");

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
