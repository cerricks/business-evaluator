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

import com.github.cerricks.evaluator.MainApp;
import com.github.cerricks.evaluator.model.NamedProperties;
import com.github.cerricks.evaluator.service.DebtRatioService;
import com.github.cerricks.evaluator.service.IncomeTaxService;
import com.github.cerricks.evaluator.service.LoanPaymentService;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import java.text.NumberFormat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Main application controller.
 *
 * @author cerricks
 */
@Component
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainApp mainApp;

    @Autowired
    private DebtRatioService debtRatioService;

    @Autowired
    private IncomeTaxService incomeTaxService;

    @Autowired
    private LoanPaymentService loanPaymentService;

    @Autowired
    private NamedProperties namedProperties;

    @Autowired
    private UserDefinedInputOverviewController userDefinedInputOverviewController;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem settingsMenuItem;

    @FXML
    private TextField askingPriceField;

    @FXML
    private TextField downPaymentField;

    @FXML
    private TextField originalCashFlowField;

    public MainController() {
    }

    @FXML
    protected void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: MainController");
        }

        // configure menu items
        exitMenuItem.setOnAction((ActionEvent t) -> {
            Platform.exit();
        });

        settingsMenuItem.setOnAction((ActionEvent t) -> {
            mainApp.showSettings();
        });

        // configure input fields
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumFractionDigits(0);
        currencyFormat.setMinimumFractionDigits(0);

        StringConverter currencyStringConverter = new CustomCurrencyStringConverter(currencyFormat);

        askingPriceField.textProperty().bindBidirectional(namedProperties.askingPriceProperty(), currencyStringConverter);
        askingPriceField.setTextFormatter(new TextFormatter(currencyStringConverter));
        downPaymentField.textProperty().bindBidirectional(namedProperties.downPaymentProperty(), currencyStringConverter);
        downPaymentField.setTextFormatter(new TextFormatter(currencyStringConverter));
        originalCashFlowField.textProperty().bindBidirectional(namedProperties.originalCashFlowProperty(), currencyStringConverter);
        originalCashFlowField.setTextFormatter(new TextFormatter(currencyStringConverter));
    }

    /**
     * Handles request to clear all inputs and reset results to default state.
     */
    @FXML
    private void handleClear() {
        // reset components
        namedProperties.reset();
        debtRatioService.reset();
        incomeTaxService.reset();
        loanPaymentService.reset();

        // reset controllers
        userDefinedInputOverviewController.reset();

        // clear input fields
        askingPriceField.clear();
        downPaymentField.clear();
        originalCashFlowField.clear();

        // reset focus
        askingPriceField.requestFocus();
    }

}
