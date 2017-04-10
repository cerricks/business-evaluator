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

import static com.github.cerricks.evaluator.Constants.PROPERTY_CASH_AFTER_TAX;
import static com.github.cerricks.evaluator.Constants.PROPERTY_ORIGINAL_CASH_FLOW;
import com.github.cerricks.evaluator.model.DebtRatio;
import com.github.cerricks.evaluator.model.Ratio;
import java.util.Iterator;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing {@link Ratio} objects.
 *
 * @author cerricks
 */
@Service
public class DebtRatioServiceImpl implements DebtRatioService {

    private static final Logger logger = LoggerFactory.getLogger(DebtRatioServiceImpl.class);

    private final ObservableList<DebtRatio> ratios;

    @Autowired
    private NamedPropertyService namedPropertyService;

    public DebtRatioServiceImpl() {
        ratios = FXCollections.observableArrayList();
    }

    @PostConstruct
    private void init() {
    }

    @Override
    public DebtRatio createRatio(final DoubleProperty property) {
        // check if ratio already exists
        for (DebtRatio ratio : ratios) {
            if (ratio.getName().equals(property.getName())) {
                return null;
            }
        }

        DebtRatio ratio = new DebtRatio(property, namedPropertyService.getByName(PROPERTY_ORIGINAL_CASH_FLOW), namedPropertyService.getByName(PROPERTY_CASH_AFTER_TAX));

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
