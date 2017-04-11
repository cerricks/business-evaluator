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
 * Represents the ratio between two properties.
 *
 * @author cerricks
 */
public class Ratio {

    private final DoubleProperty firstProperty;
    private final DoubleProperty secondProperty;
    private final DoubleProperty percentageProperty = new SimpleDoubleProperty();
    private final DoubleProperty multipleProperty = new SimpleDoubleProperty();

    /**
     * Creates a ratio between two properties.
     *
     * <p>
     * firstProperty : secondProperty</p>
     *
     * @param firstProperty the first property in the ratio.
     * @param secondProperty the second property in the ratio.
     */
    public Ratio(final DoubleProperty firstProperty, final DoubleProperty secondProperty) {
        if (firstProperty == null) {
            throw new IllegalArgumentException("firstProperty cannot be null");
        }

        if (secondProperty == null) {
            throw new IllegalArgumentException("secondProperty cannot be null");
        }

        this.firstProperty = firstProperty;
        this.secondProperty = secondProperty;
        this.percentageProperty.set(calculatePercentageRatio());
        this.multipleProperty.set(calculateMultipleRatio());

        // add listeners to update ratios if the underlying properties change
        this.firstProperty.addListener((observable, oldValue, newValue) -> {
            percentageProperty.set(calculatePercentageRatio());
            multipleProperty.set(calculateMultipleRatio());
        });

        this.secondProperty.addListener((observable, oldValue, newValue) -> {
            percentageProperty.set(calculatePercentageRatio());
            multipleProperty.set(calculateMultipleRatio());
        });
    }

    /**
     * Calculates the percentage between two properties.
     *
     * @return the percentage between two properties.
     */
    private double calculatePercentageRatio() {
        double a = firstProperty.doubleValue();
        double b = secondProperty.doubleValue();

        return a != 0
                ? b / a
                : 0;
    }

    /**
     * Calculates the multiple between two properties.
     *
     * @return the multiple between two properties.
     */
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
