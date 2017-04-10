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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@code FMLXService} for loading FXML from Spring
 * context.
 *
 * @author cerricks
 */
@Service
@Scope("singleton")
public class SpringFxmlLoaderService implements FxmlLoaderService {

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public FXMLLoader getLoader() {
        FXMLLoader loader = new FXMLLoader();

        loader.setControllerFactory((Class<?> param) -> context.getBean(param));

        return loader;
    }

    @Override
    public FXMLLoader getLoader(final URL location) {
        FXMLLoader loader = new FXMLLoader(location);

        loader.setControllerFactory((Class<?> param) -> context.getBean(param));

        return loader;
    }

    @Override
    public FXMLLoader getLoader(final URL location, final ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(location, resourceBundle);

        loader.setControllerFactory((Class<?> param) -> context.getBean(param));

        return loader;
    }

}
