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

import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_AMOUNT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_PAYMENT;
import com.github.cerricks.evaluator.MainApp;
import com.github.cerricks.evaluator.model.Input;
import com.github.cerricks.evaluator.model.InputCategory;
import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.model.LoanRate;
import com.github.cerricks.evaluator.model.LoanTerm;
import com.github.cerricks.evaluator.model.LoanTermUnit;
import static com.github.cerricks.evaluator.model.LoanTermUnit.MONTHS;
import static com.github.cerricks.evaluator.model.LoanTermUnit.YEARS;
import com.github.cerricks.evaluator.service.LoanPaymentService;
import com.github.cerricks.evaluator.service.NamedPropertyService;
import com.github.cerricks.evaluator.ui.CurrencyTableCellFormatter;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Handles displaying view of entered loan payments.
 *
 * @author cerricks
 */
@Component
public class LoanPaymentOverviewController {

    private static final Logger logger = LoggerFactory.getLogger(LoanPaymentOverviewController.class);

    @Autowired
    private MainApp mainApp;

    @Autowired
    @Qualifier("additionalCostInputCategories")
    private ObservableList<InputCategory> additionalCostInputCategories;

    @Autowired
    private ObservableList<Input> userDefinedInputs;

    @Autowired
    private LoanPaymentService loanPaymentService;

    @Autowired
    private NamedPropertyService namedValueService;

    @FXML
    private Label totalLoanAmountLabel;

    @FXML
    private Label totalLoanPaymentLabel;

    @FXML
    private TableView<LoanPayment> loanTable;

    @FXML
    private TableColumn<LoanPayment, InputCategory> categoryColumn;

    @FXML
    private TableColumn<LoanPayment, String> loanColumn;

    @FXML
    private TableColumn<LoanPayment, Double> monthlyPaymentColumn;

    @FXML
    private TableColumn<LoanPayment, Double> annualPaymentColumn;

    @FXML
    private TableColumn<LoanPayment, Double> totalInterestPaymentColumn;

    @FXML
    private ComboBox<InputCategory> categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField rateField;

    @FXML
    private Spinner<Integer> termField;

    @FXML
    private ComboBox<LoanTermUnit> termUnitField;

    public LoanPaymentOverviewController() {
    }

    @FXML
    private void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: LoanPaymentOverviewController");
        }

        totalLoanAmountLabel.textProperty().bindBidirectional(namedValueService.getByName(PROPERTY_TOTAL_LOAN_AMOUNT), new CustomCurrencyStringConverter());
        totalLoanPaymentLabel.textProperty().bindBidirectional(namedValueService.getByName(PROPERTY_TOTAL_LOAN_PAYMENT), new CustomCurrencyStringConverter());

        // configure table
        loanTable.setItems(loanPaymentService.getLoanPayments());
        loanTable.setRowFactory((TableView<LoanPayment> tableView) -> {
            final TableRow<LoanPayment> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            final ContextMenu tableMenu = tableView.getContextMenu();
            if (tableMenu != null) {
                rowMenu.getItems().addAll(tableMenu.getItems());
                rowMenu.getItems().add(new SeparatorMenuItem());
            }

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction((event) -> {
                mainApp.showLoanEditDialog(row.getItem());
            });

            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((event) -> {
                loanPaymentService.removeLoanByName(row.getItem().getCategory().getName());
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
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        loanColumn.setCellValueFactory(cellData -> cellData.getValue().loanProperty().asString());

        monthlyPaymentColumn.setCellValueFactory(cellData -> cellData.getValue().monthlyPaymentProperty().asObject());
        monthlyPaymentColumn.setCellFactory(new CurrencyTableCellFormatter());

        annualPaymentColumn.setCellValueFactory(cellData -> cellData.getValue().annualPaymentProperty().asObject());
        annualPaymentColumn.setCellFactory(new CurrencyTableCellFormatter());

        totalInterestPaymentColumn.setCellValueFactory(cellData -> cellData.getValue().totalInterestPaymentProperty().asObject());
        totalInterestPaymentColumn.setCellFactory(new CurrencyTableCellFormatter());

        // configure input fields
        categoryField.setItems(additionalCostInputCategories);
        categoryField.getSelectionModel().selectedItemProperty().addListener((observable, oldCategory, newCategory) -> {
            if (newCategory != null) {
                amountField.clear();

                // try to set amount from user defined inputs
                for (Input input : userDefinedInputs) {
                    if (input.getName().equals(newCategory.getName())) {
                        amountField.setText(Double.toString(input.getValue()));

                        break;
                    }
                }

                LoanRate loanRate = newCategory.getDefaultLoanRate();

                if (loanRate != null) {
                    rateField.setText(Double.toString(loanRate.getRate() * 100));
                    termField.getValueFactory().setValue(loanRate.getTerm().getLength());
                    termUnitField.getSelectionModel().select(loanRate.getTerm().getUnit());
                } else {
                    rateField.clear();
                    termField.getValueFactory().setValue(1);
                    termUnitField.getSelectionModel().select(YEARS);
                }
            }
        });

        amountField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));

        rateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));

        termField.setValueFactory(new IntegerSpinnerValueFactory(1, 60, 1));

        termUnitField.getItems().addAll(LoanTermUnit.values());
        termUnitField.getSelectionModel().select(YEARS);
    }

    @FXML
    private void handleAdd() {
        InputCategory category = categoryField.getSelectionModel().getSelectedItem();
        Double amount = Double.valueOf(amountField.getTextFormatter().getValue().toString());
        Double rate = Double.valueOf(rateField.getTextFormatter().getValue().toString());
        Integer term = termField.getValue();
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

        loanPaymentService.addLoan(category.getName(), amount, new LoanRate(rate, loanTerm));

        clearInput();
    }

    private void clearInput() {
        categoryField.setValue(null);
        amountField.clear();
        rateField.clear();
        termField.getValueFactory().setValue(1);
        termUnitField.getSelectionModel().select(YEARS);
    }

}
