package com.pokedroid.core.network.progressutil;

import android.os.Handler;
import android.os.Message;

/**
 * @author ali@pergikuliner
 * @created 5/10/17.
 * @project new_development.
 */

public abstract class UIProgressListener implements ProgressRequestListener{
    private boolean isFirst = false;

    private static class UIHandler extends ProgressHandler {
        public UIHandler(UIProgressListener uiProgressListener) {
            super(uiProgressListener);
        }

        @Override
        public void start(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done) {
            if (uiProgressListener != null) {
                uiProgressListener.onUIStart(currentBytes, contentLength, done);
            }
        }

        @Override
        public void progress(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done) {
            if (uiProgressListener != null) {
                uiProgressListener.onUIProgress(currentBytes, contentLength, done);
            }
        }

        @Override
        public void finish(UIProgressListener uiProgressListener, long currentBytes, long contentLength, boolean done) {
            if (uiProgressListener != null) {
                uiProgressListener.onUIFinish(currentBytes, contentLength, done);
            }
        }
    }

    private final Handler mHandler = new UIHandler(this);

    @Override
    public void onRequestProgress(long bytesWrite, long contentLength, boolean done) {
        if (!isFirst) {
            isFirst = true;
            Message start = Message.obtain();
            start.obj = new ProgressModel(bytesWrite, contentLength, done);
            start.what = ProgressHandler.START;
            mHandler.sendMessage(start);
        }

        Message message = Message.obtain();
        message.obj = new ProgressModel(bytesWrite, contentLength, done);
        message.what = ProgressHandler.UPDATE;
        mHandler.sendMessage(message);

        if (done) {
            Message finish = Message.obtain();
            finish.obj = new ProgressModel(bytesWrite, contentLength, done);
            finish.what = ProgressHandler.FINISH;
            mHandler.sendMessage(finish);
        }
    }

    public abstract void onUIProgress(long currentBytes, long contentLength, boolean done);

    public void onUIStart(long currentBytes, long contentLength, boolean done) {

    }

    public void onUIFinish(long currentBytes, long contentLength, boolean done) {

    }
}
