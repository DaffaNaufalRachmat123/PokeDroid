package com.pokedroid.core.network.progressutil;

/**
 * @author ali@pergikuliner
 * @created 5/10/17.
 * @project new_development.
 */

public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
