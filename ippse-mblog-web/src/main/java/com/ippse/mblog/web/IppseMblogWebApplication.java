package com.ippse.mblog.web;

import java.util.Locale;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.ippse.mblog")
@EnableJpaRepositories(basePackages = { "com.ippse.mblog" })
@EntityScan(basePackages = { "com.ippse.mblog" })
@ComponentScan(basePackages = { "com.ippse.mblog" })
@EnableFeignClients
@Configuration
@EnableOAuth2Client
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
public class IppseMblogWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(IppseMblogWebApplication.class, args);
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.CHINA);
		return slr;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(3600); // refresh cache once per hour
		return messageSource;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("123");
		converter.setAccessTokenConverter(new JwtConverter());
		return converter;
	}
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}

	public static class JwtConverter extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {

		@Override
		public void configure(JwtAccessTokenConverter converter) {
			converter.setAccessTokenConverter(this);
		}

		@Override
		public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
			OAuth2Authentication auth = super.extractAuthentication(map);
			log.info("JwtConverter" + map);
			auth.setDetails(map); // this will get spring to copy JWT content
									// into Authentication
			return auth;
		}
	}
	
	@Bean
    @Order(0)
    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        return multipartFilter;
    }

    //显式声明CommonsMultipartResolver为mutipartResolver
    @Bean(name="multipartResolver")
    public MultipartResolver mutipartResolver() {
        CustomMultipartResolver com = new CustomMultipartResolver();
        com.setDefaultEncoding("utf-8");
        //com.setUploadTempDir(uploadTempDir);//TODO 完善对每用户的临时文件夹大小的控制
        return com;
    }

}
