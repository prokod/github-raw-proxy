package com.github.prokod.githubrawproxy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProxyRouterTest {

	// Spring Boot will create a `WebTestClient` for you,
	// already configure and ready to issue requests against "localhost:RANDOM_PORT"
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private GitHubApiV3Client client;

	@Test
	public void testDownloadRaw() throws IOException {

		Mockito
			.when(client.getRepoContents("DATA-LAKE-10108", 
											"mapr-re", 
											"mapr-cluster-architecture/cluster-arch-support-confluence.md", 
											Optional.empty()))
			.thenReturn(Mono.just(String.join("", Files.readAllLines(
				Paths.get("src", "test", "resources", "repo-contents-response-body.json")))));

		Mockito.when(client.download("https://raw.git.dhl.com/DATA-LAKE-10108/mapr-re/master/mapr-cluster-architecture/cluster-arch-support-confluence.md?token=AAAAU4BESW3SGXDV5C5WIKC7LNKES"))
			.thenReturn(Mono.just("OK!"));

		webTestClient
			// Create a GET request to test an endpoint
			.get().uri("/v0/raw/DATA-LAKE-10108/mapr-re/mapr-cluster-architecture/cluster-arch-support-confluence.md")
			.accept(MediaType.TEXT_PLAIN)
			.exchange()
			// and use the dedicated DSL to test assertions against the response
			.expectStatus().isOk()
			.expectBody(String.class).isEqualTo("OK!");
	}
}
