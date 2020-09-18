package com.bob.common.utils.mybatis.generate.callback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.bob.common.utils.mybatis.generate.utils.ClassGenerateUtils;
import com.bob.common.utils.mybatis.generate.utils.Metadata;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 * Service及Impl生成器
 *
 * @author wb-jjb318191
 * @create 2020-07-27 15:10
 */
public class ServiceClassGenerator extends ProgressCallbackAdapter {

    private Set<Metadata> metadataSet;

    public ServiceClassGenerator(Set<String> modelPaths, Set<String> mapperPaths, Set<String> servicePaths) {
        this.metadataSet = Metadata.batchBuildMetadataSet(modelPaths, mapperPaths, servicePaths);
    }

    @Override
    public void done() {
        metadataSet.forEach(metadata -> {
            try {
                generateService(metadata);
                generateServiceImpl(metadata);
            } catch (IOException e) {
                System.err.println(e.getStackTrace());
            }
        });
    }

    /**
     * 生成Service接口
     *
     * @param metadata
     * @throws IOException
     */
    private void generateService(Metadata metadata) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("package", metadata.servicePath.substring(0, metadata.servicePath.lastIndexOf(".")));
        params.put("modelPath", metadata.modelPath);
        params.put("serviceInterface", metadata.servicePath.substring(metadata.servicePath.lastIndexOf(".") + 1));
        params.put("primaryKey", ClassGenerateUtils.extractPrimaryKeyType(metadata.fullMapperPath));
        params.put("model", metadata.modelPath.substring(metadata.modelPath.lastIndexOf(".") + 1));
        params.put("author", ClassGenerateUtils.isWindows() ? System.getenv("USERNAME") : System.getenv("USER"));
        params.put("date", ClassGenerateUtils.formatNow());

        String serviceContent = new StringSubstitutor(params).replace(ClassGenerateUtils.extractTemplateContent("ServiceInterfaceTemplate"));
        FileUtils.write(new File(metadata.fullServicePath), serviceContent, Charset.forName("UTF-8"));
    }

    /**
     * 生成ServiceImpl
     *
     * @param metadata
     * @throws IOException
     */
    private void generateServiceImpl(Metadata metadata) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("package", metadata.serviceImplPath.substring(0, metadata.serviceImplPath.lastIndexOf(".")));
        params.put("modelPath", metadata.modelPath);
        params.put("mapperPath", metadata.mapperPath);
        params.put("serviceInterfacePath", metadata.servicePath);
        params.put("service", metadata.servicePath.substring(metadata.servicePath.lastIndexOf(".") + 1));
        params.put("mapper", metadata.mapperPath.substring(metadata.mapperPath.lastIndexOf(".") + 1));
        params.put("mapperField", ClassGenerateUtils.getShortNameAsProperty(params.get("mapper")));
        params.put("primaryKey", ClassGenerateUtils.extractPrimaryKeyType(metadata.fullMapperPath));
        params.put("model", metadata.modelPath.substring(metadata.modelPath.lastIndexOf(".") + 1));
        params.put("author", System.getenv("USERNAME"));
        params.put("date", ClassGenerateUtils.formatNow());

        String serviceContent = new StringSubstitutor(params).replace(ClassGenerateUtils.extractTemplateContent("ServiceInterfaceImplTemplate"));
        FileUtils.write(new File(metadata.fullServiceImplPath), serviceContent, Charset.forName("UTF-8"));

    }

}
