package com.bob.config.mvc.excelmapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.bob.config.mvc.excelmapping.exception.ExcelMappingException;
import com.bob.config.mvc.excelmapping.exception.MappingExceptionResolver;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.hssf.util.HSSFColor.WHITE;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.apache.poi.ss.usermodel.CellStyle.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.ClientAnchor.MOVE_AND_RESIZE;
import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;

/**
 * 基于注解的Excel解析工具类
 *
 * @author JiangJibo
 * @since 2016年5月19日 下午5:07:00
 */
public final class ExcelMappingProcessor<T extends PropertyInitializer<T>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelMappingProcessor.class);

    private boolean hasError = false;
    private ExcelColumn lastExcelColumn;
    private final static String ERROR_SPLIT_BR = "\n";

    //private final Excel excel;
    private final Excel excel;
    private final Class<T> clazz;
    private final String version;
    private final Integer sheetAt;
    private final Integer dataRow;
    private final Integer titleRow;
    private final String promptAuthor;
    private final CellStyle errorCellStyle;
    private final Drawing drawingPatriarch;
    private final ClientAnchor clientAnchor;
    private static final Map<Class<?>, LinkedHashMap<Field, ExcelColumn>> EXCEL_MAPPINGS = new ConcurrentHashMap<Class<?>, LinkedHashMap<Field, ExcelColumn>>();
    private static final Map<String, Converter<?, ?>> FIELD_CONVERTERS = new ConcurrentHashMap<String, Converter<?, ?>>();
    private final LinkedHashMap<String, ExcelColumn> fieldColumns;
    private final LinkedHashMap<String, ExcelColumn> keyFieldColumns;
    private final LinkedHashMap<String, ExcelInstance<T>> correctResult;

    private final MappingExceptionResolver exceptionResolver;

    private static final String EXCELCOLUMN_ANN_NAME = ExcelColumn.class.getSimpleName();

    /**
     * 构建Excel解析器<br>
     * Default promptAuthor is {@linkplain ExcelPromptAuthor}
     *
     * @param excel
     * @param clazz
     */
    public ExcelMappingProcessor(Excel excel, Class<T> clazz, MappingExceptionResolver exceptionResolver) {
        this(excel, clazz, exceptionResolver, ExcelPromptAuthor.WB_JJB);
    }

    /**
     * 构建Excel解析器
     *
     * @param excel
     * @param clazz
     * @param promptAuthor
     */
    public ExcelMappingProcessor(Excel excel, Class<T> clazz, MappingExceptionResolver exceptionResolver, ExcelPromptAuthor promptAuthor) {
        // 1.excel相关属性
        this.excel = excel;
        this.clazz = clazz;
        this.exceptionResolver = exceptionResolver;
        ExcelMapping excelMapping = clazz.getAnnotation(ExcelMapping.class);
        Assert.notNull(excelMapping, "解析Excel对象{" + clazz.getSimpleName() + "}未标识ExcelMapping注解，请联系系统维护人员！");
        this.sheetAt = excelMapping.sheetAt();
        this.dataRow = excelMapping.dataRow();
        this.titleRow = excelMapping.titleRow();
        // 2.初始化参数
        this.promptAuthor = promptAuthor.getAuthor();
        this.version = new SimpleDateFormat("HHmmss").format(new Date());
        this.fieldColumns = new LinkedHashMap<String, ExcelColumn>();
        this.keyFieldColumns = new LinkedHashMap<String, ExcelColumn>();
        this.correctResult = new LinkedHashMap<String, ExcelInstance<T>>();
        // 3. 创建批注框
        if (excel.isXLSX()) {
            this.clientAnchor = new XSSFClientAnchor(0, 0, 0, 0, (short)3, (short)3, (short)5, (short)6);
        } else {
            this.clientAnchor = new HSSFClientAnchor(0, 0, 0, 0, (short)3, (short)3, (short)5, (short)6);
        }
        this.clientAnchor.setAnchorType(MOVE_AND_RESIZE);
        this.drawingPatriarch = excel.getSheet().createDrawingPatriarch();
        // 4. 创建错误栏样式
        this.errorCellStyle = excel.createCellStyle();
        Font font = excel.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)10);
        font.setColor(RED.index);
        font.setBoldweight(BOLDWEIGHT_BOLD);
        this.errorCellStyle.setFont(font);
        this.errorCellStyle.setWrapText(true);
        this.errorCellStyle.setFillPattern(SOLID_FOREGROUND);
    }

    private ClientAnchor generateClientAnchor() {
        if (excel.isXLSX()) {
            return new XSSFClientAnchor(0, 0, 0, 0, (short)3, (short)3, (short)5, (short)6);
        } else {
            return new HSSFClientAnchor(0, 0, 0, 0, (short)3, (short)3, (short)5, (short)6);
        }
    }

    /**
     * 获取解析正确的结果集
     *
     * @return
     */
    public Collection<ExcelInstance<T>> getCorrectResult() {
        return correctResult.values();
    }

    /**
     * 获取属性列对应信息
     *
     * @return the fieldColumns
     */
    public LinkedHashMap<String, ExcelColumn> getFieldColumns() {
        return fieldColumns;
    }

    /**
     * 创建Excel到Model的映射
     */
    private void buildExcelMapping() {
        Map<Field, ExcelColumn> fieldColumns = new HashMap<Field, ExcelColumn>();
        //解析标识了@ExcelColumn注解的属性
        ReflectionUtils.doWithLocalFields(clazz, (field) -> {
            ExcelColumn column = field.getAnnotation(ExcelColumn.class);
            if (null == column) {
                return;
            } else if (Modifier.isStatic(field.getModifiers())) {
                LOGGER.warn("[{}]注解不适用于静态属性[{}]", EXCELCOLUMN_ANN_NAME, field.getName());
                return;
            }
            fieldColumns.put(field, column);
        });
        //解析标识了@ExcelColumn注解的getter()方法
        ReflectionUtils.doWithLocalMethods(clazz, (method) -> {
            ExcelColumn column = method.getAnnotation(ExcelColumn.class);
            if (null == column) {
                return;
            } else if (Modifier.isStatic(method.getModifiers())) {
                LOGGER.warn("[{}]注解不适用于静态方法[{}]", EXCELCOLUMN_ANN_NAME, method.getName());
                return;
            }
            if (!isGetter(method)) {
                LOGGER.warn("[{}]注解不适用于非getter方法[{}]", EXCELCOLUMN_ANN_NAME, method.getName());
                return;
            }
            Field field = getFieldFromGetter(method);
            if (fieldColumns.containsKey(field)) {
                LOGGER.warn("[{}]属性被重复解析，略过", field.getName());
                return;
            }
            fieldColumns.put(field, column);
        });
        //对属性集合映射做排序
        LinkedHashMap<Field, ExcelColumn> fieldMappings = new LinkedHashMap<Field, ExcelColumn>();
        List<ExcelColumn> values = new ArrayList<ExcelColumn>(fieldColumns.values());
        Collections.sort(values, new Comparator<ExcelColumn>() {
            @Override
            public int compare(ExcelColumn o1, ExcelColumn o2) {
                return o1.value().value - o2.value().value;
            }
        });
        for (ExcelColumn excelColumn : values) {
            for (Entry<Field, ExcelColumn> entry : fieldColumns.entrySet()) {
                if (entry.getValue() == excelColumn) {
                    fieldMappings.put(entry.getKey(), entry.getValue());
                    fieldColumns.remove(entry.getKey());
                    break;
                }
            }
        }
        EXCEL_MAPPINGS.put(clazz, fieldMappings);
    }

    /**
     * 判断一个函数是否是getter()方法
     *
     * @param method
     * @return
     */
    private boolean isGetter(Method method) {
        String methodName = method.getName();
        boolean getter = methodName.startsWith("get") && Character.isUpperCase(methodName.charAt(3));
        boolean isser = methodName.startsWith("is") && Character.isUpperCase(methodName.charAt(2));
        return (getter || isser) && method.getParameterCount() == 0;
    }

    /**
     * @param method
     * @return
     */
    private Field getFieldFromGetter(Method method) {
        String methodName = method.getName();
        String filedName;
        if (methodName.startsWith("get")) {
            filedName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4, methodName.length());
        } else {
            filedName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3, methodName.length());
        }
        return ReflectionUtils.findField(clazz, filedName);
    }

    /**
     * 获取当前类的映射集合
     *
     * @return
     */
    private LinkedHashMap<Field, ExcelColumn> getExcelMapping() {
        if (EXCEL_MAPPINGS.get(clazz) == null) {
            synchronized (clazz) {
                if (EXCEL_MAPPINGS.get(clazz) == null) {
                    buildExcelMapping();
                }
            }
        }
        return EXCEL_MAPPINGS.get(clazz);
    }

    /**
     * Excel解析
     *
     * @return hasError ? true : false;
     */
    public boolean process() throws Exception {
        // 循环每一行 循环每一列，获取每一个单元格的数值，通过注解设置到指定属性中
        final int physRow = excel.getSheetAt(sheetAt).getPhysicalNumberOfRows();
        Assert.isTrue(physRow > dataRow, String.format("解析Excel错误，Excel实际可读取的物理行数%d小于指定的数据行数%d", physRow, dataRow));
        LinkedHashMap<Field, ExcelColumn> fieldColumns = this.getExcelMapping();
        for (int i = dataRow; i < physRow; i++) {
            final int rowIndex = i;
            if (exceptionResolver.excelEditorMode()) {
                this.removeErrorMsg(rowIndex);
            }
            final T newInstance = BeanUtils.instantiate(clazz).initProperties();
            final StringBuilder keyBuilder = new StringBuilder();
            boolean hasRowError = false;
            //遍历Key Column
            for (Entry<Field, ExcelColumn> entry : fieldColumns.entrySet()) {
                Field field = entry.getKey();
                ExcelColumn excelColumn = entry.getValue();
                if (excelColumn.value().value + 1 == fieldColumns.size() && null == lastExcelColumn) {
                    lastExcelColumn = excelColumn;
                }
                ExcelColumn.Column column = excelColumn.value();
                Cell cell = excel.getCell(rowIndex, column.value);
                if (excelColumn.notNull()) {
                    Assert.notNull(cell, String.format("获取Excel单元格%d行%s列为空", rowIndex + 1, column.name));
                }
                Object value = null;
                try {
                    value = getCellValue(cell, field, excelColumn);
                } catch (Exception e) {
                    hasRowError = true;
                    if (!exceptionResolver.handleCellTypeMismatch(new ExcelMappingException(e.getMessage(), rowIndex, column.value, this))) {
                        LOGGER.warn("因类型不匹配中止解析解析Excel，当前解析到第[{}]行第[{}]列", rowIndex, column.value);
                        return false;
                    }
                    continue;
                }
                boolean isKey = excelColumn.key();
                if (rowIndex == dataRow && isKey) {
                    keyFieldColumns.put(field.getName(), excelColumn);
                }
                if (isKey) {
                    keyBuilder.append("[").append(field.getName()).append(":").append(value).append("]");
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, newInstance, value);
                // 1.4 parse current remove old cell prompt
                if (exceptionResolver.excelEditorMode()) {
                    removeErrorPrompt(cell);
                }
            }

            if (hasRowError) {
                this.setError();
                continue;
            }

            if (keyFieldColumns.isEmpty()) {
                correctResult.put(String.valueOf(rowIndex), new ExcelInstance<T>(rowIndex, newInstance));
                continue;
            }

            // 2.唯一性校验
            String key = keyBuilder.toString();
            if (correctResult.containsKey(key)) {
                this.setError();
                int dupRowIndex = correctResult.get(key).getRowIndex() + 1;
                String errorMsg = "此行与第" + dupRowIndex + "行的数据存在重复情况";
                if (!exceptionResolver.handleRowUniqueConflict(new ExcelMappingException(errorMsg, rowIndex, 0, this))) {
                    LOGGER.warn("因行唯一性冲突中止解析解析Excel，当前解析到第[{}]行", rowIndex);
                    return false;
                }
                continue;
            }
            correctResult.put(keyBuilder.toString(), new ExcelInstance<T>(rowIndex, newInstance));
        }
        // 在标题行中标记唯一键
        if (exceptionResolver.excelEditorMode()) {
            markKeyColumnsPrompt();
        }
        //
        return !hasError;
    }

    /**
     * 统一在当前行最后一列增加错误信息,默认添加一条错误信息
     *
     * @param rowIndex
     * @param errorMsg
     */
    public void markErrorMsg(int rowIndex, String errorMsg) {
        // 1.set style
        int lastColumnIndex = this.getExcelMapping().size();
        Cell cell = excel.getCell(rowIndex, lastColumnIndex);
        cell.setCellStyle(errorCellStyle);
        // 2.set message
        StringBuilder errorMsgBuilder = new StringBuilder();
        String existErrorMsg = cell.getStringCellValue();
        if (StringUtils.hasText(existErrorMsg)) {
            errorMsgBuilder.append(existErrorMsg).append(ERROR_SPLIT_BR);
        }
        errorMsgBuilder.append(errorMsg);
        cell.setCellValue(errorMsgBuilder.toString());
    }

    /**
     * 统一在当前行最后一列增加错误信息,添加一系列的错误信息
     *
     * @param rowIndex
     * @param errorMsgs
     */
    public void markErrorMsg(int rowIndex, List<String> errorMsgs) {
        StringBuilder builder = new StringBuilder();
        for (String errorMsg : errorMsgs) {
            builder.append(errorMsg).append(ERROR_SPLIT_BR);
        }
        this.markErrorMsg(rowIndex, builder.substring(0, builder.length() - 1).toString());
    }

    /**
     * 删除当前行最后一列的错误信息
     *
     * @param rowIndex
     */
    private void removeErrorMsg(int rowIndex) {
        int lastColumnIndex = this.getExcelMapping().size();
        Cell cell = excel.getCell(rowIndex, lastColumnIndex);
        cell.setCellValue("");
    }

    /**
     * 根据FiledName在对应的列上添加错误批注
     *
     * @param rowIndex
     * @param bindingResult 对象属性校验异常
     */
    public void markErrorPrompt(int rowIndex, BindingResult bindingResult) {
        boolean hasError = bindingResult.hasErrors();
        if (!hasError) {
            return;
        }
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String errorMsg = fieldError.getDefaultMessage();
            ExcelColumn excelColumn = fieldColumns.get(fieldError.getField());
            if (null == excelColumn) {
                this.markErrorMsg(rowIndex, errorMsg);
                continue;
            }
            this.markErrorPrompt(excel.getCell(rowIndex, excelColumn.value().value), errorMsg);
        }
    }

    /**
     * 在对应的列上添加错误批注
     *
     * @param rowIndex
     * @param colIndex
     * @param errorPrompt
     */
    public void markErrorPrompt(int rowIndex, int colIndex, String errorPrompt) {
        this.markErrorPrompt(excel.getCell(rowIndex, colIndex), errorPrompt);
    }

    /**
     * 在对应的列上添加错误批注
     *
     * @param cell
     * @param prompt 批注信息
     */
    public void markErrorPrompt(Cell cell, String prompt) {
        // 1.check delete
        StringBuilder promptBuilder = new StringBuilder();
        if (isMarkByPromptAuthor(cell)) {
            promptBuilder.append(getErrorPrompt(cell)).append(ERROR_SPLIT_BR);
        } else {
            this.removeErrorPrompt(cell);
        }
        // 2.add
        promptBuilder.append(prompt);
        String promptText = promptBuilder.toString();
        cell.setCellComment(this.createPromptComment(promptText));
        // 3.set cell background color:Red
        this.setBackgroundColor(cell, RED.index);
    }

    /**
     * 删除对应列上的错误提示
     *
     * @param cell
     */
    private void removeErrorPrompt(Cell cell) {
        if (!isMarkErrorPrompt(cell)) {
            return;
        }
        cell.removeCellComment();
        this.setBackgroundColor(cell, WHITE.index);
    }

    /**
     * 判断当前列是否添加批注
     *
     * @param cell
     * @return
     */
    private boolean isMarkErrorPrompt(Cell cell) {
        return null != cell.getCellComment();
    }

    /**
     * 校验当前列批注是否为当前作者
     *
     * @param cell
     * @return
     */
    private boolean isMarkByPromptAuthor(Cell cell) {
        if (!isMarkErrorPrompt(cell)) {
            return false;
        }
        String cellPromptAuthor = cell.getCellComment().getAuthor();
        return cellPromptAuthor.equals(promptAuthor);
    }

    /**
     * 获取当前列批注信息
     *
     * @param cell
     * @return
     */
    private String getErrorPrompt(Cell cell) {
        return cell.getCellComment().getString().getString();
    }

    /**
     * 设置列底色
     *
     * @param cell
     * @param color
     */
    private void setBackgroundColor(Cell cell, short color) {
        CellStyle oldStyle = cell.getCellStyle();
        oldStyle.setFillBackgroundColor(color);
    }

    /**
     * 标记标题行所有唯一键列批注提示
     *
     * @return
     */
    private void markKeyColumnsPrompt() {
        if (keyFieldColumns.isEmpty()) {
            return;
        }
        for (ExcelColumn excelColumn : keyFieldColumns.values()) {
            Cell cell = excel.getCell(titleRow, excelColumn.value().value);
            if (isMarkByPromptAuthor(cell)) {
                break;
            }
            // 2.add
            String keyPromptText = "此列为Excel数据的唯一键，各行此列数据不可重复";
            Comment promptComment = this.createPromptComment(keyPromptText);
            cell.setCellComment(promptComment);
        }
    }

    /**
     * 创建批注
     *
     * @param comment
     * @return
     */
    private Comment createPromptComment(String comment) {
        Comment promptComment = drawingPatriarch.createCellComment(generateClientAnchor());
        promptComment.setAuthor(promptAuthor + version);
        promptComment.setString(excel.createRichTextString(comment));
        return promptComment;
    }

    /**
     * 设置cell值
     *
     * @param cell
     * @param value
     */
    public void setCellValue(Cell cell, Object value) {
        this.setCellValue(cell.getRowIndex(), cell.getColumnIndex(), value);
    }

    /**
     * 设置cell值
     *
     * @param rowIndex
     * @param colIndex
     * @param value
     */
    public void setCellValue(int rowIndex, int colIndex, Object value) {
        if (null == value) {
            return;
        }
        if (value instanceof String) {
            excel.setCell(rowIndex, colIndex, (String)value);
        } else if (value instanceof Number) {
            excel.setCell(rowIndex, colIndex, (Number)value);
        } else if (value instanceof BigDecimal) {
            excel.setCell(rowIndex, colIndex, (BigDecimal)value);
        } else if (value instanceof Double) {
            excel.setCell(rowIndex, colIndex, (Double)value);
        } else if (value instanceof Long) {
            excel.setCell(rowIndex, colIndex, (long)value);
        } else if (value instanceof Integer) {
            excel.setCell(rowIndex, colIndex, (int)value);
        } else if (value instanceof Short) {
            excel.setCell(rowIndex, colIndex, (short)value);
        } else if (value instanceof Byte) {
            excel.setCell(rowIndex, colIndex, (byte)value);
        } else if (value instanceof Boolean) {
            excel.setCell(rowIndex, colIndex, (Boolean)value);
        } else if (value instanceof Date) {
            excel.setCell(rowIndex, colIndex, (Date)value);
        } else if (value instanceof Calendar) {
            excel.setCell(rowIndex, colIndex, (Calendar)value);
        } else {
            throw new IllegalArgumentException(String.format("设值%s错误，暂不支持[%s]类型", value.toString(), value.getClass().getSimpleName()));
        }
    }

    /**
     * 将数据填充到Excel文件中,使用每行数据生成的对象类型,需要指定数据起始行。
     *
     * @param excel
     * @param objs
     * @return
     */
    public Excel fillInObjects(Excel excel, List<?> objs, Integer dataRow) {
        Assert.notEmpty(objs, "查询明细数据不存在");
        Class<?> clazz = objs.get(0).getClass();
        int j = 0;
        for (int i = dataRow; i < objs.size() + dataRow; i++) {
            j++;
            excel.getCell(i, 0).setCellValue(j);
            for (Entry<Field, ExcelColumn> entry : this.getExcelMapping().entrySet()) {
                Object value = ReflectionUtils.getField(entry.getKey(), objs.get(i));
                if (null != value) {
                    int column = entry.getValue().value().value;
                    excel.getCell(i, column).setCellValue(value.toString());
                }

            }
        }
        return excel;
    }

    /**
     * 删除行
     *
     * @param rowIndexs
     */
    public void removeRow(List<Integer> rowIndexs) {
        Sheet sheet = excel.getSheetAt(sheetAt);
        for (Integer rowIndex : rowIndexs) {
            Row row = sheet.getRow(rowIndex);
            if (null == row) {
                return;
            }
            sheet.removeRow(row);
        }
        // 2.上移空行
        Integer lastRowIndex = sheet.getLastRowNum();
        for (; lastRowIndex > 0; lastRowIndex--) {
            Row row = sheet.getRow(lastRowIndex);
            if (null != row) {
                continue;
            }
            sheet.shiftRows(lastRowIndex + 1, lastRowIndex, -1);
        }
    }

    /**
     * 删除行
     *
     * @param rowIndex
     */
    public void removeRow(Integer rowIndex) {
        Sheet sheet = excel.getSheet();
        Row row = sheet.getRow(rowIndex);
        if (null == row) {
            return;
        }
        sheet.removeRow(row);
        // 2.上移空行
        sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), -1);
    }

    /**
     * 设置解析出错标记
     */
    private void setError() {
        if (!hasError) {
            hasError = true;
        }
    }

    /**
     * 根据对象属性类型解析ExcelColumn值
     *
     * @param cell
     * @param field
     * @param excelColumn
     * @return
     */
    private Object getCellValue(Cell cell, Field field, ExcelColumn excelColumn) {
        // 1.
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        if (this.containsConverter(fieldName)) {
            //使用转换器的源类型当做属性类型去获取单元格的值
            fieldType = this.getSourceClass(fieldName);
        }
        Object value = excel.getCellValue(cell);
        Object strValue = excel.getCellString(cell);
        if (null == value || null == strValue) {
            Assert.state(!excelColumn.key() || !excelColumn.notNull(), "解析{" + excelColumn.value().name + "}列错误，值为空");
            return value;
        }
        // 2.
        if (fieldType.isAssignableFrom(String.class)) {
            value = strValue;
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            value = excel.getCellInteger(cell, null);
            Assert.notNull(value, "解析{" + strValue + "}错误，值应为[整型]类型");
        } else if (fieldType.isAssignableFrom(Long.class)) {
            value = excel.getCellLong(cell, null);
            Assert.notNull(value, "解析{" + strValue + "}错误，值应为[长整型]类型");
        } else if (fieldType.isAssignableFrom(Boolean.class)) {
            value = excel.getCellBoolean(cell);
            Assert.notNull(value, "解析{" + strValue + "}错误，值应为[布尔]类型");
        } else if (fieldType.isAssignableFrom(Date.class)) {
            value = excel.getCellDate(cell);
            Assert.notNull(value, "解析{" + strValue + "}错误，值应为[日期]类型");
        } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
            value = excel.getCellDecimal(cell);
            Assert.notNull(value, "解析{" + strValue + "}错误，值应为[数值]类型");
        } else {
            throw new IllegalArgumentException("解析{" + strValue + "}错误，暂不支持[" + field.getType().getName() + "]类型");
        }
        if (this.containsConverter(fieldName)) {
            Converter converter = this.getConverter(fieldName);
            value = converter.convert(value);
        }
        return value;
    }

    /**
     * 注册指定类的指定属性转换器
     *
     * @param fieldName
     * @param converter
     */
    public void registerFieldConverter(String fieldName, Converter<?, ?> converter) {
        checkFieldConverter(fieldName, converter);
        String fieldPath = clazz.getName() + "." + fieldName;
        if (FIELD_CONVERTERS.containsKey(fieldPath)) {
            LOGGER.warn("{}的{}属性已存在相应的转换器", clazz.getName(), fieldName);
            return;
        }
        FIELD_CONVERTERS.put(fieldPath, converter);
    }

    /**
     * 覆盖已存在的属性转换器
     *
     * @param fieldName
     * @param converter
     */
    public void overrideFieldConverter(String fieldName, Converter<?, ?> converter) {
        checkFieldConverter(fieldName, converter);
        String fieldPath = clazz.getName() + "." + fieldName;
        if (!FIELD_CONVERTERS.containsKey(fieldPath)) {
            LOGGER.warn("{}的{}属性不存在相应的转换器", clazz.getName(), fieldName);
            return;
        }
        FIELD_CONVERTERS.put(fieldPath, converter);
    }

    /**
     * 情况指定类的所有属性转换器
     */
    public void clearTargetClassConverterCache() {
        for (Map.Entry<String, Converter<?, ?>> entry : FIELD_CONVERTERS.entrySet()) {
            if (entry.getKey().contains(clazz.getName())) {
                LOGGER.info("移除指定类{}的{}属性的转换器", clazz.getName(), entry.getKey());
                FIELD_CONVERTERS.remove(entry.getKey());
            }
        }
    }

    /**
     * 获取指定类指定属性的转换器
     *
     * @param fieldName
     * @return
     */
    private Converter<?, ?> getConverter(String fieldName) {
        return FIELD_CONVERTERS.get(clazz.getName() + "." + fieldName);
    }

    /**
     * 校验转换器和属性类型的一致性
     * 注册转换器时，请不要使用lambda表达式，这可能会造成泛型类型丢失
     *
     * @param fieldName
     * @param converter
     */
    private void checkFieldConverter(String fieldName, Converter<?, ?> converter) {
        Assert.notNull(converter, "属性转换器不能为空");
        Field field = ReflectionUtils.findField(clazz, fieldName);
        Assert.notNull(field, String.format("[%s]类的[%s]属性不存在", clazz.getName(), fieldName));
        Class<?> targetType = getTargetClass(converter);
        Assert.state(field.getType().isAssignableFrom(targetType),
            String.format("[%s]属性的类型[%s]和转换器的目标类型[%s]不匹配", fieldName, field.getType().getName(), targetType.getName()));
    }

    /**
     * 获取目标类型
     *
     * @param fieldName
     * @return
     */
    private Class<?> getTargetClass(String fieldName) {
        Converter<?, ?> converter = this.getConverter(fieldName);
        return converter == null ? null : getGenericType(converter, 1);
    }

    /**
     * @param converter
     * @return
     */
    private Class<?> getTargetClass(Converter<?, ?> converter) {
        return this.getGenericType(converter, 1);
    }

    /**
     * 获取源类型
     *
     * @param fieldName
     * @return
     */
    private Class<?> getSourceClass(String fieldName) {
        Converter<?, ?> converter = this.getConverter(fieldName);
        return converter == null ? null : getGenericType(converter, 0);
    }

    /**
     * 获取指定位置的泛型
     *
     * @param converter
     * @param index
     * @return
     */
    private Class<?> getGenericType(Converter<?, ?> converter, int index) {
        return ResolvableType.forClass(Converter.class, converter.getClass()).resolveGeneric(index);
        //return GenericTypeResolver.resolveTypeArguments(converter.getClass(), Converter.class)[index];
    }

    /**
     * 是否含有指定属性的转换器
     *
     * @param fieldName
     * @return
     */
    private boolean containsConverter(String fieldName) {
        return FIELD_CONVERTERS.containsKey(clazz.getName() + "." + fieldName);
    }

}
