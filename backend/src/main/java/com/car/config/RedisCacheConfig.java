package com.car.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class RedisCacheConfig {
	
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
		
//		RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(
//		        new ObjectMapper()
//		            .findAndRegisterModules()
//		            .activateDefaultTyping(
//		                BasicPolymorphicTypeValidator.builder()
//		                    .allowIfSubType(Object.class)
//		                    .build(),
//		                ObjectMapper.DefaultTyping.NON_FINAL
//		            )
//		    );
		
		RedisCacheConfiguration redisCacheConfig=RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(10))
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.disableCachingNullValues();
		
		return RedisCacheManager.builder(factory)
				.cacheDefaults(redisCacheConfig)
				.build();
		
	}
}
