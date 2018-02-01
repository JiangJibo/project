package com.bob.project.utils.excelmapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.bob.project.utils.excelmapping.exception.ExcelException;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_GENERAL;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_JUSTIFY;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;
import static org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN;
import static org.apache.poi.ss.usermodel.CellStyle.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_BOTTOM;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_TOP;

/**
 * @author wb_jjb318191
 * @since
 */
public class Excel {

    final static Logger logger = LoggerFactory.getLogger(Excel.class);
    private static final ClassID INVALID_CLASS_ID = new ClassID();
    private final boolean xlsx;
    private final Workbook workbook;
    private CreationHelper creationHelper;
    private FormulaEvaluator evaluator;
    private DataFormatter formatter;
    private Sheet sheet; // active sheet
    private Row lastRow; // row object of last search
    private Cell lastCell; // cell object of last search
    private int lastRowIndex; // row index of last search
    private int lastCellIndex; // cell index of last search
    private short nowColorIndex = 0x3F; // custom color added from ox40 to ox08!
    @SuppressWarnings("unused")
    private final boolean autoWidth = false;

    /**
     *
     */
    public Excel() {
        this(false);
    }

    /**
     * HSSF is the POI Project's pure Java implementation of the Excel '97(-2007) file format.
     * <p>
     * XSSF is the POI Project's pure Java implementation of the Excel 2007 OOXML (.xlsx) file
     * format.
     *
     * @param xlsx the Excel 2007 OOXML (.xlsx) file
     */
    public Excel(boolean xlsx) {
        this(xlsx ? new XSSFWorkbook() : new HSSFWorkbook());
    }

    /**
     * @param workbook
     */
    public Excel(Workbook workbook) {
        if (null == workbook) {
            throw new IllegalArgumentException("workbook");
        }
        this.xlsx = (workbook instanceof XSSFWorkbook);
        this.workbook = workbook;
		/*if (workbook instanceof HSSFWorkbook) {
			DirectoryNode root = ((HSSFWorkbook) workbook).getRootDirectory();
			if (null != root) {
				if (INVALID_CLASS_ID.equals(root.getStorageClsid())) {
					root.setStorageClsid(ClassID.EXCEL97);
				}
			} else {
				@SuppressWarnings("resource")
				POIFSFileSystem fs = new POIFSFileSystem();
				root = fs.getRoot();
				root.setStorageClsid(ClassID.EXCEL97);
				BeanWrapperImpl beanWrapper = new BeanWrapperImpl(workbook);
				beanWrapper.setPropertyValue("directory", root);
				beanWrapper.setPropertyValue("preserveNodes", true);
			}
		}*/
        setActiveSheet();
    }

    /**
     * @param url
     * @throws IOException
     */
    public Excel(URL url) throws IOException {
        this(url.openStream());
    }

    /**
     * @param istream
     * @throws IOException
     */
    public Excel(InputStream istream) throws IOException {
        this(create(istream));
    }

    /**
     * @param file
     * @throws IOException
     */
    public Excel(File file) throws IOException {
        this(create(file));
    }

    /**
     * @param fileName
     * @throws IOException
     */
    public Excel(String fileName) throws IOException {
        this(create(new File(fileName)));
    }

    /**
     * @param istream
     * @throws IOException
     */
    public static Workbook create(InputStream istream) throws IOException {
        try {
            return WorkbookFactory.create(istream);
        } catch (InvalidFormatException e) {
            throw new ExcelException("试图读取无效的Excel格式(XLS/XLSX)数据！", e);
        }
    }

    /**
     * @param file
     * @throws IOException
     */
    public static Workbook create(File file) throws IOException {
        try {
            return WorkbookFactory.create(file);
        } catch (InvalidFormatException e) {
            throw new ExcelException("试图打开无效的Excel格式(XLS/XLSX)文件！", e);
        }
    }

    /**
     * @param ostream NOT Automatic Close
     * @param close   Close Excel Object
     * @throws IOException
     */
    public void write(OutputStream ostream, boolean close) throws IOException {
        workbook.write(ostream);
    }

    /**
     * @param ostream NOT Automatic Close
     * @throws IOException
     */
    public void write(OutputStream ostream) throws IOException {
        write(ostream, true);
    }

    /**
     * @param file
     * @param close Close Excel Object
     * @throws IOException
     */
    public void write(File file, boolean close) throws IOException {
        FileOutputStream ostream = new FileOutputStream(file);
        boolean success = false;
        try {
            write(ostream);
            success = true;
        } finally {
            try {
                ostream.close();
            } catch (IOException e) {
                if (success) {
                    throw e;
                }
            }
        }
    }

    /**
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        write(file, true);
    }

    /**
     * @return the xlsx
     */
    public boolean isXLSX() {
        return xlsx;
    }

    /**
     * @return the workbook
     */
    protected Workbook getWorkbook() {
        return workbook;
    }

    /**
     * @return the creationHelper
     */
    protected final CreationHelper getCreationHelper() {
        CreationHelper result = creationHelper;
        if (null == result) {
            result = workbook.getCreationHelper();
            creationHelper = result;
        }
        return result;
    }

    /**
     * @param text
     * @return
     */
    public RichTextString createRichTextString(String text) {
        return getCreationHelper().createRichTextString(text);
    }

    /**
     * @return
     */
    public DataFormat createDataFormat() {
        return getCreationHelper().createDataFormat();
    }

    /**
     * @return
     */
    public ClientAnchor createClientAnchor() {
        return getCreationHelper().createClientAnchor();
    }

    /**
     * @param type
     * @return
     */
    public Hyperlink createHyperlink(int type) {
        return getCreationHelper().createHyperlink(type);
    }

    /**
     * @return the formulaEvaluator
     */
    protected final FormulaEvaluator getFormulaEvaluator() {
        FormulaEvaluator result = evaluator;
        if (null == result) {
            result = getCreationHelper().createFormulaEvaluator();
            evaluator = result;
        }
        return result;
    }

