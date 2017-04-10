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

import java.io.Serializable;
import java.text.NumberFormat;
import javax.persistence.Embeddable;

/**
 * Represents a range of two number.
 *
 * @author cerricks
 * @param <T> number type
 */
@Embeddable
public class NumberRange<T extends Number> implements Serializable {

    private NumberFormat formatter;
    private T rangeStart;
    private T rangeEnd;

    protected NumberRange() {
        this.formatter = NumberFormat.getNumberInstance();
    }

    public NumberRange(final T rangeStart, final T rangeEnd) {
        this(rangeStart, rangeEnd, NumberFormat.getNumberInstance());
    }

    public NumberRange(final T rangeStart, final T rangeEnd, final NumberFormat formatter) {
        if (rangeStart == null && rangeEnd == null) {
            throw new IllegalArgumentException("'rangeStart' and 'rangeEnd' for this range cannot both be null");
        }

        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.formatter = formatter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (rangeStart != null) {
            sb.append(formatter.format(rangeStart));

            if (rangeEnd != null) {
                sb.append(" - ").append(formatter.format(rangeEnd));
            } else {
                sb.append(" and above");
            }
        } else {
            sb.append(formatter.format(rangeEnd)).append(" and below");
        }

        return sb.toString();
    }

    public T getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(final T rangeStart) {
        this.rangeStart = rangeStart;
    }

    public T getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(final T rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public void setFormatter(final NumberFormat formatter) {
        this.formatter = formatter;
    }

}
