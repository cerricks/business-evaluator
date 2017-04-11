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
package com.github.cerricks.evaluator.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A base controller for managing application settings.
 *
 * @author cerricks
 */
@Component
public class SettingsController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private Stage stage;

    @Autowired
    private IncomeTaxRateSettingsController incomeTaxRateSettingsController;

    @Autowired
    private InputCategorySettingsController inputCategorySettingsController;

    public SettingsController() {
    }

    public void setStage(final Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing: SettingsController");
        }
    }

    @FXML
    private void handleCancel() {
        if (logger.isDebugEnabled()) {
            logger.debug("Reverting changes to settings");
        }

        incomeTaxRateSettingsController.cancel();
        inputCategorySettingsController.cancel();

        stage.close();
    }

    @FXML
    private void handleSave() {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving changes to settings");
        }

        // try to save income tax rate settings
        try {
            incomeTaxRateSettingsController.saveSettings();
        } catch (Exception ex) {
            logger.warn("Failed to save income tax rate settings", ex);
        }

        // try to save custom input category settings
        try {
            inputCategorySettingsController.saveSettings();
        } catch (Exception ex) {
            logger.warn("Failed to save custom input category settings", ex);
        }

        stage.close();
    }

}
