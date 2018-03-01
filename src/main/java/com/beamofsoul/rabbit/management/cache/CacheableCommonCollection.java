package com.beamofsoul.rabbit.management.cache;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.beamofsoul.rabbit.primary.entity.BaseAbstractEntity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface CacheableCommonCollection {

	String[] value() default "";
	Class<? extends Serializable> entity() default BaseAbstractEntity.class;
}
