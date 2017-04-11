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
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Button createCategoryButton;

    public InputCategorySettingsController() {
    }

    @FXML
    protected void initialize() {
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
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        loanRateColumn.setCellValueFactory(cellData -> cellData.getValue().defaultLoanRateProperty().asString());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        // configure button to add new items
        createCategoryButton.setOnAction((ActionEvent t) -> {
            mainApp.showInputCategoryCreateDialog();
        });
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

    public void addTemporaryItem(final InputCategory category) {
        inputCategories_tmp.add(category);
    }

    public void cancel() {
        inputCategories_tmp.clear();

        // revert any changes
        inputCategories.setAll(inputCategoryRepository.findAll());
    }

}
