package dev.lukaszmichalak;

import dev.lukaszmichalak.fillers.DocxFiller;
import dev.lukaszmichalak.fillers.Filler;
import dev.lukaszmichalak.fillers.XlsxFiller;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ooxml.POIXMLDocument;

public class AppController implements Initializable {

  private static final Logger log = LogManager.getLogger(AppController.class);

  private static final Filler xlsxFiller = new XlsxFiller();
  private static final Filler docxFiller = new DocxFiller();

  private File docxFile = null;
  private File xlsxFile = null;

  @FXML private TextField moneyPerHourField;

  @FXML private Button docxButton;

  @FXML private Button xlsxButton;

  @FXML private Button generateButton;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    docxButton.setDisable(true);
    generateButton.setDisable(true);
    moneyPerHourField.setDisable(true);

    moneyPerHourField.addEventFilter(
        KeyEvent.KEY_TYPED,
        event -> {
          if (!event.getCharacter().matches("[0-9]")) {
            event.consume();
          }
        });

    moneyPerHourField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> generateButton.setDisable(newValue.isBlank()));
  }

  @FXML
  private void onXlsxClick(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select xlsx file");

    FileChooser.ExtensionFilter xlsxFilter =
        new FileChooser.ExtensionFilter("Xlsx files (*.xlsx)", "*.xlsx");
    fileChooser.getExtensionFilters().add(xlsxFilter);

    xlsxFile = fileChooser.showOpenDialog(App.mainStage);

    if (xlsxFile != null) {
      System.out.println("Selected file: " + xlsxFile.getAbsolutePath());
      docxButton.setDisable(false);
    }
  }

  @FXML
  private void onDocxClick(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select docx file");

    FileChooser.ExtensionFilter xlsxFilter =
        new FileChooser.ExtensionFilter("Docx files (*.docx)", "*.docx");
    fileChooser.getExtensionFilters().add(xlsxFilter);

    docxFile = fileChooser.showOpenDialog(App.mainStage);

    if (docxFile != null) {
      System.out.println("Selected file: " + docxFile.getAbsolutePath());
      moneyPerHourField.setDisable(false);
    }
  }

  @FXML
  private void onGenerateClick(ActionEvent actionEvent) {
    log.info("Rozpoczęto wypełnianie dla stawki " + moneyPerHourField.getText());
    try {
      POIXMLDocument workbook = xlsxFiller.getDocumentFromFile(xlsxFile);
      workbook = xlsxFiller.fill(workbook, Integer.parseInt(moneyPerHourField.getText()));
      xlsxFiller.saveToFile(workbook);

      POIXMLDocument docx = docxFiller.getDocumentFromFile(docxFile);
      docx = docxFiller.fill(docx, Integer.parseInt(moneyPerHourField.getText()));
      docxFiller.saveToFile(docx);

      showSuccess();
    } catch (Exception e) {
      showError(e);
    }
  }

  private void showSuccess() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Success");
    alert.setHeaderText(null);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    alert.setContentText("Successfully generated files");

    alert.showAndWait();
  }

  private void showError(Exception e) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    alert.setContentText("A generation error occurred! " + e.getMessage());

    alert.showAndWait();
  }
}
