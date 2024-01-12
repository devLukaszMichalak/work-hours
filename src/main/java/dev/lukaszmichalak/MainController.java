package dev.lukaszmichalak;

import dev.lukaszmichalak.fillers.DocxFiller;
import dev.lukaszmichalak.fillers.Filler;
import dev.lukaszmichalak.fillers.XlsxFiller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLDocument;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    
    private static final Logger log = LogManager.getLogger(MainController.class);
    
    private static final Filler xlsxFiller = new XlsxFiller();
    private static final Filler docxFiller = new DocxFiller();
    
    private File docxFile = null;
    private File xlsxFile = null;
    
    @FXML
    public TextField moneyPerHourField;
    
    @FXML
    public Button docxButton;
    
    @FXML
    public Button xlsxButton;
    
    @FXML
    public Button generateButton;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        docxButton.setDisable(true);
        moneyPerHourField.setDisable(true);
        
        moneyPerHourField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
    }
    
    public void onXlsxClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xlsx file");
        xlsxFile = fileChooser.showOpenDialog(Main.mainStage);
        
        if (xlsxFile != null) {
            System.out.println("Selected file: " + xlsxFile.getAbsolutePath());
            docxButton.setDisable(false);
        }
    }
    
    public void onDocxClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select docx file");
        docxFile = fileChooser.showOpenDialog(Main.mainStage);
        
        if (docxFile != null) {
            System.out.println("Selected file: " + docxFile.getAbsolutePath());
            moneyPerHourField.setDisable(false);
        }
    }
    
    public void onGenerateClick(ActionEvent actionEvent) {
        log.info("Rozpoczęto wypełnianie dla stawki " + moneyPerHourField.getText());
        
        POIXMLDocument workbook = xlsxFiller.getDocumentFromFile(xlsxFile);
        workbook = xlsxFiller.fill(workbook, Integer.parseInt(moneyPerHourField.getText()));
        xlsxFiller.saveToFile(workbook);
        
        POIXMLDocument docx = docxFiller.getDocumentFromFile(docxFile);
        docx = docxFiller.fill(docx, Integer.parseInt(moneyPerHourField.getText()));
        docxFiller.saveToFile(docx);
    }
    
}