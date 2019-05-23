package com.site.vs.videostation.kit.contact.viewholder.header;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.contact.ContactAdapter;
import com.site.vs.videostation.kit.contact.model.GroupValue;
import com.site.vs.videostation.R;

@SuppressWarnings("unused")
@LayoutRes(resId = R.layout.contact_header_group)
public class GroupViewHolder extends HeaderViewHolder<GroupValue> {

    public GroupViewHolder(Fragment fragment, ContactAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(GroupValue groupValue) {

    }
}
