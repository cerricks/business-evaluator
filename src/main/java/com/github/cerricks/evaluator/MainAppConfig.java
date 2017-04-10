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

import static com.github.cerricks.evaluator.Constants.PROPERTY_ASKING_PRICE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_ASKING_PRICE_BALANCE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_CASH_AFTER_TAX;
import static com.github.cerricks.evaluator.Constants.PROPERTY_DOWN_PAYMENT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_ORIGINAL_CASH_FLOW;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TAXABLE_INCOME;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_ADDITIONAL_EXPENSE;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_AMOUNT;
import static com.github.cerricks.evaluator.Constants.PROPERTY_TOTAL_LOAN_PAYMENT;
import com.github.cerricks.evaluator.dao.IncomeTaxRateRepository;
import com.github.cerricks.evaluator.dao.InputCategoryRepository;
import com.github.cerricks.evaluator.model.IncomeTaxRate;
import com.github.cerricks.evaluator.model.IncomeTaxRateTable;
import com.github.cerricks.evaluator.model.Input;
import com.github.cerricks.evaluator.model.InputCategory;
import static com.github.cerricks.evaluator.model.InputSource.USER;
import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.model.NamedProperty;
import com.github.cerricks.evaluator.service.DebtRatioService;
import com.github.cerricks.evaluator.service.IncomeTaxService;
import com.github.cerricks.evaluator.service.LoanPaymentService;
import com.github.cerricks.evaluator.util.IncomeTaxPaymentCalculator;
import com.github.cerricks.evaluator.util.LoanPaymentCalculator;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @PostConstruct
    public void init() {
        loanPaymentService.getLoanPayments().addListener((Change<? extends LoanPayment> c) -> {
            // process debt payment for removed items
            for (LoanPayment loanPayment : c.getRemoved()) {
                debtRatioService.removeRatio(loanPayment.annualPaymentProperty());
            }

            // process debt payment for added items
            for (LoanPayment loanPayment : c.getAddedSubList()) {
                debtRatioService.createRatio(loanPayment.annualPaymentProperty());
            }
        });
    }

    @Bean
    public Map<String, NamedProperty> namedProperties() {
        Map<String, NamedProperty> properties = new HashMap();

        NamedProperty askingPriceProperty = new NamedProperty(PROPERTY_ASKING_PRICE);
        NamedProperty askingPriceBalanceProperty = new NamedProperty(PROPERTY_ASKING_PRICE_BALANCE, 0);
        NamedProperty downPaymentProperty = new NamedProperty(PROPERTY_DOWN_PAYMENT);
        NamedProperty originalCashFlowProperty = new NamedProperty(PROPERTY_ORIGINAL_CASH_FLOW);
        NamedProperty taxableIncomeProperty = new NamedProperty(PROPERTY_TAXABLE_INCOME, 0);
        NamedProperty totalAdditionalExpenseProperty = new NamedProperty(PROPERTY_TOTAL_ADDITIONAL_EXPENSE, 0);
        NamedProperty totalLoanAmountProperty = new NamedProperty(PROPERTY_TOTAL_LOAN_AMOUNT, 0);
        NamedProperty totalLoanPaymentProperty = new NamedProperty(PROPERTY_TOTAL_LOAN_PAYMENT, 0);
        NamedProperty cashAfterTaxProperty = new NamedProperty(PROPERTY_CASH_AFTER_TAX, 0);

        properties.put(askingPriceProperty.getName(), askingPriceProperty);
        properties.put(askingPriceBalanceProperty.getName(), askingPriceBalanceProperty);
        properties.put(downPaymentProperty.getName(), downPaymentProperty);
        properties.put(originalCashFlowProperty.getName(), originalCashFlowProperty);
        properties.put(taxableIncomeProperty.getName(), taxableIncomeProperty);
        properties.put(totalAdditionalExpenseProperty.getName(), totalAdditionalExpenseProperty);
        properties.put(totalLoanAmountProperty.getName(), totalLoanAmountProperty);
        properties.put(totalLoanPaymentProperty.getName(), totalLoanPaymentProperty);
        properties.put(cashAfterTaxProperty.getName(), cashAfterTaxProperty);

        askingPriceProperty.addListener((observable, oldValue, newValue) -> {
            askingPriceBalanceProperty.setValue(askingPriceProperty.doubleValue() - downPaymentProperty.doubleValue());
            incomeTaxService.calculateTaxableIncome();

            // TODO: add listener to list instead
            if (oldValue.doubleValue() == 0 && newValue.doubleValue() != 0) {
                debtRatioService.createRatio(askingPriceProperty);
            } else if (oldValue.doubleValue() != 0 && newValue.doubleValue() == 0) {
                debtRatioService.removeRatio(askingPriceProperty);
            }
        });

        askingPriceBalanceProperty.addListener((observable, oldValue, newValue) -> {
            loanPaymentService.processDefaultLoan(askingPriceProperty.getName(), askingPriceBalanceProperty.doubleValue());
            incomeTaxService.calculateTaxableIncome();
        });

        downPaymentProperty.addListener((observable, oldValue, newValue) -> {
            askingPriceBalanceProperty.setValue(askingPriceProperty.doubleValue() - downPaymentProperty.doubleValue());
            loanPaymentService.processDefaultLoan(downPaymentProperty.getName(), downPaymentProperty.doubleValue());
            incomeTaxService.calculateTaxableIncome();

            // TODO: add listener to list instead
            if (oldValue.doubleValue() == 0 && newValue.doubleValue() != 0) {
                debtRatioService.createRatio(downPaymentProperty);
            } else if (oldValue.doubleValue() != 0 && newValue.doubleValue() == 0) {
                debtRatioService.removeRatio(downPaymentProperty);
            }
        });

        originalCashFlowProperty.addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateTaxableIncome();

            // TODO: add listener to list instead
            if (oldValue.doubleValue() == 0 && newValue.doubleValue() != 0) {
                debtRatioService.createRatio(originalCashFlowProperty);
            } else if (oldValue.doubleValue() != 0 && newValue.doubleValue() == 0) {
                debtRatioService.removeRatio(originalCashFlowProperty);
            }
        });

        taxableIncomeProperty.addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateIncomeTaxPayments();
        });

        totalLoanAmountProperty.addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateTaxableIncome();
        });

        totalLoanPaymentProperty.addListener((observable, oldValue, newValue) -> {
            incomeTaxService.calculateTaxableIncome();
        });

        return properties;
    }

    @Bean(name = "inputCategories") // TODO: convert qualifier to name
    //@Qualifier("inputCategories")
    public ObservableList<InputCategory> inputCategories() {
        return FXCollections.observableArrayList(inputCategoryRepository.findAll(new Sort(Sort.Direction.ASC, "name")));
    }

    @Bean
    @Qualifier("additionalCostInputCategories")
    public ObservableList<InputCategory> additionalCostInputCategories() {
        return new SortedList<>(new FilteredList<>(inputCategories(), (InputCategory t) -> t.isAdditionalExpense()), (InputCategory o1, InputCategory o2) -> o1.getName().compareTo(o2.getName()));
    }

    @Bean
    @Qualifier("userDefinedInputCategories")
    public ObservableList<InputCategory> userDefinedInputCategories() {
        return new SortedList<>(new FilteredList<>(inputCategories(), (InputCategory t) -> t.getSource() == USER), (InputCategory o1, InputCategory o2) -> o1.getName().compareTo(o2.getName()));
    }

    @Bean
    @Qualifier("userDeclaredInputs")
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
