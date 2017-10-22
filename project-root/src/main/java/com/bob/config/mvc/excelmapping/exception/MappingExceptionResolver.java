package com.bob.config.mvc.excelmapping.exception;

import org.springframework.validation.BindingResult;

/**
 * 映射异常处理器
 *
 * @author dell-7359
 * @create 2017-10-22 10:31
 */
public interface MappingExceptionResolver {

    /**
     * 是否需要在Excel上标识信息
     *
     * @return
     */
    default boolean excelMarkMode() {
        return false;
    }

    /**
     * 处理单元格数据类型与属性类型不匹配异常
     *
     * @param ex
     * @return 是否继续解析
     * @throws Exception
     */
    boolean handleTypeMismatch(ExcelMappingException ex) throws Exception;

    /**
     * 处理行唯一性冲突异常
     *
     * @param ex
     * @return 是否继续解析
     * @throws Exception
     */
    boolean handleUniqueConflict(ExcelMappingException ex) throws Exception;


}