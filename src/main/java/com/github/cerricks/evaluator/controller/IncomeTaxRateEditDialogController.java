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

import com.github.cerricks.evaluator.model.FilingStatus;
import com.github.cerricks.evaluator.model.IncomeTaxRate;
import com.github.cerricks.evaluator.model.NumberRange;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import java.text.NumberFormat;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Handles editing income tax rates.
 *
 * @author cerricks
 */
@Component
public class IncomeTaxRateEditDialogController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeTaxRateEditDialogController.class);

    private Stage stage;

    private IncomeTaxRate incomeTaxRate;

    @FXML
    private ComboBox<FilingStatus> filingStatusField;

    @FXML
    private TextField taxableIncomeFromField;

    @FXML
    private TextField taxableIncomeToField;

    @FXML
    private TextField flatTaxField;

    @FXML
    private TextField taxRateField;

    public IncomeTaxRateEditDialogController() {
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    public void setIncomeTaxRate(final IncomeTaxRate incomeTaxRate) {
        this.incomeTaxRate = incomeTaxRate;

        filingStatusField.setValue(incomeTaxRate.getFilingStatus());
        taxableIncomeFromField.setText(Double.toString(incomeTaxRate.getTaxableIncomeRange().getRangeStart()));
        taxableIncomeToField.setText(incomeTaxRate.getTaxableIncomeRange().getRangeEnd() == null ? "" : Double.toString(incomeTaxRate.getTaxableIncomeRange().getRangeEnd()));
        flatTaxField.setText(Double.toString(incomeTaxRate.getFlatTax()));
        taxRateField.setText(Double.toString(incomeTaxRate.getTaxRate() * 100));
    }

    @FXML
    protected void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: IncomeTaxRateEditDialogController");
        }

        filingStatusField.getItems().addAll(FilingStatus.values());
        taxableIncomeFromField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        taxableIncomeToField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        flatTaxField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        taxRateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            FilingStatus filingStatus = filingStatusField.getSelectionModel().getSelectedItem();
            Double incomeTaxFrom = Double.valueOf(taxableIncomeFromField.getTextFormatter().getValue().toString());
            Double incomeTaxTo = Double.valueOf(taxableIncomeToField.getTextFormatter().getValue().toString());
            Double flatTax = Double.valueOf(flatTaxField.getTextFormatter().getValue().toString());
            Double taxRate = Double.valueOf(taxRateField.getTextFormatter().getValue().toString());

            incomeTaxRate.setFilingStatus(filingStatus);
            incomeTaxRate.setTaxableIncomeRange(new NumberRange(incomeTaxFrom, incomeTaxTo, NumberFormat.getCurrencyInstance()));
            incomeTaxRate.setFlatTax(flatTax);
            incomeTaxRate.setTaxRate(taxRate);

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

        if (filingStatusField.getSelectionModel().getSelectedIndex() < 0) {
            filingStatusField.getStyleClass().add("error");

            validInput = false;
        } else {
            filingStatusField.getStyleClass().remove("error");
        }

        if (!StringUtils.hasText(taxableIncomeFromField.getText())) {
            taxableIncomeFromField.getStyleClass().add("error");

            validInput = false;
        } else {
            taxableIncomeFromField.getStyleClass().remove("error");
        }

        if (!StringUtils.hasText(taxRateField.getText())) {
            taxRateField.getStyleClass().add("error");

            validInput = false;
        } else {
            taxRateField.getStyleClass().remove("error");
        }

        return validInput;
    }

}
