package com.bob.root.utils.excelmapping.exception;

/**
 * 对Excel内容编辑的异常处理器
 *
 * @author dell-7359
 * @create 2017-10-22 11:26
 */
public class ExcelEditorExceptionResolver implements MappingExceptionResolver {

    @Override
    public boolean excelEditorMode() {
        return true;
    }

    @Override
    public boolean handleCellTypeMismatch(ExcelMappingException ex) throws Exception {
        ex.getExcelMappingProcessor().markErrorPrompt(ex.getRowIndex(),ex.getColumnIndex(),ex.getMessage());
        return true;
    }

    @Override
    public boolean handleRowUniqueConflict(ExcelMappingException ex) throws Exception {
        ex.getExcelMappingProcessor().markErrorMsg(ex.getRowIndex(),ex.getMessage());
        return true;
    }
}
