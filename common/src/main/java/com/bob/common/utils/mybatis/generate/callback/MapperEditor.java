package com.bob.common.utils.mybatis.generate.callback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bob.common.utils.mybatis.generate.constant.GenerateContextConfig;
import com.bob.common.utils.mybatis.generate.constant.MapperMethodMappings;
import com.bob.common.utils.mybatis.generate.plugin.DefaultGeneratorPlugin;
import com.bob.common.utils.mybatis.generate.utils.BaseMapper;
import com.bob.common.utils.mybatis.generate.utils.Metadata;
import org.apache.commons.text.StringSubstitutor;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static com.bob.common.utils.mybatis.generate.utils.MybatisGenerateFileEditor.getFile;
import static com.bob.common.utils.mybatis.generate.utils.MybatisGenerateFileEditor.readFile;
import static com.bob.common.utils.mybatis.generate.utils.MybatisGenerateFileEditor.writeFile;

/**
 * Mapper修改器, 修改方法名称, 修改Mapper.xml
 *
 * @author wb-jjb318191
 * @create 2018-08-01 9:40
 */
public class MapperEditor extends ProgressCallbackAdapter {

    private Set<Metadata> metadataSet;
    private static Map<String, String> methodNameMappings = new HashMap<>();

    private static final String QUERY_SQL_FRAGMENT_WITH_ALL_COLUMNS_NAME = "query_fragment_with_all_columns";

    static {
        for (MapperMethodMappings mapping : MapperMethodMappings.values()) {
            methodNameMappings.put(mapping.getOriginal(), mapping.getTarget());
        }
    }

    public MapperEditor(Set<String> modelPaths, Set<String> mapperPaths, Set<String> mapperXmlPaths) {
        Assert.notEmpty(modelPaths, "DO.java集合不能为空");
        Assert.notEmpty(mapperPaths, "Mapper.java接口路径集合不能为空");
        Assert.notEmpty(mapperXmlPaths, "Mapper.xml文件路径集合不能为空");
        this.metadataSet = Metadata.batchBuildMetadataSetWithXml(modelPaths, mapperPaths, mapperXmlPaths);
    }

