package io.example.config.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ModelMapper.class)
public class AutoMapperConfiguration {

    @Bean
    @ConditionalOnMissingBean(AutoMapperFactoryBean.class)
    public AutoMapperFactoryBean modelMapperFactoryBean() {
        return new AutoMapperFactoryBean();
    }
}
