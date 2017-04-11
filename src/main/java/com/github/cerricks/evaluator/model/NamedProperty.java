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

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Represents a property with a name.
 *
 * @author cerricks
 */
public class NamedProperty extends SimpleDoubleProperty {

    public NamedProperty(final String name) {
        super(null, name);
    }

    public NamedProperty(final String name, final double initialValue) {
        super(null, name, initialValue);
    }

    public NamedProperty(final Object bean, final String name) {
        super(bean, name);
    }

    public NamedProperty(final Object bean, final String name, final double initialValue) {
        super(bean, name, initialValue);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public String getName() {
        return super.getName();
    }

}
