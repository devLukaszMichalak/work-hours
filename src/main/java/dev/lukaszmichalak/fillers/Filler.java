package dev.lukaszmichalak.fillers;

import org.apache.poi.ooxml.POIXMLDocument;

import java.io.File;

public interface Filler {
    
    POIXMLDocument fill(POIXMLDocument document, int moneyPerHour);
    
    POIXMLDocument getDocumentFromFile(File file);
    
    void saveToFile(POIXMLDocument document);
}
