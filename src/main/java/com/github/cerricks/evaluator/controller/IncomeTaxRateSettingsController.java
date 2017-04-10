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
import com.github.cerricks.evaluator.dao.IncomeTaxRateRepository;
import com.github.cerricks.evaluator.model.FilingStatus;
import com.github.cerricks.evaluator.model.IncomeTaxRate;
import com.github.cerricks.evaluator.model.IncomeTaxRateTable;
import com.github.cerricks.evaluator.ui.CurrencyTableCellFormatter;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import com.github.cerricks.evaluator.ui.PercentageTableCellFormatter;
import com.github.cerricks.evaluator.util.IncomeTaxPaymentCalculator;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author cerricks
 */
@Component
public class IncomeTaxRateSettingsController {

    private static final Logger logger = LoggerFactory.getLogger(IncomeTaxRateSettingsController.class);

    private final ObservableList<IncomeTaxRate> incomeTaxRates_tmp = FXCollections.observableArrayList();

    @Autowired
    private MainApp mainApp;

    @Autowired
    private IncomeTaxRateRepository incomeTaxRateRepository;

    @Autowired
    private IncomeTaxPaymentCalculator incomeTaxPaymentCalculator;

    @Autowired
    private ObservableList<IncomeTaxRate> incomeTaxRates;

    @FXML
    private TableView<IncomeTaxRate> incomeTaxRateTable;

    @FXML
    private TableColumn<IncomeTaxRate, FilingStatus> filingStatusColumn;

    @FXML
    private TableColumn<IncomeTaxRate, String> taxableIncomeColumn;

    @FXML
    private TableColumn<IncomeTaxRate, Double> flatTaxColumn;

    @FXML
    private TableColumn<IncomeTaxRate, Double> taxRateColumn;

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

    public IncomeTaxRateSettingsController() {
    }

    @FXML
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: IncomeTaxRateSettingsController");
        }

        incomeTaxRates_tmp.setAll(incomeTaxRates);

        incomeTaxRateTable.setItems(incomeTaxRates_tmp);
        incomeTaxRateTable.setRowFactory((TableView<IncomeTaxRate> tableView) -> {
            final TableRow<IncomeTaxRate> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            final ContextMenu tableMenu = tableView.getContextMenu();
            if (tableMenu != null) {
                rowMenu.getItems().addAll(tableMenu.getItems());
                rowMenu.getItems().add(new SeparatorMenuItem());
            }

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction((ActionEvent event) -> {
                mainApp.showIncomeTaxRateEditDialog(row.getItem());
            });

            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((ActionEvent event) -> {
                incomeTaxRates_tmp.remove(row.getItem());
            });

            rowMenu.getItems().addAll(editItem, removeItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu) null));

            return row;
        });

        // configure table columns
        filingStatusColumn.setCellValueFactory(cellData -> cellData.getValue().filingStatusProperty());
        taxableIncomeColumn.setCellValueFactory(cellData -> cellData.getValue().taxableIncomeRangeProperty().asString());
        flatTaxColumn.setCellValueFactory(cellData -> cellData.getValue().flatTaxProperty().asObject());
        flatTaxColumn.setCellFactory(new CurrencyTableCellFormatter());
        taxRateColumn.setCellValueFactory(cellData -> cellData.getValue().taxRateProperty().asObject());
        taxRateColumn.setCellFactory(new PercentageTableCellFormatter());

        // configure input fields
        filingStatusField.getItems().addAll(FilingStatus.values());
        taxableIncomeFromField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        taxableIncomeToField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        flatTaxField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        taxRateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));
    }

    @FXML
    public void handleAdd() {
        FilingStatus filingStatus = filingStatusField.getSelectionModel().getSelectedItem();
        Double incomeTaxFrom = Double.valueOf(taxableIncomeFromField.getTextFormatter().getValue().toString());
        Double incomeTaxTo = taxableIncomeToField.getText() == null || taxableIncomeToField.getText().length() == 0 ? Double.NaN : Double.valueOf(taxableIncomeToField.getTextFormatter().getValue().toString());
        Double flatTax = Double.valueOf(flatTaxField.getTextFormatter().getValue().toString());
        Double taxRate = Double.valueOf(taxRateField.getTextFormatter().getValue().toString());

        IncomeTaxRate incomeTaxRate = new IncomeTaxRate(filingStatus, incomeTaxFrom, incomeTaxTo, flatTax, taxRate);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Adding income tax rate [").append(incomeTaxRate).append("]").toString());
        }

        incomeTaxRates_tmp.add(incomeTaxRate);

        resetInput();
    }

    private void resetInput() {
        filingStatusField.getSelectionModel().selectFirst();
        taxableIncomeFromField.clear();
        taxableIncomeToField.clear();
        flatTaxField.clear();
        taxRateField.clear();
    }

    public void saveSettings() {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving income tax rate settings");
        }

        // TODO: check if rates need to be updated
        boolean updateRatesRequired = true;

        if (updateRatesRequired) {
            incomeTaxRates.removeAll(incomeTaxRates_tmp);

            if (!incomeTaxRates.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Deleting rates from database [" + incomeTaxRates + "]");
                }

                incomeTaxRateRepository.delete(incomeTaxRates);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Saving rates to database [" + incomeTaxRates + "]");
            }

            incomeTaxRateRepository.save(incomeTaxRates_tmp);

            if (logger.isDebugEnabled()) {
                logger.debug("Reloading rates from database");
            }

            IncomeTaxRateTable newRateTable = new IncomeTaxRateTable(incomeTaxRateRepository.findAll());

            incomeTaxPaymentCalculator.setRateTable(newRateTable);

            incomeTaxRates.setAll(newRateTable.getRates());
        }
    }

}
