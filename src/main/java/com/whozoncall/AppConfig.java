package com.whozoncall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public @Configuration
class AppConfig {
	
	@Bean
	public ClassLoaderTemplateResolver templateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
	        templateResolver.setPrefix("static/ui/");
	        templateResolver.setCacheable(false);	
	        templateResolver.setSuffix(".html");
	        templateResolver.setTemplateMode("HTML5");
	        return templateResolver;
	    }   
    
  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {

    return new LettuceConnectionFactory(new RedisStandaloneConfiguration("whozoncall-redis.y8ijy5.0001.use2.cache.amazonaws.com", 6379));
  }
}