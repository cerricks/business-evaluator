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

import com.github.cerricks.evaluator.util.FormatUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Details on a loan.
 *
 * @author cerricks
 */
public class Loan {

    private final DoubleProperty amountProperty;
    private final DoubleProperty rateProperty;
    private final ObjectProperty<LoanTerm> termProperty;

    /**
     * Creates an instance of a Loan.
     *
     * @param amount the initial loan amountProperty.
     * @param rate the interest rateProperty.
     * @param term the termProperty length.
     */
    public Loan(final double amount, final double rate, final LoanTerm term) {
        this.amountProperty = new SimpleDoubleProperty(amount);
        this.rateProperty = new SimpleDoubleProperty(rate);
        this.termProperty = new SimpleObjectProperty(term);
    }

    @Override
    public String toString() {
        return new StringBuilder(FormatUtil.formatCurrency(amountProperty.get()))
                .append(" at ")
                .append(FormatUtil.formatPercentage(rateProperty.get()))
                .append(" over ")
                .append(termProperty.get()).toString();
    }

    public double getAmount() {
        return amountProperty.get();
    }

    public void setAmount(final double amount) {
        this.amountProperty.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amountProperty;
    }

    public double getRate() {
        return rateProperty.get();
    }

    public void setRate(final double rate) {
        this.rateProperty.set(rate);
    }

    public final DoubleProperty rateProperty() {
        return rateProperty;
    }

    public LoanTerm getTerm() {
        return termProperty.get();
    }

    public void setTerm(final LoanTerm term) {
        this.termProperty.set(term);
    }

    public final ObjectProperty<LoanTerm> termProperty() {
        return termProperty;
    }

}
