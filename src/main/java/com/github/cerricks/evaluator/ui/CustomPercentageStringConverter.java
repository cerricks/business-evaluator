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

import java.math.RoundingMode;
import java.text.NumberFormat;
import javafx.util.converter.PercentageStringConverter;

/**
 * A {@link PercentageStringConverter} that accepts input without a percent
 * symbol.
 *
 * @author cerricks
 */
public class CustomPercentageStringConverter extends PercentageStringConverter {

    private static final NumberFormat percentageFormat = NumberFormat.getPercentInstance();

    static {
        percentageFormat.setMaximumFractionDigits(4);
        percentageFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    public CustomPercentageStringConverter() {
        super(percentageFormat);
    }

    @Override
    public Number fromString(final String text) {
        if (text == null
                || text.trim().length() == 0) {
            return null;
        }

        if (!text.contains("%")) {
            return super.fromString(text + "%");
        } else {
            return super.fromString(text);
        }
    }

    @Override
    public String toString(final Number value) {
        return value != null
                ? super.toString(value.doubleValue())
                : "";
    }

}
