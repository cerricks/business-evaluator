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

import com.github.cerricks.evaluator.model.Loan;

/**
 * A utility for doing calculations on a loan.
 *
 * @author cerricks
 */
public final class LoanPaymentCalculator {

    /**
     * Calculates the monthly payment on a loan.
     *
     * @param loan the loan details.
     * @return the monthly payment on a loan.
     */
    public double calculateMonthlyPayment(final Loan loan) {
        double monthlyInterestRate = loan.getRate() / 12;

        return (monthlyInterestRate * loan.getAmount()) / (1 - Math.pow(1 + (monthlyInterestRate), -loan.getTerm().toTotalMonths()));
    }

    /**
     * Calculates the total interest paid over the life of a loan.
     *
     * @param loan the loan details.
     * @return the total interest paid over the life of a loan
     */
    public double calculateInterestPaid(final Loan loan) {
        double monthlyPayment = calculateMonthlyPayment(loan);

        return (monthlyPayment * loan.getTerm().toTotalMonths()) - loan.getAmount();
    }

    /**
     * Calculates the total interest paid within the specified period of a term.
     *
     * @param loan the loan details.
     * @param startPeriod the period of the term to calculate from.
     * @param endPeriod the period of the term to calculate to.
     * @return the total interest paid within a term period.
     */
    public double calculateInterestPaid(final Loan loan, final int startPeriod, final int endPeriod) {
        double monthlyPayment = calculateMonthlyPayment(loan);

        return -((((loan.getAmount() - monthlyPayment * 12 / loan.getRate()) * Math.pow((1 + (loan.getRate() / 12)), (startPeriod - 1)) + monthlyPayment * 12 / loan.getRate()) - ((loan.getAmount() - monthlyPayment * 12 / loan.getRate()) * Math.pow((1 + (loan.getRate() / 12)), endPeriod) + monthlyPayment * 12 / loan.getRate())) - monthlyPayment * (endPeriod - startPeriod + 1));
    }

}
