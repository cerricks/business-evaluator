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
package com.github.cerricks.evaluator;

import com.github.cerricks.evaluator.dao.IncomeTaxRateRepository;
import com.github.cerricks.evaluator.dao.InputCategoryRepository;
import com.github.cerricks.evaluator.model.IncomeTaxRate;
import com.github.cerricks.evaluator.model.IncomeTaxRateTable;
import com.github.cerricks.evaluator.model.Input;
import com.github.cerricks.evaluator.model.InputCategory;
import static com.github.cerricks.evaluator.model.InputSource.USER;
import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.model.NamedProperties;
import com.github.cerricks.evaluator.service.DebtRatioService;
import com.github.cerricks.evaluator.service.IncomeTaxService;
import com.github.cerricks.evaluator.service.LoanPaymentService;
import com.github.cerricks.evaluator.util.IncomeTaxPaymentCalculator;
import com.github.cerricks.evaluator.util.LoanPaymentCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

/**
 * Spring application configuration.
 *
 * @author cerricks
 */
@Configuration
public class MainAppConfig {

    private static final Logger logger = LoggerFactory.getLogger(MainAppConfig.class);

    @Autowired
    private IncomeTaxRateRepository incomeTaxRateRepository;

    @Autowired
    private InputCategoryRepository inputCategoryRepository;

    @Autowired
    private IncomeTaxService incomeTaxService;

    @Autowired
    private LoanPaymentService loanPaymentService;

    @Autowired
    private DebtRatioService debtRatioService;

    @Autowired
    private NamedProperties namedProperties;

    @PostConstruct
    public void init() {
        namedProperties.askingPriceProperty().addListener((observable, oldValue, newValue) -> {
            namedProperties.askingPriceBalanceProperty().setValue(namedProperties.askingPriceProperty().doubleValue() - namedProperties.downPaymentProperty().doubleValue());
            incomeTaxService.calculateTaxableIncome();
        });

        namedProperties.askingPriceBalanceProperty().addListener((observable, oldValue, newValue) -> {
            loanPaymentService.processDefaultLoan(namedProperties.askingPriceProperty().getName(), namedProperties.askingPriceBalanceProperty().doubleValue());
            incomeTaxService.calculateTaxableIncome();
        });

        namedProperties.downPaymentProperty().addListener((observable, oldValue, newValue) -> {
            namedProperties.askingPriceBalanceProperty().setValue(namedProperties.askingPriceProperty().doubleValue() - namedProperties.downPaymentProperty().doubleValue());
            loanPaymentService.processDefaultLoan(namedProperties.downPaymentProperty().getName(), namedProperties.downPaymentProperty().doubleValue());
            incomeTaxService.calculateTaxableIncome();
        });

        namedProperties.originalCashFlowProperty().addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateTaxableIncome();
        });

        namedProperties.taxableIncomeProperty().addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateIncomeTaxPayments();
        });

        namedProperties.totalLoanAmountProperty().addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateTaxableIncome();
        });

        namedProperties.totalLoanPaymentProperty().addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateTaxableIncome();
        });

        loanPaymentService.getLoanPayments().addListener((Change<? extends LoanPayment> c) -> {
            if (c.next()) {
                // process debt payment for removed items
                for (LoanPayment loanPayment : c.getRemoved()) {
                    debtRatioService.removeRatio(loanPayment.getCategory().getName());
                }

                // process debt payment for added items
                for (LoanPayment loanPayment : c.getAddedSubList()) {
                    debtRatioService.createRatio(loanPayment.getCategory().getName(), loanPayment.annualPaymentProperty());
                }
            }
        });
    }

    @Bean(name = "inputCategories")
    public ObservableList<InputCategory> inputCategories() {
        return FXCollections.observableArrayList(inputCategoryRepository.findAll(new Sort(Sort.Direction.ASC, "name")));
    }

    @Bean(name = "additionalCostInputCategories")
    public ObservableList<InputCategory> additionalCostInputCategories() {
        return new SortedList<>(new FilteredList<>(inputCategories(), (InputCategory t) -> t.isAdditionalExpense()), (InputCategory o1, InputCategory o2) -> o1.getName().compareTo(o2.getName()));
    }

    @Bean(name = "userDefinedInputCategories")
    public ObservableList<InputCategory> userDefinedInputCategories() {
        return new SortedList<>(new FilteredList<>(inputCategories(), (InputCategory t) -> t.getSource() == USER), (InputCategory o1, InputCategory o2) -> o1.getName().compareTo(o2.getName()));
    }

    @Bean(name = "userDeclaredInputs")
    public ObservableList<Input> userDeclaredInputs() {
        return FXCollections.observableArrayList();
    }

    @Bean
    public ObservableList<Input> userDefinedInputs() {
        return FXCollections.observableArrayList();
    }

    @Bean
    public ObservableList<IncomeTaxRate> incomeTaxRates() {
        return FXCollections.observableArrayList(incomeTaxRateRepository.findAll());
    }

    @Bean
    public IncomeTaxPaymentCalculator incomeTaxPaymentCalculator() {
        IncomeTaxRateTable incomeTaxRateTable = new IncomeTaxRateTable(incomeTaxRates());

        return new IncomeTaxPaymentCalculator(incomeTaxRateTable);
    }

    @Bean
    public LoanPaymentCalculator loanPaymentCalculator() {
        return new LoanPaymentCalculator();
    }

}
