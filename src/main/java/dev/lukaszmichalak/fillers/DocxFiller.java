package dev.lukaszmichalak.fillers;

import dev.lukaszmichalak.fillers.helpers.MonthTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class DocxFiller implements Filler {
    
    private static final Logger log = LogManager.getLogger(DocxFiller.class);
    
    @Override
    public POIXMLDocument fill(POIXMLDocument document, int moneyPerHour) {
        return document; //todo fill the docx document
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
    
    private XWPFDocument castToDocx(POIXMLDocument document) {
        if (!(document instanceof XWPFDocument docx)) {
            throw new FillerException("Zły typ dokumentu! Oczekiwano docx");
        }
        return docx;
    }
}
