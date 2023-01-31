package com.increff.posapp.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.posapp")
@PropertySources({ //
		@PropertySource(value = "file:./posapp.properties", ignoreResourceNotFound = false)
})
public class SpringConfig {


}
