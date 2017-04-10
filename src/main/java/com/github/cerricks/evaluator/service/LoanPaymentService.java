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

import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.model.LoanRate;
import javafx.collections.ObservableList;

/**
 *
 * @author cerricks
 */
public interface LoanPaymentService {

    LoanPayment addLoan(String name, double amount, LoanRate loanRate);

    void recalculate();

    ObservableList<LoanPayment> getLoanPayments();

    LoanPayment processDefaultLoan(String name, double amount);

    void removeLoanByName(String name);

}
