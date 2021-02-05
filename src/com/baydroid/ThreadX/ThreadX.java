package com.baydroid.ThreadX;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadX {

    public static final int DEFAULT_POOL_SIZE = 3;
    public static final String DEFAULT_TASK_TYPE = "default";

    private static Executor sMainThreadExecutor = new MainThreadExecutor();

    public static Executor onMainThread() {
        return sMainThreadExecutor;
    }

    public static BackgroundThreadExecutor onBackgroundThread() {
        return new ExecutorObtainer();
    }

    static class ExecutorObtainer implements BackgroundThreadExecutor {

        private static Map<ExecutorId, Executor> sCachedExecutors = new HashMap<ExecutorId, Executor>();

        private int mDesiredThreadPoolSize = DEFAULT_POOL_SIZE;
        private String mDesiredTaskType = DEFAULT_TASK_TYPE;

        @Override
        public BackgroundThreadExecutor serially() {
            withThreadPoolSize(1);
            return this;
        }

        @Override
        public BackgroundThreadExecutor withTaskType(String taskType) {
            if (taskType == null) {
                throw new IllegalArgumentException("Task type cannot be null");
            }
            mDesiredTaskType = taskType;
            return this;
        }

        @Override
        public BackgroundThreadExecutor withThreadPoolSize(int poolSize) {
            if (poolSize < 1) {
                throw new IllegalArgumentException("Thread pool size cannot be less than 1");
            }
            mDesiredThreadPoolSize = poolSize;
            return this;
        }

        @Override
        public void execute(Runnable runnable) {
            getExecutor().execute(runnable);
        }

        Executor getExecutor() {
            final ExecutorId executorId = new ExecutorId(mDesiredThreadPoolSize, mDesiredTaskType);
            synchronized (ExecutorObtainer.class) {
                Executor executor = sCachedExecutors.get(executorId);
                if (executor == null) {
                    executor = Executors.newFixedThreadPool(mDesiredThreadPoolSize);
                    sCachedExecutors.put(executorId, executor);
                }
                return executor;
            }
        }
    }

    private static class ExecutorId {
        private final int mThreadPoolSize;
        private final String mTaskType;

        private ExecutorId(int threadPoolSize, String taskType) {
            mThreadPoolSize = threadPoolSize;
            mTaskType = taskType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExecutorId executorId = (ExecutorId) o;
            if (mThreadPoolSize != executorId.mThreadPoolSize) return false;
            if (!mTaskType.equals(executorId.mTaskType)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return 31 * mThreadPoolSize + mTaskType.hashCode();
        }
    }

}
