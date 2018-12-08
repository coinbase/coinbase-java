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

import com.coinbase.resources.accounts.Account;
import com.coinbase.resources.transactions.entities.EmailResource;
import com.coinbase.resources.users.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public final class DynamicResourceDeserializer extends ResourceTypeDeserializer<DynamicResource> {

    public DynamicResourceDeserializer() {
        typeMapping.put("account", Account.class);
        typeMapping.put("user", User.class);
        typeMapping.put("email", EmailResource.class);

    }

    public void addTypeMapping(final String name, Class<? extends DynamicResource> type) {
        typeMapping.put(name, type);
    }

    @Override
    public DynamicResource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json.getAsJsonObject();
        final String resource = object.get("resource").getAsString();
        final Class<? extends DynamicResource> type = typeMapping.get(resource);

        if (type == null) {
            final UnknownResource data = context.deserialize(json, UnknownResource.class);
            // Parse unknown fields into map.
            data.fields = context.deserialize(json, new TypeToken<Map<String, Object>>() {}.getType());
            return data;
        }

        return context.deserialize(json, type);
    }
}

