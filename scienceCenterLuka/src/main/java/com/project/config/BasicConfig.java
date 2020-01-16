package com.project.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.camunda.bpm.engine.impl.ProcessDefinitionQueryProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.util.TaskUrlEndpoint;

@Configuration
public class BasicConfig {
	
	  @Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**")
						.allowedHeaders("*")
						.allowedMethods("*")
						.allowedOrigins("*");
				}			
			};
	    }
	    
//	  @Bean(name = "proba")
//	  public JavaMailSender javaMailSender() {
//	      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	      mailSender.setHost("smtp.gmail.com");
//	      mailSender.setPort(587);
//	       
//	      mailSender.setUsername("isa.airflights.project@gmail.com");
//	      mailSender.setPassword("isa.airflights.project12");
//	       
//	      Properties props = mailSender.getJavaMailProperties();
//	      props.put("mail.transport.protocol", "smtp");
//	      props.put("mail.smtp.auth", "true");
//	      props.put("mail.smtp.starttls.enable", "true");
//	      props.put("mail.debug", "true");
//	       
//	      return mailSender;
//	  }

	  
	@Bean
	public TaskUrlEndpoint getProperty() throws FileNotFoundException, IOException {
		TaskUrlEndpoint taskUrlEndpoint = new TaskUrlEndpoint("src/main/resources/task_url_endpoint.properties");
		
		return taskUrlEndpoint;
	}

}
