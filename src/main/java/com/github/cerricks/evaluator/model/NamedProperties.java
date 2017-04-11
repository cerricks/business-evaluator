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
import static com.github.cerricks.evaluator.Constants.PROPERTY_PERCENTAGE_OF_CASH_FLOW;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TAXABLE_INCOME;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_ADDITIONAL_EXPENSE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_AMOUNT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_PAYMENT;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

    private final DoubleProperty askingPriceProperty;
    private final DoubleProperty askingPriceBalanceProperty;
    private final DoubleProperty cashAfterTaxProperty;
    private final DoubleProperty downPaymentProperty;
    private final DoubleProperty originalCashFlowProperty;
    private final DoubleProperty percentageOfCashFlowProperty;
    private final DoubleProperty taxableIncomeProperty;
    private final DoubleProperty totalAdditionalExpenseProperty;
    private final DoubleProperty totalLoanAmountProperty;
    private final DoubleProperty totalLoanPaymentProperty;

    public NamedProperties() {
        askingPriceProperty = new SimpleDoubleProperty(this, PROPERTY_ASKING_PRICE);
        askingPriceBalanceProperty = new SimpleDoubleProperty(this, PROPERTY_ASKING_PRICE_BALANCE, 0);
        cashAfterTaxProperty = new SimpleDoubleProperty(this, PROPERTY_CASH_AFTER_TAX, 0);
        downPaymentProperty = new SimpleDoubleProperty(this, PROPERTY_DOWN_PAYMENT);
        originalCashFlowProperty = new SimpleDoubleProperty(this, PROPERTY_ORIGINAL_CASH_FLOW);
        percentageOfCashFlowProperty = new SimpleDoubleProperty(this, PROPERTY_PERCENTAGE_OF_CASH_FLOW, 0);
        taxableIncomeProperty = new SimpleDoubleProperty(this, PROPERTY_TAXABLE_INCOME, 0);
        totalAdditionalExpenseProperty = new SimpleDoubleProperty(this, PROPERTY_TOTAL_ADDITIONAL_EXPENSE, 0);
        totalLoanAmountProperty = new SimpleDoubleProperty(this, PROPERTY_TOTAL_LOAN_AMOUNT, 0);
        totalLoanPaymentProperty = new SimpleDoubleProperty(this, PROPERTY_TOTAL_LOAN_PAYMENT, 0);
    }

    public void reset() {
        askingPriceProperty.set(0);
        askingPriceBalanceProperty.set(0);
        cashAfterTaxProperty.set(0);
        downPaymentProperty.set(0);
        originalCashFlowProperty.set(0);
        percentageOfCashFlowProperty.set(0);
        taxableIncomeProperty.set(0);
        totalAdditionalExpenseProperty.set(0);
        totalLoanAmountProperty.set(0);
        totalLoanPaymentProperty.set(0);
    }

    public DoubleProperty askingPriceProperty() {
        return askingPriceProperty;
    }

    public DoubleProperty askingPriceBalanceProperty() {
        return askingPriceBalanceProperty;
    }

    public DoubleProperty cashAfterTaxProperty() {
        return cashAfterTaxProperty;
    }

    public DoubleProperty downPaymentProperty() {
        return downPaymentProperty;
    }

    public DoubleProperty originalCashFlowProperty() {
        return originalCashFlowProperty;
    }

    public DoubleProperty percentageOfCashFlowProperty() {
        return percentageOfCashFlowProperty;
    }

    public DoubleProperty taxableIncomeProperty() {
        return taxableIncomeProperty;
    }

    public DoubleProperty totalAdditionalExpenseProperty() {
        return totalAdditionalExpenseProperty;
    }

    public DoubleProperty totalLoanAmountProperty() {
        return totalLoanAmountProperty;
    }

    public DoubleProperty totalLoanPaymentProperty() {
        return totalLoanPaymentProperty;
    }

}
