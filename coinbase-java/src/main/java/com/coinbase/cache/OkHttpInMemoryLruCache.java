package com.coinbase.cache;

import android.text.TextUtils;
import android.util.LruCache;
import android.util.Pair;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An in-memory cache of etag/body pairs.  In practice, this cache doesn't need to be larger than 250kb in size
 */
public class OkHttpInMemoryLruCache extends LruCache<String, Pair<String, OkHttpInMemoryLruCache.CachedResponseBody>> {

    private final static int NOT_MODIFIED = 304;

    public OkHttpInMemoryLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected synchronized int sizeOf(String key, Pair<String, CachedResponseBody> value) {
        //Url length + etag length + body length
        return key.length() + value.first.length() + value.second.body().length;
    }

    public Interceptor createInterceptor() {
        return (chain) -> {
            Request request = chain.request();

            //Only send ETag for get requests
            if (!request.method().equalsIgnoreCase("GET")) {
                return chain.proceed(request);
            }

            String url = request.url().toString();
            Pair<String, CachedResponseBody> responseBodyPair;
            synchronized (OkHttpInMemoryLruCache.this) {
                responseBodyPair = get(url);
            }

            if (responseBodyPair != null) {
                request = request.newBuilder().header("If-None-Match", get(url).first).build();
            }

            Response response = chain.proceed(request);

            if (response.isSuccessful()) {
                //Response is between a 200 and a 300, cache the response if it has an ETag header
                String etag = response.header("ETag");
                if (!TextUtils.isEmpty(etag)) {
                    responseBodyPair = new Pair<>(etag, new CachedResponseBody(response.body().contentType(), response.body().bytes(), response.code()));
                    synchronized (OkHttpInMemoryLruCache.this) {
                        put(url, responseBodyPair);
                    }
                    return response.newBuilder()
                            .body(ResponseBody.create(responseBodyPair.second.contentType(), responseBodyPair.second.body()))
                            .code(responseBodyPair.second.successCode())
                            .build();
                }
            } else if (response.code() == NOT_MODIFIED) {
                //Server sent us a 304, return the cached body if we have it (we should)
                if (response.body().contentLength() > 0) {
                    android.util.Log.e("Coinbase", "Unexpected 304 with content length!");
                    return response;
                }
                if (responseBodyPair == null) {
                    android.util.Log.e("Coinbase", "304 but no cached response");
                    return response;
                }

                return response.newBuilder()
                        .body(ResponseBody.create(responseBodyPair.second.contentType(), responseBodyPair.second.body()))
                        .code(responseBodyPair.second.successCode())
                        .build();
            }

            return response;
        };
    }

    static class CachedResponseBody {
        private final MediaType mContentType;
        private final byte[] mBody;
        private final int mSuccessCode;

        CachedResponseBody(MediaType contentType, byte[] body, int successCode) {
            mContentType = contentType;
            mBody = body;
            mSuccessCode = successCode;
        }

        public byte[] body() {
            return mBody;
        }

        MediaType contentType() {
            return mContentType;
        }

        int successCode() {
            return mSuccessCode;
        }
    }
}
