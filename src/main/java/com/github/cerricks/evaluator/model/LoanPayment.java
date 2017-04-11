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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Represents calculated loan payments.
 *
 * @author cerricks
 */
public class LoanPayment {

    private final ObjectProperty<InputCategory> category;
    private final ObjectProperty<Loan> loan;
    private final DoubleProperty monthlyPayment;
    private final DoubleProperty annualPayment;
    private final DoubleProperty totalInterestPayment;

    public LoanPayment(final InputCategory category, final Loan loan, final Double monthlyPayment, final Double totalInterestPayment) {
        this.category = new SimpleObjectProperty(category);
        this.loan = new SimpleObjectProperty(loan);
        this.monthlyPayment = new ReadOnlyDoubleWrapper(monthlyPayment);
        this.annualPayment = new ReadOnlyDoubleWrapper(monthlyPayment * 12);
        this.totalInterestPayment = new ReadOnlyDoubleWrapper(totalInterestPayment);
    }

    public InputCategory getCategory() {
        return category.get();
    }

    public void setCategory(InputCategory category) {
        this.category.set(category);
    }

    public ObjectProperty<InputCategory> categoryProperty() {
        return category;
    }

    public Loan getLoan() {
        return loan.get();
    }

    public void setLoan(final Loan loan) {
        this.loan.set(loan);
    }

    public ObjectProperty<Loan> loanProperty() {
        return loan;
    }

    public double getMonthlyPayment() {
        return monthlyPayment.get();
    }

    public DoubleProperty monthlyPaymentProperty() {
        return monthlyPayment;
    }

    public double getAnnualPayment() {
        return annualPayment.get();
    }

    public final DoubleProperty annualPaymentProperty() {
        return annualPayment;
    }

    public double getTotalInterestPayment() {
        return totalInterestPayment.get();
    }

    public DoubleProperty totalInterestPaymentProperty() {
        return totalInterestPayment;
    }

}
