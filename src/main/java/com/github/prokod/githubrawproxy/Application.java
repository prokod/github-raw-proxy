package com.github.prokod.githubrawproxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	@Autowired
    private YAMLConfig myConfig;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		// GitHubApiV3Client gwc = new GitHubApiV3Client();
		// System.out.println(gwc.getResult());
	}
}
