package com.github.desperado2.data.open.api.datasource.manage.datasource;


import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformException;
import com.github.desperado2.data.open.api.datasource.manage.model.ScriptContext;
import com.github.desperado2.data.open.api.datasource.manage.model.TableInfo;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.util.List;
import java.util.Map;

/**
 * mongodb 数据源操作
 */
@SuppressWarnings("ALL")
public class ElasticSearchDataSource extends DataSourceDialect {

    private RestClientTransport restClientTransport;

    private String dataSourceCode;

    private ElasticSearchDataSource(){}

    public ElasticSearchDataSource(String dataSourceCode, RestClientTransport restClientTransport) {
        this.dataSourceCode = dataSourceCode;
        this.restClientTransport = restClientTransport;
    }

    public ElasticSearchDataSource(RestClientTransport restClientTransport, boolean storeApi) {
        this.restClientTransport = restClientTransport;
        this.setStoreApi(storeApi);
    }


    @Override
    public List<Map> find(ScriptContext scriptContext) throws Exception {
       throw new RuntimeException("不支持的方法");
    }

    @Override
    public JSONObject findEs(ScriptContext scriptContext) throws Exception {
        // 执行脚本
        String esIndex = scriptContext.getEsIndex();
        Request request = new Request("GET", String.format("/%s/_search",esIndex));
        request.setEntity(new NStringEntity(scriptContext.getScript().toString(), ContentType.APPLICATION_JSON));
        Response response = restClientTransport.restClient().performRequest(request);
        if(response.getStatusLine().getStatusCode() != 200){
            throw new DataOpenPlatformException("ES查询失败:" + response.toString());
        }
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSON.parseObject(responseBody);
        return jsonObject;
    }

    @Override
    public Map<String, Object> findOne(ScriptContext scriptContext) throws Exception {
        throw new RuntimeException("不支持的方法");
    }

    @Override
    public String transcoding(String param) {
        return param
                .replace("\\","\\\\")
                .replace("\"","\\\"")
                .replace("\'","\\\'");
    }

    @Override
    public void close() {

    }

    @Override
    public String getLimitSql(int page, int pageSize) {
        return null;
    }

    @Override
    public List<TableInfo> buildTableInfo() {
        return null;
    }
}
