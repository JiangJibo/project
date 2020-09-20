package com.bob.integrate.elasticsearch;

import java.util.Iterator;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wb-jjb318191
 * @create 2019-07-16 14:27
 */
@Configuration
public class ElasticsearchRestConfig {

    @Bean
    public RestHighLevelClient elasticsearchHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));
        return client;
    }

    @Bean
    public RestClient elasticsearchRestClient() {
        // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是ip,参数2是HTTP端口，参数3是通信协议
        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        // 最后配置好的clientBuilder再build一下即可得到真正的Client
        return clientBuilder.build();
    }

    private void setDefaultHeaders(RestClientBuilder clientBuilder) {
        // 设置请求头，每个请求都会带上这个请求头
        Header[] defaultHeaders = {new BasicHeader("header", "value")};
        clientBuilder.setDefaultHeaders(defaultHeaders);
    }

    private void setFailureListener(RestClientBuilder clientBuilder) {
        // 设置监听器，每次节点失败都可以监听到，可以作额外处理
        clientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                System.out.println(node.getName() + "==节点失败了");
            }
        });
    }

    private void setNodeSelector0(RestClientBuilder clientBuilder) {
        //配置节点选择器，客户端以循环方式将每个请求发送到每一个配置的节点上，
        //发送请求的节点，用于过滤客户端，将请求发送到这些客户端节点，默认向每个配置节点发送，
        //这个配置通常是用户在启用嗅探时向专用主节点发送请求（即只有专用的主节点应该被HTTP请求命中）
        clientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);

    }

    private void setNodeSelector1(RestClientBuilder clientBuilder) {
        // 进行详细的配置
        // 设置分配感知节点选择器，允许选择本地机架中的节点（如果有），否则转到任何机架中的任何其他节点。
        clientBuilder.setNodeSelector(nodes -> {
            boolean foundOne = false;
            for (Node node : nodes) {
                String rackId = node.getAttributes().get("rack_id").get(0);
                if ("rack_one".equals(rackId)) {
                    foundOne = true;
                    break;
                }
            }
            if (foundOne) {
                Iterator<Node> nodesIt = nodes.iterator();
                while (nodesIt.hasNext()) {
                    Node node = nodesIt.next();
                    String rackId = node.getAttributes().get("rack_id").get(0);
                    if ("rack_one".equals(rackId) == false) {
                        nodesIt.remove();
                    }
                }
            }
        });
    }

    private void setHttpClientConfigCallback(RestClientBuilder clientBuilder) {
        //配置异步请求的线程数量，Apache Http Async Client默认启动一个调度程序线程，以及由连接管理器使用的许多工作线程
        //（与本地检测到的处理器数量一样多，取决于Runtime.getRuntime().availableProcessors()返回的数量）。线程数可以修改如下,
        //这里是修改为1个线程，即默认情况
        clientBuilder.setHttpClientConfigCallback(
            httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultIOReactorConfig(
                IOReactorConfig.custom().setIoThreadCount(1).build()
            ));
    }

    private void setRequestConfigCallback(RestClientBuilder clientBuilder) {
        // 配置请求超时，将连接超时（默认为1秒）和套接字超时（默认为30秒）增加，
        // 这里配置完应该相应地调整最大重试超时（默认为30秒），即上面的setMaxRetryTimeoutMillis，一般于最大的那个值一致即60000
        clientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            // 连接5秒超时，套接字连接60s超时
            return requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000);
        });

    }

}
