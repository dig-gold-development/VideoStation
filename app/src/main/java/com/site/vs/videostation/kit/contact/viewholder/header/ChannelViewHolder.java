package com.site.vs.videostation.kit.contact.viewholder.header;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.site.vs.videostation.kit.annotation.LayoutRes;
import com.site.vs.videostation.kit.contact.ContactAdapter;
import com.site.vs.videostation.kit.contact.model.HeaderValue;
import com.site.vs.videostation.R;

@SuppressWarnings("unused")
@LayoutRes(resId = R.layout.contact_header_channel)
public class ChannelViewHolder extends HeaderViewHolder<HeaderValue> {

    public ChannelViewHolder(Fragment fragment, ContactAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(HeaderValue headerValue) {

    }
}
