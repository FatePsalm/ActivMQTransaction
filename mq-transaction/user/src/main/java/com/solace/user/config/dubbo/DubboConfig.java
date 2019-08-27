package com.solace.user.config.dubbo;


import com.solace.transactioncommon.service.TransactionMessageService;
import org.apache.dubbo.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DubboConfig {
    @Value("${zookeeper.zkAddress}")
    private String zkAddress;
   /* @Reference
    private TransactionMessageService transactionMessageService;*/
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("user-consumer");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress(zkAddress);
        return registryConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setVersion("1.0.0");
        config.setTimeout(1000);
        config.setCheck(false);
        return config;
    }

    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig config = new ProviderConfig();
        config.setVersion("1.0.0");
        config.setTimeout(1000);
        return config;
    }
    //@Bean
    public ServiceConfig<TransactionMessageService> transactionMessageServiceConfig(TransactionMessageService transactionMessageService) {
        ServiceConfig<TransactionMessageService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(TransactionMessageService.class);
        serviceConfig.setRef(transactionMessageService);

        //配置每一个method的信息
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("sayHello");
        methodConfig.setAsync(true);
        methodConfig.setOnreturn("notify.onReturn");
        methodConfig.setOnthrow("notify.onThrow");

        //将method的设置关联到service配置中
        List<MethodConfig> methods = new ArrayList<>();
        methods.add(methodConfig);
        serviceConfig.setMethods(methods);

        //ProviderConfig
        //MonitorConfig
        return serviceConfig;
    }

}