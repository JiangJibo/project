package com.bob.common.utils.mybatis.generate.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.CollectionUtils;

/**
 * 表生成时的元数据
 *
 * @author wb-jjb318191
 * @create 2020-08-18 11:16
 */
public class Metadata {

    public String modelPath;

    public String mapperPath;
    public String fullMapperPath;
    public String mapperXmlPath;

    public String servicePath;
    public String fullServicePath;

    public String serviceImplPath;
    public String fullServiceImplPath;

    public String controllerPath;
    public String fullControllerPath;

    public static Set<Metadata> batchBuildMetadataSetWithXml(Set<String> modelPaths, Set<String> mapperPaths, Set<String> mapperXmlPaths) {
        return batchBuildMetadataSet(modelPaths, mapperPaths, mapperXmlPaths, null, null);
    }

    public static Set<Metadata> batchBuildMetadataSet(Set<String> modelPaths, Set<String> mapperPaths, Set<String> servicePaths) {
        return batchBuildMetadataSet(modelPaths, mapperPaths, null, servicePaths, null);
    }

    /**
     * 批量构建元数据信息
     *
     * @param modelPaths
     * @param mapperPaths
     * @param servicePaths
     * @return
     */
    public static Set<Metadata> batchBuildMetadataSet(Set<String> modelPaths, Set<String> mapperPaths, Set<String> mapperXmlPaths,
                                                      Set<String> servicePaths, Set<String> controllerPaths) {
        Set<Metadata> metadataList = new HashSet<>();
        modelPaths.stream().forEach(path -> {

            Metadata metadata = new Metadata();
            metadataList.add(metadata);

            metadata.modelPath = ClassGenerateUtils.extractJavaPath(path);

            String model = path.substring(path.lastIndexOf("/") + 1);
            if (model.endsWith("DO.java")) {
                model = model.substring(0, model.indexOf("DO.java"));
            } else {
                model = model.substring(0, model.indexOf(".java"));
            }

            String mapperPath = ClassGenerateUtils.filterElement(mapperPaths, model + "Mapper");
            metadata.mapperPath = ClassGenerateUtils.extractJavaPath(mapperPath);
            metadata.fullMapperPath = mapperPath;

            if(!CollectionUtils.isEmpty(mapperXmlPaths)){
                metadata.mapperXmlPath = ClassGenerateUtils.filterElement(mapperXmlPaths, model + "Mapper");
            }

            if (CollectionUtils.isEmpty(servicePaths)) {
                return;
            }

            String servicePath = ClassGenerateUtils.filterElement(servicePaths, model + "Service");
            metadata.fullServicePath = servicePath;
            metadata.servicePath = ClassGenerateUtils.extractJavaPath(servicePath);

            String fullServiceImplPath = servicePath.substring(0, servicePath.lastIndexOf("/")) + "/impl/"
                + metadata.servicePath.substring(metadata.servicePath.lastIndexOf(".") + 1) + "Impl.java";
            if (fullServiceImplPath.startsWith("/") && ClassGenerateUtils.isWindows()) {
                fullServiceImplPath = fullServiceImplPath.substring(fullServiceImplPath.indexOf("/") + 1);
            }
            metadata.fullServiceImplPath = fullServiceImplPath;
            metadata.serviceImplPath = ClassGenerateUtils.extractJavaPath(metadata.fullServiceImplPath);

            if (CollectionUtils.isEmpty(controllerPaths)) {
                return;
            }

            String controllerPath = ClassGenerateUtils.filterElement(controllerPaths, model + "Controller");
            metadata.controllerPath = ClassGenerateUtils.extractJavaPath(controllerPath);
            metadata.fullControllerPath = controllerPath;

        });
        return metadataList;
    }
}
