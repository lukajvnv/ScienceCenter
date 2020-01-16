package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ScienceCenterApplication {

  public static void main(String[] args) {
//     SpringApplication.run(ScienceCenterApplication.class, "--debug");
    SpringApplication.run(ScienceCenterApplication.class);
    
//    ApplicationContext ctx = SpringApplication.run(ScienceCenterApplication.class, args);
//    JavaMailSender mailService = (JavaMailSender) ctx.getBean("proba");
  }

}