package com.crypto.coins;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${app.allowed.origins}")
	private String allowedOrigins;

    @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/{path:^(?!api$).*$}").setViewName("forward:/index.html");
		registry.addViewController("/*/{path:^(?!api$).*$}").setViewName("forward:/index.html");
	}
    
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("*")
                    .maxAge(3600L)
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true);
    }
}

