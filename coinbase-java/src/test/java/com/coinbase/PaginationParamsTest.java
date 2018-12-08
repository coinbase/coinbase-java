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

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.coinbase.PaginationParams.ENDING_BEFORE_KEY;
import static com.coinbase.PaginationParams.STARTING_AFTER_KEY;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PaginationParamsTest {

    @Before
    public void setUp() {
    }

    @Test
    public void noNextPageNull_ParamsNull() {
        // Given
        PagedResponse.Pagination pagination = new PagedResponse.Pagination();

        // When
        PaginationParams paginationParams = PaginationParams.nextPage(pagination);

        // Then
        assertThat(paginationParams).isNull();
    }

    @Test
    public void noPreviousPageNull_ParamsNull() {
        // Given
        PagedResponse.Pagination pagination = new PagedResponse.Pagination();

        // When
        PaginationParams paginationParams = PaginationParams.previousPage(pagination);

        // Then
        assertThat(paginationParams).isNull();
    }

    @Test
    public void noNextPage_ParamsCreated() {
        // Given
        String nextItemId = "nextid";
        PagedResponse.Pagination pagination = new PagedResponse.Pagination();
        pagination.setNextUri("/path?" + STARTING_AFTER_KEY + "=" + nextItemId);
        pagination.setLimit(1);
        pagination.setOrder(PageOrder.ASC);

        // When
        PaginationParams paginationParams = PaginationParams.nextPage(pagination);

        // Then
        assertThat(paginationParams.getStartingAfter()).isEqualTo(nextItemId);
        assertThat(paginationParams.getLimit()).isEqualTo(pagination.getLimit());
        assertThat(paginationParams.getOrder()).isEqualTo(pagination.getOrder());
    }

    @Test
    public void noPreviousPage_ParamsCreated() {
        // Given
        String previousId = "previousid";
        PagedResponse.Pagination pagination = new PagedResponse.Pagination();
        pagination.setPreviousUri("/path?" + ENDING_BEFORE_KEY + "=" + previousId);
        pagination.setLimit(2);
        pagination.setOrder(PageOrder.DESC);

        // When
        PaginationParams paginationParams = PaginationParams.previousPage(pagination);

        // Then
        assertThat(paginationParams.getEndingBefore()).isEqualTo(previousId);
        assertThat(paginationParams.getLimit()).isEqualTo(pagination.getLimit());
        assertThat(paginationParams.getOrder()).isEqualTo(pagination.getOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noNextPage_nextIdNotProvided_ExceptionThrown() {
        // Given
        PagedResponse.Pagination pagination = new PagedResponse.Pagination();
        pagination.setNextUri("/path");

        // When
        PaginationParams.nextPage(pagination);

        // Then
        Assertions.fail("Should throw an " + IllegalArgumentException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noPreviousPage_previousIdNotProvided_ExceptionThrown() {
        // Given
        PagedResponse.Pagination pagination = new PagedResponse.Pagination();
        pagination.setPreviousUri("/path");

        // When
        PaginationParams.previousPage(pagination);

        // Then
        Assertions.fail("Should throw an " + IllegalArgumentException.class);
    }

}