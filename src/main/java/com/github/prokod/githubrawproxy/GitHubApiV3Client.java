package com.github.prokod.githubrawproxy;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import reactor.core.publisher.Mono;

@Component
public class GitHubApiV3Client {

	private final YAMLConfig appConfig;
	private WebClient client;

	public GitHubApiV3Client(YAMLConfig appConfig) {
		this.appConfig = appConfig;
		this.client = WebClient.create(appConfig.getClient().getBaseUrl());
	}
	
	/**
	 * 
	 * @param org - Github organization name
	 * @param repo - Github repository name
	 * @param sourcePath - Github sourcePath to retrieve (relative to repository root directory)
	 * @param ref - Github reference (branch)
	 * @return - Raw content of sourcePath file
	 */
	public Mono<String> getRepoContents(String org, String repo, String sourcePath, Optional<String> ref) {
		Mono<String> rawAddressResult = client.get()
			.uri(uriBuilder -> constTransform(condTransform(uriBuilder, ref))
			.build(org, repo, sourcePath)) //.build("DATA-LAKE-10108", "mapr-re", "mapr-cluster-architecture/cluster-arch-support-confluence.md")
			.header("User-Agent", appConfig.getClient().getUserAgent())
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(String.class);

		return rawAddressResult;
	}

	private UriBuilder constTransform(UriBuilder uriBuilder) {
		return uriBuilder.path("/api/v3/repos/{org}/{repo}/contents/{srcPath}")
			.queryParam("access_token", appConfig.getClient().getToken());
	}

	private UriBuilder condTransform(UriBuilder uriBuilder, Optional<String> ref) {
		return ref.map(refBranch -> uriBuilder.queryParam("ref", refBranch)).orElse(uriBuilder);
	}

	/**
	 * For instance
	 * "download_url": "https://raw.git.dhl.com/DATA-LAKE-10108/mapr-re/master/mapr-cluster-architecture/cluster-arch-support-confluence.md?token=AAAAU4BESW3SGXDV5C5WIKC7LNKES"
	 * 
	 * @param uri
	 * @return
	 */
	public Mono<String> download(String uri) {
		Mono<String> downloadResult = client.get()
			.uri(uri)
			.accept(MediaType.TEXT_PLAIN)
			.header("Authorization", "token " + appConfig.getClient().getToken())
			.header("User-Agent", appConfig.getClient().getUserAgent())
			.retrieve()
			.bodyToMono(String.class);

		//return rawAddressResult.flatMap(res -> res.bodyToMono(String.class)).block();
		return downloadResult;
	}
}
