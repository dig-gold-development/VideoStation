package com.site.vs.videostation.base;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/13 14:03
 */

public class BaseActivity extends AppCompatActivity implements Lib_LifeCycleListener {
    private Set<Lib_OnCycleListener> cycleListener = new HashSet<>();
    private Toast mToast;

    @Override
    public void _addOnCycleListener(Lib_OnCycleListener lib_onCycleListener) {
        cycleListener.add(lib_onCycleListener);
    }

    @Override
    public void _removeOnCycleListener(Lib_OnCycleListener lib_onCycleListener) {
        cycleListener.remove(lib_onCycleListener);
    }

    @Override
    public Set<Lib_OnCycleListener> getCycleListeners() {
        return cycleListener;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }
}
