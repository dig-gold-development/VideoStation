package com.site.vs.videostation.entity;

import android.content.Context;
import android.content.Intent;


import com.site.vs.videostation.ui.detail.view.DetailActivity;

import java.io.Serializable;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/16 17:52
 */

public class Move implements Serializable {
    public String id;
    public String name;
    public String pic;
    public String title;
    public String year;
    public String area;
    public String actor;

    public void startActivity(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.Companion.getID(), id);
        context.startActivity(intent);
    }

}
