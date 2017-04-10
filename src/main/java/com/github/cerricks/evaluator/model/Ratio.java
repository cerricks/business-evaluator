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
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author cerricks
 */
public class Ratio {

    private final DoubleProperty firstProperty;
    private final DoubleProperty secondProperty;
    private final DoubleProperty percentageProperty = new SimpleDoubleProperty();
    private final DoubleProperty multipleProperty = new SimpleDoubleProperty();

    public Ratio(final DoubleProperty firstProperty, final DoubleProperty secondProperty) {
        this.firstProperty = firstProperty;
        this.secondProperty = secondProperty;
        this.percentageProperty.set(calculatePercentageRatio());
        this.multipleProperty.set(calculateMultipleRatio());

        this.firstProperty.addListener((observable, oldValue, newValue) -> {
            percentageProperty.set(calculatePercentageRatio());
            multipleProperty.set(calculateMultipleRatio());
        });

        this.secondProperty.addListener((observable, oldValue, newValue) -> {
            percentageProperty.set(calculatePercentageRatio());
            multipleProperty.set(calculateMultipleRatio());
        });
    }

    private double calculatePercentageRatio() {
        double a = firstProperty.doubleValue();
        double b = secondProperty.doubleValue();

        return a != 0
                ? (a - b) / a
                : 0;
    }

    private double calculateMultipleRatio() {
        double a = firstProperty.doubleValue();
        double b = secondProperty.doubleValue();

        return b != 0
                ? a / b
                : 0;
    }

    public DoubleProperty firstProperty() {
        return firstProperty;
    }

    public DoubleProperty secondProperty() {
        return secondProperty;
    }

    public double getPercentage() {
        return percentageProperty.get();
    }

    public DoubleProperty percentageProperty() {
        return percentageProperty;
    }

    public double getMultiple() {
        return multipleProperty.get();
    }

    public DoubleProperty multipleProperty() {
        return multipleProperty;
    }

}
