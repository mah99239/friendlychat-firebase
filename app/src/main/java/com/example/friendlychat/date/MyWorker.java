package com.example.friendlychat.date;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkContinuation;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.model.WorkProgress;


public class MyWorker extends Worker {
private static final String TAG = "myworker";

public MyWorker(@NonNull Context appContext , @NonNull WorkerParameters workerParams){
    super(appContext, workerParams);

}

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG ,"performing long running task in scheduled jop");
        //TODO(developer): add long running task here.
        return Result.success();
    }
}
