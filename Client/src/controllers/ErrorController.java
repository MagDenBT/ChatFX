package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorController {

    @FXML
    private Label lErrorText;

    public void setLErrorText(String errorText) {
        lErrorText.setText(errorText);
    }
}
