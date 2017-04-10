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
package com.github.cerricks.evaluator.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;

/**
 * Event handler for moving a table row up.
 *
 * @author cerricks
 */
public class TableRowMoveUpEventHandler implements EventHandler<ActionEvent> {

    private final TableView table;

    public TableRowMoveUpEventHandler(final TableView table) {
        this.table = table;
    }

    @Override
    public void handle(final ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex > 0) {
            Object removedItem = table.getItems().remove(selectedIndex);

            table.getItems().add(selectedIndex - 1, removedItem);

            table.getSelectionModel().select(selectedIndex - 1);
        }
    }

}
