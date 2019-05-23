package com.site.vs.videostation.base;





import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author zhangbb
 * @date 2016/10/18
 */
public abstract class BasePresenter<T, E> {

    private WeakReference<T> mViewRef;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    public E mModel;

    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
        this.mModel = createModel();
    }


    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }

    public T getView() {
        if (mViewRef != null) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    protected abstract E createModel();

    /**
     * 统一处理Subscribe的回收
     *
     * @param subscriber
     */
    public void addSubscriber(Subscription subscriber) {
        if (subscriber != null && this.mCompositeSubscription != null) {
            this.mCompositeSubscription.add(subscriber);
        }
    }

    protected <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        addSubscriber(o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s));
    }

    /**
     * 获取错误信息
     *
     * @param e
     * @return
     */
    public String getErrorMsg(Throwable e) {
        if (e == null) {
            return "未知错误";
        }
        if (e instanceof HttpException) {
            Response<?> response = ((HttpException) e).response();
            if (response != null) {
                ResponseBody responseBody = response.errorBody();
                if (responseBody != null) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(responseBody.string());
                        if (jsonObject.containsKey("message")) {
                            return jsonObject.getString("message");
                        }
                    } catch (Exception e1) {
                        return "未知错误";
                    }
                }
            }
        } else if (e instanceof SocketException) {
            return "网络错误，请检查网络";
        } else if (e instanceof SocketTimeoutException) {
            return "网络链接超时";
        }
        return "未知错误";
    }

}
