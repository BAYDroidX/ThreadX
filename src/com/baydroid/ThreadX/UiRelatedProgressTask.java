package com.baydroid.ThreadX;

public abstract class UiRelatedProgressTask<Result, Progress> extends UiRelatedTask<Result> implements CancelableRunnable {

    protected void publishProgress(final Progress progress) {
        if (!isCanceled()) {
            sUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    onProgressUpdate(progress);
                }
            });
        }
    }

    protected abstract void onProgressUpdate(Progress progress);
}
