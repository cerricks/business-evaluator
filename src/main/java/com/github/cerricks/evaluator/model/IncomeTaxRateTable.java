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
package com.github.cerricks.evaluator.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * A container for {@link IncomeTaxRate}.
 *
 * @author cerricks
 */
public class IncomeTaxRateTable {

    private final List<IncomeTaxRate> rates;

    /**
     * Creates instance of IncomeTaxRateTable.
     *
     * @param rates a list of {@link IncomeTaxRate} objects used to initialize
     * this rate table.
     */
    public IncomeTaxRateTable(final List<IncomeTaxRate> rates) {
        sort(rates);

        this.rates = Collections.unmodifiableList(rates);
    }

    /**
     * Sort rates in order:
     *
     * <ol>
     * <li>Filing Status {@link FilingStatus#ordinal()}</li>
     * <li>Income range start
     * {@link IncomeTaxRate#getTaxableIncomeRangeFrom()}</li>
     * </ol>
     *
     * @param rates rates to be sorted
     */
    private void sort(final List<IncomeTaxRate> rates) {
        rates.sort((final IncomeTaxRate taxRate1, final IncomeTaxRate taxRate2) -> {
            if (taxRate1.getFilingStatus() != taxRate2.getFilingStatus()) {
                return Integer.compare(taxRate1.getFilingStatus().ordinal(), taxRate2.getFilingStatus().ordinal());
            }

            return Double.compare(taxRate1.getTaxableIncomeRange().getRangeStart(), taxRate2.getTaxableIncomeRange().getRangeStart());
        });
    }

    /**
     * Retrieve the list of {@link IncomeTaxRate} objects associated with this
     * table.
     *
     * @return the list of {@link IncomeTaxRate} objects associated with this
     * table.
     */
    public List<IncomeTaxRate> getRates() {
        return rates;
    }

    /**
     * Retrieves the {@link IncomeTaxRate} to be used to calculate tax for the
     * given {@link FilingStatus} and {@link BigDecimal taxable income}.
     *
     * @param filingStatus the filing status.
     * @param taxableIncome the total taxable income.
     * @return the {@link IncomeTaxRate} to be used to calculate tax for the
     * given {@link FilingStatus} and {@link BigDecimal taxable income}.
     */
    public IncomeTaxRate getTaxRate(final FilingStatus filingStatus, final double taxableIncome) {
        for (IncomeTaxRate taxRate : rates) {
            if (taxRate.getFilingStatus() == filingStatus
                    && taxRate.isInRange(taxableIncome)) {
                return taxRate;
            }
        }

        throw new IllegalArgumentException("Tax rate not found for filingStatus [" + filingStatus + "] and taxableIncome [" + taxableIncome + "]");
    }

}
