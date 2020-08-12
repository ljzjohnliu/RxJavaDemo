package com.ljz.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testRxObserver();
//        testRxSubscriber();
        testRxJustFrom();
    }

    private void testRxObserver() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "testRxObserver, Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "testRxObserver, Error");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "testRxObserver,  s = " + s);
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
    
    private void testRxSubscriber() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                Log.d(TAG, "testRxSubscriber, onStart: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "testRxSubscriber, Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "testRxSubscriber, Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "testRxSubscriber, Error!");
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
        observable1.subscribe(subscriber);
        subscriber.unsubscribe();
    }

    private void testRxJustFrom() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                Log.d(TAG, "testRxSubscriber, onStart: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "testRxSubscriber, Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "testRxSubscriber, Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "testRxSubscriber, Error!");
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

        Observable observable2 = Observable.just("Hello2", "World2");

        String [] words = {"Hello3", "World3"};
        Observable observable3 = Observable.from(words);

        List<String> list = new ArrayList<String>();
        list.add("Hello4");
        list.add("Wrold4");
        Observable observable4 = Observable.from(list);

        //订阅 这里有个疑问，就是同时订阅多个被观察者的时候，只会执行一次
//        observable1.subscribe(subscriber);
//        observable2.subscribe(subscriber);
//        observable3.subscribe(subscriber);
        observable4.subscribe(subscriber);
//        subscriber.unsubscribe();
    }
}
