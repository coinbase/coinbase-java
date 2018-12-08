## Coinbase Sample app

This is a sample application demonstrating features of Coinbase Android SDK.

### Getting started
To start using this sample, log in to your Coinbase account, create a new [OAuth2 application](https://www.coinbase.com/oauth/applications/new) (or get an existing one),
and grab `clientId`, `clientSecret` along with `redirectUri`. Replace stub values in `./api-keys.gradle` and you are ready to go! 

```groovy
ext.clientId = "<client id>"
ext.clientSecret = "<client secret>"
ext.redirectUri = "<redirect://uri>"
```

### Features
1. Authorization with browser or Custom Chrome Tab
2. User information and loading of avatar image (using Glide library)
3. List of user accounts
4. List of transaction on an account with pagination
5. List of supported currencies
6. Spot prices for selected currency and user native currency
7. List of payment methods
8. Authorization information

The sample app uses authorization with read-only scopes for `user`, `account` and `transactions`. Loading data is implemented with both Rx and async callbacks.