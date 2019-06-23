package com.site.vs.videostation.ui.login;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.site.vs.videostation.R;
import com.site.vs.videostation.base.BaseActivity;
import com.site.vs.videostation.ui.MainActivity;
import com.site.vs.videostation.ui.MainVisitorActivity;


import java.util.List;

public class SplashActivity extends AppCompatActivity {
    Handler mHandle = new Handler();
    Runnable runnable;
    private SharedPreferences sharedPreferences;
    private String id;
    private String token;
    private Boolean login;
    private static final int REQUEST_CODE_DRAW_OVERLAY = 101;
    private static String[] permissions = {Manifest.permission.READ_PHONE_STATE,
            // 位置
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,

            //相机、麦克风
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            //存储空间
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        token = sharedPreferences.getString("token", null);
        login = getIntent().getBooleanExtra("login",false);
        if (token == null && login == false) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainVisitorActivity.class));
                    SplashActivity.this.finish();
                }
            };
            mHandle.postDelayed(runnable, 3000);
        }else {
            if (checkPermission()) {
                if (checkOverlayPermission()) {
                    new Handler().postDelayed(this::showNextScreen, 1000);
                }
            } else {
                requestPermissions(permissions, 100);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "需要悬浮窗等权限才能正常使用", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        if (checkOverlayPermission()) {
            showNextScreen();
        }
    }

    private boolean checkPermission() {
        boolean granted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                granted = checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    break;
                }
            }
        }
        return granted;
    }

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "需要悬浮窗权限", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                List<ResolveInfo> infos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (infos == null || infos.isEmpty()) {
                    return true;
                }
                startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY);
                return false;
            }
        }
        return true;
    }

    private void showNextScreen() {
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(token)) {
            showMain();
        } else {
            showLogin();
        }
    }

    private void showMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLogin() {
        Intent intent;
        intent = new Intent(this, SMSLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandle.removeCallbacks(runnable);
    }
}