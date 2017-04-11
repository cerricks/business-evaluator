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
package com.github.cerricks.evaluator.controller;

import com.github.cerricks.evaluator.model.Loan;
import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.model.LoanTerm;
import com.github.cerricks.evaluator.model.LoanTermUnit;
import static com.github.cerricks.evaluator.model.LoanTermUnit.MONTHS;
import static com.github.cerricks.evaluator.model.LoanTermUnit.YEARS;
import com.github.cerricks.evaluator.ui.CustomCurrencyStringConverter;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import com.github.cerricks.evaluator.util.LoanPaymentCalculator;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.flywaydb.core.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles displaying view to edit loan payments.
 *
 * @author cerricks
 */
@Component
public class LoanPaymentEditDialogController {

    private static final Logger logger = LoggerFactory.getLogger(LoanPaymentEditDialogController.class);

    private Stage stage;

    private LoanPayment loanPayment;

    @Autowired
    private LoanPaymentCalculator loanPaymentCalculator;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextField amountField;

    @FXML
    private TextField rateField;

    @FXML
    private Spinner<Integer> termField;

    @FXML
    public ComboBox<LoanTermUnit> termUnitField;

    public LoanPaymentEditDialogController() {
    }

    @FXML
    protected void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: LoanPaymentEditDialogController");
        }

        amountField.setTextFormatter(new TextFormatter(new CustomCurrencyStringConverter()));
        rateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));
        termField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60, 1));
        termUnitField.getItems().addAll(LoanTermUnit.values());
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param stage the stage for this dialog.
     */
    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the item to be edited in the dialog.
     *
     * @param loanPayment the item to be edited
     */
    public void setLoanPayment(final LoanPayment loanPayment) {
        this.loanPayment = loanPayment;

        Loan loan = loanPayment.getLoan();

        categoryLabel.setText(loanPayment.getCategory().getName());
        amountField.setText(Double.toString(loan.getAmount()));
        rateField.setText(Double.toString(loan.getRate() * 100));
        termField.getValueFactory().setValue(loan.getTerm().getLength());
        termUnitField.getSelectionModel().select(loan.getTerm().getUnit());
    }

    /**
     * Called when the user clicks OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            Double amount = Double.valueOf(amountField.getTextFormatter().getValue().toString());
            Double rate = Double.valueOf(rateField.getTextFormatter().getValue().toString());
            Integer term = termField.getValue();
            LoanTermUnit termUnit = termUnitField.getSelectionModel().getSelectedItem();

            LoanTerm loanTerm = null;

            switch (termUnit) {
                case YEARS:
                    loanTerm = LoanTerm.ofYears(term);
                    break;

                case MONTHS:
                    loanTerm = LoanTerm.ofMonths(term);
                    break;
            }

            Loan loan = new Loan(amount, rate, loanTerm);

            double monthlyPayment = loanPaymentCalculator.calculateMonthlyPayment(loan);
            double totalInterestPayment = loanPaymentCalculator.calculateInterestPaid(loan);

            /**
             * Note: The order that these properties are set is important. The loan must be set at
             * the end due to the listener on it that requires other properties to be set.
             *
             * TODO: is there a better way to handle this? Possibly delaying listener until end of
             * method???
             */
            loanPayment.monthlyPaymentProperty().set(monthlyPayment);
            loanPayment.annualPaymentProperty().set(monthlyPayment * 12);
            loanPayment.totalInterestPaymentProperty().set(totalInterestPayment);
            loanPayment.setLoan(loan);

            stage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        stage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        boolean validInput = true;

        if (!StringUtils.hasText(amountField.getText())) {
            amountField.getStyleClass().add("error");

            validInput = false;
        } else {
            amountField.getStyleClass().remove("error");
        }

        if (!StringUtils.hasText(rateField.getText())) {
            rateField.getStyleClass().add("error");

            validInput = false;
        } else {
            rateField.getStyleClass().remove("error");
        }

        if (termField.getValue() == null) {
            termField.getStyleClass().add("error");

            validInput = false;
        } else {
            termField.getStyleClass().remove("error");
        }

        return validInput;
    }

}
