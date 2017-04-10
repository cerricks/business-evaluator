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
import com.github.cerricks.evaluator.model.IncomeTaxPayment;
import com.github.cerricks.evaluator.service.IncomeTaxService;
import com.github.cerricks.evaluator.ui.CurrencyTableCellFormatter;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.cerricks.evaluator.service.NamedPropertyService;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TAXABLE_INCOME;

/**
 * Handles displaying view of computed income tax payments.
 *
 * @author cerricks
 */
@Component
public class IncomeTaxPaymentOverviewController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeTaxPaymentOverviewController.class);

    @Autowired
    private IncomeTaxService incomeTaxService;

    @Autowired
    private NamedPropertyService namedValueService;

    @FXML
    private Label taxableIncomeLabel;

    @FXML
    private TableView<IncomeTaxPayment> incomeTaxPaymentTable;

    @FXML
    private TableColumn<IncomeTaxPayment, FilingStatus> filingStatusColumn;

    @FXML
    private TableColumn<IncomeTaxPayment, Double> totalTaxColumn;

    @FXML
    private TableColumn<IncomeTaxPayment, Double> totalTaxPerMonthColumn;

    @FXML
    private TableColumn<IncomeTaxPayment, Double> incomeAfterTaxColumn;

    public IncomeTaxPaymentOverviewController() {
    }

    @FXML
    private void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: IncomeTaxPaymentOverviewController");
        }

        taxableIncomeLabel.textProperty().bindBidirectional(namedValueService.getByName(PROPERTY_TAXABLE_INCOME), new CustomCurrencyStringConverter());

        // configure table
        incomeTaxPaymentTable.setItems(incomeTaxService.getIncomeTaxPayments());

        // configure table columns
        filingStatusColumn.setCellValueFactory(new PropertyValueFactory("filingStatus"));

        totalTaxColumn.setCellValueFactory(new PropertyValueFactory("totalTax"));
        totalTaxColumn.setCellFactory(new CurrencyTableCellFormatter());

        totalTaxPerMonthColumn.setCellValueFactory(new PropertyValueFactory("totalTaxMonthly"));
        totalTaxPerMonthColumn.setCellFactory(new CurrencyTableCellFormatter());

        incomeAfterTaxColumn.setCellValueFactory(new PropertyValueFactory("totalIncomeAfterTax"));
        incomeAfterTaxColumn.setCellFactory(new CurrencyTableCellFormatter());
    }

}