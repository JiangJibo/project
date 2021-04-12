package com.bob.common.utils.excelmapping;

import com.alibaba.sec.yaxiangdi.excel.mapping.ExcelColumn.Column;
import com.alibaba.sec.yaxiangdi.excel.mapping.exception.ErrorCollectingExceptionResolver;
import com.alibaba.sec.yaxiangdi.excel.mapping.exception.ErrorThrowingExceptionResolver;
import com.alibaba.sec.yaxiangdi.excel.mapping.exception.ExcelEditorExceptionResolver;
import com.alibaba.sec.yaxiangdi.excel.mapping.exception.MappingExceptionResolver;
import com.alibaba.sec.yaxiangdi.excel.mapping.transform.FieldConverter;
import com.alibaba.sec.yaxiangdi.excel.mapping.transform.FieldFormatter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.alibaba.sec.yaxiangdi.excel.mapping.exception.MappingExceptionResolver.THROWING_RESOLVER;

/**
 * Excel解析工具使用简介
 *
 * 1. 解析Excel数据：
 * 定义解析器: {@link ExcelMappingProcessor#ExcelMappingProcessor(Excel, Class, MappingExceptionResolver)}
 *
 * 定义解析出错的处理策略:
 * {@link ErrorThrowingExceptionResolver}
 * {@link ExcelEditorExceptionResolver}
 * {@link ErrorCollectingExceptionResolver}
 *
 * 执行解析：{@link ExcelMappingProcessor#process()}
 *
 * 2. 将数据写入EXCEL , 详情见
 * {@link ExcelMappingProcessor#populateData(Excel, List, Integer, FieldFormatter[])}
 * {@link ExcelMappingProcessor#populateData(File, List, Integer, FieldFormatter[])}
 *
 * @author wb-jjb318191
 * @create 2019-09-02 14:07
 */
@Slf4j
public class UsageIntroduction {

    public static void main(String[] args) throws Exception {
        // 构建Excel可以从本地文件, 也可以通过InputStream
        Excel excel = new Excel(new File(""));
        ExcelMappingProcessor<ExcelModel> processor = new ExcelMappingProcessor(excel, ExcelModel.class,
            THROWING_RESOLVER);
        // 注册转换器,将Excel单元格数据转换成指定类型的属性
        processor.registerFieldConverter(new FieldConverter<String, Date>() {
            @Override
            public boolean support(Field field) {
                return "time".equals(field.getName());
            }

            @Override
            public Date convert(String s) throws ParseException {
                return new SimpleDateFormat("yyyy-MM-dd").parse(s);
            }
        });
        // 如果异常处理器是：ErrorThrowingExceptionResolver, 则解析失败会抛出异常
        // 如果异常处理器是：ErrorCollectingExceptionResolver，通过此方法的返回值来判断解析过程是否有错误, 然后从异常解析器里读取错误信息
        // 如果异常处理器是: ExcelEditorExceptionResolver, 不同的Excel解析依赖会有细微差别, 此处理类可能有些不兼容
        processor.process();
        Collection<ExcelInstance<ExcelModel>> results = processor.getCorrectResult();
    }

    @Data
    @ExcelMapping(titleRow = 0, dataRow = 1)
    public static class ExcelModel implements PropertyInitializer<ExcelModel> {

        @ExcelColumn(value = Column.A, key = true)
        private Integer id;

        @ExcelColumn(value = Column.B)
        private String userName;

        @ExcelColumn(value = Column.C)
        private String password;

        @ExcelColumn(value = Column.D)
        private Date time;

        @Override
        public ExcelModel initProperties() {
            return new ExcelModel();
        }
    }

}
