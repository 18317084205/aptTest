package com.liang.inject;

import android.support.annotation.UiThread;

public interface UnBinder {

    @UiThread
    void unbind();

    UnBinder EMPTY = new UnBinder() {

        @Override
        public void unbind() {
        }
    };
}
