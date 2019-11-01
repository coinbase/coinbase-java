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

package com.coinbase.resources.prices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coinbase.CoinbaseResponse;
import com.coinbase.network.ApiCall;
import com.coinbase.resources.buys.BuysResource;
import com.coinbase.resources.sells.SellsResource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.Single;

/**
 * Represents Prices resource and get prices for specified currencies.
 * This prices include standard Coinbase fee (1%) but excludes any other fees including bank fees.
 * If you need more accurate price estimate for a specific payment method or amount, see buy/sell resources.
 *
 * @see BuysResource
 * @see SellsResource
 * @see <a href="https://developers.coinbase.com/api/v2#prices">online docs: Prices</a> for more info.
 */
public class PricesResource {

    private final PricesApi pricesApi;
    private final PricesApiRx pricesApiRx;

    private final SimpleDateFormat dayDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public PricesResource(PricesApi pricesApi, PricesApiRx pricesApiRx) {
        this.pricesApi = pricesApi;
        this.pricesApiRx = pricesApiRx;
    }

    // region Call methods.

    /**
     * <p>
     * Get the total price to buy one unit of crypto currency.
     * </p>
     * <p>
     * Note that exchange rates fluctuates so the price is only correct for seconds at the time.
     * This buy price includes standard Coinbase fee (1%) but excludes any other fees including bank fees.
     * If you need more accurate price estimate for a specific payment method or amount,
     * see {@link BuysResource#placeBuyOrder(String, com.coinbase.resources.trades.TransferOrderBody,
     * com.coinbase.resources.trades.Trade.ExpandField...) buy endpoint} and {@code quote: true} option.
     * </p>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @return {@link ApiCall call} to get {@link Price price} of the crypto currency in fiat currency.
     * @see BuysResource
     * @see <a href="https://developers.coinbase.com/api/v2#get-buy-price">online docs: Buy Price</a>.
     */
    public ApiCall<CoinbaseResponse<Price>> getBuyPrice(@NonNull String baseCurrency,
                                                        @NonNull String fiatCurrency) {
        return pricesApi.getBuyPrice(baseCurrency, fiatCurrency);
    }

    /**
     * <p>
     * Get the total price to sell one unit of crypto currency.
     * </p>
     * <p>
     * Note that exchange rates fluctuates so the price is only correct for seconds at the time.
     * This sell price includes standard Coinbase fee (1%) but excludes any other fees including bank fees.
     * If you need more accurate price estimate for a specific payment method or amount,
     * see {@link SellsResource#placeSellOrder(String, com.coinbase.resources.trades.TransferOrderBody,
     * com.coinbase.resources.trades.Trade.ExpandField...) sell endpoint} and {@code quote: true} option.
     * </p>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @return {@link ApiCall call} to get {@link Price price} of the crypto currency in fiat currency.
     * @see SellsResource
     * @see <a href="https://developers.coinbase.com/api/v2#get-sell-price">online docs: Sell Price</a>.
     */
    public ApiCall<CoinbaseResponse<Price>> getSellPrice(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency) {
        return pricesApi.getSellPrice(baseCurrency, fiatCurrency);
    }

    /**
     * <p>
     * Get the current market price of crypto currency. This is usually somewhere in between the buy and sell price.
     * </p>
     * <p>
     * Note that exchange rates fluctuates so the price is only correct for seconds at the time.
     * </p>
     * <p>You can also get historic prices with {@code date} parameter.</p>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @param date         (optional) the date (in the past 🙂) for which to get historic price for.
     *                     Setting this parameter to {@code null} is same as sending current date.
     * @return {@link ApiCall call} to get {@link Price price} of the crypto currency in fiat currency.
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public ApiCall<CoinbaseResponse<Price>> getSpotPrice(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency,
                                                         @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApi.getSpotPrice(baseCurrency, fiatCurrency, dateParam);
    }

    /**
     * Same as {@link #getSpotPrice(String, String, Date) getSpotPrice(baseCurrency, fiatCurrency, null)}.
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @return {@link ApiCall call} to get {@link Price price} of the crypto currency in fiat currency.
     * @see #getSpotPrice(String, String, Date)
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public ApiCall<CoinbaseResponse<Price>> getSpotPrice(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency) {
        return pricesApi.getSpotPrice(baseCurrency, fiatCurrency, null);
    }

    /**
     * <p>
     * Get the current market price of all supported crypto currencies. This is usually somewhere in between the buy and sell price.
     * </p>
     * <p>
     * Note that exchange rates fluctuates so the price is only correct for seconds at the time.
     * </p>
     * <p>
     * You can also get historic prices with {@code date} parameter.
     * </p>
     * <b>This endpoint does not require authentication.</b>
     * <p>
     * SCOPES:<br/>
     * <ul>
     * <li>No permission required</li>
     * </ul>
     *
     * @param fiatCurrency fiat currency.
     * @param date         (optional) the date (in the past 🙂) for which to get historic price for.
     *                     Setting this parameter to {@code null} is same as sending current date.
     * @return {@link ApiCall call} to get list of {@link Price prices} of the supported crypto currencies in fiat currency.
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public ApiCall<CoinbaseResponse<List<Price>>> getSpotPrices(@NonNull String fiatCurrency,
                                                                @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApi.getSpotPrices(fiatCurrency, dateParam);
    }

    /**
     * Same as {@link #getSpotPrices(String, Date) getSpotPrices(fiatCurrency, null}.
     *
     * @param fiatCurrency fiat currency.
     * @return {@link ApiCall call} to get list of {@link Price prices} of the supported crypto currencies in fiat currency.
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public ApiCall<CoinbaseResponse<List<Price>>> getSpotPrices(@NonNull String fiatCurrency) {
        return pricesApi.getSpotPrices(fiatCurrency, null);
    }

    // endregion

    //region Rx methods

    /**
     * Rx version of {@link #getBuyPrice(String, String) getBuyPrice(baseCurrency, fiatCurrency)}.
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @return {@link Single rx.Single} for {@link Price price} of the crypto currency in fiat currency
     * @see BuysResource
     * @see <a href="https://developers.coinbase.com/api/v2#get-buy-price">online docs: Buy Price</a>.
     */
    public Single<CoinbaseResponse<Price>> getBuyPriceRx(@NonNull String baseCurrency,
                                                         @NonNull String fiatCurrency) {
        return pricesApiRx.getBuyPrice(baseCurrency, fiatCurrency);
    }

