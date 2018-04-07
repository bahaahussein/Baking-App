package com.example.android.bakingapp;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class SampleIdlingResource implements IdlingResource {

    private static String TAG = SampleIdlingResource.class.getSimpleName();
    @Nullable private volatile ResourceCallback mCallback;
    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(false);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        Log.d(TAG, "setIdleState: idling resource is called with " + isIdleNow);
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
