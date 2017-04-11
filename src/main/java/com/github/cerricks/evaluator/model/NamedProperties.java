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
package com.github.cerricks.evaluator.model;

import static com.github.cerricks.evaluator.Constants.PROPERTY_ASKING_PRICE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_ASKING_PRICE_BALANCE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_CASH_AFTER_TAX;
import static com.github.cerricks.evaluator.Constants.PROPERTY_DOWN_PAYMENT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_ORIGINAL_CASH_FLOW;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TAXABLE_INCOME;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_ADDITIONAL_EXPENSE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_AMOUNT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_PAYMENT;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Named properties used in system calculations.
 *
 * @author cerricks
 */
@Component
@Scope("singleton")
public class NamedProperties {

    private final NamedProperty askingPriceProperty = new NamedProperty(PROPERTY_ASKING_PRICE);
    private final NamedProperty askingPriceBalanceProperty = new NamedProperty(PROPERTY_ASKING_PRICE_BALANCE, 0);
    private final NamedProperty cashAfterTaxProperty = new NamedProperty(PROPERTY_CASH_AFTER_TAX, 0);
    private final NamedProperty downPaymentProperty = new NamedProperty(PROPERTY_DOWN_PAYMENT);
    private final NamedProperty originalCashFlowProperty = new NamedProperty(PROPERTY_ORIGINAL_CASH_FLOW);
    private final NamedProperty taxableIncomeProperty = new NamedProperty(PROPERTY_TAXABLE_INCOME, 0);
    private final NamedProperty totalAdditionalExpenseProperty = new NamedProperty(PROPERTY_TOTAL_ADDITIONAL_EXPENSE, 0);
    private final NamedProperty totalLoanAmountProperty = new NamedProperty(PROPERTY_TOTAL_LOAN_AMOUNT, 0);
    private final NamedProperty totalLoanPaymentProperty = new NamedProperty(PROPERTY_TOTAL_LOAN_PAYMENT, 0);

    public NamedProperties() {
    }

    public void reset() {
        askingPriceProperty.set(0);
        askingPriceBalanceProperty.set(0);
        cashAfterTaxProperty.set(0);
        downPaymentProperty.set(0);
        originalCashFlowProperty.set(0);
        taxableIncomeProperty.set(0);
        totalAdditionalExpenseProperty.set(0);
        totalLoanAmountProperty.set(0);
        totalLoanPaymentProperty.set(0);
    }

    public NamedProperty askingPriceProperty() {
        return askingPriceProperty;
    }

    public NamedProperty askingPriceBalanceProperty() {
        return askingPriceBalanceProperty;
    }

    public NamedProperty cashAfterTaxProperty() {
        return cashAfterTaxProperty;
    }

    public NamedProperty downPaymentProperty() {
        return downPaymentProperty;
    }

    public NamedProperty originalCashFlowProperty() {
        return originalCashFlowProperty;
    }

    public NamedProperty taxableIncomeProperty() {
        return taxableIncomeProperty;
    }

    public NamedProperty totalAdditionalExpenseProperty() {
        return totalAdditionalExpenseProperty;
    }

    public NamedProperty totalLoanAmountProperty() {
        return totalLoanAmountProperty;
    }

    public NamedProperty totalLoanPaymentProperty() {
        return totalLoanPaymentProperty;
    }

}
