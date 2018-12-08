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

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

/**
 * Pagination request parameters.
 * <p>
 * Object containing all pagination parameters for paged request.
 *
 * @see <a href="https://developers.coinbase.com/api/v2#pagination">
 * https://developers.coinbase.com/api/v2#pagination</a>
 */
public class PaginationParams {

    @VisibleForTesting
    static final String ENDING_BEFORE_KEY = "ending_before";

    @VisibleForTesting
    static final String STARTING_AFTER_KEY = "starting_after";

    @VisibleForTesting
    static final String LIMIT_KEY = "limit";

    @VisibleForTesting
    static final String ORDER_KEY = "order";

    /**
     * Creates an instance of {@link PaginationParams} to get next page items.
     * <p>
     * Limits and order are not set.
     *
     * @return {@link PaginationParams} to get next items page.
     */
    public static PaginationParams fromStartingAfter(String startingAfter) {
        PaginationParams params = new PaginationParams();
        params.startingAfter = startingAfter;
        return params;
    }

    /**
     * Creates an instance of {@link PaginationParams} to get previous page items.
     * <p>
     * Limits and order are not set.
     *
     * @return {@link PaginationParams} to get previous items page.
     */
    public static PaginationParams fromEndingBefore(String endingBefore) {
        PaginationParams params = new PaginationParams();
        params.endingBefore = endingBefore;
        return params;
    }

    /**
     * Creates an instance of {@link PaginationParams} to get next page items.
     * <p>
     * Keeps limit and order from original {@link PaginationParams}.
     *
     * @param pagination current page pagination params.
     * @return {@link PaginationParams} to get next items page.
     * @throws IllegalArgumentException when {@link PagedResponse.Pagination#getNextUri()}
     *                                  returns <code>null</code>, which means, the end of the items
     *                                  list is already reached.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public static PaginationParams nextPage(@NonNull PagedResponse.Pagination pagination) throws IllegalArgumentException {
        if (pagination.getNextUri() == null) {
            return null;
        }
        try {
            String startingAfterId = getQueryParameter(pagination.getNextUri(), STARTING_AFTER_KEY);
            return copyLimitAndOrder(fromStartingAfter(startingAfterId), pagination);
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed url", e);
        }
    }

    /**
     * Creates an instance of {@link PaginationParams} to get previous page items.
     * <p>
     * Keeps limit and order from original {@link PaginationParams}.
     *
     * @param pagination current page pagination params.
     * @return {@link PaginationParams} to get previous items page.
     * @throws IllegalArgumentException when {@link PagedResponse.Pagination#getPreviousUri()}
     *                                  returns <code>null</code>, which means, the beginning of the
     *                                  items list is already reached.
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    public static PaginationParams previousPage(@NonNull PagedResponse.Pagination pagination) throws IllegalArgumentException {
        if (pagination.getPreviousUri() == null) {
            return null;
        }
        try {
            String endingBeforeId = getQueryParameter(pagination.getPreviousUri(), ENDING_BEFORE_KEY);
            return copyLimitAndOrder(fromEndingBefore(endingBeforeId), pagination);
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed url", e);
        }
    }

    private static String getQueryParameter(@NonNull String uri, String paramKey) {
        String value = Uri.parse(uri).getQueryParameter(paramKey);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("No value provided for a " + paramKey + " key");
        }
        return value;
    }

    private static PaginationParams copyLimitAndOrder(PaginationParams paginationParams, PagedResponse.Pagination pagination) {
        paginationParams.limit = pagination.getLimit();
        paginationParams.order = pagination.getOrder();
        return paginationParams;
    }

    private String startingAfter;
    private String endingBefore;
    private int limit;
    private PageOrder order;

    /**
     * Gets startingAfter.
     *
     * @return value of startingAfter.
     * @see #setStartingAfter(String)
     */
    public String getStartingAfter() {
        return startingAfter;
    }

    /**
     * Sets the cursor to use for pagination. It is an resource id of the previous item to
     * the response first item.
     *
     * @param startingAfter value.
     */
    public void setStartingAfter(String startingAfter) {
        this.startingAfter = startingAfter;
    }

    /**
     * Gets endingBefore.
     *
     * @return value of endingBefore.
     * @see #setEndingBefore(String)
     */
    public String getEndingBefore() {
        return endingBefore;
    }

    /**
     * Sets the cursor to use for pagination. It is an resource id of the next item to
     * the response last item.
     *
     * @param endingBefore value.
     */
    public void setEndingBefore(String endingBefore) {
        this.endingBefore = endingBefore;
    }

    /**
     * Gets limit.
     *
     * @return value of limit.
     * @see #setLimit(int)
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets number of items per page. Accepted values: 0 - 100 (default is 25).
     *
     * @param limit value.
     * @see #limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Gets order.
     *
     * @return value of order.
     * @see #setOrder(PageOrder)
     */
    public PageOrder getOrder() {
        return order;
    }

    /**
     * Sets result order. Default value is {@link PageOrder#DESC}.
     * <p/>
     * This parameter also affects {@link #startingAfter} end {@link #endingBefore} parameters.
     * If order is set to {@link PageOrder#ASC}, {@link #startingAfter} would be an id of item that
     * is after {@link #endingBefore} of original {@link PageOrder#DESC} order.
     *
     * @param order value.
     */
    public void setOrder(PageOrder order) {
        this.order = order;
    }

    public Map<String, String> toQueryMap() {
        Map<String, String> queryMap = new HashMap<>();
        if (limit != 0) {
            queryMap.put(LIMIT_KEY, String.valueOf(limit));
        }
        if (order != null) {
            queryMap.put(ORDER_KEY, order.getValue());
        }
        if (startingAfter != null) {
            queryMap.put(STARTING_AFTER_KEY, startingAfter);
        }
        if (endingBefore != null) {
            queryMap.put(ENDING_BEFORE_KEY, endingBefore);
        }
        return queryMap;
    }
}
