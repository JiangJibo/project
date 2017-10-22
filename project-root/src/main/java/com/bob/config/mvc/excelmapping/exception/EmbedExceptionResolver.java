package com.bob.config.mvc.excelmapping.exception;

/**
 * 内嵌式的异常处理器
 *
 * @author dell-7359
 * @create 2017-10-22 11:26
 */
public class EmbedExceptionResolver implements MappingExceptionResolver {

    @Override
    public boolean excelMarkMode() {
        return true;
    }

    @Override
    public boolean handleTypeMismatch(ExcelMappingException ex) throws Exception {
        ex.getExcelMappingProcessor().markErrorPrompt(ex.getRowIndex(),ex.getColumnIndex(),ex.getMessage());
        return true;
    }

    @Override
    public boolean handleUniqueConflict(ExcelMappingException ex) throws Exception {
        ex.getExcelMappingProcessor().markErrorMsg(ex.getRowIndex(),ex.getMessage());
        return true;
    }
}
