package com.ippse.mblog.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.discovery.ConsulCatalogWatch;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ecwid.consul.v1.ConsulClient;

@Configuration
public class ConsulConfiguration {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired(required = false)
	private TtlScheduler ttlScheduler;
	
	@Autowired
	private ConsulClient consulClient;
	
	@Bean
	@Primary
	public ConsulServiceRegistry consulServiceRegistry(ConsulClient consulClient, ConsulDiscoveryProperties properties,
													   HeartbeatProperties heartbeatProperties) {
		log.info("ConsulConfiguration", "自定义配置，使用MyConsulServiceRegistry替代ConsulServiceRegistry");
		return new MyConsulServiceRegistry(consulClient, properties, ttlScheduler, heartbeatProperties);
	}
	
	@Bean
	@ConditionalOnProperty(name = "spring.cloud.consul.discovery.catalog-services-watch.enabled", matchIfMissing = true)
	public ConsulCatalogWatch consulCatalogWatch(
			ConsulDiscoveryProperties discoveryProperties) {
		return new MyConsulCatalogWatch(discoveryProperties, consulClient);
	}
	
}
