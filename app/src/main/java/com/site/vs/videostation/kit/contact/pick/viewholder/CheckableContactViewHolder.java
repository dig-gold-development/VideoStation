package com.site.vs.videostation.kit.contact.pick.viewholder;

import android.view.View;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import com.site.vs.videostation.kit.contact.model.UIUserInfo;
import com.site.vs.videostation.kit.contact.pick.CheckableContactAdapter;
import com.site.vs.videostation.kit.contact.viewholder.ContactViewHolder;
import com.site.vs.videostation.R;

public class CheckableContactViewHolder extends ContactViewHolder {
    @BindView(R.id.checkbox)
    CheckBox checkBox;

    public CheckableContactViewHolder(Fragment fragment, CheckableContactAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(UIUserInfo userInfo) {
        super.onBind(userInfo);

        checkBox.setVisibility(View.VISIBLE);
        if (!userInfo.isCheckable()) {
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        } else {
            checkBox.setEnabled(true);
            checkBox.setChecked(userInfo.isChecked());
        }
        checkBox.setEnabled(userInfo.isCheckable());
    }
}
