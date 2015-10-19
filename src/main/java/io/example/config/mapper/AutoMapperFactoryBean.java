package io.example.config.mapper;

import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AutoMapperFactoryBean implements FactoryBean<AutoMapper> {

    private static final Class<AutoMapper> AUTO_MAPPER_CLASS = AutoMapper.class;

    @Autowired(required = false)
    private List<AutoMapperConfigurer> configurers;

    @Override
    public AutoMapper getObject() throws Exception {
        final AutoMapper autoMapper = new AutoMapper();
		autoMapper.getConfiguration()
			.setFieldMatchingEnabled(true)
			.setFieldAccessLevel(Configuration.AccessLevel.PACKAGE_PRIVATE)
			.setMatchingStrategy(MatchingStrategies.LOOSE);
        configure(autoMapper);
        return autoMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return AUTO_MAPPER_CLASS;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void configure(AutoMapper autoMapper) {
        if (configurers != null) {
            for (AutoMapperConfigurer autoMapperConfigurer : configurers) {
                autoMapperConfigurer.configure(autoMapper);
            }
        }
    }
}
