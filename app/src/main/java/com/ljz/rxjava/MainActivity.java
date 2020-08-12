package com.ljz.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testRx();
    }

    private void testRx() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Error");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, s);
            }
        };
        //使用Observable.create()创建被观察者
        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Wrold");
                subscriber.onCompleted();
            }
        });
        //订阅
        observable1.subscribe(observer);
    }
}
