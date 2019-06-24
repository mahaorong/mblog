package com.ippse.mblog.api.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.scheduling.annotation.Scheduled;

import com.ecwid.consul.ConsulException;
import com.ecwid.consul.v1.ConsulClient;

public class MyConsulServiceRegistry extends ConsulServiceRegistry {
	private static Log log = LogFactory.getLog(MyConsulServiceRegistry.class);
	
	private final ConsulClient client;
	private final ConsulDiscoveryProperties properties;
	private final TtlScheduler ttlScheduler;
	private final HeartbeatProperties heartbeatProperties;
	
	private ConsulRegistration consulRegistration;
	private boolean isRegister = false;
	
	public MyConsulServiceRegistry(ConsulClient client, ConsulDiscoveryProperties properties, TtlScheduler ttlScheduler,
			HeartbeatProperties heartbeatProperties) {
		super(client, properties, ttlScheduler, heartbeatProperties);
		log.info("MyConsulServiceRegistry:构造器执行");
		this.client = client;
		this.properties = properties;
		this.ttlScheduler = ttlScheduler;
		this.heartbeatProperties = heartbeatProperties;
	}

	/**
	 * 系统启动时会进行第一次注册，如果consul服务器没启动，则进入计划任务。
	 * 
	 */
	@Override
	public void register(ConsulRegistration reg) {
		log.info("MyConsulServiceRegistry:Registering service with consul: " + reg.getService());
		consulRegistration = reg;
		try {
			client.agentServiceRegister(reg.getService(), properties.getAclToken());
			if (heartbeatProperties.isEnabled() && ttlScheduler != null) {
				ttlScheduler.add(reg.getInstanceId());
			}
			isRegister = true;
		}
		catch (ConsulException e) {
			if (this.properties.isFailFast()) {
				//log.error("=======Error registering service with consul: " + reg.getService(), e);
				log.error("Failfast is true. Error registering service with consul: " + reg.getService());
				//ReflectionUtils.rethrowRuntimeException(e);
				/*try {
	                Thread.sleep(10*1000);
	            } catch (InterruptedException e1) {
	                e1.printStackTrace();
	            }*/
				//this.register(reg);
			} else {
				//log.warn("MyConsulServiceRegistry:Failfast is false. Error registering service with consul: " + reg.getService(), e);
				log.warn("Failfast is false. Error registering service with consul: " + reg.getService());
				log.error(e.getMessage());
			}
		}
		//super.register(reg);
	}
	
	@Scheduled(fixedDelay=10*1000)
	public void reg() {
		if (!isRegister) {
			try {
				log.warn("Retry registering service with consul: " + consulRegistration.getService());
				this.register(consulRegistration);
			} catch (NullPointerException e) {
				log.warn("Service is null");
			}
		}
	}

}
