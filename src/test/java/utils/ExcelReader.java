package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelReader {

    public static Map<String, String> getCustomerData(String customerName) {
        Map<String, String> data = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream("src/test/resources/reports/VasuAutomationReport.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0); // Headers in row 0

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Cell nameCell = row.getCell(0); // A = Customer Name
                if (nameCell != null && nameCell.getStringCellValue().equalsIgnoreCase(customerName)) {
                    for (int col = 0; col <= 32; col++) { // A (0) to AG (32)
                        String key = headerRow.getCell(col).getStringCellValue().trim();
                        String value = getCellValueAsString(row.getCell(col));
                        data.put(key, value);
                    }
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Convert to MM/dd/yyyy (to match UI)
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == Math.floor(val)) {
                        return String.valueOf((int) val);
                    } else {
                        return String.valueOf(val);
                    }
                }

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }

            case BLANK:
                return "";

            default:
                return cell.toString().trim();  // fallback for unexpected types
        }
    }

}
