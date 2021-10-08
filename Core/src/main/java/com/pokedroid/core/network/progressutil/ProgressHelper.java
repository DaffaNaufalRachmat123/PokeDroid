package com.pokedroid.core.network.progressutil;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author ali@pergikuliner
 * @created 5/10/17.
 * @project new_development.
 */

public class ProgressHelper {

    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ProgressRequestListener progressListener){
        Interceptor interceptor = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
        };

        return client.newBuilder()
                .addInterceptor(interceptor)
                .build();
    }

    public static RxProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressRequestListener progressRequestListener){
        return new RxProgressRequestBody(requestBody,progressRequestListener);
    }
}
