package io.example.config.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AutoMapper extends ModelMapper {

	private static Map<Class, PropertyDescriptor[]> descriptorsMap = new HashMap<Class, PropertyDescriptor[]>();

	public <D> D map(Object sourceObject, Object targetObject, Class<D> destinationType) {
		Object resultObject = null;
		try {
			if (sourceObject == null || targetObject == null) {
				throw new NullPointerException("A null paramter was passed into targetObject");
			}
			resultObject = targetObject.getClass().newInstance();
			Class sourceClass = sourceObject.getClass();
			Class targetClass = targetObject.getClass();
			if (!sourceClass.equals(targetClass)) {
				throw new IllegalArgumentException("Received parameters are not the same type of class, but must be");
			}

			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(targetClass);
			if (descriptors == null) {
				descriptors = PropertyUtils.getPropertyDescriptors(targetClass);
				descriptorsMap.put(targetClass, descriptors);
			}

			for (PropertyDescriptor descriptor : descriptors) {
				if (PropertyUtils.isReadable(targetObject, descriptor.getName()) && PropertyUtils.isWriteable(targetObject,	descriptor.getName())) {
					Method readMethod = descriptor.getReadMethod();
					Object sourceValue = readMethod.invoke(sourceObject);
					Object targetValue = readMethod.invoke(targetObject);
					Object resultValue = targetValue;
					if (sourceValue != null || sourceValue == targetValue) {
						resultValue = sourceValue;
					}
					Method writeMethod = descriptor.getWriteMethod();
					writeMethod.invoke(resultObject, resultValue);
				}
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return destinationType.cast(resultObject);
	}
}