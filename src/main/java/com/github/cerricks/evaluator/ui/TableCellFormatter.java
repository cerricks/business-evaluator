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

import java.text.Format;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * A base class for a table cell formatter.
 *
 * @author cerricks
 *
 * @param <S> The type of the TableView generic type (i.e. S ==
 * TableView&lt;S&gt;)
 * @param <T> The type of the content in all cells in this TableColumn.
 */
public class TableCellFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private final Format format;

    public TableCellFormatter(final Format format) {
        super();
        this.format = format;
    }

    @Override
    public TableCell<S, T> call(final TableColumn<S, T> arg0) {
        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        };
    }

}
