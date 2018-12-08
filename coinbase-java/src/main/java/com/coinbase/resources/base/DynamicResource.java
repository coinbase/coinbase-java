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

/**
 * Marker class with a custom JSON deserializer for dynamic type parsing.
 *
 * @see com.coinbase.resources.transactions.Transaction#getTo()
 * @see com.coinbase.resources.transactions.Transaction#getFrom()
 */
public abstract class DynamicResource extends BaseResource {

}
