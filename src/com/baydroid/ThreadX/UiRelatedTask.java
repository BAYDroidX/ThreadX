package com.baydroid.ThreadX;

import android.os.Handler;
import android.os.Looper;

public abstract class UiRelatedTask<Result> extends AbstractCancelableRunnable implements CancelableRunnable {

    protected static Handler sUiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void run() {
        if (!isCanceled()) {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            final Result result = doWork();
            if (!isCanceled()) {
                sUiHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (!isCanceled()) {
                            thenDoUiRelatedWork(result);
                        }
                    }
                });
            }
        }
    }

    protected abstract Result doWork();

    protected abstract void thenDoUiRelatedWork(Result result);
}
