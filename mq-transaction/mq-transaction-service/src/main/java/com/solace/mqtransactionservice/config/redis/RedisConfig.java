package com.solace.mqtransactionservice.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.timeout}")
    long timeout;
    @Value("${spring.redis.lettuce.pool.max-active}")
    int maxActive;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    int maxWait;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    int minIdle;
     /**
       * 作者 CG
       * 时间 2019/8/5 19:57
       * 注释 开启事务
       */
    @Bean
    public StringRedisTemplate stringRedisTemplate(
            @Value("${spring.redis.redis1.database}") int database,
            @Value("${spring.redis.redis1.host}") String hostName,
            @Value("${spring.redis.redis1.port}") int port,
            @Value("${spring.redis.redis1.password}") String password) {
        //初始化Factory
        RedisConnectionFactory redisTemplate = createRedisTemplate(database, hostName, port, password);
        StringRedisTemplate redis = createStringRedisTemplate(redisTemplate);
        //开启事务
        redis.setEnableTransactionSupport(true);
        return redis;
    }
     /**
       * 作者 CG
       * 时间 2019/8/5 19:57
       * 注释 开启事务
       */
    @Bean
    public StringRedisTemplate stringRedisTemplate2(
            @Value("${spring.redis.redis2.database}") int database,
            @Value("${spring.redis.redis2.host}") String hostName,
            @Value("${spring.redis.redis2.port}") int port,
            @Value("${spring.redis.redis2.password}") String password) {
        //初始化Factory
        RedisConnectionFactory redisTemplate = createRedisTemplate(database, hostName, port, password);
        StringRedisTemplate redis = createStringRedisTemplate(redisTemplate);
        //开启事务
        redis.setEnableTransactionSupport(true);
        return redis;
    }
     /**
       * 作者 CG
       * 时间 2019/8/5 19:57
       * 注释 未开启事务
       */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            @Value("${spring.redis.redis1.database}") int database,
            @Value("${spring.redis.redis1.host}") String hostName,
            @Value("${spring.redis.redis1.port}") int port,
            @Value("${spring.redis.redis1.password}") String password) {
        //初始化Factory
        RedisConnectionFactory redisTemplate = createRedisTemplate(database, hostName, port, password);
        return createRedisTemplate(redisTemplate);
    }
     /**
       * 作者 CG
       * 时间 2019/8/5 19:57
       * 注释 未开启事务
       */
    @Bean
    public RedisTemplate<String, Object> redisTemplate2(
            @Value("${spring.redis.redis2.database}") int database,
            @Value("${spring.redis.redis2.host}") String hostName,
            @Value("${spring.redis.redis2.port}") int port,
            @Value("${spring.redis.redis2.password}") String password) {
        //初始化Factory
        RedisConnectionFactory redisTemplate = createRedisTemplate(database, hostName, port, password);
        return createRedisTemplate(redisTemplate);
    }

    public RedisConnectionFactory createRedisTemplate(int database, String hostName, int port, String password) {
        /* ========= 基本配置 ========= */
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        configuration.setPort(port);
        configuration.setDatabase(database);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        /* ========= 连接池通用配置 ========= */
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        /* ========= lettuce pool ========= */
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.poolConfig(genericObjectPoolConfig);
        builder.commandTimeout(Duration.ofSeconds(timeout));
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, builder.build());
        connectionFactory.afterPropertiesSet();
        /* ========= 创建 RedisConnectionFactory ========= */
        return connectionFactory;
    }

    public RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer StringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setDefaultSerializer(StringRedisSerializer);//设置默认的Serialize，包含 keySerializer & valueSerializer
        //redisTemplate.setKeySerializer(fastJsonRedisSerializer);//单独设置keySerializer
        //redisTemplate.setValueSerializer(fastJsonRedisSerializer);//单独设置valueSerializer
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public StringRedisTemplate createStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer StringRedisSerializer = new StringRedisSerializer();
        stringRedisTemplate.setDefaultSerializer(StringRedisSerializer);//设置默认的Serialize，包含 keySerializer & valueSerializer
        //redisTemplate.setKeySerializer(fastJsonRedisSerializer);//单独设置keySerializer
        //redisTemplate.setValueSerializer(fastJsonRedisSerializer);//单独设置valueSerializer
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }

    /**
     * 作者 CG
     * 时间 2019/8/5 10:57
     * 注释 redis 事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}