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
 * Controller生成器
 *
 * @author wb-jjb318191
 * @create 2020-07-27 15:11
 */
public class ControllerClassGenerator extends ProgressCallbackAdapter {

    private Set<Metadata> metadataSet;

    public ControllerClassGenerator(Set<String> modelPaths, Set<String> mapperPaths, Set<String> servicePaths,
                                    Set<String> controllerPaths) {
        this.metadataSet = Metadata.batchBuildMetadataSet(modelPaths, mapperPaths, null, servicePaths, controllerPaths);
    }

    @Override
    public void done() {
        metadataSet.forEach(metadata -> {
            try {
                generateControllerClass(metadata);
            } catch (IOException e) {

            }
        });
    }

    /**
     * 生成Controller
     *
     * @param metadata
     * @throws IOException
     */
    private void generateControllerClass(Metadata metadata) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("package", metadata.controllerPath.substring(0, metadata.controllerPath.lastIndexOf(".")));
        params.put("modelPath", metadata.modelPath);
        params.put("servicePath", metadata.servicePath);
        params.put("serviceInterface", metadata.servicePath.substring(metadata.servicePath.lastIndexOf(".") + 1));
        params.put("serviceField", ClassGenerateUtils.getShortNameAsProperty(params.get("serviceInterface")));
        params.put("controller", metadata.controllerPath.substring(metadata.controllerPath.lastIndexOf(".") + 1));
        params.put("primaryKey", ClassGenerateUtils.extractPrimaryKeyType(metadata.fullMapperPath));
        params.put("model", metadata.modelPath.substring(metadata.modelPath.lastIndexOf(".") + 1));
        params.put("author", params.put("author", ClassGenerateUtils.isWindows() ? System.getenv("USERNAME") : System.getenv("USER")));
        params.put("date", ClassGenerateUtils.formatNow());

        String serviceContent = new StringSubstitutor(params).replace(ClassGenerateUtils.extractTemplateContent("ControllerClassTemplate"));
        FileUtils.write(new File(metadata.fullControllerPath), serviceContent, Charset.forName("UTF-8"));
    }

}
