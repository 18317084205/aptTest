package com.liang.inject;

import android.support.annotation.UiThread;

public interface UnBinder {

    @UiThread
    void unbind(Object object);

    UnBinder EMPTY = new UnBinder() {

        @Override
        public void unbind(Object object) {
        }
    };
}
