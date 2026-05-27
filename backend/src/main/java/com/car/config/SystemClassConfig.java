package com.car.config;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SystemClassConfig {
	
	@Bean("random")
	public Random createRandomBean() {
		return new Random();
	}
	
	@Bean("otpholder")
	public Map<String, Object[]> getUserverificationMap(){
		ConcurrentHashMap<String, Object[]> map=new ConcurrentHashMap<String, Object[]>();
		return map;
	}
	@Bean("invalidjwt")
	public Set<String> createInvalidJwt(){
		return Collections.newSetFromMap(new ConcurrentHashMap<>());
	}
	
	@Bean
	public ModelMapper modelMapper(){
		ModelMapper mapper=new ModelMapper();
		mapper.getConfiguration()
		      .setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}
}
