package io.example.config.mapper;

import org.modelmapper.PropertyMap;

public abstract class PropertyMapConfigurerSupport<S, D> implements AutoMapperConfigurer {

    public abstract PropertyMap<S, D> mapping();

    @Override
    public void configure(AutoMapper autoMapper) {
		autoMapper.addMappings(mapping());
    }
}
