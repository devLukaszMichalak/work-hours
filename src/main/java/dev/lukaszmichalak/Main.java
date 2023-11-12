package dev.lukaszmichalak;

import dev.lukaszmichalak.fillers.DocxFiller;
import dev.lukaszmichalak.fillers.Filler;
import dev.lukaszmichalak.fillers.XlsxFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLDocument;

public class Main {
    
    private static final Logger log = LogManager.getLogger(Main.class);
    
    private static final Filler xlsxFiller = new XlsxFiller();
    private static final Filler docxFiller = new DocxFiller();
    
    public static void main(String[] args) {
        
        int moneyPerHour = getMoneyPerHour(args);
        log.info("Rozpoczęto wypełnianie dla stawki " + moneyPerHour);
        
        Filler[] fillers = {xlsxFiller, docxFiller};
        
        for (Filler filler : fillers) {
            POIXMLDocument workbook = filler.getDocument();
            workbook = filler.fill(workbook, moneyPerHour);
            filler.saveToFile(workbook);
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
    
}