    /**
     * Rx version of {@link #getSellPrice(String, String) getSellPrice(baseCurrency, fiatCurrency)}.
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @return {@link Single rx.Single} to get {@link Price price} of the crypto currency in fiat currency.
     * @see SellsResource
     * @see <a href="https://developers.coinbase.com/api/v2#get-sell-price">online docs: Sell Price</a>.
     */
    public Single<CoinbaseResponse<Price>> getSellPriceRx(@NonNull String baseCurrency,
                                                          @NonNull String fiatCurrency) {
        return pricesApiRx.getSellPrice(baseCurrency, fiatCurrency);
    }

    /**
     * Rx version of {@link #getSpotPrice(String, String, Date) getSpotPrice(baseCurrency, fiatCurrency, date)}.
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @param date         (optional) the date (in the past 🙂) for which to get historic price for.
     * @return {@link Single rx.Single} to get {@link Price price} of the crypto currency in fiat currency.
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public Single<CoinbaseResponse<Price>> getSpotPriceRx(@NonNull String baseCurrency,
                                                          @NonNull String fiatCurrency,
                                                          @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApiRx.getSpotPrice(baseCurrency, fiatCurrency, dateParam);
    }

    /**
     * Same as {@link #getSpotPriceRx(String, String, Date) getSpotPriceRx(baseCurrency, fiatCurrency, null)}.
     *
     * @param baseCurrency crypto currency.
     * @param fiatCurrency fiat currency.
     * @return {@link Single rx.Single} to get {@link Price price} of the crypto currency in fiat currency.
     * @see #getSpotPrice(String, String, Date)
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public Single<CoinbaseResponse<Price>> getSpotPriceRx(@NonNull String baseCurrency,
                                                          @NonNull String fiatCurrency) {
        return pricesApiRx.getSpotPrice(baseCurrency, fiatCurrency, null);
    }

    /**
     * Rx version of {@link #getSpotPrices(String, Date) getSpotPrices(fiatCurrency, date)}.
     *
     * @param fiatCurrency fiat currency.
     * @param date         (optional) the date (in the past 🙂) for which to get historic price for.
     * @return {@link Single rx.Single} to get list of {@link Price prices} of the supported crypto currencies in fiat currency.
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public Single<CoinbaseResponse<List<Price>>> getSpotPricesRx(@NonNull String fiatCurrency,
                                                                 @Nullable Date date) {
        final String dateParam = date != null ? dayDateFormatter.format(date) : null;
        return pricesApiRx.getSpotPrices(fiatCurrency, dateParam);
    }

    /**
     * Same as {@link #getSpotPricesRx(String, Date) getSpotPricesRx(fiatCurrency, null}.
     *
     * @param fiatCurrency fiat currency.
     * @return {@link Single rx.Single} to get list of {@link Price prices} of the supported crypto currencies in fiat currency.
     * @see <a href="https://developers.coinbase.com/api/v2#get-spot-price">online docs: Spot Price</a>.
     */
    public Single<CoinbaseResponse<List<Price>>> getSpotPricesRx(@NonNull String fiatCurrency) {
        return pricesApiRx.getSpotPrices(fiatCurrency, null);
    }

    //endregion
}
