package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderAPI {

    public static Map<String, String> getCustomerDataExcel(File file, String customerName) {
        Map<String, String> data = new LinkedHashMap<>();

        waitForFileReady(file);

        if (file.getName().toLowerCase().endsWith(".csv")) {
            readCSV(file, customerName, data);
        } else if (file.getName().toLowerCase().endsWith(".xlsx")) {
            readXLSX(file, customerName, data);
        } else {
            throw new RuntimeException("Unsupported file type: " + file.getName());
        }

        return data;
    }

    private static void readCSV(File file, String customerName, Map<String, String> data) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String headerLine = br.readLine();
            if (headerLine == null) return;

            // Remove BOM if present
            headerLine = headerLine.replace("\uFEFF", "");

            String[] headers = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String nameValue = values[0].trim();

                if (nameValue.equalsIgnoreCase(customerName.trim()) ||
                    nameValue.toLowerCase().contains(customerName.trim().toLowerCase())) {

                    for (int i = 0; i < headers.length && i < values.length; i++) {
                        String headerKey = headers[i].replace("\uFEFF", "").trim(); // Ensure BOM removed
                        data.put(headerKey, values[i].trim());
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + file.getAbsolutePath(), e);
        }
    }


    private static void readXLSX(File file, String customerName, Map<String, String> data) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String nameValue = getCellValueAsString(row.getCell(0)).trim();

                if (nameValue.equalsIgnoreCase(customerName.trim()) ||
                    nameValue.toLowerCase().contains(customerName.trim().toLowerCase())) {

                    for (int col = 0; col <= headerRow.getLastCellNum() - 1; col++) {
                        String key = headerRow.getCell(col).getStringCellValue().trim();
                        String value = getCellValueAsString(row.getCell(col));
                        data.put(key, value);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading XLSX file: " + file.getAbsolutePath(), e);
        }
    }

    private static void waitForFileReady(File file) {
        int retries = 5;
        while (retries-- > 0) {
            if (file.exists() && file.length() > 0) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    return;
                } catch (IOException ignored) {}
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
        throw new RuntimeException("File not ready: " + file.getAbsolutePath());
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
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
                return cell.toString().trim();
        }
    }
}
