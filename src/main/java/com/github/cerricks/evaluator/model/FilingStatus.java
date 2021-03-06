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

/**
 * Income tax filing status.
 *
 * @author cerricks
 */
public enum FilingStatus {

    SINGLE {
        @Override
        public String toString() {
            return "Single";
        }
    },
    MARRIED_FILING_JOINTLY {
        @Override
        public String toString() {
            return "Married Filing Jointly";
        }
    },
    QUALIFYING_WIDOWER {
        @Override
        public String toString() {
            return "Qualifying Widow(er)";
        }
    },
    MARRIED_FILING_SEPARATELY {
        @Override
        public String toString() {
            return "Married Filing Separately";
        }
    },
    HEAD_OF_HOUSEHOLD {
        @Override
        public String toString() {
            return "Head of Household";
        }
    };

}
