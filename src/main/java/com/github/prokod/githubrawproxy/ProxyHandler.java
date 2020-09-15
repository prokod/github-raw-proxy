package com.github.prokod.githubrawproxy;

import java.util.Optional;

import com.jayway.jsonpath.JsonPath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class ProxyHandler {

	@Autowired
	private GitHubApiV3Client client;

	public Mono<ServerResponse> downloadRaw(ServerRequest request) {
		String org = request.pathVariable("org");
		String repo = request.pathVariable("repo");
		String srcPath = request.pathContainer().subPath(9).value();
		Optional<String> ref = request.queryParam("ref");

		return client.getRepoContents(org, repo, srcPath, ref).flatMap(json -> {
			String downloadUrl = JsonPath.read(json, "$.download_url");
			return client.download(downloadUrl);
		}).flatMap(txt -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromValue(txt)));
	}
}