    /**
     * @return the dataFormatter
     */
    protected final DataFormatter getDataFormatter() {
        DataFormatter result = formatter;
        if (null == result) {
            result = new DataFormatter();
            formatter = result;
        }
        return result;
    }

    /**
     * @return the active sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * @return
     */
    public int getSheetCount() {
        return workbook.getNumberOfSheets();
    }

    /**
     * @param index of the sheet number (0-based physical & logical)
     * @return
     */
    public Sheet getSheetAt(int index) {
        return workbook.getSheetAt(index);
    }

    /**
     * @param sheet
     */
    protected void setSheet(Sheet sheet) {
        this.sheet = sheet;
        lastRow = null;
        lastCell = null;
        lastRowIndex = -1;
        lastCellIndex = -1;
    }

    /**
     * @param sheetName
     */
    public void setSheetName(String sheetName) {
        int index = workbook.getActiveSheetIndex();
        if (index >= 0) {
            workbook.setSheetName(index, sheetName);
        }
    }

    /**
     * @param index     index of the sheet (0-based)
     * @param sheetName
     */
    public void setSheetName(int index, String sheetName) {
        workbook.setSheetName(index, sheetName);
    }

    /**
     * @return
     */
    public int getActiveSheetIndex() {
        return workbook.getActiveSheetIndex();
    }

    /**
     *
     */
    protected void setActiveSheet() {
        Sheet activeSheet;
        int count = workbook.getNumberOfSheets();
        if (count > 0) {
            int index = workbook.getActiveSheetIndex();
            if (index < 0 || index >= count) {
                index = 0;
                workbook.setActiveSheet(0);
            }
            activeSheet = workbook.getSheetAt(index);
        } else {
            activeSheet = workbook.createSheet();
        }
        setSheet(activeSheet);
    }

    /**
     * @param index index of the active sheet (0-based)
     */
    public void setActiveSheet(int index) {
        workbook.setActiveSheet(index);
        setActiveSheet();
    }

    /**
     * @return
     */
    public Sheet createSheet() {
        Sheet newSheet = workbook.createSheet();
        int index = workbook.getSheetIndex(newSheet);
        workbook.setActiveSheet(index);
        setSheet(newSheet);
        return newSheet;
    }

    /**
     * @param sheetName
     * @return
     */
    public Sheet createSheet(String sheetName) {
        Sheet newSheet = workbook.createSheet(sheetName);
        int index = workbook.getSheetIndex(newSheet);
        workbook.setActiveSheet(index);
        setSheet(newSheet);
        return newSheet;
    }

    /**
     * Returns the number of phsyically defined rows (NOT the number of rows in the sheet)
     *
     * @return
     */
    public int getPhysicalNumberOfRows() {
        return sheet.getPhysicalNumberOfRows();
    }

    /**
     * Gets the first row on the sheet
     *
     * @return the number of the first logical row on the sheet, zero based
     */
    public int getFirstRowNum() {
        return sheet.getFirstRowNum();
    }

    /**
     * Gets the number last row on the sheet. Owing to idiosyncrasies in the excel file format, if
     * the result of calling this method is zero, you can't tell if that means there are zero rows
     * on the sheet, or one at position zero. For that case, additionally call
     * {@link #getPhysicalNumberOfRows()} to tell if there is a row at position zero or not.
     *
     * @return the number of the last row contained in this sheet, zero based.
     */
    public int getLastRowNum() {
        return sheet.getLastRowNum();
    }

    /**
     * @return (lastRow - firstRow + 1)
     * @see #getPhysicalNumberOfRows()
     */
    public int getRowCount() {
        int firstRow = getFirstRowNum();
        if (firstRow >= 0) {
            int lastRow = getLastRowNum();
            if (lastRow >= 0) {
                return (lastRow - firstRow + 1);
            }
        }
        return (0);
    }

    /**
     * Returns the logical row (not physical) 0-based. If you ask for a row that is not defined you
     * get a null. This is to say row 4 represents the fifth row on a sheet.
     *
     * @param rowIndex 0 based row number
     * @return Row representing the rownumber or create new if its not defined on the sheet, never
     * to return null
     */
    public Row getRow(int rowIndex) {
        Row row;
        if (rowIndex != lastRowIndex) {
            row = sheet.getRow(rowIndex);
            if (null == row) {
                row = sheet.createRow(rowIndex);
            }
            lastRow = row;
            lastRowIndex = rowIndex;
        } else {
            row = lastRow;
        }
        return row;
    }

    /**
     * @return an iterator of the PHYSICAL rows. Meaning the 3rd element may not be the third row if
     * say for instance the second row is undefined. Call getRowNum() on each row if you
     * care which one it is.
     */
    public Iterator<Row> iterator() {
        return sheet.iterator();
    }

    /**
     * get the number of the first cell contained in this row.
     *
     * @param rowIndex 0 based row number
     * @return short representing the first logical cell in the row, or -1 if the row does not
     * contain any cells.
     */
    public short getFirstCellNum(int rowIndex) {
        return getRow(rowIndex).getFirstCellNum();
    }

    /**
     * Gets the index of the last cell contained in this row <b>PLUS ONE</b>. The result also
     * happens to be the 1-based column number of the last cell. This value can be used as a
     * standard upper bound when iterating over cells:
     *
     * <pre>
     * short minColIx = row.getFirstCellNum();
     * short maxColIx = row.getLastCellNum();
     * for (short colIx = minColIx; colIx &lt; maxColIx; colIx++) {
     * 	Cell cell = row.getCell(colIx);
     * 	if (cell == null) {
     * 		continue;
     *    }
     * 	// ... do something with cell
     * }
     * </pre>
     *
     * @param rowIndex 0 based row number
     * @return short representing the last logical cell in the row <b>PLUS ONE</b>, or -1 if the row
     * does not contain any cells.
     */
    public short getLastCellNum(int rowIndex) {
        return getRow(rowIndex).getLastCellNum();
    }

