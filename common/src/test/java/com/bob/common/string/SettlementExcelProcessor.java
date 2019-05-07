package com.bob.common.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.bob.common.utils.excelmapping.Excel;
import com.bob.common.utils.excelmapping.ExcelInstance;
import com.bob.common.utils.excelmapping.ExcelMappingProcessor;
import org.apache.commons.io.FileUtils;

import static com.bob.common.utils.excelmapping.exception.MappingExceptionResolver.THROWING_RESOLVER;

/**
 * @author wb-jjb318191
 * @create 2019-04-16 11:36
 */
public class SettlementExcelProcessor {

    public static void main(String[] args) throws Exception {
        ExcelMappingProcessor<SettlementRow> processor = new ExcelMappingProcessor<>(
            new Excel("C:\\Users\\wb-jjb318191\\Desktop\\荷兰问题订单.xlsx"), SettlementRow.class,THROWING_RESOLVER);
        processor.process();
        Collection<ExcelInstance<SettlementRow>> result =  processor.getCorrectResult();
        List<SettlementRow> rows = new ArrayList<>();
        result.forEach(instance -> rows.add(instance.getInstance()));
        File file = new File("C:\\Users\\wb-jjb318191\\Desktop\\问题订单.json");
        FileUtils.write(file, JSON.toJSONString(rows));
    }

}
