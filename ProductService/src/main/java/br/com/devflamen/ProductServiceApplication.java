package br.com.devflamen;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import br.com.devflamen.command.interceptors.CreateProductCommandInterceptor;
import br.com.devflamen.core.exceptions.ProductServiceEventsErrorHandling;

@EnableEurekaClient
@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext ctx, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(ctx.getBean(CreateProductCommandInterceptor.class));
	}

	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group", 
				conf -> new ProductServiceEventsErrorHandling());
		
		/*
		 * config.registerListenerInvocationErrorHandler("product-group", conf ->
		 * PropagatingErrorHandler.instance());
		 */
	}
	
	@Bean(name = "productSnapshotTriggerDefinition")
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
	}
}
