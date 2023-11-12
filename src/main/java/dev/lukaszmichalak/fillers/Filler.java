package dev.lukaszmichalak.fillers;

import org.apache.poi.ooxml.POIXMLDocument;

public interface Filler {
    
    POIXMLDocument fill(POIXMLDocument document, int moneyPerHour);
    
    POIXMLDocument getDocument();
    
    void saveToFile(POIXMLDocument document);
}
