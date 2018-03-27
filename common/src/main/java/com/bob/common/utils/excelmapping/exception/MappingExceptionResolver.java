package com.bob.common.utils.excelmapping.exception;

/**
 * 映射异常处理器
 *
 * @author dell-7359
 * @create 2017-10-22 10:31
 */
public interface MappingExceptionResolver {

    /**
     * 是否在Excel上直接编辑错误信息
     *
     * @return
     */
    default boolean excelEditorMode() {
        return false;
    }

    /**
     * 处理单元格数据类型与属性类型不匹配异常
     *
     * @param ex
     * @return 是否继续解析
     * @throws Exception
     */
    boolean handleCellTypeMismatch(ExcelMappingException ex) throws Exception;

    /**
     * 处理行唯一性冲突异常
     *
     * @param ex
     * @return 是否继续解析
     * @throws Exception
     */
    boolean handleRowUniqueConflict(ExcelMappingException ex) throws Exception;


}