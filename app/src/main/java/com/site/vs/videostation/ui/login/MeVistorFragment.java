package com.site.vs.videostation.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lqr.optionitemview.OptionItemView;
import com.site.vs.videostation.R;
import com.site.vs.videostation.kit.WfcUIKit;
import com.site.vs.videostation.kit.setting.SettingActivity;
import com.site.vs.videostation.kit.user.UserInfoActivity;
import com.site.vs.videostation.kit.user.UserViewModel;
import com.site.vs.videostation.ui.about.AboutActivity;
import com.site.vs.videostation.ui.browse.BrowseActvity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfirechat.model.UserInfo;

public class MeVistorFragment extends Fragment {



    @BindView(R.id.accountTextView)
    TextView accountTextView;



    private UserViewModel userViewModel;
    private UserInfo userInfo;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_vistor_fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.browseOptionItemView)
    void borwse() {
        Intent intent = new Intent(getActivity(), BrowseActvity.class);
        startActivity(intent);
    }

    @OnClick(R.id.aboutOptionItemView)
    void about() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginOptionItemView)
    void login() {
        Intent intent = new Intent(getActivity(),SplashActivity.class);
        intent.putExtra("login",true);
        startActivity(intent);
    }


}
