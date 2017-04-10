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

import java.text.NumberFormat;
import javafx.util.converter.CurrencyStringConverter;

/**
 * A {@link CurrencyStringConverter} that accepts input without a currency
 * symbol.
 *
 * @author cerricks
 */
public class CustomCurrencyStringConverter extends CurrencyStringConverter {

    private final String currencySymbol;

    public CustomCurrencyStringConverter() {
        super();

        NumberFormat defaultNumberFormat = getNumberFormat();
        defaultNumberFormat.setMaximumFractionDigits(0);
        defaultNumberFormat.setMinimumFractionDigits(0);

        currencySymbol = defaultNumberFormat.getCurrency().getSymbol();
    }

    public CustomCurrencyStringConverter(final NumberFormat numberFormat) {
        super(numberFormat);

        currencySymbol = numberFormat.getCurrency().getSymbol();
    }

    @Override
    public Number fromString(final String text) {
        if (text == null
                || text.trim().length() == 0) {
            return null;
        }

        if (currencySymbol != null
                && !text.contains(currencySymbol)) {
            return super.fromString("$" + text);
        } else {
            return super.fromString(text);
        }
    }

    @Override
    final protected NumberFormat getNumberFormat() {
        return super.getNumberFormat();
    }

}
