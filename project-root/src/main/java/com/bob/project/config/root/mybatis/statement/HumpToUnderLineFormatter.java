package com.bob.project.config.root.mybatis.statement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * 依据驼峰原则来将表的信息格式化为实体类的信息，在驼峰处改小写同时插入下划线
 *
 * @author wb-jjb318191
 * @create 2017-09-08 14:55
 */
@Component
public class HumpToUnderLineFormatter implements TableFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HumpToUnderLineFormatter.class);

    private static final Map<Class<?>, Map<Field, String>> FIELD_TO_COLUMN_MAPPINGS = new HashMap<Class<?>, Map<Field, String>>();

    private static final Map<Class, String> CLASS_TO_TABLE_MAPPING = new HashMap<Class, String>();

    private static final StringBuilder SB = new StringBuilder();

    private static final Object LOCK = new Object();

    @Override
    public String getColumnName(Field field) {
        Assert.notNull(field, "属性不能为空");
        Map<Field, String> mappings = FIELD_TO_COLUMN_MAPPINGS.get(field.getDeclaringClass());
        if (mappings == null) {
            synchronized (LOCK) {
                mappings = FIELD_TO_COLUMN_MAPPINGS.get(field.getDeclaringClass());
                if (mappings == null) {
                    mappings = buildMapping(field.getDeclaringClass());
                }
            }
        }
        return mappings.get(field);
    }

    @Override
    public String getKeyColumnName(Class<?> clazz) {
        Table table = checkClass(clazz);
        return getColumnName(ReflectionUtils.findField(clazz,table.key()));
    }

    @Override
    public String getKeyFiledName(Class<?> clazz) {
        Table table = checkClass(clazz);
        Field field = ReflectionUtils.findField(clazz,table.key());
        Assert.state(field != null,"@Table的key()指定的属性必须存在");
        return field.getName();
    }

    private Table checkClass(Class<?> clazz){
        Assert.isTrue(clazz != null , "与Table对应的Class不能为空");
        Table table = clazz.getAnnotation(Table.class);
        Assert.isTrue(table != null && StringUtils.hasText(table.key()),"["+clazz.getName()+"]必须标识@Table注解且key()不能为空");
        return table;
    }

    @Override
    public String getTableName(Class<?> clazz) {
        Assert.notNull(clazz, "类不能为空");
        Assert.isTrue(clazz.isAnnotationPresent(Table.class), "[" + clazz.getName() + "]类上必须含有@Table注解");
        String name = CLASS_TO_TABLE_MAPPING.get(clazz);
        if (name == null) {
            synchronized (LOCK) {
                name = CLASS_TO_TABLE_MAPPING.get(clazz);
                if (name == null) {
                    buildMapping(clazz);
                }
            }
        }
        return CLASS_TO_TABLE_MAPPING.get(clazz);
    }

    @Override
    public Map<Field, String> getFieldMappings(Class<?> clazz) {
        Assert.isTrue(clazz != null && clazz.isAnnotationPresent(Table.class), "与Table对应的Class不能为空且必须标识@Table注解");
        Map<Field, String> mappings = FIELD_TO_COLUMN_MAPPINGS.get(clazz);
        if (mappings == null) {
            synchronized (LOCK) {
                mappings = FIELD_TO_COLUMN_MAPPINGS.get(clazz);
                if (mappings == null) {
                    mappings = buildMapping(clazz);
                }
            }
        }
        return FIELD_TO_COLUMN_MAPPINGS.get(clazz);
    }

    /**
     * 创建实体到表映射
     *
     * @param clazz
     */
    private Map<Field, String> buildMapping(Class<?> clazz) {
        buildClassToTableMapping(clazz);
        Map<Field, String> mappings = new HashMap<Field, String>();
        FIELD_TO_COLUMN_MAPPINGS.put(clazz, mappings);
        buildFiledToColumnMapping(clazz, mappings);
        buildFiledToColumnMappingWithGetter(clazz, mappings);
        return mappings;
    }

    /**
     * 创建类名到表名的名称映射
     *
     * @param clazz
     */
    private void buildClassToTableMapping(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        Assert.notNull(table, "[" + clazz.getName() + "]类上必须有@Table注解");
        CLASS_TO_TABLE_MAPPING.put(clazz, StringUtils.hasText(table.value()) ? table.value() : doFormatWithHunmRule(clazz.getSimpleName()));
    }

    /**
     * 通过Filed建立属性名称到字段名称的映射
     *
     * @param clazz
     * @param mappings
     */
    private void buildFiledToColumnMapping(Class<?> clazz, Map<Field, String> mappings) {
        ReflectionUtils.doWithLocalFields(clazz, (field) -> {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        LOGGER.error("[{}]注解不适用于静态方法:[{}]", Column.class.toString(), field);
                        return;
                    }
                    mappings.put(field, StringUtils.hasText(column.value()) ? column.value() : doFormatWithHunmRule(field.getName()));
                }
            }
        );
    }

    /**
     * 通过getter()建立属性名称到字段名称的映射
     *
     * @param clazz
     * @param mappings
     */
    private void buildFiledToColumnMappingWithGetter(Class<?> clazz, Map<Field, String> mappings) {

        ReflectionUtils.doWithLocalMethods(clazz, (method) -> {
                Column column = method.getAnnotation(Column.class);
                if (column != null) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        LOGGER.warn("[{}]注解不适用于静态方法: [{}]", Column.class.toString(), method);
                        return;
                    }
                    if (!method.getName().startsWith("get") || method.getParameterTypes().length > 0) {
                        LOGGER.warn("[{}]注解只适用于getter方法,而非: [{}]方法", Column.class.toString(), method);
                        return;
                    }
                    String fieldName = BeanUtils.findPropertyForMethod(method).getName();
                    mappings.put(ReflectionUtils.findField(clazz, fieldName),
                        StringUtils.hasText(column.value()) ? column.value() : doFormatWithHunmRule(fieldName));
                }
            }
        );

    }

    /**
     * 依据驼峰原则格式化属性或者类名称,在驼峰处改小写同时前一位插入下划线,忽略首字母
     *
     * @param name
     * @return
     */
    private static String doFormatWithHunmRule(String name) {
        Assert.hasText(name, "属性或者类名称不能为空");
        SB.delete(0, SB.length());
        SB.append(toUpperCase(name.charAt(0)));
        for (int i = 1; i < name.length(); i++) {
            if (isUpperCase(name.charAt(i))) {
                SB.append("_");
            }
            SB.append(toUpperCase(name.charAt(i)));
        }
        return SB.toString();
    }

    /**
     * 将字符转换为大写
     *
     * @param ch
     * @return
     */
    private static char toUpperCase(char ch) {
        return Character.toUpperCase(ch);
    }

    /**
     * 判断是否为大写
     *
     * @param ch
     * @return
     */
    private static boolean isUpperCase(char ch) {
        return Character.isUpperCase(ch);
    }

    public static void main(String[] args) {
        TableFormatter formatter = new HumpToUnderLineFormatter();
        String tableName = formatter.getTableName(MybatisEntity.class);
        System.out.println(tableName);
    }

}
