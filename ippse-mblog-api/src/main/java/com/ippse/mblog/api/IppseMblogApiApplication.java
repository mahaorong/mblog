package com.ippse.mblog.api;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import feign.Request;
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
public class IppseMblogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IppseMblogApiApplication.class, args);
	}
	
	@Bean
    Request.Options feignOptions() {
        return new Request.Options(/**connectTimeoutMillis**/12 * 1000, /** readTimeoutMillis **/12 * 1000);
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
