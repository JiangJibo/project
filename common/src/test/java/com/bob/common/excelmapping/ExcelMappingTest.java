package com.bob.common.excelmapping;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.bob.common.utils.excelmapping.Excel;
import com.bob.common.utils.excelmapping.ExcelInstance;
import com.bob.common.utils.excelmapping.ExcelMappingProcessor;
import com.bob.common.utils.excelmapping.exception.ErrorCollectingExceptionResolver;
import com.bob.common.utils.excelmapping.exception.ErrorThrowingExceptionResolver;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

/**
 * Excel解析工具类测试
 *
 * @author wb-jjb318191
 * @create 2017-09-26 16:56
 */
public class ExcelMappingTest {

    private ExcelMappingProcessor<ExcelModel> processor;

    @Test
    public void testGetCell() throws IOException {
        Excel excel = new Excel("C:\\Users\\dell-7359\\Desktop\\Excel原始数据.xlsx");
        Row row = excel.getRow(1);
        row = excel.getRow(2);
        Cell cell = excel.getCell(1, 0);
        cell = excel.getCell(2, 0);
        System.out.println(cell.getNumericCellValue());
    }

    @Test
    public void testParsingWithEditor() throws Exception {
        File original = new File("C:\\Users\\dell-7359\\Desktop\\Excel原始数据.xlsx");
        Excel excel = new Excel(original);
        processor = new ExcelMappingProcessor<ExcelModel>(excel, ExcelModel.class, new ErrorThrowingExceptionResolver());
        boolean success = processor.process();
        Collection<ExcelInstance<ExcelModel>> results = processor.getCorrectResult();
        System.out.println(results.size());
        File errorFile = new File("C:\\Users\\dell-7359\\Desktop\\Excel原始数据1.xlsx");
        errorFile.createNewFile();
        excel.write(errorFile);
    }

    @Test
    public void testParsingWithCombining() throws Exception {
        File original = new File("C:\\Users\\dell-7359\\Desktop\\Excel原始数据.xlsx");
        Excel excel = new Excel(original);
        ErrorCollectingExceptionResolver exceptionResolver = new ErrorCollectingExceptionResolver();
        processor = new ExcelMappingProcessor<ExcelModel>(excel, ExcelModel.class, exceptionResolver);
        boolean success = processor.process();
        if (!success) {
            System.out.println(exceptionResolver.getCombinedMsg());
        }
    }

    @Test
    public void testGetRow() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("C:\\Users\\wb-jjb318191\\Desktop\\bank_account.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < 100; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < 9; j++) {
                Cell cell = row.getCell(j);
                switch (j) {
                    case 0:
                    case 1:
                    case 5:
                    case 7:
                    case 8:
                        System.out.print(cell.getNumericCellValue());
                        break;
                    case 2:
                    case 6:
                        System.out.print(cell.getRichStringCellValue().getString());
                        break;
                    case 3:
                    case 4:
                        System.out.print(cell.getDateCellValue().getTime());
                        break;

                }

            }
            System.out.println();
        }
    }

}
