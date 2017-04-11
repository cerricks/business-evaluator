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

import static com.github.cerricks.evaluator.model.InputType.GENERAL_NUMBER;
import java.io.Serializable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

/**
 * Input details.
 *
 * @author cerricks
 */
@Entity
@Table(name = "input_categories")
public class InputCategory implements Serializable {

    private Long id;
    private final StringProperty nameProperty = new SimpleStringProperty();
    private final ObjectProperty<InputSource> sourceProperty = new ReadOnlyObjectWrapper();
    private final ObjectProperty<InputType> typeProperty = new ReadOnlyObjectWrapper();
    private final BooleanProperty additionalExpenseProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<LoanRate> defaultLoanRateProperty = new SimpleObjectProperty();

    protected InputCategory() {
    }

    public InputCategory(final String name) {
        this(name, GENERAL_NUMBER);
    }

    public InputCategory(final String name, final InputType type) {
        nameProperty.set(name);
        typeProperty.set(type);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final InputCategory other = (InputCategory) obj;

        return this.getName().equals(other.getName());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Column(name = "input_name")
    public String getName() {
        return nameProperty.get();
    }

    public void setName(final String name) {
        nameProperty.set(name);
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    @Column(name = "input_source")
    @Enumerated(EnumType.STRING)
    public InputSource getSource() {
        return sourceProperty.get();
    }

    public void setSource(final InputSource source) {
        sourceProperty.set(source);
    }

    public ObjectProperty<InputSource> sourceProperty() {
        return sourceProperty;
    }

    @Column(name = "input_type")
    @Enumerated(EnumType.STRING)
    public InputType getType() {
        return typeProperty.get();
    }

    public void setType(final InputType type) {
        typeProperty.set(type);
    }

    public ObjectProperty<InputType> typeProperty() {
        return typeProperty;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "rate", column = @Column(name = "default_loan_rate"))
        ,
        @AttributeOverride(name = "term.length", column = @Column(name = "default_loan_term_length"))
        ,
        @AttributeOverride(name = "term.unit", column = @Column(name = "default_loan_term_unit"))
    })
    public LoanRate getDefaultLoanRate() {
        return defaultLoanRateProperty.get();
    }

    public void setDefaultLoanRate(final LoanRate loanRate) {
        defaultLoanRateProperty.set(loanRate);
    }

    public ObjectProperty<LoanRate> defaultLoanRateProperty() {
        return defaultLoanRateProperty;
    }

    @Column(name = "additional_expense_ind")
    public boolean isAdditionalExpense() {
        return additionalExpenseProperty.get();
    }

    public void setAdditionalExpense(final boolean additionalExpense) {
        additionalExpenseProperty.set(additionalExpense);
    }

    public BooleanProperty additionalExpenseProperty() {
        return additionalExpenseProperty;
    }

}
