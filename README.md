# Coinbase Android SDK

![Platform](https://img.shields.io/badge/minsdk-15-blue.svg)
![SDK version](https://img.shields.io/badge/sdk__version-2.0.0-orange.svg)
![License](https://img.shields.io/badge/license-apache%202.0-green.svg)

An easy way to buy, sell, send, and accept [bitcoin](http://en.wikipedia.org/wiki/Bitcoin) through the [Coinbase API](https://developers.coinbase.com/).

This library is a wrapper around the [Coinbase JSON API](https://developers.coinbase.com/api/v2). It supports OAuth 2.0 for performing actions on other people's accounts.

Working with the SDK:
* [Installation](#installation)
* [Usage](#usage)
* [OAuth2 and Authentication](#oauth-20-authentication-accessing-users-account-data)
* [Examples](#examples)
* [Proguard](#proguard-setup)
* [Security Notes](#security-notes)
* [Testing](#testing)
* [Contributing](#contributing)

Other resources:
* [Sample app](https://github.com/coinbase/coinbase-android-sdk-private/tree/v2.0.0/app) with examples
* [Project Wiki](https://github.com/coinbase/coinbase-java/wiki)
* [Coinbase REST API v2](https://developers.coinbase.com/api/v2)
* [OAuth2 Reference](https://developers.coinbase.com/docs/wallet/coinbase-connect/reference)

## Installation

### Using Maven

Add the following dependency to your project's Maven pom.xml:

```xml
<dependency>
	<groupId>com.coinbase</groupId>
	<artifactId>coinbase-android</artifactId>
	<version>3.0.0</version>
</dependency>
```

The library will automatically be pulled from Maven Central.

### Using Gradle

```gradle
dependencies {
    compile 'com.coinbase:coinbase-android:3.0.0'
}
```

### Manual

You can build this library aar and all its dependencies to a folder as follows:

```bash
git clone git@github.com:coinbase/coinbase-java.git
./gradlew coinbase-java:assembleRelease
mv coinbase-java/build/outputs/aar/coinbase-java-release.aar $YOUR_JAR_DIRECTORY
```

## Usage

### Basic setup (only accessing public data)

Configure `coinbase` object to access [public data](https://developers.coinbase.com/api/v2#data-endpoints).

```kotlin
// Set up Coinbase object for public data access only
val coinbase = CoinbaseBuilder.withPublicDataAccess(applicationContext).build()

// Get any of public data resource and request data from it
coinbase.currenciesResource.supportedCurrencies.enqueue(callback)
```

When 'coinbase' instance is setup for public data access you can use these resources:

1. currenciesResource
2. exchangeRatesResource
3. pricesResource
4. currenciesResource

### OAuth 2.0 Authentication (accessing user's account data)

Start by [creating a new OAuth 2.0 application](https://www.coinbase.com/settings/api). Register redirect url under <b>Permitted Redirect URIs</b>.
This URL will be used after successful authorization. It should be an URL that your application is capable to handle, so auth result
delivered back to your app.
<br/>
<br/>
After you create OAuth 2.0 application, go to application web page that will have an address like https://www.coinbase.com/oauth/applications/{your_app_id}.
Copy <b>Client Id</b> and <b>Client Secret</b> to your android application.
<br/>
<br/>
Your android application can now be authorized to access user account data:

```kotlin
// Set up Coinbase object to access user data
val coinbase = CoinbaseBuilder.withClientIdAndSecret(applicationContext, clientId, clientSecret).build()

// Begin OAuth 2.0 flow with web sign in
coinbase.beginAuthorization(activityContext, redirectUri, scopes)

// Get result of web authorization as an intent with mentioned redirectUri. Complete OAuth 2.0 flow
override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null) return
        coinbase.completeAuthorizationRx(intent)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .doFinally { hideProgress() }
                .subscribe(subscriber)
}
```

After authorization suceseed, you can call methods on `coinbase` similar to the ones described in the
[Wallet Endpoints documentation](https://developers.coinbase.com/api/v2#wallet-endpoints).  For example:

```kotlin
coinbase.userResource.getAuthInfo.enqueue(callback);
```

## Examples

### Get User currencies accounts

```kotlin
// get user accounts asynchronously.
coinbase.accountResource.getAccounts().enqueue(object: Callback<PagedResponse<Account>> {

    override fun onSuccess(result: PagedResponse<Account>?) {
            TODO("Process accounts data")
    }

    override fun onFailure(t: Throwable?) {
        TODO("process error")
    }
})
```

Get a specific account.

```kotlin
coinbase.accountResource.getAccount(accountId).enqueue(callback)
```

The account name can be changed with

```kotlin
coinbase.accountResource.updateAccount(accountId, newName).enqueue(callback)
```

Also, an account can be deleted

```kotlin
coinbase.accountResource.deleteAccount(accountId).enqueue(callback)
```


### Send bitcoin

```kotlin
val sendMoneyRequest = SendMoneyRequest("user2@example.com", "0.01", "BTC")
coinbase.transactionsResource.sendMoney(accountId, twoFactorAuthToken, sendMoneyRequest).enqueue(callback)
```

The `to` value can be a bitcoin address and a description (notes) can be attached to the money.  The description is only visible on Coinbase (not on the general bitcoin network).

```kotlin
val sendMoneyRequest = SendMoneyRequest("user2@example.com", "2.25", "USD")
sendMoneyRequest.setDescription("Thanks for the coffee!")
coinbase.transactionsResource.sendMoney(accountId, twoFactorAuthToken, sendMoneyRequest).enqueue(callback)
```

### Request bitcoin

This will send an email to the recipient, requesting payment, and give them an easy way to pay.

```kotlin
// Synchronous calls are used for simplicity
val moneyRequest = MoneyRequest("user2@example.com", "100", "USD")
moneyRequest.setDescription("Invoice for window cleaning")
val moneyRequest = coinbase.transactionsResource.requestMoney(accountId, moneyRequest).execute().data

coinbase.transactionsResource.resendMoneyRequest(accountId, moneyRequest.id).execute()

coinbase.transactionsResource.cancelRequest(accountId, moneyRequest.id).execute()

// From the other side

coinbase.transactionsResource.completeRequest(accountId, transactionId).execute()
```

### List your current transactions

By default sorted in descending order by createdAt, 30 transactions per page

```kotlin
// Synchronous call is used for simplicity
var transactions = coinbase.transactionsResource.listTransactions(accountId).execute().data
transactions[0].id
```

Transactions will always have an `id` attribute which is the primary way to identify them through the Coinbase API.

### Get transaction details

This will fetch the details/status of a transaction that was made within Coinbase

```kotlin
// Synchronous call is used for simplicity
val t = coinbase.transactionsResource.showTransaction(accountId, transactionId).execute().data
t.status; // Transaction.STATUS_PENDING
```

### Buy or Sell bitcoin

Buying and selling bitcoin requires you to [add a payment method](https://coinbase.com/accounts) through the web app first.

Then you can call `buy` or `sell` and pass a `quantity` of bitcoin you want to buy.

```kotlin
val transferOrder = TransferOrderBody("0.01", "BTC", paymentMethodId)
// Synchronous call is used for simplicity
coinbase.buysResource.placeBuyOrder(accountId,transferOrder).execute()
```


```kotlin
val transferOrder = TransferOrderBody("0.01", "BTC", paymentMethodId)
// Synchronous call is used for simplicity
coinbase.sellsResource.placeSellOrder(accountId, transferOrder).execute()
```

### Listing Buy/Sell History

You can use `listBuys`, `listSells` to view past buys and sells.

```java
coinbase.buysResource.listBuys(accountId).enqueue(callback)
coinbase.sellsResource.listSells(accountId).enqueue(callback)
```

Check out the [sample app](https://github.com/coinbase/coinbase-android-sdk-private/tree/documentation/app#getting-started)
with example of how to use the SDK (both async and Rx).

## Proguard setup

If you are using proguard, include following lines to the application proguard properties file.

```bash
-dontwarn okio.**
-dontwarn retrofit2.**
```

## Security Notes

When creating an API Key, make sure you only grant it the permissions necessary for your application to function.

You should take precautions to store your API key securely in your application.  How to do this is application specific, but it's something you should [research](http://programmers.stackexchange.com/questions/65601/is-it-smart-to-store-application-keys-ids-etc-directly-inside-an-application) if you have never done this before.

## Testing

If you'd like to contribute code or modify this library, you can run the test suite with:

```bash
./gradlew :coinbase-java:test
```

## Contributing

1. Fork this repo and make changes in your own copy
2. Add Git pre-commit hook by executing ./add_precommit_git_hook.sh. This will add `Checkstyle` and `pmd` checks before commit
3. Add a test if applicable and run the existing tests with `./gradlew :coinbase-java:test` to make sure they pass
4. Commit your changes and push to your fork `git push origin master`
5. Create a new pull request and submit it back to us!
