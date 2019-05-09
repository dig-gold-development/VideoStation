package com.site.vs.videostation.base;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.site.vs.videostation.widget.FrescoImageView;
import com.zhusx.core.app.Lib_BaseFragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/13 14:43
 */

public class BaseFragment extends Lib_BaseFragment {
    private CompositeSubscription mCompositeSubscription;


    public void addSubscription(Subscription s) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
    }
    public void setImageURI(View view, String path) {
        ((FrescoImageView) view).setImageURI(path);

    }
}
