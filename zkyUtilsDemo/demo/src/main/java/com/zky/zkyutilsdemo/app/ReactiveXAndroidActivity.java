package com.zky.zkyutilsdemo.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.zky.zkyutils.utils.LogUtils;
import com.zky.zkyutilsdemo.R;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReactiveXAndroidActivity extends Activity {
    private String TAG = ReactiveXAndroidActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactive_x_android);

        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private void test() {
        rx.Observable observable = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d(TAG, "call currentThread:" + Thread.currentThread().getName());
                subscriber.onNext("test");
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtils.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d(TAG, "onError");
            }

            @Override
            public void onNext(String s) {
                LogUtils.d(TAG, "onNext currentThread:" + Thread.currentThread().getName());
                LogUtils.d(TAG, "onNext:" + s);
            }
        };

        observable.subscribeOn(Schedulers.io()) //Observable 的call方法执行在子线程
                .observeOn(AndroidSchedulers.mainThread())//Subscriber 的回调在主线程
                .subscribe(subscriber);
    }
}