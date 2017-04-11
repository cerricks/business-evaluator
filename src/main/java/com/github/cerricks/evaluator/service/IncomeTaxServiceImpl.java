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
package com.github.cerricks.evaluator.service;

import com.github.cerricks.evaluator.model.FilingStatus;
import static com.github.cerricks.evaluator.model.FilingStatus.SINGLE;
import com.github.cerricks.evaluator.model.IncomeTaxPayment;
import com.github.cerricks.evaluator.model.NamedProperties;
import com.github.cerricks.evaluator.util.IncomeTaxPaymentCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * A service implementation for handling income tax calculations.
 *
 * @author cerricks
 */
@Service
@Scope("singleton")
public class IncomeTaxServiceImpl implements IncomeTaxService {

    private static final Logger logger = LoggerFactory.getLogger(IncomeTaxServiceImpl.class);

    private final ObservableList<IncomeTaxPayment> incomeTaxPayments;

    @Autowired
    private NamedProperties namedProperties;

    @Autowired
    private IncomeTaxPaymentCalculator incomeTaxCalculator;

    public IncomeTaxServiceImpl() {
        incomeTaxPayments = FXCollections.observableArrayList();
    }

    @Override
    public void calculateIncomeTaxPayments() {
        incomeTaxPayments.clear();

        calculateTaxableIncome();

        double taxableIncome = namedProperties.taxableIncomeProperty().doubleValue();

        if (taxableIncome > 0) {
            for (FilingStatus filingStatus : FilingStatus.values()) {
                try {
                    double incomeTax = incomeTaxCalculator.calculateTax(filingStatus, taxableIncome);
                    double incomeAfterTax = taxableIncome - incomeTax;
                    double percentageOfOriginalCashFlow = namedProperties.originalCashFlowProperty().doubleValue() > 0 ? incomeAfterTax / namedProperties.originalCashFlowProperty().doubleValue() : 0;

                    incomeTaxPayments.add(new IncomeTaxPayment(filingStatus, incomeTax, incomeAfterTax, percentageOfOriginalCashFlow));
                } catch (Exception ex) {
                    logger.warn(ex.getMessage());
                }
            }
        } else {
            for (FilingStatus filingStatus : FilingStatus.values()) {
                incomeTaxPayments.add(new IncomeTaxPayment(filingStatus, 0, taxableIncome, 0));
            }
        }

        for (IncomeTaxPayment incomeTaxPayment : incomeTaxPayments) {
            if (incomeTaxPayment.getFilingStatus() == SINGLE) {
                namedProperties.cashAfterTaxProperty().set(incomeTaxPayment.getTotalIncomeAfterTax());
            }
        }
    }

    @Override
    public void calculateTaxableIncome() {
        try {
            double originalCashFlow = namedProperties.originalCashFlowProperty().doubleValue();
            double totalLoanPayment = namedProperties.totalLoanPaymentProperty().doubleValue();

            double taxableIncome = originalCashFlow - totalLoanPayment;

            namedProperties.taxableIncomeProperty().set(taxableIncome);
            namedProperties.percentageOfCashFlowProperty().set(originalCashFlow > 0 ? taxableIncome / originalCashFlow : 0);
        } catch (NumberFormatException ex) {
            logger.warn("Unable to calculate taxable income: " + ex.getMessage());
        }
    }

    @Override
    public ObservableList<IncomeTaxPayment> getIncomeTaxPayments() {
        return incomeTaxPayments;
    }

    @Override
    public void reset() {
        incomeTaxPayments.clear();
    }

}
