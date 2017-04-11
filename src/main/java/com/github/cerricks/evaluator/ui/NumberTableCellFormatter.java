/*
 * Copyright 2017 Clifford Errickson.
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

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * A {@link TableCellFormatter} for formatting a table cell value as a generic
 * number.
 *
 * @author cerricks
 * @param <S> The type of the TableView generic type (i.e. S ==
 * TableView&lt;S&gt;)
 */
public class NumberTableCellFormatter<S> extends TableCellFormatter<S, Double> {

    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    public NumberTableCellFormatter() {
        super(numberFormat);
    }

}
