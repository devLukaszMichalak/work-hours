package dev.lukaszmichalak.fillers;

import java.io.File;
import org.apache.poi.ooxml.POIXMLDocument;

public interface Filler {

  POIXMLDocument fill(POIXMLDocument document, int moneyPerHour);

  POIXMLDocument getDocumentFromFile(File file);

  void saveToFile(POIXMLDocument document);
}
