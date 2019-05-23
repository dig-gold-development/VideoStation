package com.site.vs.videostation.kit.contact.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.site.vs.videostation.kit.GlideApp;
import com.site.vs.videostation.kit.WfcUIKit;
import com.site.vs.videostation.kit.contact.ContactAdapter;
import com.site.vs.videostation.kit.contact.model.UIUserInfo;
import com.site.vs.videostation.kit.user.UserViewModel;
import com.site.vs.videostation.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    protected Fragment fragment;
    protected ContactAdapter adapter;
    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.categoryTextView)
    TextView categoryTextView;

    protected UIUserInfo userInfo;

    public ContactViewHolder(Fragment fragment, ContactAdapter adapter, View itemView) {
        super(itemView);
        this.fragment = fragment;
        this.adapter = adapter;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(UIUserInfo userInfo) {
        this.userInfo = userInfo;
        if (userInfo.isShowCategory()) {
            categoryTextView.setVisibility(View.VISIBLE);
            categoryTextView.setText(userInfo.getCategory());
        } else {
            categoryTextView.setVisibility(View.GONE);
        }
        UserViewModel userViewModel = WfcUIKit.getAppScopeViewModel(UserViewModel.class);
        nameTextView.setText(userViewModel.getUserDisplayName(userInfo.getUserInfo()));
        GlideApp.with(fragment).load(userInfo.getUserInfo().portrait).error(R.mipmap.default_header).into(portraitImageView);
    }

    public UIUserInfo getBindContact() {
        return userInfo;
    }
}
