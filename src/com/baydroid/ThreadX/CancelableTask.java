package com.baydroid.ThreadX;

public abstract class CancelableTask extends AbstractCancelableRunnable implements CancelableRunnable {

    @Override
    public void run() {
        if (!isCanceled()) {
            doWork();
        }
    }

    protected abstract void doWork();
}
