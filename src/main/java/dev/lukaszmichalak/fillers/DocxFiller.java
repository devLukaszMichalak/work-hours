package dev.lukaszmichalak.fillers;

import dev.lukaszmichalak.fillers.helpers.MonthTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DocxFiller implements Filler {
    
    private static final Logger log = LogManager.getLogger(DocxFiller.class);
    
    @Override
    public POIXMLDocument fill(POIXMLDocument document, int moneyPerHour) {
        XWPFDocument docx = castToDocx(document);
        
        docx.getParagraphs().stream()
                .flatMap(paragraph -> paragraph.getRuns().stream())
                .forEach(xwpfRun -> fillRunIfNecessary(xwpfRun, moneyPerHour));
        
        return docx;
    }
    
    private void fillRunIfNecessary(XWPFRun run, int moneyPerHour) {
        switch (run.text()) {
            case "date":
                fillDate(run);
                break;
            
            case "month":
                fillMonth(run); //todo fill year
                break;
            
            case "day-range":
                fillDayRange(run);
                break;
            
            case "hours":
                System.out.println("hours");
                break;
            case "total":
                System.out.println("total");
                break;
            case "spelled-out-total":
                System.out.println("spelled-out-total");
                break;
            //todo add case for money-per-hour
            default:
        }
    }
    
    private void fillDayRange(XWPFRun run) {
        cleanRun(run);
        run.setItalic(false);
        
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedRange = "1-" + lastDayOfMonth.format(formatter) + " r.";
        
        run.setText(formattedRange);
    }
    
    private void fillDate(XWPFRun run) {
        cleanRun(run);
        run.setItalic(false);
        
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = lastDayOfMonth.format(formatter) + " r.";
        
        run.setText(formattedDate);
    }
    
    private void fillMonth(XWPFRun run) {
        cleanRun(run);
        run.setItalic(false);
        
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String monthText = MonthTranslator.getMonthNameInPolish(month).toUpperCase();
        
        run.setText(monthText);
    }
    
    @Override
    public POIXMLDocument getDocument() {
        try (FileInputStream templateXlsx = new FileInputStream("template.docx")) {
            return new XWPFDocument(templateXlsx);
            
        } catch (IOException e) {
            throw new FillerException(e);
        }
    }
    
    @Override
    public void saveToFile(POIXMLDocument document) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String name = "rachunek-" + MonthTranslator.getMonthNameInPolish(month).toLowerCase();
        
        try (FileOutputStream fos = new FileOutputStream(name + ".docx")) {
            XWPFDocument docx = castToDocx(document);
            docx.write(fos);
            log.info("Zapisano plik docx");
            
        } catch (Exception e) {
            throw new FillerException(e);
        }
    }
    
    public void cleanRun(XWPFRun run) {
        for (int i = 0; i <= run.text().length(); i++) {
            run.setText("", i);
        }
    }
    
    private XWPFDocument castToDocx(POIXMLDocument document) {
        if (!(document instanceof XWPFDocument docx)) {
            throw new FillerException("Zły typ dokumentu! Oczekiwano docx");
        }
        return docx;
    }
}
