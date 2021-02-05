package com.baydroid.ThreadX;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

class MainThreadExecutor implements Executor {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        mHandler.post(runnable);
    }
}
