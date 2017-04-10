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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A user defined input amount.
 *
 * @author cerricks
 */
public class Input {

    private final ObjectProperty<InputCategory> categoryProperty = new ReadOnlyObjectWrapper();
    private final DoubleProperty valueProperty;

    public Input(final InputCategory category) {
        this.categoryProperty.set(category);
        this.valueProperty = new SimpleDoubleProperty(this, category.getName());
    }

    public Input(final InputCategory category, final double value) {
        this.categoryProperty.set(category);
        this.valueProperty = new SimpleDoubleProperty(this, category.getName(), value);
    }

    public Input(final InputCategory category, final DoubleProperty valueProperty) {
        this.categoryProperty.set(category);
        this.valueProperty = valueProperty;
    }

    public String getName() {
        return valueProperty.getName();
    }

    public InputCategory getCategory() {
        return categoryProperty.get();
    }

    public ObjectProperty<InputCategory> categoryProperty() {
        return categoryProperty;
    }

    public double getValue() {
        return valueProperty.get();
    }

    public void setValue(final double value) {
        valueProperty.set(value);
    }

    public DoubleProperty valueProperty() {
        return valueProperty;
    }

}
