package com.ljz.rxjava.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ljz.rxjava.R;
import com.ljz.rxjava.net.TestRequestInterface;
import com.ljz.rxjava.net.model.WeatherInfoData;
import com.ljz.rxjava.utils.ToastUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RetrofitActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RetrofitActivity.class.getSimpleName();
    private Context mContext;

    private final Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        mContext = this;

        findViewById(R.id.test_retrofit1).setOnClickListener(this);
        findViewById(R.id.test_retrofit2).setOnClickListener(this);
        findViewById(R.id.test_retrofit3).setOnClickListener(this);
        findViewById(R.id.test_retrofit4).setOnClickListener(this);
        findViewById(R.id.test_retrofit5).setOnClickListener(this);
        findViewById(R.id.test_retrofit6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.test_retrofit1:
                getWeatherSyncRequest();
                break;
            case R.id.test_retrofit2:
                getWeatherAsyncRequest();
                break;
            case R.id.test_retrofit3:
                getWeatherAsyncRequest2();
                break;
            case R.id.test_retrofit4:
                getWeatherAsResponse();
                break;
            case R.id.test_retrofit5:
                getWeatherAsString();
                break;
            case R.id.test_retrofit6:
                getWeatherUseRxJavaAsJson();
                break;
            default:
                break;
        }
    }

    /**
     * 同步网络请求获取天气信息
     */
    private void getWeatherSyncRequest() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();

        // 创建 网络请求接口 的实例
        TestRequestInterface request = retrofit.create(TestRequestInterface.class);
        //对 发送请求 进行封装

        Call<JsonObject> call = request.getWeaterAsJson();

        //同步请求
        try {
            Response<JsonObject> response = call.execute();
            Log.d(TAG, "getWeaterSyncRequest: response.body = " + response.body().toString());
            WeatherInfoData weatherInfoData = mGson.fromJson(response.body().toString(), WeatherInfoData.class);
            weatherInfoData.show();
            ToastUtil.toast(RetrofitActivity.this, weatherInfoData.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "getWeaterAsyncRequest, onFailure: 连接失败 : " + e.getMessage());
        }
    }

    /**
     * 异步网络请求获取天气信息
     */
    private void getWeatherAsyncRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();

        // 创建 网络请求接口 的实例
        TestRequestInterface request = retrofit.create(TestRequestInterface.class);
        //对 发送请求 进行封装

        Call<JsonObject> call = request.getWeaterAsJson();
        call.enqueue(new Callback<JsonObject>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //请求处理,输出结果

                Log.d(TAG, "getWeaterAsyncRequest: response.body = " + response.body().toString());
                WeatherInfoData weatherInfoData = mGson.fromJson(response.body().toString(), WeatherInfoData.class);
                weatherInfoData.show();
                ToastUtil.toast(RetrofitActivity.this, weatherInfoData.toString());
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.d(TAG, "getWeaterAsyncRequest, onFailure: 连接失败 : " + throwable.getMessage());
            }
        });
    }

    /**
     * 异步网络请求获取天气信息
     */
    private void getWeatherAsyncRequest2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();

        // 创建 网络请求接口 的实例
        TestRequestInterface request = retrofit.create(TestRequestInterface.class);
        //对 发送请求 进行封装

        Call<WeatherInfoData> call = request.getWeaterInfo();
        call.enqueue(new Callback<WeatherInfoData>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<WeatherInfoData> call, Response<WeatherInfoData> response) {
                //请求处理,输出结果
                Log.d(TAG, "getWeaterAsyncRequest2: response.body = " + response.body().toString());
                WeatherInfoData weatherInfoData = response.body();
                weatherInfoData.show();
                ToastUtil.toast(RetrofitActivity.this, weatherInfoData.toString());
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<WeatherInfoData> call, Throwable throwable) {
                Log.d(TAG, "getWeaterAsyncRequest2, onFailure: 连接失败 : " + throwable.getMessage());
            }
        });
    }

    private void getWeatherAsResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();

        // 创建 网络请求接口 的实例
        TestRequestInterface request = retrofit.create(TestRequestInterface.class);
        //对 发送请求 进行封装

        Call<ResponseBody> call = request.getWeater();
        call.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //请求处理,输出结果

                Log.d(TAG, "getWeaterAsResponse: response = " + response.toString());
                Log.d(TAG, "getWeaterAsResponse: response.body = " + response.body().toString());
                response.body();
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.d(TAG, "testRetrofit1, onFailure: 连接失败 : " + throwable.getMessage());
            }
        });
    }

    private void getWeatherAsString() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();

        // 创建 网络请求接口 的实例
        TestRequestInterface request = retrofit.create(TestRequestInterface.class);
        //对 发送请求 进行封装

        Call<String> call = request.getWeaterAsString();
        call.enqueue(new Callback<String>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //请求处理,输出结果

                Log.d(TAG, "getWeaterAsString: response = " + response.toString());
                Log.d(TAG, "getWeaterAsString: response.body = " + response.body().toString());
                WeatherInfoData weatherInfoData = mGson.fromJson(response.body(), WeatherInfoData.class);
                weatherInfoData.show();
                ToastUtil.toast(RetrofitActivity.this, weatherInfoData.toString());
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d(TAG, "getWeaterAsString, onFailure: 连接失败 : " + throwable.getMessage());
            }
        });
    }

    private void getWeatherUseRxJavaAsJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        TestRequestInterface requestInterface = retrofit.create(TestRequestInterface.class);

        Observable observable = requestInterface.getWeaterUseRxjavaAsJson();

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.d(TAG, "getWeatherUseRxJavaAsJson, call: o = " + o);
                Log.d(TAG, "getWeatherUseRxJavaAsJson, call: o.toString = " + o.toString());
                WeatherInfoData weatherInfoData = mGson.fromJson(o.toString(), WeatherInfoData.class);
                weatherInfoData.show();
                ToastUtil.toast(RetrofitActivity.this, weatherInfoData.toString());
            }
        });
    }
}