    /**
     * gets the number of defined cells (NOT number of cells in the actual row!). That is to say if
     * only columns 0,4,5 have values then there would be 3.
     *
     * @param rowIndex 0 based row number
     * @return int representing the number of defined cells in the row.
     */
    public int getPhysicalNumberOfCells(int rowIndex) {
        return getRow(rowIndex).getPhysicalNumberOfCells();
    }

    /**
     * 不同于 getFirstCellNum(int)面向单行查找并返回，getFirstColumnNum()是面向所有getFirstRowNum()
     * 与getLastRowNum()之间的行查找并返回最小的起始有效列。
     *
     * @return
     * @see #getFirstCellNum(int)
     * @see #getFirstRowNum()
     * @see #getLastRowNum()
     */
    public short getFirstColumnNum() {
        int firstRow = getFirstRowNum();
        if (firstRow >= 0) {
            int lastRow = getLastRowNum();
            short minColIx = Short.MAX_VALUE;
            short firstCell;
            Row row;
            for (int i = firstRow; i <= lastRow; ++i) {
                row = sheet.getRow(i);
                if (null != row) {
                    firstCell = row.getFirstCellNum();
                    if (firstCell >= 0 && firstCell < minColIx) {
                        minColIx = firstCell;
                    }
                }
            }
            if (minColIx < Short.MAX_VALUE) {
                return minColIx;
            }
        }
        return (-1);
    }

    /**
     * 不同于 getLastCellNum(int)面向单行查找并返回，getLastColumnNum()是面向所有getFirstRowNum()
     * 与getLastRowNum()之间的行查找并返回最大的最后有效列。
     * <p>
     * 与 getLastCellNum(int) 方法一样，返回包含内容的最后有效列加1(PLUS ONE)。
     *
     * @return
     * @see #getLastCellNum(int)
     * @see #getFirstRowNum()
     * @see #getLastRowNum()
     */
    public short getLastColumnNum() {
        int firstRow = getFirstRowNum();
        if (firstRow >= 0) {
            int lastRow = getLastRowNum();
            short maxColIx = Short.MIN_VALUE;
            short lastCell;
            Row row;
            for (int i = firstRow; i <= lastRow; ++i) {
                row = sheet.getRow(i);
                if (null != row) {
                    lastCell = row.getLastCellNum();
                    if (lastCell >= 0 && lastCell > maxColIx) {
                        maxColIx = lastCell;
                    }
                }
            }
            if (maxColIx >= 0) {
                return maxColIx;
            }
        }
        return (-1);
    }

    /**
     * 不同于 getPhysicalNumberOfCells(int)面向单行查找并返回，getColumnCount() 是面向所有getFirstRowNum()
     * 与getLastRowNum()之间的行查找并返回最小的起始有效列与最大的最后有效列之间的列数，包括两者。
     *
     * @return (lastCol - firstCol)
     * @see #getPhysicalNumberOfCells(int)
     * @see #getFirstColumnNum()
     * @see #getLastColumnNum()
     */
    public int getColumnCount() {
        short firstCol = getFirstColumnNum();
        if (firstCol >= 0) {
            short lastCol = getLastColumnNum();
            if (lastCol > 0) { // NOTE: PLUS ONE
                return (lastCol - firstCol);
            }
        }
        return (0);
    }

    /**
     * /** Get the cell representing a given column (logical cell) 0-based. If you ask for a cell
     * that is not defined then you get a null, unless you have set a different on the base
     * workbook.
     *
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return Cell representing that column or create new if undefined. never to return null.
     */
    public Cell getCell(int rowIndex, int cellIndex) {
        Cell cell;
        if (cellIndex != lastCellIndex || rowIndex != lastRowIndex) {
            Row row = getRow(rowIndex);
            cell = row.getCell(cellIndex);
            if (null == cell) {
                cell = row.createCell(cellIndex);
            }
            lastCell = cell;
            lastCellIndex = cellIndex;
        } else {
            cell = lastCell;
        }
        return cell;
    }

