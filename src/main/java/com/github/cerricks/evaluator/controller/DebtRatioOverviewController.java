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

import com.github.cerricks.evaluator.model.DebtRatio;
import com.github.cerricks.evaluator.service.DebtRatioService;
import com.github.cerricks.evaluator.ui.NumberTableCellFormatter;
import com.github.cerricks.evaluator.ui.PercentageTableCellFormatter;
import com.github.cerricks.evaluator.ui.TableRowMoveDownEventHandler;
import com.github.cerricks.evaluator.ui.TableRowMoveUpEventHandler;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
 * Handles displaying debt ratios in table form.
 *
 * @author cerricks
 */
@Component
public class DebtRatioOverviewController {

    private static final Logger logger = LoggerFactory.getLogger(DebtRatioOverviewController.class);

    @Autowired
    private DebtRatioService debtRatioService;

    @FXML
    private TableView<DebtRatio> debtRatioTable;

    @FXML
    private TableColumn<DebtRatio, String> nameColumn;

    @FXML
    private TableColumn<DebtRatio, Double> preDebtMultipleColumn;

    @FXML
    private TableColumn<DebtRatio, Double> preDebtPercentageColumn;

    @FXML
    private TableColumn<DebtRatio, Double> postTaxMultipleColumn;

    @FXML
    private TableColumn<DebtRatio, Double> postTaxPercentageColumn;

    public DebtRatioOverviewController() {
    }

    @FXML
    private void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: DebtRatioOverviewController");
        }

        // configure table
        debtRatioTable.setItems(debtRatioService.getRatios());
        debtRatioTable.setRowFactory((TableView<DebtRatio> tableView) -> {
            final TableRow<DebtRatio> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            final ContextMenu tableMenu = tableView.getContextMenu();
            if (tableMenu != null) {
                rowMenu.getItems().addAll(tableMenu.getItems());
                rowMenu.getItems().add(new SeparatorMenuItem());
            }

            MenuItem moveUpItem = new MenuItem("Move Up");
            moveUpItem.setOnAction(new TableRowMoveUpEventHandler(debtRatioTable));

            MenuItem moveDownItem = new MenuItem("Move Down");
            moveDownItem.setOnAction(new TableRowMoveDownEventHandler(debtRatioTable));

            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((ActionEvent event) -> {
                debtRatioService.removeRatio(row.getItem());
            });

            rowMenu.getItems().addAll(moveUpItem, moveDownItem, new SeparatorMenuItem(), removeItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu) null));

            return row;
        });

        // configure table columns
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        preDebtMultipleColumn.setCellValueFactory(cellData -> cellData.getValue().preDebtMultipleProperty().asObject());
        preDebtMultipleColumn.setCellFactory(new NumberTableCellFormatter());
        preDebtPercentageColumn.setCellValueFactory(cellData -> cellData.getValue().preDebtPercentageProperty().asObject());
        preDebtPercentageColumn.setCellFactory(new PercentageTableCellFormatter());
        postTaxMultipleColumn.setCellValueFactory(cellData -> cellData.getValue().postTaxMultipleProperty().asObject());
        postTaxMultipleColumn.setCellFactory(new NumberTableCellFormatter());
        postTaxPercentageColumn.setCellValueFactory(cellData -> cellData.getValue().postTaxPercentageProperty().asObject());
        postTaxPercentageColumn.setCellFactory(new PercentageTableCellFormatter());
    }

}
