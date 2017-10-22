package com.bob.config.mvc.excelmapping.exception;

/**
 * 仅收集异常信息，不对Excel做标识处理
 *
 * @author dell-7359
 * @create 2017-10-22 14:53
 */
public class ErrorCombineResolver implements MappingExceptionResolver {

    @Override
    public boolean handleTypeMismatch(ExcelMappingException ex) throws Exception {
        return false;
    }

    @Override
    public boolean handleUniqueConflict(ExcelMappingException ex) throws Exception {
        return false;
    }
}
