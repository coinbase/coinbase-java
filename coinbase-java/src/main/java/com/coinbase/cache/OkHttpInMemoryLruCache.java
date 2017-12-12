package com.coinbase.cache;

import android.text.TextUtils;
import android.util.LruCache;
import android.util.Pair;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * An in-memory cache of etag/body pairs.  In practice, this cache doesn't need to be larger than 250kb in size
 */
public class OkHttpInMemoryLruCache extends LruCache<String, Pair<String, OkHttpInMemoryLruCache.CachedResponseBody>> {

    private final static int NOT_MODIFIED = 304;

    private String[] mEnabledForcedCachePathPrefixes;

    private final Map<String, Long> mForcedCacheUrls = new HashMap<>();

    private volatile long mTimeoutInMillis;

    private volatile boolean mForcedCacheEnabled = false;

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
                /**
                 * PUT/POST/DELETE can cause state update on the server
                 */
                synchronized (OkHttpInMemoryLruCache.this) {
                    mForcedCacheUrls.clear();
                }
                return chain.proceed(request);
            }


            String url = request.url().toString();
            Pair<String, CachedResponseBody> responseBodyPair;
            synchronized (OkHttpInMemoryLruCache.this) {
                responseBodyPair = get(url);
            }

            if (responseBodyPair != null) {
                synchronized (OkHttpInMemoryLruCache.this) {
                    request = request.newBuilder().header("If-None-Match", get(url).first).build();
                }
            }

            synchronized (OkHttpInMemoryLruCache.this) {
                Response cachedResponse = handleForcedCacheResponseIfEnabled(request, responseBodyPair, chain);
                if (cachedResponse != null) {
                    return cachedResponse;
                }
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

    /**
     * Force cache response if timeout hasn't been hit and the url is one of the urls in the set.
     * @param paths the set of paths to force cache for
     * @param timeoutInMillis timeout in milliseconds, after the timeout is hit the response will not be from the local cache.
     */
    public void setForcedCache(Set<String> paths, long timeoutInMillis) {
        synchronized (OkHttpInMemoryLruCache.this) {
            mForcedCacheUrls.clear();
            mEnabledForcedCachePathPrefixes = paths.toArray(new String[paths.size()]);
            mTimeoutInMillis = timeoutInMillis;
        }
    }

    /**
     * Disable forced cache response
     */
    public void clearForcedCache() {
        synchronized (OkHttpInMemoryLruCache.this) {
            mForcedCacheUrls.clear();
            mEnabledForcedCachePathPrefixes = null;
        }
    }

    /**
     * Enable/disable forced cache.
     */
    public void setForcedCacheEnabled(boolean enabled) {
        synchronized (OkHttpInMemoryLruCache.this) {
            mForcedCacheEnabled = enabled;
        }
    }

    public Response handleForcedCacheResponseIfEnabled(Request request,
                                                       Pair<String, CachedResponseBody> responseBodyPair,
                                                       Interceptor.Chain chain) {
        if (!mForcedCacheEnabled) {
            return null;
        }
        /**
         * If we're forcing caching of this url path
         */
        String url = request.url().toString();
        String path = request.url().encodedPath();
        if (mEnabledForcedCachePathPrefixes != null && StringUtils.startsWithAny(path, mEnabledForcedCachePathPrefixes)) {

            long currentTimeMillis = System.currentTimeMillis();
            /**
             * We have a response body, we've cached this path/query before and the response hasn't timed out,
             * then terminate the chain and return the cached response.
             */
            if (responseBodyPair != null &&
                    mForcedCacheUrls.containsKey(url) &&
                    (currentTimeMillis - mForcedCacheUrls.get(url)) < mTimeoutInMillis) {
                return new Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .body(ResponseBody.create(responseBodyPair.second.contentType(), responseBodyPair.second.body()))
                        .code(responseBodyPair.second.successCode())
                        .build();
            } else {
                /**
                 * Skip force cache and remember the last time we served a real server response.
                 */
                mForcedCacheUrls.put(url, currentTimeMillis);
            }
        }
        return null;
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
