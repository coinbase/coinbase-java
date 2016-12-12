# coinbase-java

An easy way to buy, sell, send, and accept [bitcoin](http://en.wikipedia.org/wiki/Bitcoin) through the [Coinbase API](https://coinbase.com/docs/api/overview).

This library is a wrapper around the [Coinbase JSON API](https://coinbase.com/api/doc). It supports both the [api key + secret authentication method](https://coinbase.com/docs/api/authentication) as well as OAuth 2.0 for performing actions on other people's account.

## Installation

### Using Maven

Add the following dependency to your project's Maven pom.xml:

```xml
<dependency>
	<groupId>com.coinbase.api</groupId>
	<artifactId>coinbase-java</artifactId>
	<version>1.10.0</version>
</dependency>
```

The library will automatically be pulled from Maven Central.

### Manual

You can copy this library jar and all its dependency jars to a folder as follows:

```bash
git clone git@github.com:coinbase/coinbase-java.git
cd coinbase-java
mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=$YOUR_JAR_DIRECTORY
mvn package
cp target/coinbase-java-1.10.0.jar $YOUR_JAR_DIRECTORY
```

## Usage

### HMAC Authentication (for accessing your own account)

Start by [enabling an API Key on your account](https://coinbase.com/settings/api)

Next, build an instance of the client by passing your API Key + Secret to a CoinbaseBuilder object.

```java
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;

Coinbase cb = new CoinbaseBuilder()
                      .withApiKey(System.getenv("COINBASE_API_KEY"), System.getenv("COINBASE_API_SECRET"))
                      .build();
```

Notice here that we did not hard code the API keys into our codebase, but set them in environment variables instead. This is just one example, but keeping your credentials separate from your code base is a good [security practice](https://coinbase.com/docs/api/authentication#security).

### OAuth 2.0 Authentication (for accessing others' accounts)

Start by [creating a new OAuth 2.0 application](https://coinbase.com/oauth/applications)

```java
// Obtaining the OAuth token is outside the scope of this library
String token = "the_oauth_token"
Coinbase cb = new CoinbaseBuilder()
                      .withAccessToken(token)
                      .build();
```

Now you can call methods on `coinbase` similar to the ones described in the [api reference](https://coinbase.com/api/doc).  For example:

```java
cb.getUser().getEmail(); // user@example.com
```

[Joda Money objects](http://www.joda.org/joda-money/) are returned for most amounts dealing with currency.  You can call `getAmount`, `toString`, or perform math operations on money objects.

## Examples

### Send bitcoin

```java
Transaction t = new Transaction();
t.setTo("user2@example.com");
t.setAmount(Money.parse("BTC 0.10"));
Transaction r = cb.sendMoney(t);
```

You can also send money in [a number of currencies](https://github.com/coinbase/coinbase-ruby/blob/master/supported_currencies.json).  The amount will be automatically converted to the correct BTC amount using the current exchange rate.

```java
Transaction t = new Transaction();
t.setTo("user2@example.com");
t.setAmount(Money.parse("CAD 100"));
Transaction r = cb.sendMoney(t);
```

The `to` field can be a bitcoin address and notes can be attached to the money.  Notes are only visible on Coinbase (not on the general bitcoin network).

```java
Transaction t = new Transaction();
t.setTo("mpJKwdmJKYjiyfNo26eRp4j6qGwuUUnw9x");
t.setAmount(Money.parse("USD 2.23"));
t.setNotes("Thanks for the coffee!");
Transaction r = cb.sendMoney(t);
```

### Request bitcoin

This will send an email to the recipient, requesting payment, and give them an easy way to pay.

```java
Transaction t = new Transaction();
t.setFrom("customer@example.com");
t.setAmount(Money.parse("USD 100"));
t.setNotes("Invoice for window cleaning");

Transaction r = cb.requestMoney(t);

cb.resendRequest(r.getId());

cb.cancelRequest(r.getId());

// From the other side

cb.completeRequest(requestId);
```

### List your current transactions

Sorted in descending order by createdAt, 30 transactions per page

```java
TransactionsResponse r = cb.getTransactions();

r.getTotalCount(); // 45
r.getCurrentPage(); // 1
r.getNumPages(); // 2

List<Transaction> txs = r.getTransactions();

txs.get(0).getId();

TransactionsResponse page_2 = cb.getTransactions(2);
```

Transactions will always have an `id` attribute which is the primary way to identify them through the Coinbase api.  They will also have a `hsh` (bitcoin hash) attribute once they've been broadcast to the network (usually within a few seconds).

### Get transaction details

This will fetch the details/status of a transaction that was made within Coinbase

```java
Transaction t = cb.getTransaction("5011f33df8182b142400000e");
t.getStatus(); // Transaction.Status.PENDING
t.getRecipientAddress(); // "mpJKwdmJKYjiyfNo26eRp4j6qGwuUUnw9x"
```

### Get quotes for buying or selling Bitcoin on Coinbase

This price includes Coinbase's fee of 1% and the bank transfer fee of $0.15.

The price to buy or sell per Bitcoin will increase and decrease respectively as the quantity increases. This [slippage](http://en.wikipedia.org/wiki/Slippage_(finance)) is normal and is influenced by the [market depth](http://en.wikipedia.org/wiki/Market_depth) on the exchanges we use.

```java
Quote q = cb.getBuyQuote(Money.parse("BTC 2.2"));
Map<String, Money> fees = q.getFees();
q.getSubtotal();
q.getTotal();
```

```java
Quote q = cb.getSellQuote(Money.parse("BTC 2.2"));
Map<String, Money> fees = q.getFees();
q.getSubtotal();
q.getTotal();
```

Check the spot price of Bitcoin in a given currency. This is usually somewhere in between the buy and sell price, current to within a few minutes and does not include any Coinbase or bank transfer fees.

```java
Money spotPrice = cb.getSpotPrice(CurrencyUnit.EUR);
```

### Buy or Sell bitcoin

Buying and selling bitcoin requires you to [add a payment method](https://coinbase.com/buys) through the web app first.

Then you can call `buy` or `sell` and pass a `quantity` of bitcoin you want to buy.

```java
Transfer t = cb.buy(Money.parse("BTC 0.005"));
t.getCode(); // "6H7GYLXZ"
t.getTotal().toString(); // "USD 3"
t.getPayoutDate.toString(); // "2013-02-01T18:00:00-0800"
```


```java
Transfer t = cb.sell(Money.parse("BTC 0.005"));
t.getCode(); // "6H7GYLXZ"
t.getTotal().toString(); // "USD 2.99"
t.getPayoutDate.toString(); // "2013-02-01T18:00:00-0800"
```

### Listing Buy/Sell History

You can use `getTransfers` to view past buys and sells.

```java
TransfersResponse r = cb.getTransfers();

r.getTotalCount(); // 30
r.getCurrentPage(); // 1
r.getNumPages(); // 2

List<Transfer> transfers = r.getTransfers();

for (Transfer t : transfers) {
	System.out.println(t.getCode());
	// ...
}
```

### Create a payment button

This will create the code for a payment button (and modal window) that you can use to accept bitcoin on your website.  You can read [more about payment buttons here and try a demo](https://coinbase.com/docs/merchant_tools/payment_buttons).

The `custom` param will get passed through in [callbacks](https://coinbase.com/docs/merchant_tools/callbacks) to your site.  The list of valid `parameters` [are described here](https://coinbase.com/api/doc/1.0/buttons/create.html).

```java
Button buttonParams = new Button();
buttonParams.setText("This is the text");
buttonParams.setDescription("This is the description");
buttonParams.setPrice(Money.parse("USD 1.23"));
buttonParams.setName("This is the name");
buttonParams.setCustom("Custom tracking code here");

Button button = cbMain.createButton(buttonParams);

button.getCode(); // "93865b9cae83706ae59220c013bc0afd"
```

### Create an order for a button

This will generate an order associated with a button. You can read [more about creating an order for a button here](https://coinbase.com/api/doc/1.0/buttons/create_order.html).

```java
Order order = cb.createOrderForButton("93865b9cae83706ae59220c013bc0afd");
order.getReceiveAddress(); // "12mYY1z31J6mmYgzMXzRY8s8fAENiksWB8"
```

### Create a new user

```java
User userParams = new User();
userParams.setEmail("newuser@example.com");
userParams.setPassword("correct horse battery staple");
User newUser = cb.createUser(userParams);
newUser.getEmail(); // "newuser@example.com"
```

You can optionally pass in a client_id parameter that corresponds to your OAuth2 application and space separated list of permissions. When these are provided, the generated user will automatically have the permissions you’ve specified granted for your application. See the [API Reference](https://coinbase.com/api/doc/1.0/users/create.html) for more details.

```java
User userParams = new User();
userParams.setEmail("newuser@example.com");
userParams.setPassword("correct horse battery staple");
User newUser = cb.createUser(userParams, "oauth_client_id_here", "user merchant");
newUser.getEmail(); // "newuser@example.com"
```

### Verifying merchant callbacks

```java
String raw_http_post_body = ...;
String signature_header   = ...;
cb.verifyCallback(raw_http_post_body, signature_header); // true/false
```

## Building an account specific client

Some API calls only apply to a single account and take an account_id parameter. To easily make account specific calls, build a client with an account id as follows:

```java
Coinbase cb = new CoinbaseBuilder()
                      .withApiKey(System.getenv("COINBASE_API_KEY"), System.getenv("COINBASE_API_SECRET"))
                      .withAccountId("DESIRED_ACCOUNT_ID")
                      .build();

cb.getBalance(); // Gets the balance of desired account
```

## Security Notes

When creating an API Key, make sure you only grant it the permissions necessary for your application to function.

You should take precautions to store your API key securely in your application.  How to do this is application specific, but it's something you should [research](http://programmers.stackexchange.com/questions/65601/is-it-smart-to-store-application-keys-ids-etc-directly-inside-an-application) if you have never done this before.

## Decimal precision

This gem relies on the [Joda Money](http://www.joda.org/joda-money/) library, based on the [JDK BigDecimal](http://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html) class for arithmetic to maintain decimal precision for all values returned.

When working with currency values in your application, it's important to remember that floating point arithmetic is prone to [rounding errors](http://en.wikipedia.org/wiki/Round-off_error).

## Testing

If you'd like to contribute code or modify this library, you can run the test suite with:

```bash
mvn clean test
```

## Contributing

1. Fork this repo and make changes in your own copy
2. Add a test if applicable and run the existing tests with `mvn clean test` to make sure they pass
3. Commit your changes and push to your fork `git push origin master`
4. Create a new pull request and submit it back to us!
