package com.bob.intergration.concrete.elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bob.integrate.elasticsearch.ElasticsearchRestConfig;
import com.bob.intergration.config.TestContextConfig;
import com.github.maltalex.ineter.base.IPv4Address;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author JiangJibo
 * @create 2020-09-20 16:24
 */
@ContextConfiguration(classes = ElasticsearchRestConfig.class)
public class Ipv4IndexRequestTest extends TestContextConfig {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void indexIpv4Document() throws IOException {
        File txt = new File("C:\\Users\\JiangJibo\\Desktop\\ipv4.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        String line;

        int docNum = 0;
        BulkRequest bulkRequest = new BulkRequest();
        while ((line = reader.readLine()) != null) {
            int index = 0;
            IndexRequest request = new IndexRequest("ipv4");
            List<String> splits = Splitter.on(",").splitToList(line);
            Map<String, Object> record = new HashMap<>();
            record.put("start", IPv4Address.of(splits.get(index++)).toInt());
            record.put("end", IPv4Address.of(splits.get(index++)).toInt());
            record.put("country", splits.get(index++));
            record.put("province", splits.get(index++));
            record.put("city", splits.get(index++));
            record.put("region", splits.get(index++));
            record.put("isp", splits.get(index++));
            record.put("address",
                Joiner.on(" ").join(record.get("country").toString(), record.get("province"), record.get("city"), record.get("region"), record.get("isp")));

            request.source(record);
            bulkRequest.add(request);

            docNum++;
            if (docNum == 3000) {
                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                docNum = 0;
            }
        }
    }
}
