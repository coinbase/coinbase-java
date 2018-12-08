/*
 * Copyright 2018 Coinbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coinbase.resources.base;

import java.util.Map;

/**
 * Class for handling cases of unknown objects. Holds a reference to map of data
 * fields {@link #getFields()}.
 */
public class UnknownResource extends DynamicResource {

    Map<String, Object> fields;

    public Map<String, Object> getFields() {
        return fields;
    }
}
