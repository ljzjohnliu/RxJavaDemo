package com.ljz.rxjava.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.rxjava.R;
import com.ljz.rxjava.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class TestRxSimpleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TestRxSimpleActivity.class.getSimpleName();
    private Context mContext;
    private Button observerBtn;
    private Button subscriberBtn;
    private Button justfromBtn;
    private Button simpleBtn;
    private Button actionBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        mContext = this;

        observerBtn = findViewById(R.id.test_observer);
        observerBtn.setOnClickListener(this);
        subscriberBtn = findViewById(R.id.test_subscriber);
        subscriberBtn.setOnClickListener(this);
        justfromBtn = findViewById(R.id.test_justfrom);
        justfromBtn.setOnClickListener(this);
        simpleBtn = findViewById(R.id.test_simple);
        simpleBtn.setOnClickListener(this);
        actionBtn = findViewById(R.id.test_action);
        actionBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.test_observer:
                testRxObserver();
                break;
            case R.id.test_subscriber:
                testRxSubscriber();
                break;
            case R.id.test_justfrom:
                testRxJustFrom();
                break;
            case R.id.test_simple:
                testRxSimple();
                break;
            case R.id.test_action:
                testRxAction();
                break;
            default:
                break;
        }
    }

    private void testRxObserver() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "testRxObserver, Completed");
                ToastUtil.toast(mContext, "testRxObserver, onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "testRxObserver, Error");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "testRxObserver,  s = " + s);
                ToastUtil.toast(mContext, "testRxObserver, onNext " + s);
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
                ToastUtil.toast(mContext, "testRxSubscriber, onNext " + s);
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
                Log.d(TAG, "testRxJustFrom, onStart: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "testRxJustFrom, onNext Item: " + s);
                ToastUtil.toast(mContext, "testRxJustFrom, onNext " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "testRxJustFrom, Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "testRxJustFrom, Error!");
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

        String[] words = {"Hello3", "World3"};
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

    private void testRxSimple() {

        Observable.just("Hello simple", "World simple").subscribe(new Subscriber<String>() {
            @Override
            public void onStart() {
                Log.d(TAG, "testRxSimple, onStart: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "testRxSimple, onNext Item: " + s);
                ToastUtil.toast(mContext, "testRxSimple, onNext " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "testRxSimple, Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "testRxSimple, Error!");
            }
        });
    }

    private void testRxAction() {

//        Observable.just("Hello action", "World action").subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.d(TAG, "testRxSubscriber, call s: " + s);
//                ToastUtil.toast(mContext, "testRxAction, call " + s);
//            }
//        });

        Observable observable = Observable.just("Hello action", "World action");
        //处理onNext()中的内容
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, s);
                ToastUtil.toast(mContext, "testRxAction, onNextAction call " + s);
            }
        };
        //处理onError()中的内容
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtil.toast(mContext, "testRxAction, onErrorAction");
            }
        };
        //处理onCompleted()中的内容
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.i(TAG, "Completed");
                ToastUtil.toast(mContext, "testRxAction, Completed");

            }
        };

        //使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        //使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        //使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }
}
