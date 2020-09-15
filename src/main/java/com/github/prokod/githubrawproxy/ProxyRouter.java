package com.github.prokod.githubrawproxy;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProxyRouter {

	@Bean
	@RouterOperation(path = "/v0/raw/{org}/{repo}/**", beanClass = ProxyHandler.class, beanMethod = "downloadRaw")
	public RouterFunction<ServerResponse> route(ProxyHandler proxyHandler) {

		return RouterFunctions
			.route(RequestPredicates.GET("/v0/raw/{org}/{repo}/**").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), proxyHandler::downloadRaw);
	}
}
