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

import static com.github.cerricks.evaluator.Constants.PROPERTY_ASKING_PRICE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_DOWN_PAYMENT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_ORIGINAL_CASH_FLOW;
import com.github.cerricks.evaluator.MainApp;
import com.github.cerricks.evaluator.model.NamedProperty;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import java.text.NumberFormat;
import java.util.Map;
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
    private Map<String, NamedProperty> properties;

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
    private void initialize() {
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

        askingPriceField.textProperty().bindBidirectional(properties.get(PROPERTY_ASKING_PRICE), currencyStringConverter);
        askingPriceField.setTextFormatter(new TextFormatter(currencyStringConverter));

        downPaymentField.textProperty().bindBidirectional(properties.get(PROPERTY_DOWN_PAYMENT), currencyStringConverter);
        downPaymentField.setTextFormatter(new TextFormatter(currencyStringConverter));

        originalCashFlowField.textProperty().bindBidirectional(properties.get(PROPERTY_ORIGINAL_CASH_FLOW), currencyStringConverter);
        originalCashFlowField.setTextFormatter(new TextFormatter(currencyStringConverter));
    }

    /**
     * Handles request to clear all inputs and reset results to default state.
     */
    @FXML
    private void handleClear() {
        // clear input fields
        askingPriceField.clear();
        downPaymentField.clear();
        originalCashFlowField.clear();

        // reset focus
        askingPriceField.requestFocus();
    }

}
