package com.baydroid.ThreadX;

import java.util.concurrent.Executor;

public interface BackgroundThreadExecutor extends Executor {
  
    BackgroundThreadExecutor serially();
    
    BackgroundThreadExecutor withTaskType(String taskType);
    
    BackgroundThreadExecutor withThreadPoolSize(int poolSize);
}
