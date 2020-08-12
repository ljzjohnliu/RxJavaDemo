package com.ljz.rxjava;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava在不指定线程的情况下，发起时间和消费时间默认使用当前线程。
 */
public class SchedulerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SchedulerActivity.class.getSimpleName();
    private Context mContext;
    private Button scheduler1Btn;
    private Button scheduler2Btn;

    private void printPid(String from) {
//        Log.d(TAG, from + " printPid, 获取应用主线程ID: " + Looper.getMainLooper().getThread().getId());
//        Log.d(TAG, from + " printPid, 获取当前进程ID: " + android.os.Process.myPid());
//        Log.d(TAG, from + " printPid, 获取当前进程的用户ID: " + android.os.Process.myUid());
        Log.d(TAG, from + " printPid, 获取当前线程ID（1）: " + Thread.currentThread().getId());
        Log.d(TAG, from + " printPid, 获取当前线程ID（2）: " + android.os.Process.myTid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        mContext = this;
        printPid("onCreate");

        scheduler1Btn = findViewById(R.id.test_scheduler1);
        scheduler1Btn.setOnClickListener(this);
        scheduler2Btn = findViewById(R.id.test_scheduler2);
        scheduler2Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.test_scheduler1:
                testRxScheduler1();
                break;
            case R.id.test_scheduler2:
                testRxScheduler2();
                break;
            default:
                break;
        }
    }


    /**
     * 模拟一个需求：新的线程发起事件，在主线程中消费
     * subscribeOn() 和 observeOn()方法来指定发生的线程和消费的线程。
     * 如果只指定subscribeOn线程，不指定observeOn的话，Subscriber 的回调发生在subscribeOn指定的线程中
     * RxJava已经为我们提供了一下几个Scheduler
     * <p>
     * Schedulers.immediate()：直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     * Schedulers.newThread()：总是启用新线程，并在新线程执行操作。
     * Schedulers.io()： I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * Schedulers.computation()：计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     * AndroidSchedulers.mainThread()：它指定的操作将在 Android 主线程运行。
     */
    private void testRxScheduler1() {
        Observable.just("Hello", "World")
                .subscribeOn(Schedulers.newThread()) //指定 subscribe() 发生在新的线程
                .observeOn(AndroidSchedulers.mainThread()) //指定 Subscriber 的回调发生在主线程
//                .observeOn(Schedulers.newThread()) //指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "testRxScheduler1, Action1 call: s = " + s);
                        printPid("Action1");
                    }
                });
    }

    /**
     * 若将observeOn(AndroidSchedulers.mainThread())去掉会怎么样？不为消费事件show(s)指定线程后，show(s)会在那里执行？
     * 其实，observeOn() 指定的是它之后的操作所在的线程。也就是说，map的处理和最后的消费事件Action1@call都会在io线程中执行。
     * observeOn()可以多次使用，可以随意变换线程
     *
     */
    private void testRxScheduler2() {
        Observable.just("Hello"/*, "World"*/)
                .subscribeOn(Schedulers.newThread()) //指定：在新的线程中发起
                .observeOn(Schedulers.io())          //指定：在io线程中处理
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        printPid("Func1");
                        return handleString(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //指定：在主线程中处理
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "testRxScheduler2, Action1 call: s = " + s);
                        printPid("Action1");
                    }
                });
    }

    private String handleString(String s) {
        return s + " ** ";
    }
}
