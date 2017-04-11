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

/**
 * Results of an income tax calculation.
 *
 * @author cerricks
 */
public class IncomeTaxPayment {

    private final FilingStatus filingStatus;
    private final double totalTax;
    private final double totalTaxMonthly;
    private final double totalIncomeAfterTax;
    private final double percentageOfOriginalCashFlow;

    public IncomeTaxPayment(final FilingStatus filingStatus, final double totalTax, final double totalIncomeAfterTax, final double percentageOfOriginalCashFlow) {
        this.filingStatus = filingStatus;
        this.totalTax = totalTax;
        this.totalTaxMonthly = totalTax / 12;
        this.totalIncomeAfterTax = totalIncomeAfterTax;
        this.percentageOfOriginalCashFlow = percentageOfOriginalCashFlow;
    }

    public FilingStatus getFilingStatus() {
        return filingStatus;
    }

    public double getPercentageOfOriginalCashFlow() {
        return percentageOfOriginalCashFlow;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getTotalTaxMonthly() {
        return totalTaxMonthly;
    }

    public double getTotalIncomeAfterTax() {
        return totalIncomeAfterTax;
    }

}
