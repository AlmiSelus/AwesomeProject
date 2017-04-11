package com.awesomegroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Spring security disabled - TODO: enable with changed password
 */
@SpringBootApplication(scanBasePackages = {"com.awesomegroup"}, exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.awesomegroup"})
public class AwesomeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomeProjectApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.url("jdbc:h2:mem:awesomedb:H2")
				.driverClassName("org.h2.Driver")
				.username("sa")
				.password("")
				.build();
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		HibernateJpaSessionFactoryBean factoryBean = new HibernateJpaSessionFactoryBean();
		factoryBean.getJpaPropertyMap().put("show_sql", "true");
		return factoryBean;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins("http://localhost:3000")
						.allowedHeaders("x-requested-with", "accept", "authorization", "content-type")
						.exposedHeaders("access-control-allow-headers", "access-control-allow-methods", "access-control-allow-origin", "access-control-max-age", "X-Frame-Options")
						.allowedMethods(RequestMethod.OPTIONS.name(), RequestMethod.GET.name(), RequestMethod.POST.name());
			}
		};
	}
}
