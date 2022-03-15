package br.com.devflamen;

import org.axonframework.commandhandling.CommandBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import br.com.devflamen.command.interceptors.CreateProductCommandInterceptor;

@EnableEurekaClient
@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	@Bean
	public void registerCreateProductCommandInterceptor(ApplicationContext ctx, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(ctx.getBean(CreateProductCommandInterceptor.class));
	}
}
