package kz.symtech.antifraud.coreservice.config;

import kz.symtech.antifraud.coreservice.properties.ElasticSearchProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ElasticSearchConfig {

    private final ElasticSearchProperties elasticSearchProperties;

    @Bean
    public CredentialsProvider credentialsProvider() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticSearchProperties.getUsername(), elasticSearchProperties.getPassword()));
        return credentialsProvider;
    }

    @Bean
    public RestClientBuilder restHighLevelClient() {
        return RestClient.builder(
                        new HttpHost(elasticSearchProperties.getHost(), elasticSearchProperties.getPort()))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider()));
    }

    @Bean(name = "customElasticsearchClient")
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(elasticSearchProperties.getHost(), elasticSearchProperties.getPort()))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider()))
        );
    }
}
