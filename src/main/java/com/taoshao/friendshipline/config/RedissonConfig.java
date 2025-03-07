package com.taoshao.friendshipline.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * @Author taoshao
 * @Date 2024/6/15
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        // 1. 创建配置对象
        Config config = new Config();
        String reidAddress = String.format("redis://%s:%s",host,port);
        config.useSingleServer().setAddress(reidAddress).setDatabase(4);

        // 2. 创建实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
