package com.bob.common.utils.mybatis.generate.callback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.sec.yaxiangdi.mybatis.generate.constant.MapperMethodMappings;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.getFile;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.readFile;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.writeFile;

/**
 * Mapper方法名称修改器
 *
 * @author wb-jjb318191
 * @create 2018-08-01 9:40
 */
public class MapperMethodEditor extends ProgressCallbackAdapter {

    private Set<String> mapperPaths;
    private Set<String> interfacePaths;
    private static Map<String, String> methodNameMappings = new HashMap<>();

    static {
        for (MapperMethodMappings mapping : MapperMethodMappings.values()) {
            methodNameMappings.put(mapping.getOriginal(), mapping.getTarget());
        }
    }

    public MapperMethodEditor(Set<String> interfacePaths, Set<String> mapperPaths) {
        Assert.notEmpty(mapperPaths, "Mapper.xml文件路径集合不能为空");
        this.mapperPaths = mapperPaths;
        Assert.notEmpty(interfacePaths, "Mapper.java接口路径集合不能为空");
        this.interfacePaths = interfacePaths;
    }

    @Override
    public void done() {
        for (String path : mapperPaths) {
            editMethodName(path, false);
        }
        for (String path : interfacePaths) {
            editMethodName(path, true);
        }
    }

    /**
     * 编辑Mapper接口方法
     *
     * @param path
     */
    private void editMethodName(String path, boolean isInterface) {
        File file = getFile(path);
        List<String> content = readFile(file);
        boolean edited = editContent(content, methodNameMappings, isInterface);
        if (edited) {
            writeFile(file, content);
        }
    }

    /**
     * 编辑Java接口方法名称
     *
     * @param content
     * @param mappings
     * @return 是否修改过
     */
    private boolean editContent(List<String> content, Map<String, String> mappings, boolean isInterface) {
        boolean edited = false;
        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);
            for (Entry<String, String> entry : mappings.entrySet()) {
                if (!StringUtils.hasText(entry.getValue())) {
                    continue;
                }
                boolean javaLineMatch = isInterface && javaLineMatch(line, entry.getKey());
                boolean mapperLineMatch = !isInterface && mapperLineMatch(line, entry.getKey());
                if (javaLineMatch || mapperLineMatch) {
                    edited = true;
                    content.set(i, StringUtils.replace(line, entry.getKey(), entry.getValue()));
                }
            }
        }
        return edited;
    }

    /**
     * @param line
     * @param rawName
     * @return
     */
    private boolean javaLineMatch(String line, String rawName) {
        int index = line.indexOf(rawName);
        return index > 0 && line.charAt(index + rawName.length()) == '(';
    }

    /**
     * @param line
     * @param rawName
     * @return
     */
    private boolean mapperLineMatch(String line, String rawName) {
        return line.contains("\"" + rawName + "\"");
    }

}
