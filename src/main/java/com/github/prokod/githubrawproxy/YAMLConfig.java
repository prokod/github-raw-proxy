package com.github.prokod.githubrawproxy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {
    public static class Client {
        private String baseUrl;
        private String token;
        private String userAgent;
		public String getBaseUrl() {
			return baseUrl;
		}
		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
        }
        public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
        }
        public String getUserAgent() {
			return userAgent;
		}
		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}
    }

    private Client client;

    public Client getClient() {
        return this.client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}
