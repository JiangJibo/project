package com.bob.common.utils.mybatis.generate.callback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.util.ClassUtils;

import static com.alibaba.sec.yaxiangdi.mybatis.generate.constant.GenerateContextConfig.appendSuperMapper;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.constant.GenerateContextConfig.appendSuperModel;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.constant.GenerateContextConfig.superMapperName;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.constant.GenerateContextConfig.superModelName;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.getFile;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.insertImportLine;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.readFile;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.writeFile;

/**
 * 基础Mapper，基础Model 设置类
 *
 * @author Administrator
 * @create 2018-03-29 22:47
 */
public class SuperClassAppender extends ProgressCallbackAdapter {

    private Set<String> modelPaths;
    private Set<String> interfacePaths;

    public SuperClassAppender(Set<String> modelPaths, Set<String> interfacePaths) {
        this.modelPaths = modelPaths;
        this.interfacePaths = interfacePaths;
        if (appendSuperModel && !isClassExists(superModelName)) {
            throw new IllegalStateException(String.format("[%s]不存在", superModelName));
        }
        if (appendSuperMapper && !isClassExists(superMapperName)) {
            throw new IllegalStateException(String.format("[%s]不存在", superMapperName));
        }
    }

    @Override
    public void done() {
        if(appendSuperModel){
            for (String path : modelPaths) {
                appendSuperModel(path);
            }
        }
        if(appendSuperMapper){
            for (String path : interfacePaths) {
                appendSuperMapper(path);
            }
        }
    }

    /**
     * 向指定的Java文件追加父类
     *
     * @param javaPath
     */
    private void appendSuperModel(String javaPath) {
        File model = getFile(javaPath);
        List<String> content = readFile(model);
        insertImportLine(content, superModelName);
        insertSuperModel(content);
        writeFile(model, content);
    }

    /**
     * 向指定的Mapper接口追加父接口
     *
     * @param mapperPath
     */
    private void appendSuperMapper(String mapperPath) {
        File mapper = getFile(mapperPath);
        List<String> content = readFile(mapper);
        insertImportLine(content, superMapperName);
        writeFile(mapper, insertSuperMapper(content));
    }


    /**
     * 插入 extends Paging
     *
     * @param content
     */
    private void insertSuperModel(List<String> content) {
        int classLineIndex = inspectClassLineIndex(content);
        String insertWord = "extends " + superModelName.substring(superModelName.lastIndexOf(".") + 1);
        String newClassLine = content.get(classLineIndex).replace("{", insertWord + " {");
        content.set(classLineIndex, newClassLine);
    }

    /**
     * 插入 extends BaseMapper<Key,Target>
     *
     * @param content
     */
    private List<String> insertSuperMapper(List<String> content) {
        int classLineIndex = inspectClassLineIndex(content);
        String key = getTypeString(content, "deleteByPrimaryKey");
        key = "Integer";
        String target = getTypeString(content, "insertSelective");
        String insertWords = "extends " + superMapperName.substring(superMapperName.lastIndexOf(".") + 1) + "<" + key + "," + target + ">";
        String newClassLine = content.get(classLineIndex).replace("{", insertWords + " {");
        content = content.subList(0, classLineIndex);
        appendMapperComments(content);
        content.add(newClassLine);
        content.add("}");
        return content;
    }

    /**
     * 为Mapper接口添加注释
     *
     * @param content
     */
    private void appendMapperComments(List<String> content) {
        StringBuffer sb = new StringBuffer();
        String newLineWord = System.getProperty("line.separator");
        String dateLine = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        sb.append("/**").append(newLineWord)
            .append(" * @author " + System.getenv("USERNAME")).append(newLineWord)
            .append(" * @create " + dateLine).append(newLineWord)
            .append(" */");
        content.add(sb.toString());
    }

    /**
     * 获取Mapper的Key,Target类型的字符串
     *
     * @param content
     * @param keyword
     * @return
     */
    private String getTypeString(List<String> content, String keyword) {
        for (String line : content) {
            if (line.contains(keyword)) {
                String argBody = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                return argBody.split(" ")[0];
            }
        }
        return null;
    }

    /**
     * 获取类定义行
     *
     * @param content
     * @return
     */
    private int inspectClassLineIndex(List<String> content) {
        int classLineIndex = 0;
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).startsWith("public")) {
                classLineIndex = i;
                break;
            }
        }
        return classLineIndex;
    }

    /**
     * @param className
     */
    private boolean isClassExists(String className) {
        return ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
