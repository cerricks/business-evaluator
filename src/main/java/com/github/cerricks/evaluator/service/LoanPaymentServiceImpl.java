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

import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_AMOUNT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_PAYMENT;
import com.github.cerricks.evaluator.dao.InputCategoryRepository;
import com.github.cerricks.evaluator.model.InputCategory;
import com.github.cerricks.evaluator.model.Loan;
import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.model.LoanRate;
import com.github.cerricks.evaluator.model.NamedProperty;
import com.github.cerricks.evaluator.util.LoanPaymentCalculator;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cerricks
 */
@Service
public class LoanPaymentServiceImpl implements LoanPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(LoanPaymentServiceImpl.class);

    @Autowired
    private LoanPaymentCalculator loanPaymentCalculator;

    @Autowired
    private InputCategoryRepository inputCategoryRepository;

    @Autowired
    private IncomeTaxService incomeTaxService;

    @Autowired
    private Map<String, NamedProperty> properties;

    private final ObservableList<LoanPayment> loanPayments;

    public LoanPaymentServiceImpl() {
        loanPayments = FXCollections.observableArrayList();

        loanPayments.addListener((ListChangeListener.Change<? extends LoanPayment> change) -> {
            this.recalculate();
        });
    }

    @Override
    public void recalculate() {
        if (logger.isDebugEnabled()) {
            logger.debug("recalculating loan payment");
        }

        double totalLoanAmount = 0;
        double totalLoanPayment = 0;

        for (LoanPayment loanPayment : loanPayments) {
            if (logger.isDebugEnabled()) {
                logger.debug("Calculating loan: " + loanPayment.getLoan());
            }

            totalLoanAmount += loanPayment.getLoan().getAmount();
            totalLoanPayment += loanPayment.getAnnualPayment();
        }

        properties.get(PROPERTY_TOTAL_LOAN_AMOUNT).set(totalLoanAmount);
        properties.get(PROPERTY_TOTAL_LOAN_PAYMENT).set(totalLoanPayment);
    }

    @Override
    public ObservableList<LoanPayment> getLoanPayments() {
        return loanPayments;
    }

    @Override
    public LoanPayment processDefaultLoan(final String name, final double amount) {
        if (logger.isDebugEnabled()) {
            logger.debug("Processing auto-loan for property [" + name + "]");
        }

        LoanPayment loanPayment = findLoanPaymentByName(name);

        if (loanPayment != null) {
            if (amount <= 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Removing loan payment for property [" + name + "]");
                }

                loanPayments.remove(loanPayment);

                return null;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Updating loan for property [" + name + "]");
            }

            Loan loan = new Loan(amount, loanPayment.getLoan().getRate(), loanPayment.getLoan().getTerm()); // use existing terms

            double monthlyPayment = loanPaymentCalculator.calculateMonthlyPayment(loan);
            double totalInterestPayment = loanPaymentCalculator.calculateInterestPaid(loan);

            /**
             * Note: The order that these properties are set is important. The loan must be set at
             * the end due to the listener on it that requires other properties to be set.
             *
             * TODO: is there a better way to handle this? Possibly delaying listener until end of
             * method???
             */
            loanPayment.setCategory(loanPayment.getCategory());
            loanPayment.monthlyPaymentProperty().set(monthlyPayment);
            loanPayment.annualPaymentProperty().set(monthlyPayment * 12);
            loanPayment.totalInterestPaymentProperty().set(totalInterestPayment);
            loanPayment.setLoan(loan);

            return loanPayment;
        } else {
            if (amount > 0) {
                InputCategory category = inputCategoryRepository.findByName(name);

                if (category == null
                        || category.getDefaultLoanRate() == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Default loan preferences not found for property [" + name + "]");
                    }

                    return null;
                }

                loanPayment = addLoan(name, amount, category.getDefaultLoanRate());
            }

            return loanPayment;
        }
    }

    @Override
    public LoanPayment addLoan(final String name, final double amount, final LoanRate loanRate) {
        InputCategory category = inputCategoryRepository.findByName(name);

        Loan loan = new Loan(amount, loanRate.getRate(), loanRate.getTerm());

        double monthlyPayment = loanPaymentCalculator.calculateMonthlyPayment(loan);
        double totalInterestPayment = loanPaymentCalculator.calculateInterestPaid(loan);

        LoanPayment loanPayment = new LoanPayment(category, loan, monthlyPayment, totalInterestPayment);

        loanPayment.loanProperty().addListener((observable, oldValue, newValue) -> {
            this.recalculate();
        });

        loanPayments.add(loanPayment);

        return loanPayment;
    }

    @Override
    public void removeLoanByName(final String name) {
        LoanPayment loanPayment = findLoanPaymentByName(name);

        if (loanPayment != null) {
            loanPayments.remove(loanPayment);
        }
    }

    private LoanPayment findLoanPaymentByName(final String name) {
        for (LoanPayment loanPayment : loanPayments) {
            if (loanPayment.getCategory().getName().equals(name)) {
                return loanPayment;
            }
        }

        return null; // if not found
    }

}
