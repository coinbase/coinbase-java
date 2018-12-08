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

package com.coinbase.util;

import com.coinbase.Coinbase;
import com.coinbase.resources.base.DynamicResource;
import com.coinbase.resources.base.DynamicResourceDeserializer;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;

import okio.Buffer;
import okio.Okio;

public class Resource {

    public static Buffer readResource(String resourceName, Object test) throws IOException {
        try (Buffer buffer = new Buffer()) {
            buffer.writeAll(Okio.source(test.getClass().getClassLoader().getResourceAsStream(resourceName)));
            return buffer;
        }
    }

    public static HashMap<String, String> parseJsonAsHashMap(Buffer buffer) {
        return parseJsonAsHashMap(buffer.readUtf8());
    }

    public static HashMap<String, String> parseJsonAsHashMap(String json) {
        final TypeToken<HashMap<String, String>> typeToken = new TypeToken<HashMap<String, String>>() {
        };

        return Coinbase.createGsonBuilder().create().fromJson(json, typeToken.getType());
    }

    public static <T> T parseJsonAsObject(Buffer buffer, Class<T> clazz) {
        return parseJsonAsObject(buffer.readUtf8(), clazz);
    }

    public static <T> T parseJsonAsObject(String json, Class<T> clazz) {
        return Coinbase.createGsonBuilder()
                .registerTypeAdapter(DynamicResource.class, new DynamicResourceDeserializer())
                .create()
                .fromJson(json, clazz);
    }
}
