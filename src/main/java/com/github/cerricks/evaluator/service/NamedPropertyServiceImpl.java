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
package com.github.cerricks.evaluator.service;

import com.github.cerricks.evaluator.dao.NamedPropertyRepository;
import com.github.cerricks.evaluator.model.NamedProperty;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cerricks
 */
@Service
public class NamedPropertyServiceImpl implements NamedPropertyService {

    private static final Logger logger = LoggerFactory.getLogger(NamedPropertyServiceImpl.class);

    @Autowired
    private NamedPropertyRepository propertyRepository;

    public NamedPropertyServiceImpl() {
    }

    @Override
    public NamedProperty getByName(final String name) {
        return propertyRepository.findOne(name);
    }

    @Override
    public void setValue(final String name, final Number value) {
        NamedProperty property = getByName(name);

        property.setValue(value);

        propertyRepository.save(property);
    }

    @Override
    public Number getValue(final String name) {
        return getByName(name).getValue();
    }

    @Override
    public List<NamedProperty> getNamedProperties() {
        return propertyRepository.findAll();
    }

}