    /**
     * Get the cell representing a given column (logical cell) 0-based. If you ask for a cell that
     * is not defined then you get a null, unless you have set a different on the base workbook.
     *
     * @param row
     * @param cellIndex 0 based column number
     * @return Cell representing that column or create new if undefined. never to return null.
     */
    public Cell getCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (null == cell) {
            cell = row.createCell(cellIndex);
        }
        return cell;
    }

    /**
     * <p>
     * Returns the formatted value of a cell as a <tt>String</tt> regardless of the cell type. If
     * the Excel format pattern cannot be parsed then the cell value will be formatted using a
     * default format.
     * </p>
     * <p>
     * When passed a null or blank cell, this method will return an empty String (""). Formula cells
     * will be evaluated. The caller is responsible for setting the currentRow on the evaluator
     * </p>
     *
     * @param cell
     * @return a string value of the cell
     */
    public String formatCellValue(Cell cell) {
        return getDataFormatter().formatCellValue(cell, getFormulaEvaluator());
    }

    /**
     * <p>
     * Returns the formatted value of a cell as a <tt>String</tt> regardless of the cell type. If
     * the Excel format pattern cannot be parsed then the cell value will be formatted using a
     * default format.
     * </p>
     * <p>
     * When passed a null or blank cell, this method will return an empty String (""). Formula cells
     * will be evaluated. The caller is responsible for setting the currentRow on the evaluator
     * </p>
     *
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return a string value of the cell
     */
    public String formatCellValue(int rowIndex, int cellIndex) {
        return formatCellValue(getCell(rowIndex, cellIndex));
    }

    /**
     * <p>
     * Returns the formatted value of a cell as a <tt>String</tt> regardless of the cell type. If
     * the Excel format pattern cannot be parsed then the cell value will be formatted using a
     * default format.
     * </p>
     * <p>
     * When passed a null or blank cell, this method will return an empty String (""). Formula cells
     * will be evaluated. The caller is responsible for setting the currentRow on the evaluator
     * </p>
     *
     * @param row
     * @param cellIndex 0 based column number
     * @return a string value of the cell
     */
    public String formatCellValue(Row row, int cellIndex) {
        return formatCellValue(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public Object getRichCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return Double.valueOf(cell.getNumericCellValue());
                }
            case CELL_TYPE_STRING:
                return cell.getRichStringCellValue();
            case CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cell.getBooleanCellValue());
            case CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return cell.getDateCellValue();
                        } else {
                            return Double.valueOf(cell.getNumericCellValue());
                        }
                    case CELL_TYPE_STRING:
                        return cell.getRichStringCellValue();
                    case CELL_TYPE_BOOLEAN:
                        return Boolean.valueOf(cell.getBooleanCellValue());
                }
            case CELL_TYPE_ERROR:
            case CELL_TYPE_BLANK:
            default:
                return null;
        }
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public Object getRichCellValue(int rowIndex, int cellIndex) {
        return getRichCellValue(getCell(rowIndex, cellIndex));
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public Object getRichCellValue(Row row, int cellIndex) {
        return getRichCellValue(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public Object getCellValue(Cell cell) {
        RichTextString richText;
        switch (cell.getCellType()) {
            case CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return Double.valueOf(cell.getNumericCellValue());
                }
            case CELL_TYPE_STRING:
                richText = cell.getRichStringCellValue();
                if (null != richText) {
                    return richText.getString();
                } else {
                    return null;
                }
            case CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cell.getBooleanCellValue());
            case CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return cell.getDateCellValue();
                        } else {
                            return Double.valueOf(cell.getNumericCellValue());
                        }
                    case CELL_TYPE_STRING:
                        richText = cell.getRichStringCellValue();
                        if (null != richText) {
                            return richText.getString();
                        } else {
                            return null;
                        }
                    case CELL_TYPE_BOOLEAN:
                        return Boolean.valueOf(cell.getBooleanCellValue());
                }
            case CELL_TYPE_ERROR:
            case CELL_TYPE_BLANK:
            default:
                return null;
        }
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public Object getCellValue(int rowIndex, int cellIndex) {
        return getCellValue(getCell(rowIndex, cellIndex));
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public Object getCellValue(Row row, int cellIndex) {
        return getCellValue(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public String getCellString(Cell cell) {
        return getCellString(cell, null);
    }

    /**
     * @param cell
     * @param defaultValue
     * @return
     */
    public String getCellString(Cell cell, String defaultValue) {
        Object value = getCellValue(cell);
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").format((Date)value);
            } catch (Exception e) {
                throw new IllegalArgumentException("坐标为{" + cell.getRowIndex() + "},{" + cell.getColumnIndex() + "}的Cell不符合日期格式", e);
            }
        } else if (value instanceof Number) {
            return ((Number)value).toString();
        } else if (value instanceof Boolean) {
            return ((Boolean)value).toString();
        } else if (value instanceof String) {
            return (String)value;
        } else {
            return defaultValue;
        }
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public String getCellString(int rowIndex, int cellIndex) {
        return getCellString(getCell(rowIndex, cellIndex));
    }

    /**
     * @param rowIndex     0 based row number
     * @param cellIndex    0 based column number
     * @param defaultValue
     * @return
     */
    public String getCellString(int rowIndex, int cellIndex, String defaultValue) {
        return getCellString(getCell(rowIndex, cellIndex), defaultValue);
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public String getCellString(Row row, int cellIndex) {
        return getCellString(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public int getCellInt(Cell cell) {
        return intValue(getCellValue(cell), 0);
    }

    /**
     * @param value
     * @param defaultValue
     * @return 如果 value 为空值(null)、"null"、空串("")或空白串("   ")或存在格式异常则返回 defaultValue
     */
    private int intValue(Object value, int defaultValue) {
        if (null == value) {
            return defaultValue;
        } else if (value instanceof Integer) {
            return ((Integer)value).intValue();
        } else if (value instanceof Number) {
            if (isInfiniteOrNaN(value)) {
                return defaultValue;
            }
            return ((Number)value).intValue();
        } else if (value instanceof String) {
            return Integer.valueOf((String)value).intValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * @param value
     * @return
     */
    private boolean isInfiniteOrNaN(Object value) {
        if (value instanceof Double) {
            Double d = (Double)value;
            return (d.isInfinite() || d.isNaN());
        } else if (value instanceof Float) {
            Float f = (Float)value;
            return (f.isInfinite() || f.isNaN());
        } else {
            return false;
        }
    }

    /**
     * @param cell
     * @param defaultValue
     * @return
     */
    public int getCellInt(Cell cell, int defaultValue) {
        return intValue(getCellValue(cell), defaultValue);
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public int getCellInt(int rowIndex, int cellIndex) {
        return getCellInt(getCell(rowIndex, cellIndex));
    }

    /**
     * @param rowIndex     0 based row number
     * @param cellIndex    0 based column number
     * @param defaultValue
     * @return
     */
    public int getCellInt(int rowIndex, int cellIndex, int defaultValue) {
        return getCellInt(getCell(rowIndex, cellIndex));
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public int getCellInt(Row row, int cellIndex) {
        return getCellInt(getCell(row, cellIndex));
    }

    /**
     * 无损数字解析，可能返回类型：Integer, Long, BigInteger, BigDecimal
     *
     * @param value
     * @param defaultValue
     * @return 如果 s 为空值(null)、"null"、空串("")或空白串("   ")，则返回null，存在格式异常则返回 defaultValue
     */
    private Number numberOf(final Object value, final Number defaultValue) {
        if (null != value) {
            if (value instanceof Number) {
                if (value instanceof Integer || value instanceof Long) {
                    return (Integer)value;
                } else if (value instanceof BigDecimal || value instanceof BigInteger) {
                    return (BigDecimal)value;
                } else if (value instanceof Double) {
                    return (Double)value;
                } else if (value instanceof Float) {
                    return (Float)value;
                } else if (value instanceof String && isNumber((String)value)) {
                    return Double.valueOf((String)value);
                }
            }
        }
        return null;
    }

    /**
     * 字符串是否是数字
     *
     * @param value
     * @return
     */
    private boolean isNumber(String value) {
        char[] chars = ((String)value).toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param value
     * @param defaultValue
     * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
     */
    private Integer integerOf(Object value, Integer defaultValue) {
        Number n = numberOf(value, defaultValue);
        if (null != n) {
            if (n instanceof Integer) {
                return (Integer)n;
            } else {
                return Integer.valueOf(n.intValue());
            }
        }
        return null;
    }

    /**
     * @param cell
     * @return
     */
    public Integer getCellInteger(Cell cell) {
        return getCellInteger(cell, null);
    }

    /**
     * @param cell
     * @param defaultValue
     * @return
     */
    public Integer getCellInteger(Cell cell, Integer defaultValue) {
        return integerOf(getCellValue(cell), defaultValue);
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public Integer getCellInteger(int rowIndex, int cellIndex) {
        return getCellInteger(getCell(rowIndex, cellIndex));
    }

    /**
     * @param rowIndex     0 based row number
     * @param cellIndex    0 based column number
     * @param defaultValue
     * @return
     */
    public Integer getCellInteger(int rowIndex, int cellIndex, Integer defaultValue) {
        return getCellInteger(getCell(rowIndex, cellIndex), defaultValue);
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public Integer getCellInteger(Row row, int cellIndex) {
        return getCellInteger(getCell(row, cellIndex));
    }

    /**
     * @param value
     * @param defaultValue
     * @return 如果 value 为空值(null)、空串("")或"null"，则返回null，存在格式异常则返回 defaultValue
     */
    private Long longOf(Object value, Long defaultValue) {
        Number n = numberOf(value, defaultValue);
        if (null != n) {
            if (n instanceof Long) {
                return (Long)n;
            } else {
                return Long.valueOf(n.longValue());
            }
        }
        return null;
    }

    /**
     * @param cell
     * @return
     */
    public Long getCellLong(Cell cell) {
        return longOf(getCellValue(cell), null);
    }

    /**
     * @param cell
     * @param defaultValue
     * @return
     */
    public Long getCellLong(Cell cell, Long defaultValue) {
        return longOf(getCellValue(cell), defaultValue);
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public Long getCellLong(int rowIndex, int cellIndex) {
        return getCellLong(getCell(rowIndex, cellIndex));
    }

    /**
     * @param rowIndex     0 based row number
     * @param cellIndex    0 based column number
     * @param defaultValue
     * @return
     */
    public Long getCellLong(int rowIndex, int cellIndex, Long defaultValue) {
        return longOf(getCell(rowIndex, cellIndex), defaultValue);
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public Long getCellLong(Row row, int cellIndex) {
        return getCellLong(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public Date getCellDate(Cell cell) {
        Object value = getCellValue(cell);
        if (null == value) {
            return null;
        } else if (value instanceof Date) {
            return (Date)value;
        } else if (value instanceof String) {
            return parseDateText((String)value);
        } else {
            return null;
        }
    }

    /**
     * @param text
     * @return
     */
    private Date parseDateText(String text) {
        try {
            return new SimpleDateFormat(text.length() < 10 ? "yyyy-MM-dd" : "yyyy-MM-dd hh:mm:ss").parse(text);
        } catch (ParseException e) {
            logger.error("解析日志字符串{[]}出现异常", text, e);
            return null;
        }
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public Date getCellDate(int rowIndex, int cellIndex) {
        return getCellDate(getCell(rowIndex, cellIndex));
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public Date getCellDate(Row row, int cellIndex) {
        return getCellDate(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public Boolean getCellBoolean(Cell cell) {
        Object value = getCellValue(cell);
        if (null == value) {
            return null;
        } else if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            return valueOf((String)value, null);
        } else if (value instanceof Integer) {
            return (Integer)value == 1;
        } else if (value instanceof Double) {
            return ((Double)value).intValue() == 1;
        } else {
            return null;
        }
    }

    /**
     * @param text
     * @param defaultValue
     * @return
     */
    private Boolean valueOf(String text, Boolean defaultValue) {
        if (null == text || text.isEmpty() || text.trim().isEmpty()) {
            return defaultValue;
        }
        if (text.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (text.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        return defaultValue;
    }

    /**
     * @param rowIndex  0 based row number
     * @param cellIndex 0 based column number
     * @return
     */
    public Boolean getCellBoolean(int rowIndex, int cellIndex) {
        return getCellBoolean(getCell(rowIndex, cellIndex));
    }

    /**
     * @param row
     * @param cellIndex 0 based column number
     * @return
     */
    public Boolean getCellBoolean(Row row, int cellIndex) {
        return getCellBoolean(getCell(row, cellIndex));
    }

    /**
     * @param cell
     * @return
     */
    public BigDecimal getCellDecimal(Cell cell) {
        return toBigDecimal(numberOf(getCellValue(cell), null), BigDecimal.ZERO);
    }

    /**
     * @param n
     * @param defaultValue
     * @return
     */
    private BigDecimal toBigDecimal(Number n, BigDecimal defaultValue) {
        if (null == n) {
            return null;
        } else if (n instanceof BigDecimal) {
            return (BigDecimal)n;
        } else if (n instanceof BigInteger) {
            return new BigDecimal((BigInteger)n);
        } else if (n instanceof Double || n instanceof Float) {
            if (isInfiniteOrNaN(n)) {
                return defaultValue;
            }
            /**
             * 对于不是 double/float NaN 和 ±Infinity 的值，BigDecimal(String)构造方法与
             * Double/Float.toString(double/float) 返回的值兼容。这通常是将 double/float 转换为 BigDecimal
             * 的首选方法，因为它不会遇到 BigDecimal(double) 构造方法的不可预知问题。
             */
            return new BigDecimal(n.toString());
        } else {
            return BigDecimal.valueOf(n.longValue());
        }
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @return
     */
    public Cell setCellBlank(int rowIndex, int colIndex) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellType(CELL_TYPE_BLANK);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, String value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(createRichTextString(value));
        CellStyle cellStyle = cell.getCellStyle();
        if (!cellStyle.getShrinkToFit()) {
            cellStyle.setShrinkToFit(true);
            cell.setCellStyle(cellStyle);
        }
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, RichTextString value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, byte value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, short value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, int value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, long value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, double value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, Double value) {
        Cell cell = getCell(rowIndex, colIndex);
        if (null == value) {
            cell.setCellType(CELL_TYPE_BLANK);
        } else {
            cell.setCellValue(value);
        }
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, BigDecimal value) {
        Cell cell = getCell(rowIndex, colIndex);
        if (null == value) {
            cell.setCellType(CELL_TYPE_BLANK);
        } else {
            cell.setCellValue(value.doubleValue());
        }
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, Number value) {
        Cell cell = getCell(rowIndex, colIndex);
        if (null == value) {
            cell.setCellType(CELL_TYPE_BLANK);
        } else {
            cell.setCellValue(value.doubleValue());
        }
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, boolean value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, Boolean value) {
        Cell cell = getCell(rowIndex, colIndex);
        if (null == value) {
            cell.setCellType(CELL_TYPE_BLANK);
        } else {
            cell.setCellValue(value);
        }
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, Date value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param value
     * @return
     */
    public Cell setCell(int rowIndex, int colIndex, Calendar value) {
        Cell cell = getCell(rowIndex, colIndex);
        cell.setCellValue(value);
        return cell;
    }

    /**
     * 合并单元格 起始行列+跨度 >>> 起止行列
     *
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param rowSpan
     * @param colSpan
     * @param border
     */
    public void mergedCells(int rowIndex, int colIndex, int rowSpan, int colSpan, int border) {
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, (rowIndex + rowSpan - 1), colIndex, (colIndex + colSpan - 1)));
        if ((border & 15) != 0) {
            for (int r = 0; r < rowSpan; r++) {
                for (int c = 0; c < colSpan; c++) {
                    Cell cell = getCell(rowIndex + r, colIndex + c);
                    CellStyle cellStyle = workbook.createCellStyle(); // 在工作薄的基础上建立一个样式
                    if ((border & 1) == 1) {
                        cellStyle.setBorderLeft(BORDER_THIN);
                    }
                    if ((border & 2) == 2) {
                        cellStyle.setBorderTop(BORDER_THIN);
                    }
                    if ((border & 4) == 4) {
                        cellStyle.setBorderRight(BORDER_THIN);
                    }
                    if ((border & 8) == 8) {
                        cellStyle.setBorderBottom(BORDER_THIN);
                    }
                    cell.setCellStyle(cellStyle);
                }
            }
        }
    }

    /**
     * 设置行高InPoints：Height的单位是twips (1/20 of a point)(height:3point=4像素)
     *
     * @param rowIndex 0 based row number
     * @param height
     * @return
     */
    public Row setHeight(int rowIndex, float height) {
        Row row = sheet.getRow(rowIndex);
        row.setHeightInPoints(height);
        return row;
    }

    /**
     * 设置列宽：单位是1/256个字符宽度 (256/14*2=36.57) //!=35.7
     *
     * @param colIndex 0 based column number
     * @param width
     */
    public void setWidth(int colIndex, int width) {
        sheet.setColumnWidth(colIndex, (int)Math.abs(width * 36.57));
        if (width <= 0) {
            setHidden(colIndex);
        }
    }

    /**
     * 设置列隐藏Hidden
     *
     * @param colIndex 0 based column number
     */
    public void setHidden(int colIndex) {
        sheet.setColumnHidden(colIndex, true);
    }

    /**
     * 设置单元格格式：统一 style: 0大标题/1首尾项/2列标头/>=3数据列
     *
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param style
     * @return
     */
    public Cell setCellStyle(int rowIndex, int colIndex, int style) {
        Cell cell = getCell(rowIndex, colIndex);
        CellStyle cellStyle = getCellStyle(cell, style);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    /**
     * 设置单元格格式：统一 style: 0大标题/1首尾项/2列标头/>=3数据列
     *
     * @param cell
     * @param style
     * @return
     */
    public Cell setCellStyle(Cell cell, int style) {
        CellStyle cellStyle = getCellStyle(cell, style);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    /**
     * 生成单元格格式：独立
     *
     * @param style
     * @return
     */
    public CellStyle getCellStyle(int style) {
        CellStyle cellStyle = workbook.createCellStyle(); // 在工作薄的基础上建立一个样式
        cellStyle = getCellStyle(cellStyle, style);
        return cellStyle;
    }

    /**
     * 生成单元格格式：叠加
     *
     * @param cell
     * @param style
     * @return
     */
    public CellStyle getCellStyle(Cell cell, int style) {
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle = getCellStyle(cellStyle, style);
        return cellStyle;
    }

    /**
     * 生成单元格格式 style: 0大标题/1首尾项/2列标头/3~n数据列
     *
     * @param cellStyle
     * @param style
     * @return
     */
    public CellStyle getCellStyle(CellStyle cellStyle, int style) {
        if (null == cellStyle) {
            cellStyle = workbook.createCellStyle();
        }
        // 统一：
        // 1.边框
        if (style >= 2) {
            cellStyle.setBorderLeft(BORDER_THIN);
            cellStyle.setBorderTop(BORDER_THIN);
            cellStyle.setBorderRight(BORDER_THIN);
            cellStyle.setBorderBottom(BORDER_THIN);
        }
        // 2.对齐：
        if (style <= 0 || style == 2) {
            cellStyle.setAlignment(ALIGN_CENTER);
        }
        cellStyle.setVerticalAlignment(VERTICAL_CENTER); // VERTICAL_TOP
        // 3.字体：
        Font font = workbook.createFont();
        if (style == 0) {
            font.setFontName("华文新魏");
            font.setFontHeightInPoints((short)18);
        } else {
            font.setFontName("宋体");
            font.setFontHeightInPoints((short)9);
        }
        cellStyle.setFont(font);
        // 4.换行：
        cellStyle.setWrapText(true);
        // 5.背景：
		/*if (style == 2) { // TODO
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
			cellStyle.setFillBackgroundColor(IndexedColors.AUTOMATIC.getIndex());
		}*/
        // 9.个性：
        switch (style) {
            case 0: //
                break;
            case 1: //
                break;
            case 2: //
                break;
            default: //
        }
        //
        return cellStyle;
    }

    /**
     * 生成单元格格式：叠加(个性化) border, align, font, other
     *
     * @param cellStyle
     * @param border
     * @param align
     * @param fonts
     * @param other
     * @param color
     * @return
     */
    public CellStyle getCellStyle(CellStyle cellStyle, int border, int align, int fonts, int other, int color) {
        CellStyle newStyle = workbook.createCellStyle();
        newStyle.cloneStyleFrom(cellStyle);
        // 1.Border: 1=Left/2=Top/4=Right/8=Bottom/?=粗细
        if ((border & 1) == 1) {
            newStyle.setBorderLeft(BORDER_THIN);
        }
        if ((border & 2) == 2) {
            newStyle.setBorderTop(BORDER_THIN);
        }
        if ((border & 4) == 4) {
            newStyle.setBorderRight(BORDER_THIN);
        }
        if ((border & 8) == 8) {
            newStyle.setBorderBottom(BORDER_THIN);
        }
        // 2.Align: cellStyle.ALIGN_GENERAL, cellStyle.ALIGN_LEFT,
        // cellStyle.ALIGN_CENTER, cellStyle.ALIGN_RIGHT,
        // cellStyle.ALIGN_JUSTIFY, cellStyle.ALIGN_FILL,
        // cellStyle.ALIGN_CENTER_SELECTION
        if ((align & 15) == 0) { // 0=GENERAL/1=LEFT/2=CENTER/4=RIGHT/8=JUSTIFY/!=FILL
            newStyle.setAlignment(ALIGN_GENERAL);
        } else if ((align & 15) == 1) {
            newStyle.setAlignment(ALIGN_LEFT);
        } else if ((align & 15) == 2) {
            newStyle.setAlignment(ALIGN_CENTER);
        } else if ((align & 15) == 4) {
            newStyle.setAlignment(ALIGN_RIGHT);
        } else if ((align & 15) == 8) {
            newStyle.setAlignment(ALIGN_JUSTIFY);
        } else {
            // newStyle.setAlignment(CellStyle.ALIGN_FILL); //none
        }
        // VerticalAlignment: cellStyle.VERTICAL_TOP, cellStyle.VERTICAL_CENTER,
        // cellStyle.VERTICAL_BOTTOM,
        // cellStyle.VERTICAL_JUSTIFY
        if ((align & 112) == 16) { // 16=TOP/32=CENTER/64=BOTTOM/!=JUSTIFY
            newStyle.setVerticalAlignment(VERTICAL_TOP);
        } else if ((align & 112) == 32) {
            newStyle.setVerticalAlignment(VERTICAL_CENTER);
        } else if ((align & 112) == 64) {
            newStyle.setVerticalAlignment(VERTICAL_BOTTOM);
        } else {
            // newStyle.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
        }
        // 3.fonts: (fonts % 100) = font.size && (fonts / 100) = font.name &&.
        int iFontSize = (fonts % 100);
        int iFontName = ((fonts % 1000) / 100);
        int iFontType = ((fonts % 10000) / 1000);
        int iFontClrs = ((fonts % 100000) / 10000);
        if ((iFontSize + iFontName + iFontType + iFontClrs) > 0) {
            Font font = workbook.createFont(); // 应该先从当前Style中提取Font，再在其上增改。
            // 字号
            if (iFontSize <= 0) {
                iFontSize = 9;
            }
            font.setFontHeightInPoints((short)iFontSize);
            // 字体
            String sFontName = "宋体";
            switch (iFontName) {
                case 9: //
                    sFontName = "Arial";
                    break;
                case 8: //
                    sFontName = "Times New Roman";
                    break;
                case 7: //
                    sFontName = "Wingdings";
                    break;
                case 6: //
                    sFontName = "华文新魏";
                    break;
                case 5: //
                    sFontName = "华文彩云";
                    break;
                case 4: //
                    sFontName = "幼圆";
                    break;
                case 3: //
                    sFontName = "黑体";
                    break;
                case 2: //
                    sFontName = "隶书";
                    break;
            }
            font.setFontName(sFontName);
            if (iFontType > 0) {
                if ((iFontType & 1) == 1) {
                    //font.setBold(true);
                }
                if ((iFontType & 2) == 2) {
                    font.setItalic(true);
                }
                if ((iFontType & 4) == 4) {
                    font.setUnderline((byte)1);
                }
                if ((iFontType & 8) == 8) {
                    font.setStrikeout(true);
                }
                // if ((iFontType & 15) == ?) { ... }
            }
            if (iFontClrs > 0) {
                short colorIdx = 0;
                switch (iFontClrs) {
                    case 1: //
                        colorIdx = IndexedColors.RED.getIndex();
                        break;
                    case 2: //
                        colorIdx = IndexedColors.GREEN.getIndex();
                        break;
                    case 3: //
                        colorIdx = IndexedColors.BLUE.getIndex();
                        break;
                    case 4: //
                        colorIdx = IndexedColors.LIME.getIndex();
                        break;
                    case 5: //
                        colorIdx = IndexedColors.MAROON.getIndex();
                        break;
                    case 6: //
                        colorIdx = IndexedColors.ROSE.getIndex();
                        break;
                    case 7: //
                        colorIdx = IndexedColors.GOLD.getIndex();
                        break;
                    case 8: //
                        colorIdx = IndexedColors.OLIVE_GREEN.getIndex();
                        break;
                    case 9: //
                        colorIdx = IndexedColors.GREY_25_PERCENT.getIndex();
                        break;
                    default: //
                }
                if (colorIdx > 0) {
                    font.setColor(colorIdx);
                }
            }
            newStyle.setFont(font);
        }
        // 4.other：
        // ...
        // 5.背景：// 底色: setFillBackgroundColor/填充: setFillForegroundColor >>
        // #dce9f1=>(241,233,220)=>14477809
        if (setCellStyleForegroundColor(newStyle, color)) {
            newStyle.setFillPattern(SOLID_FOREGROUND);
            newStyle.setFillBackgroundColor(IndexedColors.AUTOMATIC.getIndex());
        }
        return newStyle;
    }

    /**
     * 设置int型color对应的颜色
     *
     * @param cellStyle
     * @param color      颜色值使用Windows平台存储顺序：0x00bbggrr，与Java默认的0x00rrggbb有所不同
     * @param foreground
     * @return
     */
    protected boolean setCellStyleColor(CellStyle cellStyle, int color, boolean foreground) {
        color &= 0x00ffffff;
        if (color == 0) {
            // 无需处理，否则，某些单元将出现黑色背景。
            return false;
        }

        byte r = (byte)(color & 0x00ff);
        byte g = (byte)((color >> 8) & 0x00ff);
        byte b = (byte)((color >> 16) & 0x00ff);
        if (xlsx) {
            XSSFColor xssfColor = new XSSFColor(new byte[] {r, g, b});
            if (foreground) {
                ((XSSFCellStyle)cellStyle).setFillForegroundColor(xssfColor);
            } else {
                ((XSSFCellStyle)cellStyle).setFillBackgroundColor(xssfColor);
            }
        } else {
            HSSFPalette palette = ((HSSFWorkbook)workbook).getCustomPalette();
            HSSFColor hssfColor = palette.findColor(r, g, b);
            if (null == hssfColor) {
                try {
                    hssfColor = palette.addColor(r, g, b);
                } catch (RuntimeException ignore) {
                    palette.setColorAtIndex(nowColorIndex, r, g, b);
                    hssfColor = palette.findColor(r, g, b);
                    if (null != hssfColor) {
                        --nowColorIndex;
                    } else {
                        return false;
                    }
                }
            }
            if (foreground) {
                cellStyle.setFillForegroundColor(hssfColor.getIndex());
            } else {
                cellStyle.setFillBackgroundColor(hssfColor.getIndex());
            }
        }
        return true;
    }

    /**
     * 设置int型color对应的前景颜色
     *
     * @param cellStyle
     * @param color     颜色值使用Windows平台存储顺序：0x00bbggrr，与Java默认的0x00rrggbb有所不同
     * @return
     */
    public boolean setCellStyleForegroundColor(CellStyle cellStyle, int color) {
        return setCellStyleColor(cellStyle, color, true);
    }

    /**
     * 设置int型color对应的背景颜色
     *
     * @param cellStyle
     * @param color     颜色值使用Windows平台存储顺序：0x00bbggrr，与Java默认的0x00rrggbb有所不同
     * @return
     */
    public boolean setCellStyleBackgroundColor(CellStyle cellStyle, int color) {
        return setCellStyleColor(cellStyle, color, false);
    }

    /**
     * 设置单元格格式
     *
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param border
     * @param align
     * @param font
     * @param other
     * @param color
     * @return
     */
    public Cell setCellStyle(int rowIndex, int colIndex, int border, int align, int font, int other, int color) {
        Cell cell = getCell(rowIndex, colIndex);
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle = getCellStyle(cellStyle, border, align, font, other, color);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    /**
     * Invoking {@link HSSFWorkbook#getBytes()} does not return all of the data necessary to re-
     * construct a complete Excel file.
     * <p>
     * use {@link ByteArrayOutputStream} because it is good style to include anyway in case its
     * later changed to a different kind of stream
     *
     * @return the byte array with this {@link #workbook}
     * @throws IOException if anything can't be written
     * @author CaiBo
     */
    public byte[] getBytes() throws IOException {
        // we're best off getting the size because it takes too much time
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
        }
        return bos.toByteArray();
    }

    /**
     * 设置某单元格为指定样式
     *
     * @param rowIndex 0 based row number
     * @param colIndex 0 based column number
     * @param style    该单元格的样式，可通过本类中的 {@link #createCellStyle} 方法创建
     * @author CaiBo
     */
    public void setCellStyle(int rowIndex, int colIndex, CellStyle style) {
        getCell(rowIndex, colIndex).setCellStyle(style);
    }

    /**
     * 创建一个自定义样式
     * <p>
     * &nbsp&nbsp<font color=red>设置字体、字号等</font>: 请使用 {@link #createFont} 方法
     *
     * @return 自定义样式
     * @author CaiBo
     */
    public CellStyle createCellStyle() {
        return workbook.createCellStyle();
    }

    /**
     * 创建自定义字体
     *
     * @return
     */
    public Font createFont() {
        return workbook.createFont();
    }

}
