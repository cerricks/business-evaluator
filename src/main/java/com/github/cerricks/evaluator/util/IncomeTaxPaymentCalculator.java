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

import com.github.cerricks.evaluator.model.FilingStatus;
import com.github.cerricks.evaluator.model.IncomeTaxRate;
import com.github.cerricks.evaluator.model.IncomeTaxRateTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility for calculating tax owed on taxable income.
 *
 * @author cerricks
 */
public class IncomeTaxPaymentCalculator {

    private static final Logger logger = LoggerFactory.getLogger(IncomeTaxPaymentCalculator.class);

    private IncomeTaxRateTable rateTable;

    /**
     * Creates an instance of IncomeTaxCalculator based on the given {@link IncomeTaxRateTable}.
     *
     * @param rateTable the rate table used for calculations.
     */
    public IncomeTaxPaymentCalculator(final IncomeTaxRateTable rateTable) {
        this.rateTable = rateTable;
    }

    /**
     * Get the underlying rate table used for calculations.
     *
     * @return the underlying rate table used for calculations.
     */
    public IncomeTaxRateTable getRateTable() {
        return rateTable;
    }

    /**
     * Set the underlying rate table used for calculations.
     *
     * @param rateTable the underlying rate table used for calculations.
     */
    public void setRateTable(final IncomeTaxRateTable rateTable) {
        this.rateTable = rateTable;
    }

    /**
     * Calculates the total tax owed on the given income and filing status.
     *
     * <p>
     * <code>totalTax = flatTax + (taxableIncome - incomeThreshold) * percentage</code>
     *
     * @param filingStatus the filing status.
     * @param taxableIncome the amount of taxable income.
     * @return the total tax owed on the given income and filing status.
     */
    public double calculateTax(final FilingStatus filingStatus, final double taxableIncome) {
        if (filingStatus == null) {
            throw new IllegalArgumentException("filingStatus cannot be null");
        }

        // get the tax rate from the tax table
        IncomeTaxRate taxRate = rateTable.getTaxRate(filingStatus, taxableIncome);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder("Calculating income tax for filing status [").append(filingStatus).append("] on ").append(FormatUtil.formatCurrency(taxableIncome)).append(" using tax rate [").append(taxRate).append("]").toString());
        }

        return taxRate.getFlatTax() + ((taxableIncome - taxRate.getTaxableIncomeThreshold()) * taxRate.getTaxRate());
    }

}
