package com.project.config;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class CamundaConfig {

	@Bean
	@Order(Ordering.DEFAULT_ORDER + 1)
	public static ProcessEnginePlugin myCustomConfiguration() {
		return new CustomTypePlugin();
	}
}
