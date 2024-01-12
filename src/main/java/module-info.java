module dev.lukaszmichalak {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.poi.ooxml;
    
    opens dev.lukaszmichalak to javafx.fxml;
    exports dev.lukaszmichalak;
}