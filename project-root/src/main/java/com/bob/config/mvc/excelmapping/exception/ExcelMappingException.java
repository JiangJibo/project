package com.bob.config.mvc.excelmapping.exception;

import java.io.Serializable;

import com.bob.config.mvc.excelmapping.ExcelMappingProcessor;

/**
 * Excel映射异常
 *
 * @author JiangJibo
 * @since 2017年3月21日 下午6:56:39
 */
public final class ExcelMappingException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -1582949558953456846L;

    private int rowIndex;
    private int columnIndex;
    private Exception original;
    private ExcelMappingProcessor excelMappingProcessor;

    public ExcelMappingException(String msg,int rowIndex, int colIndex, ExcelMappingProcessor excelMappingProcessor) {
        super(msg);
        this.rowIndex = rowIndex;
        this.columnIndex = colIndex;
        this.original = original;
        this.excelMappingProcessor = excelMappingProcessor;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Exception getOriginal() {
        return original;
    }

    public void setOriginal(Exception original) {
        this.original = original;
    }

    public ExcelMappingProcessor getExcelMappingProcessor() {
        return excelMappingProcessor;
    }

    public void setExcelMappingProcessor(ExcelMappingProcessor excelMappingProcessor) {
        this.excelMappingProcessor = excelMappingProcessor;
    }
}
