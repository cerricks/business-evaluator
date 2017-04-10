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
package com.github.cerricks.evaluator.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * A utility to for simple formatting.
 *
 * @author cerricks
 */
public class FormatUtil {

    private static final NumberFormat currencyFormat;
    private static final NumberFormat numberFormat;
    private static final NumberFormat percentFormat;

    static {
        // initialize currency format
        currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setRoundingMode(RoundingMode.HALF_UP);

        // initialize generic number format
        numberFormat = NumberFormat.getNumberInstance();

        // initialize percentage format
        percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        percentFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * Formats an object as a currency.
     *
     * @param obj the object to format.
     * @return a formatted currency.
     */
    public static String formatCurrency(final Object obj) {
        return currencyFormat.format(obj);
    }

    /**
     * Formats an object as a generic number.
     *
     * @param obj the object to format.
     * @return a formatted number.
     */
    public static String formatNumber(final Object obj) {
        return numberFormat.format(obj);
    }

    /**
     * Formats an object as a percentage.
     *
     * @param obj the object to format.
     * @return a formatted percentage.
     */
    public static String formatPercentage(final Object obj) {
        return percentFormat.format(obj);
    }

}
