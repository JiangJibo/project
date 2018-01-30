package com.bob.project.config.mvc.excelmapping.exception;

/**
 * 以异常的形式抛出错误信息的异常解析器
 *
 * @author wb-jjb318191
 * @create 2017-11-30 13:22
 */
public class ErrorThrowingExceptionResolver implements MappingExceptionResolver {

    @Override
    public boolean excelEditorMode() {
        return false;
    }

    @Override
    public boolean handleCellTypeMismatch(ExcelMappingException ex) throws Exception {
        throw new IllegalStateException(String.format("单元格[%s]异常,%s", (char)(ex.getColumnIndex() + 65) + "" + (ex.getRowIndex() + 1), ex.getMessage()));
    }

    @Override
    public boolean handleRowUniqueConflict(ExcelMappingException ex) throws Exception {
        throw new IllegalStateException("第" + (ex.getRowIndex() + 1) + "行与" + ex.getMessage() + "行存在数据重复的情况，可查看标题栏上的唯一列批注");
    }
}
