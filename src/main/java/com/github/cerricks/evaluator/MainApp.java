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
package com.github.cerricks.evaluator;

import static com.github.cerricks.evaluator.Constants.FXML_INCOME_TAX_RATE_EDIT_DIALOG;
import static com.github.cerricks.evaluator.Constants.FXML_INPUT_CATEGORY_EDIT_DIALOG;
import static com.github.cerricks.evaluator.Constants.FXML_LOAN_PAYMENT_EDIT_DIALOG;
import static com.github.cerricks.evaluator.Constants.FXML_MAIN;
import static com.github.cerricks.evaluator.Constants.FXML_SETTINGS;
import static com.github.cerricks.evaluator.Constants.FXML_USER_DEFINED_INPUT_CREATE_DIALOG;
import static com.github.cerricks.evaluator.Constants.FXML_USER_DEFINED_INPUT_EDIT_DIALOG;
import static com.github.cerricks.evaluator.Constants.STYLESHEET_DEFAULT;
import com.github.cerricks.evaluator.controller.IncomeTaxRateEditDialogController;
import com.github.cerricks.evaluator.controller.InputCategoryEditDialogController;
import com.github.cerricks.evaluator.controller.LoanPaymentEditDialogController;
import com.github.cerricks.evaluator.controller.SettingsController;
import com.github.cerricks.evaluator.controller.UserDefinedInputCreateDialogController;
import com.github.cerricks.evaluator.controller.UserDefinedInputEditDialogController;
import com.github.cerricks.evaluator.model.IncomeTaxRate;
import com.github.cerricks.evaluator.model.Input;
import com.github.cerricks.evaluator.model.InputCategory;
import com.github.cerricks.evaluator.model.LoanPayment;
import com.github.cerricks.evaluator.service.FxmlLoaderService;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;

/**
 * Main application.
 *
 * @author cerricks
 */
@Lazy
@SpringBootApplication
public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    private Stage mainStage;
    private Stage settingsStage;
    private Parent rootLayout;
    private static String[] savedArgs;
    private static ConfigurableApplicationContext applicationContext;

    @Autowired
    private FxmlLoaderService fxmlLoaderService;

    public static void main(final String[] args) {
        savedArgs = args;

        launch(MainApp.class, args);
    }

    @Override
    public void init() throws Exception {
        Thread.currentThread().setName("main");

        applicationContext = SpringApplication.run(getClass(), savedArgs);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        Thread.currentThread().setName("main-ui");

        // register exception handler
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            logger.error("Handling uncaught exception", throwable);
        });

        this.mainStage = stage;

        initRootLayout();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        applicationContext.close();
    }

    /**
     * Initialize the root layout.
     */
    public void initRootLayout() {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_MAIN));

        try {
            rootLayout = loader.load();
        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_MAIN, ex);

            return;
        }

        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(STYLESHEET_DEFAULT);

        mainStage.setScene(scene);
        mainStage.setTitle("Business eValuator");
        mainStage.setResizable(true);
        mainStage.centerOnScreen();
        mainStage.setHeight(900);
        mainStage.show();
    }

    public void showUserDefinedInputCreateDialog() {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_USER_DEFINED_INPUT_CREATE_DIALOG));

        Parent page;

        try {
            page = loader.load();
        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_USER_DEFINED_INPUT_CREATE_DIALOG, ex);

            return;
        }

        // set the stage
        Stage stage = new Stage();
        stage.setTitle("Create Custom Input");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.initOwner(mainStage);

        Scene scene = new Scene(page);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET_DEFAULT).toExternalForm());

        stage.setScene(scene);

        // Set the item into the controller.
        UserDefinedInputCreateDialogController controller = loader.getController();
        controller.setStage(stage);

        stage.showAndWait();
    }

    public void showUserDefinedInputEditDialog(final Input userDefinedInput) {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_USER_DEFINED_INPUT_EDIT_DIALOG));

        Parent page;

        try {
            page = loader.load();
        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_USER_DEFINED_INPUT_EDIT_DIALOG, ex);

            return;
        }

        // set the stage
        Stage stage = new Stage();
        stage.setTitle("Edit Custom Input");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.initOwner(mainStage);

        Scene scene = new Scene(page);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET_DEFAULT).toExternalForm());

        stage.setScene(scene);

        // Set the item into the controller.
        UserDefinedInputEditDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setUserDefinedInput(userDefinedInput);

        stage.showAndWait();
    }

    public void showInputCategoryEditDialog(final InputCategory inputCategory) {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_INPUT_CATEGORY_EDIT_DIALOG));

        Parent page;

        try {
            page = loader.load();
        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_INPUT_CATEGORY_EDIT_DIALOG, ex);

            return;
        }

        // set the stage
        Stage stage = new Stage();
        stage.setTitle("Edit Input Category");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.initOwner(settingsStage);

        Scene scene = new Scene(page);
        scene.getStylesheets().add(STYLESHEET_DEFAULT);

        stage.setScene(scene);

        // Set the item into the controller.
        InputCategoryEditDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setInputCategory(inputCategory);

        stage.showAndWait();
    }

    public void showIncomeTaxRateEditDialog(final IncomeTaxRate incomeTaxRate) {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_INCOME_TAX_RATE_EDIT_DIALOG));

        Parent page;

        try {
            page = loader.load();
        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_INCOME_TAX_RATE_EDIT_DIALOG, ex);

            return;
        }

        // set the stage
        Stage stage = new Stage();
        stage.setTitle("Edit Income Tax Rate");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.initOwner(settingsStage);

        Scene scene = new Scene(page);
        scene.getStylesheets().add(STYLESHEET_DEFAULT);

        stage.setScene(scene);

        // Set the item into the controller.
        IncomeTaxRateEditDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setIncomeTaxRate(incomeTaxRate);

        stage.showAndWait();
    }

    public void showLoanEditDialog(final LoanPayment loanPayment) {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_LOAN_PAYMENT_EDIT_DIALOG));

        Parent page;

        try {
            page = loader.load();
        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_LOAN_PAYMENT_EDIT_DIALOG, ex);

            return;
        }

        // set the stage
        Stage stage = new Stage();
        stage.setTitle("Edit Loan");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.initOwner(mainStage);

        Scene scene = new Scene(page);
        scene.getStylesheets().add(STYLESHEET_DEFAULT);

        stage.setScene(scene);

        // Set the item into the controller.
        LoanPaymentEditDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setLoanPayment(loanPayment);

        stage.showAndWait();
    }

    public void showSettings() {
        FXMLLoader loader = fxmlLoaderService.getLoader(getClass().getResource(FXML_SETTINGS));

        Parent page;

        try {
            page = loader.load();

        } catch (IOException ex) {
            logger.warn("Failed to load: " + FXML_SETTINGS, ex);

            return;
        }

        // set the stage
        settingsStage = new Stage();
        settingsStage.setTitle("Settings");
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.centerOnScreen();
        settingsStage.initOwner(mainStage);

        Scene scene = new Scene(page);
        scene.getStylesheets().add(STYLESHEET_DEFAULT);

        settingsStage.setScene(scene);

        // Set the item into the controller.
        SettingsController controller = loader.getController();
        controller.setStage(settingsStage);

        settingsStage.showAndWait();
    }

}
