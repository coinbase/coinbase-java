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

import androidx.annotation.VisibleForTesting;

import java.util.List;

/**
 * Represents a {@link CoinbaseResponse} with pagination.
 * <b/>
 * All GET endpoints which return an object list support cursor based pagination with pagination
 * information inside a {@link Pagination} object.
 *
 * @see Pagination
 * @see <a href="https://developers.coinbase.com/api/v2#pagination">
 * https://developers.coinbase.com/api/v2#pagination</a>
 */
public class PagedResponse<T> extends CoinbaseResponse<List<T>> {

    private Pagination pagination;

    /**
     * Gets current page items pagination parameters.
     *
     * @return value of pagination.
     * @see #pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Object that contains current items page pagination data.
     *
     * @see <a href="https://developers.coinbase.com/api/v2#pagination">
     * https://developers.coinbase.com/api/v2#pagination</a>
     */
    public static class Pagination {

        private String endingBefore;
        private String startingAfter;
        private Integer limit;
        private PageOrder order;
        private String previousUri;
        private String nextUri;

        /**
         * Gets the cursor used in pagination for current request. It is an resource id of the next
         * item to the current response last item.
         * <p>
         * <code>null</code> if parameter was not provided.
         *
         * @return value of endingBefore.
         */
        public String getEndingBefore() {
            return endingBefore;
        }

        /**
         * Gets the cursor used in pagination for current request. It is an resource id of the
         * previous item to the current response first item.
         * <p>
         * <code>null</code> if parameter was not provided.
         *
         * @return value of startingAfter.
         */
        public String getStartingAfter() {
            return startingAfter;
        }

        /**
         * Gets number of items per page.
         *
         * @return value of limit.
         */
        public Integer getLimit() {
            return limit;
        }

        /**
         * Gets page items order.
         *
         * @return value of order.
         */
        public PageOrder getOrder() {
            return order;
        }

        /**
         * Gets previousUri.
         * <p>
         * API will construct the previous page URI together with all the currently used pagination
         * parameters.
         * <p>
         * You know that you have paginated all the results when the response’s
         * {@link #previousUri} is empty (<code>null</code>).
         *
         * @return value of previousUri.
         */
        public String getPreviousUri() {
            return previousUri;
        }

        /**
         * Gets nextUri.
         * <p>
         * API will construct the next page URI together with all the currently used pagination
         * parameters.
         * <p>
         * You know that you have paginated all the results when the response’s {@link #nextUri} is
         * empty (<code>null</code>).
         *
         * @return value of nextUri.
         */
        public String getNextUri() {
            return nextUri;
        }

        /**
         * Creates {@link PaginationParams} to add to request to get the next page of items.
         *
         * @return new instance of {@link PaginationParams} for the next page request.
         * @see PaginationParams
         */
        public PaginationParams nextPage() {
            return PaginationParams.nextPage(this);
        }

        /**
         * Creates {@link PaginationParams} to add to request to get the previous page of items.
         *
         * @return new instance of {@link PaginationParams} for the previous page request.
         * @see PaginationParams
         */
        public PaginationParams previousPage() {
            return PaginationParams.previousPage(this);
        }

        @VisibleForTesting
        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        @VisibleForTesting
        public void setOrder(PageOrder order) {
            this.order = order;
        }

        @VisibleForTesting
        public void setPreviousUri(String previousUri) {
            this.previousUri = previousUri;
        }

        @VisibleForTesting
        public void setNextUri(String nextUri) {
            this.nextUri = nextUri;
        }
    }
}
