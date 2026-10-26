package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache POI helper: reads one sheet of an .xlsx file into Object[][]
 * so it can be returned straight from a TestNG @DataProvider.
 *
 * Row 1 of the sheet is the header and is skipped.
 * Every cell is returned as a String (empty cell -> "").
 */
public class ExcelUtils {

    public static Object[][] getSheetData(String sheetName) {
        return getSheetData(ConfigReader.get("testdata.path"), sheetName);
    }

    public static Object[][] getSheetData(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            DataFormatter formatter = new DataFormatter();   // turns any cell type into text
            int columnCount = sheet.getRow(0).getLastCellNum();
            List<Object[]> data = new ArrayList<>();

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                String[] values = new String[columnCount];
                boolean blankRow = true;
                for (int c = 0; c < columnCount; c++) {
                    values[c] = formatter.formatCellValue(row.getCell(c)).trim();
                    if (!values[c].isEmpty()) {
                        blankRow = false;
                    }
                }
                if (!blankRow) {
                    data.add(values);
                }
            }
            return data.toArray(new Object[0][]);

        } catch (IOException e) {
            throw new RuntimeException("Could not read Excel file: " + filePath, e);
        }
    }
}
