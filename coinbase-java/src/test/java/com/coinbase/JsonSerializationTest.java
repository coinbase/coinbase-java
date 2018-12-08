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

package com.coinbase;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(RobolectricTestRunner.class)
public class JsonSerializationTest {

    private Coinbase coinbase;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        // Set default timezone to UTC for test to pass on different machines and timezones.
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        coinbase = CoinbaseBuilder.withPublicDataAccess(RuntimeEnvironment.application).build();
        gson = coinbase.gson;
    }

    @Test
    public void shouldDeserializeModelWithSnakeCase() {
        // Given:
        //language=JSON
        String json = "{ \"class\": 1, \"some_long_name\": \"The name\", \"created_at\": \"2018-01-30T10:30:40Z\" }";

        // When:
        final Model model = gson.fromJson(json, Model.class);

        // Then:
        assertThat(model.modelClass).isEqualTo(1);
        assertThat(model.someLongName).isEqualTo("The name");

        final Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 0, 30, 10, 30, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertThat(model.createdAt).isInstanceOf(Date.class).isEqualTo(calendar.getTime());
    }

    @Test
    public void shouldSerializeRequestModel() {
        // Given:

        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2018, 0, 30, 10, 30, 40);
        calendar.set(Calendar.MILLISECOND, 0);
        final long time = calendar.getTime().getTime();
        final Date date = new Date(time);

        System.err.println("calendar = " + calendar.getTime());

        final Model model = new Model();
        model.someLongName = "Serialized";
        model.modelClass = 10;
        model.createdAt = date;

        // When:
        final String json = gson.toJson(model);

        // Then:
        assertThat(json).containsOnlyOnce("\"class\":10");
        assertThat(json).containsOnlyOnce("\"some_long_name\":\"Serialized\"");
        assertThat(json).containsOnlyOnce("\"created_at\":\"2018-01-30T10:30:40Z\"");

    }

    static class Model {

        String someLongName;

        @SerializedName("class")
        int modelClass;

        Date createdAt;
    }
}