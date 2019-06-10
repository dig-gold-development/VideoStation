package com.site.vs.videostation.ui.browse;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseActivity;

import static com.zhusx.core.utils._Activitys._addFragment;

public class BrowseActvity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.content,new MainMineFragment())
                .commit();


    }
}
