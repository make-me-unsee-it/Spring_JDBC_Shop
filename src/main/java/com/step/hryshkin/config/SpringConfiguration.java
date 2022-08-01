package com.step.hryshkin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration

@ComponentScan("com.step.hryshkin")
@PropertySource("classpath:dataBase.properties")
public class SpringConfiguration {
}