    @Override
    public void done() {
        for (Metadata metadata : metadataSet) {
            editMethodName(metadata.mapperXmlPath, false);
            editMethodName(metadata.fullMapperPath, true);
            IntrospectedTable table = DefaultGeneratorPlugin.getIntrospectedTable(metadata.modelPath);
            // 在mapper里追加新方法
            if (!GenerateContextConfig.appendSuperMapper) {
                appendMapperMethod(metadata.fullMapperPath, metadata.modelPath);
            }
            buildSqlFragmentWithAllQueryColumns(table, metadata.mapperXmlPath);
            processListByQuerySQL(table, metadata.mapperXmlPath, metadata.modelPath);
            processCountByQuerySQL(table, metadata.mapperXmlPath, metadata.modelPath);
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

    /**
     * mapper.java里追加listWithQuery, countWithQuery方法
     */
    private void appendMapperMethod(String fullMapperPath, String modelPath) {
        String modelName = modelPath.substring(modelPath.lastIndexOf(".") + 1);
        String content = "\n"
            + "    /**\n"
            + "     * 多条件查询\n"
            + "     *\n"
            + "     * @param query\n"
            + "     * @return\n"
            + "     */\n"
            + "    List<" + modelName + "> listWithQuery(" + modelName + " query);\n"
            + "\n"
            + "    /**\n"
            + "     * 多条件查数量\n"
            + "     *\n"
            + "     * @param query\n"
            + "     * @return\n"
            + "     */\n"
            + "    int countWithQuery(" + modelName + " query);\n"
            + "\n";
        File file = new File(fullMapperPath);
        List<String> strings = readFile(file);
        strings.add(2, "import java.util.List;");
        strings.add(strings.size() - 1, content);
        writeFile(file, strings);
    }

    /**
     * 构建sql查询where片段
     *
     * @param table
     * @param mapperXmlPath
     */
    private void buildSqlFragmentWithAllQueryColumns(IntrospectedTable table, String mapperXmlPath) {
        StringBuilder sb = new StringBuilder("\r\n\t<sql id=\"").append(QUERY_SQL_FRAGMENT_WITH_ALL_COLUMNS_NAME).append("\">");
        sb.append(buildSqlFragmentWithColumns(table));
        sb.append("\t</sql>");
        File file = new File(mapperXmlPath);
        List<String> content = readFile(file);
        content.add(content.size() - 1, sb.toString());
        writeFile(file, content);
    }

    /**
     * 处理 {@link BaseMapper#listWithQuery(Object)} 对应的SQL
     *
     * @param table
     * @param mapperXmlPath
     */
    private void processListByQuerySQL(IntrospectedTable table, String mapperXmlPath, String modelPath) {

        StringBuilder sb = new StringBuilder("\r\n\t<select id=\"listWithQuery\" parameterType=\"").append(modelPath).append("\" resultMap=\"BaseResultMap\">");
        sb.append("\r\n");

        String tableName = table.getTableConfiguration().getTableName();
        sb.append("\t\tselect <include refid=\"Base_Column_List\"/> from ").append(tableName);
        sb.append("\r\n");

        sb.append("\t\t<where>\r\n");

        sb.append("\t\t\t<include refid=\"").append(QUERY_SQL_FRAGMENT_WITH_ALL_COLUMNS_NAME).append("\"/>");

        sb.append("\r\n\t\t</where>");
        sb.append("\r\n\t</select>");

        File file = new File(mapperXmlPath);
        List<String> content = readFile(file);
        content.add(content.size() - 1, sb.toString());
        writeFile(file, content);
    }

    /**
     * 处理 {@link BaseMapper#countWithQuery(Object)} 对应的SQL
     *
     * @param table
     * @param mapperXmlPath
     */
    private void processCountByQuerySQL(IntrospectedTable table, String mapperXmlPath, String modelPath) {
        StringBuilder sb = new StringBuilder("\r\n\t<select id=\"countWithQuery\" parameterType=\"").append(modelPath).append("\" resultType=\"int\">");
        sb.append("\r\n");

        String tableName = table.getTableConfiguration().getTableName();
        sb.append("\t\tselect count(1) from ").append(tableName);
        sb.append("\r\n");

        sb.append("\t\t<where>\r\n");

        sb.append("\t\t\t<include refid=\"").append(QUERY_SQL_FRAGMENT_WITH_ALL_COLUMNS_NAME).append("\"/>");
        sb.append("\r\n\t\t</where>");
        sb.append("\r\n\t</select>");

        File file = new File(mapperXmlPath);
        List<String> content = readFile(file);
        content.add(content.size() - 1, sb.toString());
        writeFile(file, content);
    }

    /**
     * 构建sql片段
     *
     * @param table
     * @return
     */
    private String buildSqlFragmentWithColumns(IntrospectedTable table) {
        StringBuilder sb = new StringBuilder();
        List<IntrospectedColumn> allColumns = table.getAllColumns();
        // 主键列
        IntrospectedColumn primaryKey = table.getPrimaryKeyColumns().get(0);
        for (IntrospectedColumn column : allColumns) {
            // 跳过主键
            if (primaryKey == column) {
                continue;
            }
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put("columnName", column.getActualColumnName());
            valueMap.put("property", column.getJavaProperty());
            valueMap.put("jdbcType", column.getJdbcTypeName());

            String template = "\r\n\t\t<if test=\"${property} != null\">\n\t\t\t${columnName} = #{${property},jdbcType=${jdbcType}},\n\t\t</if>";
            sb.append(new StringSubstitutor(valueMap).replace(template)).append("\r\n");
        }
        return sb.toString();
    }

}
