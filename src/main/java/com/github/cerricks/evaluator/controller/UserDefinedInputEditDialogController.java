/*
 * Copyright 2017 cerricks.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cerricks.evaluator.controller;

import com.github.cerricks.evaluator.model.Input;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handles editing user defined input table entries.
 *
 * @author cerricks
 */
@Component
public class UserDefinedInputEditDialogController {

    private static final Logger logger = LoggerFactory.getLogger(UserDefinedInputEditDialogController.class);

    private Stage stage;

    private Input userDefinedInput;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextField amountField;

    public UserDefinedInputEditDialogController() {
    }

    @FXML
    private void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: UserDefinedInputEditDialogController");
        }

        amountField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the user defined input item to be edited in the dialog.
     *
     * @param userDefinedInput the item to be edited
     */
    public void setUserDefinedInput(final Input userDefinedInput) {
        this.userDefinedInput = userDefinedInput;

        categoryLabel.setText(userDefinedInput.getName());
        amountField.setText(Double.toString(userDefinedInput.getValue()));
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            userDefinedInput.setValue(Double.valueOf(amountField.getTextFormatter().getValue().toString()));

            stage.close();
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        boolean validInput = true;

        if (amountField.getText() == null || amountField.getText().length() == 0) {
            amountField.getStyleClass().add("error");

            validInput = false;
        } else {
            amountField.getStyleClass().remove("error");
        }

        return validInput;
    }

}