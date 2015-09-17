package com.coinbase.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.output.NullOutputStream;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.AccountChange;
import com.coinbase.api.entity.AccountChangesResponse;
import com.coinbase.api.entity.AccountsResponse;
import com.coinbase.api.entity.Address;
import com.coinbase.api.entity.AddressResponse;
import com.coinbase.api.entity.AddressesResponse;
import com.coinbase.api.entity.Application;
import com.coinbase.api.entity.Button;
import com.coinbase.api.entity.Contact;
import com.coinbase.api.entity.ContactsResponse;
import com.coinbase.api.entity.HistoricalPrice;
import com.coinbase.api.entity.Merchant;
import com.coinbase.api.entity.Order;
import com.coinbase.api.entity.OrdersResponse;
import com.coinbase.api.entity.PaymentMethod;
import com.coinbase.api.entity.PaymentMethodsResponse;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.entity.RecurringPayment;
import com.coinbase.api.entity.RecurringPaymentsResponse;
import com.coinbase.api.entity.Report;
import com.coinbase.api.entity.Token;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.TransactionsResponse;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.TransfersResponse;
import com.coinbase.api.entity.User;
import com.coinbase.api.exception.CoinbaseException;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest(CoinbaseImpl.class)
public class CoinbaseTest {

    private HttpsURLConnection mockConnection;
    private URL mockURL;
    private Coinbase cb;

    @Before
    public void setUp() throws Exception {
        mockConnection = mock(HttpsURLConnection.class);
        mockURL = mock(URL.class);

        whenNew(URL.class).withAnyArguments().thenReturn(mockURL);
        when(mockURL.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getOutputStream()).thenReturn(new NullOutputStream());

        cb = new CoinbaseBuilder().build();
    }

