package com.ippse.mblog.web.config;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.consul.discovery.ConsulCatalogWatch;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyConsulCatalogWatch extends ConsulCatalogWatch {
	private final ConsulDiscoveryProperties properties;
	private final ConsulClient consul;
	private final AtomicReference<BigInteger> catalogServicesIndex = new AtomicReference<>();
	private ApplicationEventPublisher publisher;

	public MyConsulCatalogWatch(ConsulDiscoveryProperties properties, ConsulClient consul) {
		super(properties, consul);
		this.properties = properties;
		this.consul = consul;
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	@Scheduled(fixedDelayString = "${spring.cloud.consul.discovery.catalogServicesWatchDelay:30000}")
	public void catalogServicesWatch() {
		try {
			long index = -1;
			if (catalogServicesIndex.get() != null) {
				index = catalogServicesIndex.get().longValue();
			}

			Response<Map<String, List<String>>> response = consul
					.getCatalogServices(new QueryParams(properties
							.getCatalogServicesWatchTimeout(), index));
			Long consulIndex = response.getConsulIndex();
			if (consulIndex != null) {
				catalogServicesIndex.set(BigInteger.valueOf(consulIndex));
			}

			log.trace("Received services update from consul: {}, index: {}",
					response.getValue(), consulIndex);
			publisher.publishEvent(new HeartbeatEvent(this, consulIndex));
		}
		catch (Exception e) {
			log.error("Error watching Consul CatalogServices", e.getMessage());
		}
	}

}
