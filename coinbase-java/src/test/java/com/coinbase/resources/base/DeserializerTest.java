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

import com.coinbase.Coinbase;
import com.coinbase.resources.accounts.Account;
import com.coinbase.resources.transactions.entities.EmailResource;
import com.coinbase.resources.users.User;
import com.coinbase.util.Resource;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for custom JSON deserialization of {@link DynamicResource}.
 */
@RunWith(RobolectricTestRunner.class)
public class DeserializerTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = Coinbase.createGsonBuilder()
                .registerTypeAdapter(DynamicResource.class, new DynamicResourceDeserializer())
                .create();
    }

    @Test
    public void shouldDeserializeAccount() throws Exception {
        shouldDeserializeType(Account.class, "accounts/account.json");
    }

    @Test
    public void shouldDeserializeUser() throws Exception {
        shouldDeserializeType(User.class, "users/user_by_id.json");
    }

    @Test
    public void shouldDeserializeEmail() throws Exception {
        final EmailResource emailResource = shouldDeserializeType(EmailResource.class, "email.json");

        assertThat(emailResource.getEmail())
                .isNotNull();

        assertThat(emailResource.getCurrency())
                .isNotNull();
    }

    @Test
    public void shouldDeserializeUnknownResource() {
        // Given:
        final String json = "{\"resource\": \"unknown\", \"id\": 123.0, \"name\":\"hello\"}";

        // When:
        final DynamicResource resource = gson.fromJson(json, DynamicResource.class);

        // Then:
        assertThat(resource.getResource()).isEqualTo("unknown");
        assertThat(resource).isInstanceOf(UnknownResource.class);

        final UnknownResource data = (UnknownResource) resource;
        assertThat(data.getFields().get("id")).isEqualTo(123.0);
        assertThat(data.getFields().get("name")).isEqualTo("hello");
    }

    private <T> T shouldDeserializeType(Class<T> clazz, String resourceName) throws Exception {
        // Given:
        final String json = Resource.readResource(resourceName, this).readUtf8();

        // When:
        final TypeToken<DataWrapper<DynamicResource>> typeToken = new TypeToken<DataWrapper<DynamicResource>>() {
        };
        final DataWrapper<DynamicResource> wrapper = gson.fromJson(json, typeToken.getType());

        // Then:
        assertThat(wrapper.data)
                .isNotNull()
                .isInstanceOf(clazz);

        //noinspection unchecked
        return (T) wrapper.data;
    }

    static class DataWrapper<T> {
        @SerializedName("data")
        T data;
    }
}
