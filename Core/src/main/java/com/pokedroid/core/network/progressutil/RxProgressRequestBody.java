package com.pokedroid.core.network.progressutil;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author ali@pergikuliner
 * @created 5/10/17.
 * @project new_development.
 */

public class RxProgressRequestBody extends RequestBody {
    private RequestBody requestBody;
    private ProgressRequestListener progressListener;
    private BufferedSink bufferedSink;

    public RxProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }


    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        try {
            if (bufferedSink == null) {
                bufferedSink = Okio.buffer(sink(sink));
            }

            requestBody.writeTo(bufferedSink);

            bufferedSink.flush();
        } catch (IllegalStateException e) {

        }

    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(@NonNull Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }

                bytesWritten += byteCount;
                if (progressListener != null) {
                    progressListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            }
        };
    }
}
