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
import java.text.NumberFormat;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Income tax rate entity.
 *
 * @author cerricks
 */
@Entity
@Table(name = "income_tax_rates")
public class IncomeTaxRate {

    private Long id;
    private ObjectProperty<FilingStatus> filingStatusProperty;
    private ObjectProperty<NumberRange<Double>> taxableIncomeRangeProperty;
    private DoubleProperty flatTaxProperty;
    private DoubleProperty taxRateProperty;

    /**
     * Creates instance of IncomeTaxRate.
     */
    protected IncomeTaxRate() {
    }

    /**
     * Creates an instance of IncomeTaxRate.
     *
     * @param filingStatus the filing status.
     * @param taxableIncomeRangeFrom the start of the associated taxable income
     * range.
     * @param taxableIncomeRangeTo the end of the associated taxable income
     * range.
     * @param flatTax the flat tax amount used to calculate the total tax owed.
     * @param taxRate the rate used to calculate the total tax owed.
     */
    public IncomeTaxRate(final FilingStatus filingStatus, final double taxableIncomeRangeFrom, final double taxableIncomeRangeTo, final double flatTax, final double taxRate) {
        this.filingStatusProperty = new SimpleObjectProperty(filingStatus);
        this.taxableIncomeRangeProperty = new SimpleObjectProperty(new NumberRange(taxableIncomeRangeFrom, Double.isNaN(taxableIncomeRangeTo) ? null : taxableIncomeRangeTo, NumberFormat.getCurrencyInstance()));
        this.flatTaxProperty = new SimpleDoubleProperty(flatTax);
        this.taxRateProperty = new SimpleDoubleProperty(taxRate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(filingStatusProperty.get()).append(") ");

        sb.append(taxableIncomeRangeProperty.get());

        sb.append(": ");

        if (flatTaxProperty.get() > 0) {
            sb.append(FormatUtil.formatCurrency(flatTaxProperty.get())).append(" plus ");
        }

        sb.append(FormatUtil.formatPercentage(taxRateProperty.get()));

        double taxableIncomeThreshold = getTaxableIncomeThreshold();

        if (taxableIncomeThreshold > 0) {
            sb.append(" of the amount over ").append(FormatUtil.formatCurrency(taxableIncomeThreshold));
        }

        return sb.toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "filing_status")
    @Enumerated(EnumType.STRING)
    public FilingStatus getFilingStatus() {
        return filingStatusProperty.get();
    }

    public void setFilingStatus(final FilingStatus filingStatus) {
        if (this.filingStatusProperty == null) {
            filingStatusProperty = new SimpleObjectProperty(filingStatus);
        } else {
            filingStatusProperty.set(filingStatus);
        }
    }

    public ObjectProperty<FilingStatus> filingStatusProperty() {
        return filingStatusProperty;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rangeStart", column = @Column(name = "taxable_income_range_from"))
        ,
        @AttributeOverride(name = "rangeEnd", column = @Column(name = "taxable_income_range_to"))
    })
    public NumberRange<Double> getTaxableIncomeRange() {
        return taxableIncomeRangeProperty.getValue();
    }

    public void setTaxableIncomeRange(final NumberRange<Double> taxableIncomeRange) {
        if (taxableIncomeRangeProperty == null) {
            taxableIncomeRange.setFormatter(NumberFormat.getCurrencyInstance());

            taxableIncomeRangeProperty = new SimpleObjectProperty(taxableIncomeRange);
        } else {
            taxableIncomeRangeProperty.set(taxableIncomeRange);
        }
    }

    public ObjectProperty<NumberRange<Double>> taxableIncomeRangeProperty() {
        return taxableIncomeRangeProperty;
    }

    @Column(name = "flat_tax")
    public double getFlatTax() {
        return flatTaxProperty.get();
    }

    public void setFlatTax(final double flatTax) {
        if (flatTaxProperty == null) {
            flatTaxProperty = new SimpleDoubleProperty(flatTax);
        } else {
            flatTaxProperty.set(flatTax);
        }
    }

    public DoubleProperty flatTaxProperty() {
        return flatTaxProperty;
    }

    @Column(name = "tax_rate")
    public double getTaxRate() {
        return taxRateProperty.get();
    }

    public void setTaxRate(final double taxRate) {
        if (taxRateProperty == null) {
            taxRateProperty = new SimpleDoubleProperty(taxRate);
        } else {
            taxRateProperty.set(taxRate);
        }
    }

    public DoubleProperty taxRateProperty() {
        return taxRateProperty;
    }

    @Transient
    public double getTaxableIncomeThreshold() {
        return taxableIncomeRangeProperty.get().getRangeStart() > 0
                ? taxableIncomeRangeProperty.get().getRangeStart() - 1
                : 0;
    }

    public boolean isInRange(final double taxableIncome) {
        return (taxableIncome > getTaxableIncomeThreshold()
                && (taxableIncomeRangeProperty.get().getRangeEnd() == null || taxableIncome <= taxableIncomeRangeProperty.get().getRangeEnd()))
                || (taxableIncome == 0 && taxableIncomeRangeProperty.get().getRangeStart() == 0);
    }

}
