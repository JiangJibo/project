package com.bob.common.utils.mybatis.generate.callback;

import java.io.File;
import java.util.List;
import java.util.Set;

import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.getFile;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.insertImportLine;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.readFile;
import static com.alibaba.sec.yaxiangdi.mybatis.generate.utils.MybatisGenerateUtils.writeFile;

/**
 * lombok风格的Model转换器
 *
 * @author wb-jjb318191
 * @create 2019-05-14 14:40
 */
public class LombokStyleManager extends ProgressCallbackAdapter {

    private Set<String> modelPaths;

    public LombokStyleManager(Set<String> modelPaths) {
        this.modelPaths = modelPaths;
    }

    @Override
    public void done() {
        for (String path : modelPaths) {
            applyModelLombokStyle(path);
        }
    }

    /**
     * 将model转换为lombok风格
     *
     * @param modelPath
     */
    private void applyModelLombokStyle(String modelPath) {
        File model = getFile(modelPath);
        List<String> lines = readFile(model);
        insertImportLine(lines, "lombok.Data");
        insertAnnotationLine(lines);
        writeFile(model, deleteSetterAndGetter(lines));
    }

    /**
     * 插入注解行
     *
     * @param lines
     */
    private void insertAnnotationLine(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().startsWith("public class")) {
                lines.add(i, "@Data");
                return;
            }
        }
    }

    /**
     * 删除setter和getter
     *
     * @param lines
     */
    private List<String> deleteSetterAndGetter(List<String> lines) {
        int deleteStartLine = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().startsWith("public") && !line.contains("class")) {
                deleteStartLine = i;
                break;
            }
        }
        List<String> newLines = lines.subList(0, deleteStartLine);
        newLines.add("}");
        return newLines;
    }

}
