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
import com.github.cerricks.evaluator.dao.InputCategoryRepository;
import com.github.cerricks.evaluator.model.Input;
import com.github.cerricks.evaluator.model.InputCategory;
import static com.github.cerricks.evaluator.model.InputSource.USER;
import com.github.cerricks.evaluator.model.InputType;
import com.github.cerricks.evaluator.model.NamedProperties;
import com.github.cerricks.evaluator.service.IncomeTaxService;
import com.github.cerricks.evaluator.service.LoanPaymentService;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import com.github.cerricks.evaluator.util.FormatUtil;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Handles displaying view of custom user defined input amounts.
 *
 * @author cerricks
 */
@Component
public class UserDefinedInputOverviewController {

    private static final Logger logger = LoggerFactory.getLogger(UserDefinedInputOverviewController.class);

    //private final DoubleProperty totalAdditionalCost = new SimpleDoubleProperty(0);
    @Autowired
    @Qualifier("inputCategories")
    private ObservableList<InputCategory> inputCategories;

    @Autowired
    @Qualifier("userDefinedInputCategories")
    private ObservableList<InputCategory> userDefinedInputCategories;

    @Autowired
    private NamedProperties namedProperties;

    @Autowired
    private ObservableList<Input> userDefinedInputs;

    @Autowired
    private MainApp mainApp;

    @Autowired
    private InputCategoryRepository inputCategoryRepository;

    @Autowired
    private LoanPaymentService loanPaymentService;

    @Autowired
    private IncomeTaxService incomeTaxService;

    @FXML
    private Label totalAdditionalCostLabel;

    @FXML
    private TableView<Input> userDefinedInputTable;

    @FXML
    private TableColumn<Input, InputCategory> categoryColumn;

    @FXML
    private TableColumn<Input, Double> valueColumn;

    @FXML
    private ComboBox<InputCategory> categoryField;

    @FXML
    private TextField valueField;

    @FXML
    private Button createInputButton;

    public UserDefinedInputOverviewController() {
    }

    @FXML
    private void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: UserDefinedInputOverviewController");
        }

        totalAdditionalCostLabel.textProperty().bindBidirectional(namedProperties.totalAdditionalExpenseProperty(), new CustomCurrencyStringConverter());

        userDefinedInputs.addListener((Change<? extends Input> change) -> {
            calculateTotalAdditionalCost();

            incomeTaxService.calculateTaxableIncome();
        });

        userDefinedInputTable.setItems(userDefinedInputs);
        userDefinedInputTable.setRowFactory((TableView<Input> tableView) -> {
            final TableRow<Input> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            final ContextMenu tableMenu = tableView.getContextMenu();
            if (tableMenu != null) {
                rowMenu.getItems().addAll(tableMenu.getItems());
                rowMenu.getItems().add(new SeparatorMenuItem());
            }

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction((ActionEvent event) -> {
                mainApp.showUserDefinedInputEditDialog(row.getItem());

                this.calculateTotalAdditionalCost();

                loanPaymentService.processDefaultLoan(row.getItem().getName(), row.getItem().getValue());
            });

            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((ActionEvent event) -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete Item");
                alert.setHeaderText("Delete '" + row.getItem().getName() + "'");
                alert.setContentText("Are you sure you want to delete this item? Any loan entered for this category will also be deleted.");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    userDefinedInputs.remove(row.getItem());

                    loanPaymentService.removeLoanByName(row.getItem().getName());
                }
            });

            rowMenu.getItems().addAll(editItem, removeItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu) null));

            return row;
        });

        categoryColumn.setCellValueFactory(new PropertyValueFactory("name"));

        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
        valueColumn.setCellFactory(param -> new TableCell<Input, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);

                if (value == null || empty) {
                    setText(null);
                } else {
                    int index = indexProperty().get() < 0 ? 0 : indexProperty().get();

                    InputType type = param.getTableView().getItems().get(index).getCategory().getType();

                    switch (type) {
                        case CURRENCY:
                            setText(FormatUtil.formatCurrency(value));
                            break;

                        case GENERAL_NUMBER:
                            setText(FormatUtil.formatNumber(value));
                            break;

                        default:
                            setText(value.toString());
                    }
                }
            }
        });

        categoryField.setItems(userDefinedInputCategories);
        categoryField.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                if (categoryField.getSelectionModel().getSelectedIndex() == -1) {
                    String name = categoryField.getEditor().getText();

                    if (name == null) {
                        categoryField.setValue(null);
                    } else {
                        InputCategory category = inputCategoryRepository.findByName(name);

                        if (category == null) {
                            category = new InputCategory(name);
                            category.setSource(USER);
                        }

                        categoryField.setValue(category);
                    }
                }
            }
        });

        valueField.setTextFormatter(new TextFormatter(new NumberStringConverter()));

        // configure button to add new items
        createInputButton.setOnAction((ActionEvent t) -> {
            mainApp.showUserDefinedInputCreateDialog();
        });
    }

    @FXML
    public void handleAdd() {
        InputCategory category = categoryField.getValue();
        Double amount = Double.valueOf(valueField.getTextFormatter().getValue().toString());

        Input customInput = new Input(category, amount);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Adding custom input [").append(customInput).append("] to table").toString());
        }

        userDefinedInputs.add(customInput);

        this.calculateTotalAdditionalCost();

        if (!userDefinedInputCategories.contains(category)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding new user defined input category: " + category);
            }

            inputCategories.add(category);

            inputCategoryRepository.save(category);
        }

        if (category.getDefaultLoanRate() != null) {
            loanPaymentService.processDefaultLoan(category.getName(), amount);
        }

        clearInput();
    }

    /**
     * Resets this entire view to a default state.
     */
    public void reset() {
        clearInput();

        namedProperties.totalAdditionalExpenseProperty().set(0);
        userDefinedInputs.clear();
    }

    /**
     * Clears input fields to a default state.
     */
    private void clearInput() {
        categoryField.setValue(null);
        valueField.clear();
    }

    /**
     * Calculates the sum all additional input amounts.
     */
    public void calculateTotalAdditionalCost() {
        double total = 0;

        for (Input item : userDefinedInputs) {
            if (item.getCategory().isAdditionalExpense()) {
                total += item.getValue();
            }
        }

        namedProperties.totalAdditionalExpenseProperty().set(total);
    }

}
