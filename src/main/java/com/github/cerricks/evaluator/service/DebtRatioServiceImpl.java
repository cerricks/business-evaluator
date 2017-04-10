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
package com.github.cerricks.evaluator.service;

import com.github.cerricks.evaluator.model.DebtRatio;
import com.github.cerricks.evaluator.model.NamedProperty;
import java.util.Iterator;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing {@link DebtRatio} objects.
 *
 * @author cerricks
 */
@Service
public class DebtRatioServiceImpl implements DebtRatioService {

    private static final Logger logger = LoggerFactory.getLogger(DebtRatioServiceImpl.class);

    private final ObservableList<DebtRatio> ratios;

    private NamedProperty originalCashFlowProperty;

    private NamedProperty cashAfterTaxProperty;

    public DebtRatioServiceImpl() {
        ratios = FXCollections.observableArrayList();
    }

    @PostConstruct
    public void init() {
        if (originalCashFlowProperty == null) {
            throw new IllegalStateException("originalCashFlowProperty cannot be null");
        }

        if (cashAfterTaxProperty == null) {
            throw new IllegalStateException("originalCashFlowProperty cannot be null");
        }
    }

    public NamedProperty getOriginalCashFlowProperty() {
        return originalCashFlowProperty;
    }

    public void setOriginalCashFlowProperty(NamedProperty originalCashFlowProperty) {
        this.originalCashFlowProperty = originalCashFlowProperty;
    }

    public NamedProperty getCashAfterTaxProperty() {
        return cashAfterTaxProperty;
    }

    public void setCashAfterTaxProperty(NamedProperty cashAfterTaxProperty) {
        this.cashAfterTaxProperty = cashAfterTaxProperty;
    }

    @Override
    public DebtRatio createRatio(final DoubleProperty property) {
        // check if ratio already exists
        for (DebtRatio ratio : ratios) {
            if (ratio.getName().equals(property.getName())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Debt ratio already exists for property with name [" + property.getName() + "]");
                }

                return null;
            }
        }

        DebtRatio ratio = new DebtRatio(property, originalCashFlowProperty, cashAfterTaxProperty);

        ratios.add(ratio);

        return ratio;
    }

    @Override
    public ObservableList<DebtRatio> getRatios() {
        return ratios;
    }

    @Override
    public void removeRatio(final DebtRatio ratio) {
        ratios.remove(ratio);
    }

    @Override
    public void removeRatio(final DoubleProperty property) {
        Iterator<DebtRatio> it = ratios.iterator();

        DebtRatio ratio;

        while (it.hasNext()) {
            ratio = it.next();

            if (ratio.getName().equals(property.getName())) {
                removeRatio(ratio);
            }
        }
    }

}
