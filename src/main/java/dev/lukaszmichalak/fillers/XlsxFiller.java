package dev.lukaszmichalak.fillers;

import dev.lukaszmichalak.fillers.helpers.MonthTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class XlsxFiller implements Filler {
    
    private static final Logger log = LogManager.getLogger(XlsxFiller.class);
    
    @Override
    public POIXMLDocument fill(POIXMLDocument document, int moneyPerHour) {
        XSSFWorkbook workbook = castToWorkbook(document);
        
        XSSFSheet sheet = workbook.getSheet("C.H.Beck");
        
        LocalTime workStartHour = LocalTime.of(8, 30);
        LocalTime workEndHour = LocalTime.of(16, 30);
        
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        
        setRawCellValue(sheet, 3, 2, year);
        setRawCellValue(sheet, 5, 2, MonthTranslator.getMonthNameInPolish(month));
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
        
        return workbook;
    }
    
    @Override
    public POIXMLDocument getDocumentFromFile(File file) {
        try (FileInputStream templateXlsx = new FileInputStream(file)) {
            return new XSSFWorkbook(templateXlsx);
            
        } catch (IOException e) {
            throw new FillerException(e);
        }
    }
    
    @Override
    public void saveToFile(POIXMLDocument document) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String name = "kalkulator-" + MonthTranslator.getMonthNameInPolish(month).toLowerCase();
        
        try (FileOutputStream fos = new FileOutputStream(name + ".xlsx")) {
            XSSFWorkbook workbook = castToWorkbook(document);
            workbook.write(fos);
            log.info("Zapisano plik xlsx");
            
        } catch (Exception e) {
            throw new FillerException(e);
        }
    }
    
    private XSSFWorkbook castToWorkbook(POIXMLDocument document) {
        if (!(document instanceof XSSFWorkbook workbook)) {
            throw new FillerException("Zły typ dokumentu! Oczekiwano xlsx!");
        }
        return workbook;
    }
    
    private void setRawCellValue(XSSFSheet sheet, int rowNumber, int cellNumber, int value) {
        XSSFRow row = sheet.getRow(rowNumber);
        XSSFCell cell = row.getCell(cellNumber);
        cell.setBlank();
        cell.setCellValue(value);
    }
    
    private void setRawCellValue(XSSFSheet sheet, int rowNumber, int cellNumber, String value) {
        XSSFRow row = sheet.getRow(rowNumber);
        XSSFCell cell = row.getCell(cellNumber);
        cell.setBlank();
        cell.setCellValue(value);
    }
    
    private void setWorkHourCell(XSSFRow row, int cellNumber, int day, int daysInMonth, int dayOfWeek, LocalTime hour) {
        XSSFCell startTimeCell = row.getCell(cellNumber);
        startTimeCell.setBlank();
        if (day > daysInMonth) {
            return;
        }
        
        if (dayOfWeek == Calendar.SATURDAY) {
            startTimeCell.setCellValue("SOBOTA");
            
        } else if (dayOfWeek == Calendar.SUNDAY) {
            startTimeCell.setCellValue("NIEDZIELA");
            
        } else {
            var timeToSet = LocalDateTime.of(LocalDate.now(), hour);
            startTimeCell.setCellValue(timeToSet);
        }
    }
}
