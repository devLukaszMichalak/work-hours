package dev.lukaszmichalak;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        int moneyPerHour = getMoneyPerHour(args);

        XSSFWorkbook workbook = getWorkbook();
        XSSFSheet sheet = workbook.getSheet("C.H.Beck");

        LocalDateTime workStartHour = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(8, 30)
        );

        LocalDateTime workEndHour = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(16, 30)
        );

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        setRawCellValue(sheet, 3, 2, year);
        setRawCellValue(sheet, 5, 2, getMonthNameInPolish(month));
        setRawCellValue(sheet, 11, 12, moneyPerHour);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= 31; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);

            XSSFRow row = sheet.getRow(day + 10);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            setWorkHourCell(row, 2, day, daysInMonth, dayOfWeek, workStartHour);
            setWorkHourCell(row, 3, day, daysInMonth, dayOfWeek, workEndHour);
        }

        XSSFFormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateAll();

        saveToFile(workbook, "kalkulator-" + getMonthNameInPolish(month).toLowerCase());
        log.info("Zapisano plik dla stawki: " + moneyPerHour);

    }

    private static void setRawCellValue(XSSFSheet sheet, int rowNumber, int cellNumber, int value) {
        XSSFRow row = sheet.getRow(rowNumber);
        XSSFCell cell = row.getCell(cellNumber);
        cell.setBlank();
        cell.setCellValue(value);
    }

    private static void setRawCellValue(XSSFSheet sheet, int rowNumber, int cellNumber, String value) {
        XSSFRow row = sheet.getRow(rowNumber);
        XSSFCell cell = row.getCell(cellNumber);
        cell.setBlank();
        cell.setCellValue(value);
    }

    private static void setWorkHourCell(XSSFRow row, int cellNumber, int day, int daysInMonth, int dayOfWeek, LocalDateTime hour) {
        XSSFCell startTimeCell = row.getCell(cellNumber);
        startTimeCell.setBlank();
        if (day <= daysInMonth) {
            if (dayOfWeek == Calendar.SATURDAY) {
                startTimeCell.setCellValue("SOBOTA");
            } else if (dayOfWeek == Calendar.SUNDAY) {
                startTimeCell.setCellValue("NIEDZIELA");
            } else {
                startTimeCell.setCellValue(hour);
            }
        }
    }

    private static void saveToFile(XSSFWorkbook workbook, String name) {
        try (FileOutputStream fos = new FileOutputStream(name + ".xlsx")) {
            workbook.write(fos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int getMoneyPerHour(String[] args) {
        int moneyPerHour;
        if (args.length > 0) {
            moneyPerHour = Integer.parseInt(args[0]);
        } else {
            throw new RuntimeException("No command-line arguments provided.");
        }
        return moneyPerHour;
    }

    private static XSSFWorkbook getWorkbook() {
        try (FileInputStream templateXlsx = new FileInputStream("template.xlsx")) {
            return new XSSFWorkbook(templateXlsx);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMonthNameInPolish(int month) {
        String[] months = {
                "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec",
                "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"
        };
        return months[month];
    }
}
