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

import com.github.cerricks.evaluator.model.InputCategory;
import static com.github.cerricks.evaluator.model.InputSource.SYSTEM;
import com.github.cerricks.evaluator.model.LoanRate;
import com.github.cerricks.evaluator.model.LoanTerm;
import com.github.cerricks.evaluator.model.LoanTermUnit;
import static com.github.cerricks.evaluator.model.LoanTermUnit.MONTHS;
import static com.github.cerricks.evaluator.model.LoanTermUnit.YEARS;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author cerricks
 */
@Component
public class InputCategoryEditDialogController {

    private static final Logger logger = LoggerFactory.getLogger(InputCategoryEditDialogController.class);

    private Stage stage;

    private InputCategory inputCategory;

    @FXML
    private TextField nameField;

    @FXML
    private TextField rateField;

    @FXML
    private Spinner<Integer> termField;

    @FXML
    private ComboBox<LoanTermUnit> termUnitField;

    public InputCategoryEditDialogController() {
    }

    @FXML
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: InputCategoryEditDialogController");
        }

        rateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));
        termField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 1));
        termUnitField.getItems().addAll(LoanTermUnit.values());
        termUnitField.getSelectionModel().select(YEARS);
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    public void setInputCategory(final InputCategory inputCategory) {
        this.inputCategory = inputCategory;

        nameField.setText(this.inputCategory.getName());

        if (this.inputCategory.getDefaultLoanRate() != null) {
            rateField.setText(Double.toString(this.inputCategory.getDefaultLoanRate().getRate() * 100));
            termField.getValueFactory().setValue(this.inputCategory.getDefaultLoanRate().getTerm().getLength());
            termUnitField.setValue(this.inputCategory.getDefaultLoanRate().getTerm().getUnit());
        } else {
            rateField.setText(null);
            termField.getValueFactory().setValue(1);
            termUnitField.getSelectionModel().select(YEARS);
        }

        // prevent name from being modified on system properties
        if (inputCategory.getSource() == SYSTEM) {
            nameField.setEditable(false);
        }
    }

    /**
     * Called when the user clicks OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            String name = nameField.getText();

            inputCategory.setName(name);

            if (StringUtils.hasText(rateField.getText())) {
                double rate = Double.valueOf(rateField.getTextFormatter().getValue().toString());
                int term = termField.getValue();
                LoanTermUnit termUnit = termUnitField.getSelectionModel().getSelectedItem();

                LoanTerm loanTerm = null;

                switch (termUnit) {
                    case YEARS:
                        loanTerm = LoanTerm.ofYears(term);
                        break;

                    case MONTHS:
                        loanTerm = LoanTerm.ofMonths(term);
                        break;
                }

                inputCategory.setDefaultLoanRate(new LoanRate(rate, loanTerm));
            }

            stage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
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

        if (!StringUtils.hasText(nameField.getText())) {
            nameField.getStyleClass().add("error");

            validInput = false;
        } else {
            nameField.getStyleClass().remove("error");
        }

        return validInput;
    }

}
