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
import com.github.cerricks.evaluator.model.InputCategory;
import static com.github.cerricks.evaluator.model.InputSource.SYSTEM;
import com.github.cerricks.evaluator.model.InputType;
import com.github.cerricks.evaluator.model.LoanRate;
import com.github.cerricks.evaluator.model.LoanTerm;
import com.github.cerricks.evaluator.model.LoanTermUnit;
import static com.github.cerricks.evaluator.model.LoanTermUnit.MONTHS;
import static com.github.cerricks.evaluator.model.LoanTermUnit.YEARS;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Handles managing input category settings.
 *
 * @author cerricks
 */
@Component
public class InputCategorySettingsController {

    private static final Logger logger = LoggerFactory.getLogger(InputCategorySettingsController.class);

    private final ObservableList<InputCategory> inputCategories_tmp = FXCollections.observableArrayList();

    @Autowired
    private MainApp mainApp;

    @Autowired
    private InputCategoryRepository inputCategoryRepository;

    @Autowired
    @Qualifier("inputCategories")
    private ObservableList<InputCategory> inputCategories;

    @FXML
    private TableView<InputCategory> inputCategoryTable;

    @FXML
    private TableColumn<InputCategory, String> nameColumn;

    @FXML
    private TableColumn<InputCategory, String> loanRateColumn;

    @FXML
    private TableColumn<InputCategory, InputType> typeColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField rateField;

    @FXML
    private Spinner<Integer> termField;

    @FXML
    private ComboBox<LoanTermUnit> termUnitField;

    public InputCategorySettingsController() {
    }

    @FXML
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: InputCategorySettingsController");
        }

        inputCategories_tmp.setAll(inputCategories);

        inputCategoryTable.setItems(inputCategories_tmp);
        inputCategoryTable.setRowFactory((TableView<InputCategory> tableView) -> {
            final TableRow<InputCategory> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            final ContextMenu tableMenu = tableView.getContextMenu();
            if (tableMenu != null) {
                rowMenu.getItems().addAll(tableMenu.getItems());
                rowMenu.getItems().add(new SeparatorMenuItem());
            }

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction((ActionEvent event) -> {
                mainApp.showInputCategoryEditDialog(row.getItem());
            });

            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((ActionEvent event) -> {
                // prevent name from being modified on system properties
                if (row.getItem().getSource() == SYSTEM) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Delete Failed");
                    alert.setContentText("System defined categories cannot be deleted.");

                    alert.showAndWait();
                } else {
                    inputCategories_tmp.remove(row.getItem());
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

        // configure table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        loanRateColumn.setCellValueFactory(new PropertyValueFactory("loanRate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));

        // configure input fields
        rateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));
        termField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 1));
        termUnitField.getItems().addAll(LoanTermUnit.values());
        termUnitField.getSelectionModel().select(YEARS);
    }

    @FXML
    public void handleAdd() {
        String name = nameField.getText();
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

        InputCategory category = new InputCategory(name);
        category.setDefaultLoanRate(new LoanRate(rate, loanTerm));

        inputCategories_tmp.add(category);

        clearInput();
    }

    private void clearInput() {
        nameField.clear();
        rateField.clear();
        termField.getValueFactory().setValue(1);
        termUnitField.getSelectionModel().select(YEARS);
    }

    public void saveSettings() {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving cost category settings");
        }

        // TODO: check if rates need to be updated
        boolean updateRatesRequired = true;

        if (updateRatesRequired) {
            // delete categories that were removed from list
            for (InputCategory costCategory : inputCategories) {
                if (!inputCategories_tmp.contains(costCategory)) {
                    inputCategoryRepository.delete(costCategory);
                }
            }

            // save categories
            inputCategoryRepository.save(inputCategories_tmp);

            // reload categories
            inputCategories.setAll(inputCategoryRepository.findAll());
        }
    }

}
