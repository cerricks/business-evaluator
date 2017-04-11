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
package com.github.cerricks.evaluator.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author cerricks
 */
public class DebtRatio {

    private final StringProperty nameProperty = new SimpleStringProperty();
    private final ObjectProperty<Ratio> preDebtRatioProperty = new SimpleObjectProperty();
    private final ObjectProperty<Ratio> postTaxRatioProperty = new SimpleObjectProperty();

    public DebtRatio(final NamedProperty value, final DoubleProperty preDebtValue, final DoubleProperty postTaxValue) {
        nameProperty.set(value.getName());
        preDebtRatioProperty.set(new Ratio(preDebtValue, value));
        postTaxRatioProperty.set(new Ratio(postTaxValue, value));
    }

    public DebtRatio(final String name, final DoubleProperty value, final DoubleProperty preDebtValue, final DoubleProperty postTaxValue) {
        nameProperty.set(name);
        preDebtRatioProperty.set(new Ratio(preDebtValue, value));
        postTaxRatioProperty.set(new Ratio(postTaxValue, value));
    }

    public String getName() {
        return nameProperty.get();
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public Ratio getPreDebtRatio() {
        return preDebtRatioProperty.get();
    }

    public DoubleProperty preDebtMultipleProperty() {
        return preDebtRatioProperty.get().multipleProperty();
    }

    public DoubleProperty preDebtPercentageProperty() {
        return preDebtRatioProperty.get().percentageProperty();
    }

    public Ratio getPostTaxRatio() {
        return postTaxRatioProperty.get();
    }

    public DoubleProperty postTaxMultipleProperty() {
        return postTaxRatioProperty.get().multipleProperty();
    }

    public DoubleProperty postTaxPercentageProperty() {
        return postTaxRatioProperty.get().percentageProperty();
    }

}