    @Test(expected = CoinbaseException.class)
    public void errorResponse() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/error.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        cb.createAccount(new Account());
    }

    @Test(expected = CoinbaseException.class)
    public void errorsResponse() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/errors.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        cb.createAccount(new Account());
    }

    @Test(expected = CoinbaseException.class)
    public void falseSuccessResponse() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/false_success.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        cb.createAccount(new Account());
    }

    @Test
    public void accounts() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/accounts.json");

        when(mockConnection.getInputStream()).thenReturn(in);

        AccountsResponse r = cb.getAccounts();
        List<Account> accounts = r.getAccounts();
        assertEquals(2, accounts.size());

        Account a1 = accounts.get(0);
        assertEquals("536a541fa9393bb3c7000023", a1.getId());
        assertEquals("My Wallet", a1.getName());
        assertEquals(Money.parse("BTC 50"), a1.getBalance());
        assertEquals(Money.parse("USD 500.12"), a1.getNativeBalance());
        assertTrue(DateTime.parse("2014-05-07T08:41:19-07:00").isEqual(a1.getCreatedAt()));
        assertTrue(a1.isPrimary());
        assertTrue(a1.isActive());

        Account a2 = accounts.get(1);
        assertEquals("536a541fa9393bb3c7000034", a2.getId());
        assertEquals("Savings", a2.getName());
        assertEquals(Money.parse("BTC 0"), a2.getBalance());
        assertEquals(Money.parse("USD 0"), a2.getNativeBalance());
        assertTrue(DateTime.parse("2014-05-07T08:50:10-07:00").isEqual(a2.getCreatedAt()));
        assertFalse(a2.isPrimary());
        assertTrue(a2.isActive());
    }

    @Test
    public void users() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/users.json");

        when(mockConnection.getInputStream()).thenReturn(in);

        User user = cb.getUser();

        assertEquals("512db383f8182bd24d000001", user.getId());
        assertEquals("User One", user.getName());
        assertEquals("user1@example.com", user.getEmail());
        assertEquals("Pacific Time (US & Canada)", user.getTimeZone());
        assertEquals(CurrencyUnit.USD, user.getNativeCurrency());
        assertEquals(Money.parse("BTC 49.76"), user.getBalance());
        assertEquals(1, user.getBuyLevel().intValue());
        assertEquals(1, user.getSellLevel().intValue());
        assertEquals(Money.parse("BTC 10"), user.getBuyLimit());
        assertEquals(Money.parse("BTC 100"), user.getSellLimit());

        Merchant merchant = user.getMerchant();
        assertEquals("test company name", merchant.getCompanyName());
        assertEquals("http://localhost/logo.jpeg", merchant.getLogo().getSmall());
    }

    @Test
    public void transaction() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/transaction.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Transaction t = cb.getTransaction("5018f833f8182b129c00002f");

        assertEquals("5018f833f8182b129c00002f", t.getId());
        assertTrue(DateTime.parse("2012-08-01T02:34:43-07:00").isEqual(t.getCreatedAt()));
        assertEquals(Money.parse("BTC -1.1"), t.getAmount());
        assertTrue(t.isRequest());
        assertEquals(Transaction.Status.PENDING, t.getStatus());

        User sender = t.getSender();

        assertEquals("5011f33df8182b142400000e", sender.getId());
        assertEquals("User Two", sender.getName());
        assertEquals("user2@example.com", sender.getEmail());

        User recipient = t.getRecipient();

        assertEquals("5011f33df8182b142400000a", recipient.getId());
        assertEquals("User One", recipient.getName());
        assertEquals("user1@example.com", recipient.getEmail());
    }

    @Test
    public void transactions() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/transactions.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        TransactionsResponse r = cb.getTransactions();

        User current_user = r.getCurrentUser();
        assertEquals("5011f33df8182b142400000e", current_user.getId());
        assertEquals("User Two", current_user.getName());
        assertEquals("user2@example.com", current_user.getEmail());

        assertEquals(Money.parse("BTC 50"), r.getBalance());
        assertEquals(Money.parse("USD 500"), r.getNativeBalance());
        assertEquals(2, r.getTotalCount());
        assertEquals(1, r.getNumPages());
        assertEquals(1, r.getCurrentPage());

        List<Transaction> txs = r.getTransactions();
        assertEquals(2, txs.size());

        Transaction tx1 = txs.get(0);
        assertEquals("5018f833f8182b129c00002f", tx1.getId());

        Transaction tx2 = txs.get(1);
        assertEquals("5018f833f8182b129c00002e", tx2.getId());
    }

    @Test
    public void transfers() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/transfers.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        TransfersResponse r = cb.getTransfers();

        assertEquals(1, r.getTotalCount());
        assertEquals(1, r.getNumPages());
        assertEquals(1, r.getCurrentPage());
        List<Transfer> transfers = r.getTransfers();
        assertEquals(1, transfers.size());
        Transfer t = transfers.get(0);
        assertEquals(Transfer.Type.BUY, t.getType());
        assertEquals("QPCUCZHR", t.getCode());
        assertTrue(DateTime.parse("2013-02-27T23:28:18-08:00").isEqual(t.getCreatedAt()));
        assertEquals(Money.parse("USD 0.14"), t.getFees().get("coinbase"));
        assertEquals(Money.parse("USD 0.15"), t.getFees().get("bank"));
        assertTrue(DateTime.parse("2013-03-05T18:00:00-08:00").isEqual(t.getPayoutDate()));
        assertEquals("5011f33df8182b142400000e", t.getTransactionId());
        assertEquals(Transfer.Status.PENDING, t.getStatus());
        assertEquals(Money.parse("BTC 1"), t.getBtc());
        assertEquals(Money.parse("USD 13.55"), t.getSubtotal());
        assertEquals(Money.parse("USD 13.84"), t.getTotal());
        assertEquals("Paid for with $13.84 from Test xxxxx3111.", t.getDescription());
    }

    @Test
    public void quote() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/quote.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Quote q = cb.getBuyQuote(Money.parse("BTC 1"));

        assertEquals(Money.parse("BTC 1"), q.getBtc());
        assertEquals(Money.parse("USD 10.10"), q.getSubtotal());
        assertEquals(2, q.getFees().size());
        assertEquals(Money.parse("USD 0.10"), q.getFees().get("coinbase"));
        assertEquals(Money.parse("USD 0.15"), q.getFees().get("bank"));
        assertEquals(Money.parse("USD 10.35"), q.getTotal());
    }

    @Test
    public void addresses() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/addresses.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        AddressesResponse r = cb.getAddresses();

        assertNull(r.isSuccess());

        List<Address> addresses = r.getAddresses();
        assertEquals(2, addresses.size());

        Address a1 = addresses.get(0);
        assertEquals("moLxGrqWNcnGq4A8Caq8EGP4n9GUGWanj4", a1.getAddress());
        assertNull(a1.getCallbackUrl());
        assertNull(a1.getLabel());
        assertTrue(DateTime.parse("2013-05-09T23:07:08-07:00").isEqual(a1.getCreatedAt()));

        Address a2 = addresses.get(1);
        assertEquals("mwigfecvyG4MZjb6R5jMbmNcs7TkzhUaCj", a2.getAddress());
        assertEquals("http://localhost/callback", a2.getCallbackUrl());
        assertEquals("My Label", a2.getLabel());
        assertTrue(DateTime.parse("2013-05-09T17:50:37-07:00").isEqual(a2.getCreatedAt()));
    }

    @Test
    public void button() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/button.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Button buttonParams = new Button();
        buttonParams.setPrice(Money.parse("USD 2"));
        Button button = cb.createButton(buttonParams);

        assertEquals(Money.parse("USD 1.23"), button.getPrice());
        assertEquals("http://www.example.com/my_custom_button_callback", button.getCallbackUrl());
        assertEquals("Order123", button.getCustom());
        assertEquals("Sample description", button.getDescription());
        assertEquals("test", button.getName());
        assertEquals("Pay With Bitcoin", button.getText());
        assertEquals(Button.Style.CUSTOM_LARGE, button.getStyle());
        assertEquals(Button.Type.BUY_NOW, button.getType());
        assertEquals("93865b9cae83706ae59220c013bc0afd", button.getCode());
    }

    @Test
    public void contacts() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/contacts.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        ContactsResponse r = cb.getContacts();

        Contact c1 = r.getContacts().get(0);
        Contact c2 = r.getContacts().get(1);

        assertEquals("user1@example.com", c1.getEmail());
        assertEquals("user2@example.com", c2.getEmail());
    }

    @Test
    public void sell() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/new_sell.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Transfer t = cb.sell(Money.parse("BTC 1.00"));

        assertEquals(Money.parse("USD 13.21"), t.getTotal());
        assertEquals(Money.parse("USD 13.50"), t.getSubtotal());
        assertEquals(Money.parse("BTC 1"), t.getBtc());
        assertEquals(Transfer.Type.SELL, t.getType());
        assertEquals(Transfer.Status.CREATED, t.getStatus());
        assertEquals(Money.parse("USD 0.14"), t.getFees().get("coinbase"));
        assertEquals(Money.parse("USD 0.15"), t.getFees().get("bank"));
        assertEquals("RD2OC8AL", t.getCode());
    }

    @Test
    public void buy() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/new_buy.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Transfer t = cb.buy(Money.parse("BTC 1.00"));

        assertEquals(Money.parse("USD 13.84"), t.getTotal());
        assertEquals(Money.parse("USD 13.55"), t.getSubtotal());
        assertEquals(Money.parse("BTC 1"), t.getBtc());
        assertEquals(Transfer.Type.BUY, t.getType());
        assertEquals(Transfer.Status.CREATED, t.getStatus());
        assertEquals(Money.parse("USD 0.14"), t.getFees().get("coinbase"));
        assertEquals(Money.parse("USD 0.15"), t.getFees().get("bank"));
        assertEquals("6H7GYLXZ", t.getCode());
    }

    @Test
    public void orders() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/orders.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        OrdersResponse r = cb.getOrders();
        Order order = r.getOrders().get(0);
        Transaction t = order.getTransaction();
        Button b = order.getButton();

        assertEquals("513eb768f12a9cf27400000b", t.getId());
        assertEquals("4cc5eec20cd692f3cdb7fc264a0e1d78b9a7e3d7b862dec1e39cf7e37ababc14", t.getHash());
        assertEquals(Integer.valueOf(1), t.getConfirmations());

        assertEquals(Button.Type.BUY_NOW, b.getType());
        assertEquals("Order #1234", b.getName());
        assertEquals("order description", b.getDescription());
        assertEquals("eec6d08e9e215195a471eae432a49fc7", b.getId());

        assertEquals("mgrmKftH5CeuFBU3THLWuTNKaZoCGJU5jQ", order.getReceiveAddress());
        assertEquals("custom_123", order.getCustom());
        assertEquals(Money.parse("USD 30"), order.getTotalNative());
        assertEquals(Money.parse("BTC 1"), order.getTotalBtc());
        assertEquals(Order.Status.COMPLETED, order.getStatus());
        assertTrue(DateTime.parse("2013-03-11T22:04:37-07:00").isEqual(order.getCreatedAt()));
        assertEquals("A7C52JQT", order.getId());
    }

    @Test
    public void createOrder() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/new_order.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Button buttonParams = new Button();
        buttonParams.setPrice(Money.parse("USD 2"));
        Order order = cb.createOrder(buttonParams);

        Button button = order.getButton();

        assertEquals(Money.parse("USD 1.23"), order.getTotalNative());
        assertEquals(Money.parse("BTC 0.123"), order.getTotalBtc());
        assertEquals(Button.Type.BUY_NOW, button.getType());
        assertEquals("1741b3be1eb5dc50625c48851a94ae13", button.getId());
    }

    @Test
    public void getPaymentMethods() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/payment_methods.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        PaymentMethodsResponse r = cb.getPaymentMethods();
        assertEquals("530eb5b217cb34e07a000011", r.getDefaultBuy());
        assertEquals("530eb5b217cb34e07a000011", r.getDefaultSell());

        PaymentMethod p1 = r.getPaymentMethods().get(0);
        PaymentMethod p2 = r.getPaymentMethods().get(1);

        assertEquals("530eb5b217cb34e07a000011", p1.getId());
        assertEquals("US Bank ****4567", p1.getName());
        assertTrue(p1.canBuy());
        assertTrue(p1.canSell());

        assertFalse(p2.canBuy());
        assertFalse(p2.canSell());
    }

    @Test
    public void getSubscribers() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/subscribers.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        RecurringPaymentsResponse r = cb.getSubscribers();

        RecurringPayment p1 = r.getRecurringPayments().get(0);
        RecurringPayment p2 = r.getRecurringPayments().get(1);

        assertEquals("51a7cf58f8182b4b220000d5", p1.getId());
        assertTrue(DateTime.parse("2013-05-30T15:14:48-07:00").isEqual(p1.getCreatedAt()));
        assertEquals(RecurringPayment.Status.ACTIVE, p1.getStatus());
        assertEquals("user123", p1.getCustom());

        Button b1 = p1.getButton();
        assertEquals(Button.Type.SUBSCRIPTION, b1.getType());
        assertEquals("Test", b1.getName());
        assertEquals("", b1.getDescription());
        assertEquals("1b7a1019f371402ec02af389d1b24e55", b1.getId());

        assertEquals("51a7be2ff8182b4b220000a5", p2.getId());
    }

    @Test
    public void getRecurringPayments() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/recurring_payments.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        RecurringPaymentsResponse r = cb.getRecurringPayments();

        RecurringPayment p1 = r.getRecurringPayments().get(0);
        RecurringPayment p2 = r.getRecurringPayments().get(1);

        assertEquals("51a7b9e9f8182b4b22000013", p1.getId());
        assertEquals(RecurringPayment.INDEFINITE, p1.getTimes());
        assertEquals(RecurringPayment.StartType.NOW, p1.getStartType());
        assertEquals(Button.Repeat.MONTHLY, p1.getRepeat());
        assertEquals(Money.parse("BTC 0.02"), p1.getAmount());


        assertTrue(DateTime.parse("2013-05-15T00:22:57-07:00").isEqual(p2.getLastRun()));
        assertTrue(DateTime.parse("2013-05-16T00:22:57-07:00").isEqual(p2.getNextRun()));
    }

    @Test
    public void getExchangeRates() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/rates_response.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Map<String, BigDecimal> rates = cb.getExchangeRates();

        assertEquals(new BigDecimal("0.000076"), rates.get("czk_to_btc"));
        assertEquals(new BigDecimal("22.98199"), rates.get("usd_to_uyu"));
    }

    @Test
    public void getSupportedCurrencies() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/currencies_response.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        List<CurrencyUnit> currencies = cb.getSupportedCurrencies();

        assertTrue(currencies.contains(CurrencyUnit.CAD));
    }

    @Test
    public void createToken() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/token_response.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Token token = cb.createToken();

        assertEquals("abc12e821cf6e128afc2e821cf68e12cf68e168e128af21cf682e821cf68e1fe", token.getTokenId());
        assertEquals("n3NzN74qGYHSHPhKM1hdts3bF1zV4N1Aa3", token.getAddress());
    }

    @Test
    public void generateReceiveAddress() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/address_response.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        AddressResponse address = cb.generateReceiveAddress();

        assertEquals("muVu2JZo8PbewBHRp6bpqFvVD87qvqEHWA", address.getAddress());
        assertEquals("http://www.example.com/callback", address.getCallbackUrl());
        assertEquals("Dalmation donations", address.getLabel());
    }

    @Test
    public void createApplication() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/application_response.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Application app = cb.createApplication(new Application());

        assertEquals("5302ebdb137f73dcf7000047", app.getId());
        assertTrue(DateTime.parse("2014-02-17T21:12:59-08:00").isEqual(app.getCreatedAt()));
        assertEquals("Test App 3", app.getName());
        assertEquals("http://example.com", app.getRedirectUri());
        assertEquals("ee0ed3e5092e75e2b66afed97ecb54b8408b5e1b153f9841ce3f9c555f45db74", app.getClientId());
        assertEquals("8c9217790a1fc001a37d09aa2d28e218868242390670f41440822dbb1173fe58", app.getClientSecret());
        assertEquals(Integer.valueOf(0), app.getNumUsers());
    }

    @Test
    public void getHistoricalPrices() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/historical_prices.csv");
        when(mockConnection.getInputStream()).thenReturn(in);

        List<HistoricalPrice> prices = cb.getHistoricalPrices();

        assertEquals(DateTime.parse("2014-01-06T00:25:24-08:00"), prices.get(0).getTime());
        assertEquals(Money.parse("USD 10"), prices.get(0).getSpotPrice());
    }

    @Test
    public void createReport() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/report.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        Report reportParams = new Report();
        reportParams.setType(Report.Type.TRANSFERS);
        reportParams.setTimeRange(Report.TimeRange.CUSTOM);
        reportParams.setTimeRangeStart(DateTime.parse("2014-01-06T00:00:00Z"));
        assertEquals(reportParams.getTimeRangeStart(), "01/06/2014");
        reportParams.setTimeRangeEnd(DateTime.parse("2014-01-07T00:00:00Z"));
        assertEquals(reportParams.getTimeRangeEnd(), "01/07/2014");
        reportParams.setStartType(Report.StartType.ON);
        reportParams.setNextRun(DateTime.parse("2014-02-06T12:00:00Z"));
        assertEquals(reportParams.getNextRunDate(), "02/06/2014");
        assertEquals(reportParams.getNextRunTime(), "12:00");

        Report report = cb.createReport(reportParams);

        assertEquals("534711dd137f730e1c0000c6", report.getId());
        assertEquals(Report.Type.TRANSFERS, report.getType());
        assertEquals(Report.Status.ACTIVE, report.getStatus());
        assertEquals("dummy@example.com", report.getEmail());
        assertEquals(Report.Repeat.NEVER, report.getRepeat());
        assertEquals(Report.TimeRange.CUSTOM, report.getTimeRange());
        assertEquals(Report.INFINITE, report.getTimes());
        assertEquals(Integer.valueOf(0), report.getTimesRun());
        assertTrue(DateTime.parse("2014-04-10T14:00:00-07:00").isEqual(report.getNextRun()));
        assertTrue(DateTime.parse("2014-04-10T14:49:17-07:00").isEqual(report.getCreatedAt()));
    }

    @Test
    public void getAccountChanges() throws Exception {
        final InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/entity/account_changes_response.json");
        when(mockConnection.getInputStream()).thenReturn(in);

        AccountChangesResponse response = cb.getAccountChanges();
        User user = response.getCurrentUser();
        AccountChange transaction = response.getAccountChanges().get(0);

        assertEquals(Money.parse("BTC 50"), response.getBalance());
        assertEquals(Money.parse("USD 500"), response.getNativeBalance());

        assertEquals("524a75a3f8182b7d2a00000a", user.getId());
        assertEquals("user2@example.com", user.getEmail());
        assertEquals("User 2", user.getName());

        assertEquals("524a75a3f8182b7d2a000018", transaction.getId());
        assertEquals("524a75a3f8182b7d2a000010", transaction.getTransactionId());
        assertTrue(DateTime.parse("2013-10-01T00:11:31-07:00").isEqual(transaction.getCreatedAt()));
        assertFalse(transaction.isConfirmed().booleanValue());
        assertEquals(Money.parse("BTC 50"), transaction.getAmount());

        AccountChange.Cache cache = transaction.getCache();
        assertFalse(cache.isNotesPresent().booleanValue());
        assertEquals(AccountChange.Cache.Category.TRANSACTION, cache.getCategory());
        assertEquals("an external account", cache.getOtherUser().getName());

        transaction = response.getAccountChanges().get(1);

        assertEquals("546d3189543d0664da000016", transaction.getId());
        assertEquals("546d3189543d0664da000013", transaction.getTransactionId());
        assertTrue(DateTime.parse("2014-11-19T16:10:49-08:00").isEqual(transaction.getCreatedAt()));
        assertTrue(transaction.isConfirmed().booleanValue());
        assertEquals(Money.parse("BTC -0.005"), transaction.getAmount());

        cache = transaction.getCache();
        assertTrue(cache.isNotesPresent().booleanValue());
        assertEquals(AccountChange.Cache.Category.TRANSFER, cache.getCategory());
        assertEquals("EUR Wallet", cache.getPaymentMethod().getName());
        assertEquals("54630e62f1096f083e000002", cache.getPaymentMethod().getAccountId());

    }

    @Test
    public void verifyCallback() throws Exception {
        final String signature = "6yQRl17CNj5YSHSpF+tLjb0vVsNVEv021Tyy1bTVEQ69SWlmhwmJYuMc7jiDyeW9TLy4vRqSh4g4YEyN8eoQIM57pMoNw6Lw6Oudubqwp+E3cKtLFxW0l18db3Z/vhxn5BScAutHWwT/XrmkCNaHyCsvOOGMekwrNO7mxX9QIx21FBaEejJeviSYrF8bG6MbmFEs2VGKSybf9YrElR8BxxNe/uNfCXN3P5tO8MgR5wlL3Kr4yq8e6i4WWJgD08IVTnrSnoZR6v8JkPA+fn7I0M6cy0Xzw3BRMJAvdQB97wkobu97gFqJFKsOH2u/JR1S/UNP26vL0mzuAVuKAUwlRn0SUhWEAgcM3X0UCtWLYfCIb5QqrSHwlp7lwOkVnFt329Mrpjy+jAfYYSRqzIsw4ZsRRVauy/v3CvmjPI9sUKiJ5l1FSgkpK2lkjhFgKB3WaYZWy9ZfIAI9bDyG8vSTT7IDurlUhyTweDqVNlYUsO6jaUa4KmSpg1o9eIeHxm0XBQ2c0Lv/T39KNc/VOAi1LBfPiQYMXD1e/8VuPPBTDGgzOMD3i334ppSr36+8YtApAn3D36Hr9jqAfFrugM7uPecjCGuleWsHFyNnJErT0/amIt24Nh1GoiESEq42o7Co4wZieKZ+/yeAlIUErJzK41ACVGmTnGoDUwEBXxADOdA=";
        final String body = "{\"order\":{\"id\":null,\"created_at\":null,\"status\":\"completed\",\"event\":null,\"total_btc\":{\"cents\":100000000,\"currency_iso\":\"BTC\"},\"total_native\":{\"cents\":1000,\"currency_iso\":\"USD\"},\"total_payout\":{\"cents\":1000,\"currency_iso\":\"USD\"},\"custom\":\"123456789\",\"receive_address\":\"mzVoQenSY6RTBgBUcpSBTBAvUMNgGWxgJn\",\"button\":{\"type\":\"buy_now\",\"name\":\"Test Item\",\"description\":null,\"id\":null},\"transaction\":{\"id\":\"53bdfe4d091c0d74a7000003\",\"hash\":\"4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b\",\"confirmations\":0}}}";
        assertTrue(cb.verifyCallback(body, signature));
    }

    @Test
    public void verifyCallbackFailure() throws Exception {
        final String signature = "6yQRl17CNj5YSHSpF+tLjb0vVsNVEv021Tyy1bTVEQ69SWlmhwmJYuMc7jiDyeW9TLy4vRqSh4g4YEyN8eoQIM57pMoNw6Lw6Oudubqwp+E3cKtLFxW0l18db3Z/vhxn5BScAutHWwT/XrmkCNaHyCsvOOGMekwrNO7mxX9QIx21FBaEejJeviSYrF8bG6MbmFEs2VGKSybf9YrElR8BxxNe/uNfCXN3P5tO8MgR5wlL3Kr4yq8e6i4WWJgD08IVTnrSnoZR6v8JkPA+fn7I0M6cy0Xzw3BRMJAvdQB97wkobu97gFqJFKsOH2u/JR1S/UNP26vL0mzuAVuKAUwlRn0SUhWEAgcM3X0UCtWLYfCIb5QqrSHwlp7lwOkVnFt329Mrpjy+jAfYYSRqzIsw4ZsRRVauy/v3CvmjPI9sUKiJ5l1FSgkpK2lkjhFgKB3WaYZWy9ZfIAI9bDyG8vSTT7IDurlUhyTweDqVNlYUsO6jaUa4KmSpg1o9eIeHxm0XBQ2c0Lv/T39KNc/VOAi1LBfPiQYMXD1e/8VuPPBTDGgzOMD3i334ppSr36+8YtApAn3D36Hr9jqAfFrugM7uPecjCGuleWsHFyNnJErT0/amIt24Nh1GoiESEq42o7Co4wZieKZ+/yeAlIUErJzK41ACVGmTnGoDUwEBXxADOdA=";
        final String body = "{\"order\":{\"id\":null,\"created_at\":null,\"status\":\"completed\",\"event\":null,\"total_btc\":{\"cents\":1000000000,\"currency_iso\":\"BTC\"},\"total_native\":{\"cents\":1000,\"currency_iso\":\"USD\"},\"total_payout\":{\"cents\":1000,\"currency_iso\":\"USD\"},\"custom\":\"123456789\",\"receive_address\":\"mzVoQenSY6RTBgBUcpSBTBAvUMNgGWxgJn\",\"button\":{\"type\":\"buy_now\",\"name\":\"Test Item\",\"description\":null,\"id\":null},\"transaction\":{\"id\":\"53bdfe4d091c0d74a7000003\",\"hash\":\"4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b\",\"confirmations\":0}}}";
        assertFalse(cb.verifyCallback(body, signature));
    }
}
