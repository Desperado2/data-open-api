package com.github.desperado2.data.open.api.datasource.manage.utils;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.desperado2.data.open.api.datasource.manage.entity.DataSourceConfig;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.util.Arrays;

/**
 * ES工具
 * @author tu nan
 * @date 2023/3/31
 **/
public class ElasticSearchUtils {

    public static RestClientTransport getRestClientTransport(DataSourceConfig config){
        String host = config.getUrl();
        String schema = "http";
        RestClient client = RestClient.builder(Arrays.stream(host.split(","))
                .map(it -> it.split(":"))
                .map(it -> new HttpHost(it[0], Integer.parseInt(it[1]), schema))
                .toArray(HttpHost[]::new))
                .build();
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper();
        jacksonJsonpMapper.objectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return new RestClientTransport(client, jacksonJsonpMapper);
    }
}
