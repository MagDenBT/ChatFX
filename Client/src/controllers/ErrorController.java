package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorController {

    @FXML
    private Label lErrorText;

    public void setlErrorText(String errorText) {
        lErrorText.setText(errorText);
    }
}
