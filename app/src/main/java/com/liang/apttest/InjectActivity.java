package com.liang.apttest;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Method;

public class InjectActivity {
    private static final ArrayMap<String, Object> injectMap = new ArrayMap<>();

    public static void inject(AppCompatActivity activity) {
        String className = activity.getClass().getName();
        try {

            Object inject = injectMap.get(className);

            if (inject == null) {
                //加载build生成的类
                Class<?> aClass = Class.forName(className + "$$InjectActivity");
                inject = aClass.newInstance();
                injectMap.put(className, inject);
            }
            //反射执行方法
            Method m1 = inject.getClass().getDeclaredMethod("inject", activity.getClass());
            m1.invoke(inject, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
