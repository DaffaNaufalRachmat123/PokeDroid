package com.pokedroid.core.network.progressutil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author ali@pergikuliner
 * @created 5/10/17.
 * @project new_development.
 */

public abstract class ProgressHandler extends Handler {
    public static final int UPDATE = 0x01;
    public static final int START = 0x02;
    public static final int FINISH = 0x03;

    private final WeakReference<UIProgressListener> mUIProgressListenerWeakReference;

    public ProgressHandler(UIProgressListener uiProgressListener) {
        super(Looper.getMainLooper());
        mUIProgressListenerWeakReference = new WeakReference<UIProgressListener>(uiProgressListener);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE: {
                UIProgressListener uiProgessListener = mUIProgressListenerWeakReference.get();
                if (uiProgessListener != null) {
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    progress(uiProgessListener, progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
                }
                break;
            }
            case START: {
                UIProgressListener uiProgressListener = mUIProgressListenerWeakReference.get();
                if (uiProgressListener != null) {
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    start(uiProgressListener, progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());

                }
                break;
            }
            case FINISH: {
                UIProgressListener uiProgressListener = mUIProgressListenerWeakReference.get();
                if (uiProgressListener != null) {
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    finish(uiProgressListener, progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
                }
                break;
            }
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public abstract void start(UIProgressListener uiProgressListener,long currentBytes, long contentLength, boolean done);
    public abstract void progress(UIProgressListener uiProgressListener,long currentBytes, long contentLength, boolean done);
    public abstract void finish(UIProgressListener uiProgressListener,long currentBytes, long contentLength, boolean done);

}
