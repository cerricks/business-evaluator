/*
 * Copyright 2017 Clifford Errickson.
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

import com.github.cerricks.evaluator.dao.InputCategoryRepository;
import com.github.cerricks.evaluator.model.InputCategory;
import static com.github.cerricks.evaluator.model.InputSource.USER;
import com.github.cerricks.evaluator.model.InputType;
import static com.github.cerricks.evaluator.model.InputType.CURRENCY;
import com.github.cerricks.evaluator.model.LoanRate;
import com.github.cerricks.evaluator.model.LoanTerm;
import com.github.cerricks.evaluator.model.LoanTermUnit;
import static com.github.cerricks.evaluator.model.LoanTermUnit.MONTHS;
import static com.github.cerricks.evaluator.model.LoanTermUnit.YEARS;
import com.github.cerricks.evaluator.ui.CustomPercentageStringConverter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Handles dialog to create user defined input.
 *
 * @author cerricks
 */
@Component
public class UserDefinedInputCreateDialogController {

    private static final Logger logger = LoggerFactory.getLogger(UserDefinedInputCreateDialogController.class);

    private Stage stage;

    @Autowired
    private ObservableList<InputCategory> inputCategories;

    @Autowired
    private InputCategoryRepository inputCategoryRepository;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<InputType> typeField;

    @FXML
    private CheckBox additionalExpenseOption;

    @FXML
    private CheckBox defaultFinancingOption;

    @FXML
    private TextField defaultLoanRateField;

    @FXML
    private TextField defaultLoanLengthField;

    @FXML
    private ChoiceBox<LoanTermUnit> defaultLoanLengthUnitField;

    public UserDefinedInputCreateDialogController() {
    }

    @FXML
    protected void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: UserDefinedInputCreateDialogController");
        }

        typeField.getItems().addAll(InputType.values());
        typeField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == CURRENCY) {
                // enable inputs
                additionalExpenseOption.setDisable(false);
            } else {
                // reset and disable inputs
                additionalExpenseOption.setSelected(false);
                additionalExpenseOption.setDisable(true);
                defaultFinancingOption.setSelected(false);
                defaultFinancingOption.setDisable(true);
                defaultLoanRateField.clear();
                defaultLoanRateField.setDisable(true);
                defaultLoanLengthField.clear();
                defaultLoanLengthField.setDisable(true);
                defaultLoanLengthUnitField.setValue(YEARS);
                defaultLoanLengthUnitField.setDisable(true);
            }
        });

        additionalExpenseOption.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                // enable inputs
                defaultFinancingOption.setDisable(false);
            } else {
                // reset and disable inputs
                defaultFinancingOption.setSelected(false);
                defaultFinancingOption.setDisable(true);
                defaultLoanRateField.clear();
                defaultLoanRateField.setDisable(true);
                defaultLoanLengthField.clear();
                defaultLoanLengthField.setDisable(true);
                defaultLoanLengthUnitField.setValue(YEARS);
                defaultLoanLengthUnitField.setDisable(true);
            }
        });

        defaultFinancingOption.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                // enable inputs
                defaultLoanRateField.setDisable(false);
                defaultLoanLengthField.setDisable(false);
                defaultLoanLengthUnitField.setDisable(false);
            } else {
                // reset and disable inputs
                defaultLoanRateField.clear();
                defaultLoanRateField.setDisable(true);
                defaultLoanLengthField.clear();
                defaultLoanLengthField.setDisable(true);
                defaultLoanLengthUnitField.setValue(YEARS);
                defaultLoanLengthUnitField.setDisable(true);
            }
        });

        defaultLoanRateField.setTextFormatter(new TextFormatter(new CustomPercentageStringConverter()));
        defaultLoanLengthField.setTextFormatter(new TextFormatter(new IntegerStringConverter()));
        defaultLoanLengthUnitField.getItems().addAll(LoanTermUnit.values());
        defaultLoanLengthUnitField.getSelectionModel().select(YEARS);
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            String name = nameField.getText();
            InputType type = typeField.getValue();

            boolean additionalExpense = false;
            LoanRate defaultLoanRate = null;

            if (type == CURRENCY) {
                additionalExpense = additionalExpenseOption.isSelected();

                if (defaultFinancingOption.isSelected()) {
                    Double rate = Double.valueOf(defaultLoanRateField.getTextFormatter().getValue().toString());
                    Integer term = Integer.valueOf(defaultLoanLengthField.getTextFormatter().getValue().toString());
                    LoanTermUnit termUnit = defaultLoanLengthUnitField.getSelectionModel().getSelectedItem();

                    LoanTerm loanTerm = null;

                    switch (termUnit) {
                        case YEARS:
                            loanTerm = LoanTerm.ofYears(term);
                            break;

                        case MONTHS:
                            loanTerm = LoanTerm.ofMonths(term);
                            break;
                    }

                    defaultLoanRate = new LoanRate(rate, loanTerm);
                }
            }

            InputCategory category = new InputCategory(name, type);
            category.setSource(USER);

            category.setAdditionalExpense(additionalExpense);

            if (defaultLoanRate != null) {
                category.setDefaultLoanRate(defaultLoanRate);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Adding new category: " + category);
            }

            inputCategories.add(category);

            inputCategoryRepository.save(category);

            stage.close();
        }
    }

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

        if (!StringUtils.hasText(nameField.getText())) {
            nameField.getStyleClass().add("error");

            validInput = false;
        } else {
            String name = nameField.getText().toUpperCase();

            for (InputCategory category : inputCategories) {
                if (category.getName().toUpperCase().equals(name)) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Invalid Name");
                    alert.setContentText("An input already exists with a name matching [" + name + "]");

                    alert.showAndWait();

                    validInput = false;

                    nameField.getStyleClass().add("error");

                    break;
                }
            }

            if (validInput) {
                nameField.getStyleClass().remove("error");
            }
        }

        if (typeField.getSelectionModel().getSelectedIndex() < 0) {
            typeField.getStyleClass().add("error");

            validInput = false;
        } else {
            typeField.getStyleClass().remove("error");
        }

        if (defaultFinancingOption.isSelected()) {
            if (!StringUtils.hasText(defaultLoanRateField.getText())) {
                defaultLoanRateField.getStyleClass().add("error");

                validInput = false;
            } else {
                defaultLoanRateField.getStyleClass().remove("error");
            }

            if (!StringUtils.hasText(defaultLoanLengthField.getText())) {
                defaultLoanLengthField.getStyleClass().add("error");

                validInput = false;
            } else {
                defaultLoanLengthField.getStyleClass().remove("error");
            }

            if (defaultLoanLengthUnitField.getSelectionModel().getSelectedIndex() < 0) {
                defaultLoanLengthUnitField.getStyleClass().add("error");

                validInput = false;
            } else {
                defaultLoanLengthUnitField.getStyleClass().remove("error");
            }
        }

        return validInput;
    }

}
