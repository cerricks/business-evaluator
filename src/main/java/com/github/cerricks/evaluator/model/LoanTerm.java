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

import static com.github.cerricks.evaluator.model.LoanTermUnit.MONTHS;
import static com.github.cerricks.evaluator.model.LoanTermUnit.YEARS;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Represents a loan term period (i.e. 5 years, 18 months, etc).
 *
 * @author cerricks
 */
@Embeddable
public class LoanTerm implements Serializable {

    private int length;
    private LoanTermUnit unit;

    public LoanTerm() {
    }

    /**
     * Creates instance of LoanTerm with the given length in years.
     *
     * @param length the term length (in years).
     */
    public LoanTerm(final int length) {
        this(length, YEARS);
    }

    /**
     * Creates instance of LoanTerm with the given length and unit.
     *
     * @param length the term length.
     * @param unit the unit of time of the term.
     */
    public LoanTerm(final int length, final LoanTermUnit unit) {
        this.length = length;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return length + " " + unit;
    }

    /**
     * Get the term length.
     *
     * @return the term length.
     */
    @Column(name = "loan_term_length")
    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * The unit of time of the term.
     *
     * @return the unit of time of the term.
     */
    @Column(name = "loan_term_unit")
    @Enumerated(EnumType.STRING)
    public LoanTermUnit getUnit() {
        return unit;
    }

    public void setUnit(final LoanTermUnit unit) {
        this.unit = unit;
    }

    /**
     * Calculate the total number of months.
     *
     * @return the total number of months.
     */
    public int toTotalMonths() {
        return unit == YEARS
                ? length * 12
                : length;
    }

    /**
     * Creates a {@code LoanTerm} for the given number of months.
     *
     * @param length the term length (in months).
     * @return a {@code LoanTerm} for the given number of months.
     */
    public static LoanTerm ofMonths(final int length) {
        return new LoanTerm(length, MONTHS);
    }

    /**
     * Creates a {@code LoanTerm} for the given number of years.
     *
     * @param length the term length (in years).
     * @return a {@code LoanTerm} for the given number of years.
     */
    public static LoanTerm ofYears(int length) {
        return new LoanTerm(length, YEARS);
    }

}
