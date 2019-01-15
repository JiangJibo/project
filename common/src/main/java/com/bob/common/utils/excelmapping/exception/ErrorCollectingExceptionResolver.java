package com.bob.common.utils.excelmapping.exception;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 异常信息合并处理器
 *
 * @author dell-7359
 * @create 2017-10-22 19:11
 */
public class ErrorCollectingExceptionResolver implements MappingExceptionResolver {

    private  LinkedHashMap<Integer, String> rowErrorMappings = new LinkedHashMap<Integer, String>(16);
    private Integer maxErrorSize = Integer.MAX_VALUE;

    public ErrorCollectingExceptionResolver() {
    }

    public ErrorCollectingExceptionResolver(Integer maxErrorSize) {
        this.maxErrorSize = maxErrorSize;
    }

    @Override
    public boolean excelEditorMode() {
        return false;
    }

    @Override
    public boolean handleCellTypeMismatch(ExcelMappingException ex) throws Exception {
        return combineErrorMsg(ex.getRowIndex(),
            String.format("单元格[%s]异常,%s", (char)(ex.getColumnIndex() + 65) + "" + (ex.getRowIndex() + 1), ex.getMessage()));
    }

    @Override
    public boolean handleRowUniqueConflict(ExcelMappingException ex) throws Exception {
        return combineErrorMsg(ex.getRowIndex(), "此行与" + ex.getMessage() + "行存在数据重复的情况，可查看标题栏上的唯一列批注");
    }

    /**
     * 获取组合好的错误信息以供前端展示
     *
     * @return
     */
    public String getCombinedMsg() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : rowErrorMappings.entrySet()) {
            sb.append(String.format("第%d行:%s\n", entry.getKey() + 1, entry.getValue()));
        }
        return sb.toString();
    }

    private boolean combineErrorMsg(int rowIndex, String msg) {
        if (!rowErrorMappings.containsKey(rowIndex)) {
            rowErrorMappings.put(rowIndex, msg);
        } else {
            rowErrorMappings.put(rowIndex, rowErrorMappings.get(rowIndex) + ";" + msg);
        }
        return rowErrorMappings.size() < maxErrorSize;
    }

    public LinkedHashMap<Integer, String> getRowErrorMappings() {
        return rowErrorMappings;
    }
}
