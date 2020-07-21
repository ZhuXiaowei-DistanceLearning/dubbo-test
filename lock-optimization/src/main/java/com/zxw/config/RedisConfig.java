package com.zxw.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * @author zxw
 * @date 2020/7/1 15:35
 */
@Configuration
public class RedisConfig {
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s",redisProperties.getHost()+"",redisProperties.getPort()+"");
        config.useSingleServer().setAddress(redisUrl);
//                .setPassword(redisProperties.getPassword());
        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate redisTemplate(){
        redisTemplate.setKeySerializer(new GenericToStringSerializer<String>(String.class));
        redisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
        return redisTemplate;
    }
}
